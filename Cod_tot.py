import serial
import mysql.connector
import os
import RPi.GPIO as GPIO
#GPIO.setmode(GPIO.BCM)            # choose BCM or BOARD  
from time import sleep
import time
import numpy as np


comanda1 = "python3 /home/pi/whatsapp/yowsup-3.3.0/yowsup-cli demos -s 40756329194 "
comanda2 = " --config /home/pi/whatsapp/yowsup-3.3.0/config"
comanda = comanda1 + comanda2
ser=serial.Serial('/dev/ttyUSB0',19200)

a=0 
flag=0
flag_a=0
pwm_m_db = np.arange(0, 20, 1)
pwm_m_db = [-1] * 20 
pwm_gaz_db = np.arange(0, 20, 1)
pwm_gaz_db = [-1] * 20 
tempa_db = np.arange(0, 20, 1)
tempa_db = [-1] * 20 
vasc_db = np.arange(0, 20, 1)
vasc_db = [-1] * 20
timp_db = np.arange(0, 20, 1)
timp_db = [-1] * 20  
vasc=0
pwm_gaz=0
pwm_gaz_t=0
pwm_m=0
tempa=0
start=0
start2=0
id_reteta=0
timp=0
a_max=100
flag_a_max=0
GPIO.setwarnings(False)         #disable warnings
GPIO.setmode(GPIO.BOARD)        #set pin numbering system
GPIO.setup(38, GPIO.IN, pull_up_down=GPIO.PUD_UP)  # set a port/pin as n input  





while True:
	if start == 0:
		try:
			connection = mysql.connector.connect(host='localhost',
			                                     database='pandemic_pot',
			                                     user='admin',
			                                     password='pandemic_pot')

			sql_select_Query = "SELECT * FROM `Retete` where activ=1"
			cursor = connection.cursor()
			cursor.execute(sql_select_Query)
			# get all records
			records = cursor.fetchall()
			#print(len(records))

			for row in records:
			    print("Nume reteta", row[0])
			    id_reteta=row[3]
			    print("id_reteta=",id_reteta)
			    #comanda=comanda1 + str(row[2]) + comanda2
			    #comanda=comanda1 + comanda2
			    #print(comanda)
			    #os.system(comanda)
			if len(records) == 1:
				start=1
		except mysql.connector.Error as e:
			print("Error reading data from MySQL table", e)
		finally:
			if connection.is_connected():
			    connection.close()
			    cursor.close()
			    #print("MySQL connection is closed")
	if start == 1 and start2==0:
		try:
			connection = mysql.connector.connect(host='localhost',
			                                     database='pandemic_pot',
			                                     user='admin',
			                                     password='pandemic_pot')

			sql_select_Query = "SELECT * FROM `Pasi retete` where id_reteta="+str(id_reteta)
			cursor = connection.cursor()
			cursor.execute(sql_select_Query)
			# get all records
			records = cursor.fetchall()
			#print(len(records))
			a_max=len(records)
			print("a_max=",a_max)
			for row in records:
			    pas_db = row[1]-1
			    pwm_m_db[pas_db]=row[3]
			    pwm_gaz_db[pas_db]=row[4]
			    tempa_db[pas_db]=row[5]
			    vasc_db[pas_db]=row[6]
			    timp_db[pas_db]=row[7]
			    print("id_reteta=",id_reteta)
			    #print ("pwm_select",pwm_m_db[pas_db])
			    #comanda=comanda1 + str(row[2]) + comanda2
			    #comanda=comanda1 + comanda2
			    #print(comanda)
			    #os.system(comanda)
			print(pwm_gaz_db[1])
			start2=1
			flag_a_max=0
			a=0
		except mysql.connector.Error as e:
			print("Error reading data from MySQL table", e)
		finally:
			if connection.is_connected():
			    connection.close()
			    cursor.close()
			    #print("MySQL connection is closed")
	d = ser.readline()
	print(d)
	timp=timp+1
	button_pressed = GPIO.input(38)
	#print(button_pressed)
	#d.sub(r'^\(|\)$', '',string).split(',')
	print("a=",a)
	if a>a_max and flag_a_max==0:
		print("gata reteta")
		flag_a_max=1
		a_max=100
		start=0
		start2=0
		flag=0
		z="m000#"
		ser.write(z.encode())
		sleep(1)
		ser.write(z.encode())
		sleep(1)
		z="p000#"
		ser.write(z.encode())
		sleep(1)
		ser.write(z.encode())
		sleep(1)
		msg='"Gata reteta. Sta-ti-ar in gat"'
		print (msg)
		comanda = comanda1 + msg + comanda2
		os.system(comanda)
		try:
			connection = mysql.connector.connect(host='localhost',
			                                     database='pandemic_pot',
			                                     user='admin',
			                                     password='pandemic_pot')

			sql_select_Query = "UPDATE `Retete` SET `activ` = '0'"
			cursor = connection.cursor()
			cursor.execute(sql_select_Query)
			connection.commit()
			print (sql_select_Query)
			# get all records
			records = cursor.fetchall()
		except mysql.connector.Error as e:
			print("Error reading data from MySQL table", e)
		finally:
			if connection.is_connected():
			    connection.close()
			    cursor.close()
			    #print("MySQL connection is closed")

	vect2=str(d)
	try:
		vasc=int(vect2[10:13])
		pwm_gaz_t=int(vect2[24:28])
		pwm_gaz=int(vect2[37:41])
		pwm_m=int(vect2[48:51])
		tempa=int(vect2[58:61])
		print("pwm_gaz",pwm_gaz)
		print("vasc",vasc)
		print("pwm_gaz_t",pwm_gaz_t)
		print("pwm_m",pwm_m)
		print("tempa",tempa)
	except:
		print("eroare_conv_serial")	
	if button_pressed == 0:
		sleep(0.1)
		if button_pressed == 0 and flag==0:
			print("apasat")
			a=a+1
			#z="B600#"+"\r\n"
			#ser.write(z.encode())
			flag=1
			flag_a=0
			timp=0
	if button_pressed == 1:
		sleep(0.1)
		if button_pressed == 1 and flag==1:
			flag=0
	if start2==1:
		#print("tempa[",a,"]=",tempa)
		print("tempdb",tempa_db[a])
		print("timp",timp)
		#tempa=62
		if tempa>int(tempa_db[a]) :
			print("pas temp=",a)
			print("pwm_m_db",pwm_m_db[a])
		if tempa>int(tempa_db[a]) and timp>int(timp_db[a]) and flag_a==0:
			if pwm_m_db[a]!=-1:
				z=pwm_m_db[a]
				#z="m112#"+ '\r\n'
				ser.write(z.encode())
				sleep(1)
				#ser.write(z.encode())
				#sleep(1)
				#ser.write(z.encode())
				#sleep(1)
				print("pwm_db=",z)
			if pwm_gaz_db[a]!=-1:
				z1=pwm_gaz_db[a]
				#z1="p300#"
				ser.write(z1.encode())
				sleep(1)
				#ser.write(z1.encode())
				#sleep(1)
				#ser.write(z1.encode())
				#sleep(1)
				print(z)
			#print("pasul ",a," terminat")
			flag_a=1
			msg='"pasul ' + str(a) + ' terminat"'
			print (msg)
			comanda = comanda1 + msg + comanda2
			os.system(comanda)
