#include <Arduino.h>
#include "BasicStepperDriver.h"
#include "max6675.h"


int soPin = A2;
int csPin = A1;
int sckPin = A3;
int PinPuterePlus = A4;
int PinPutereMinus = A5;
MAX6675 robojax(sckPin, csPin, soPin);

const int motor_pwm=9;
const int LED=13;
#define LED PB5

// Motor steps per revolution. Most steppers are 200 steps or 1.8 degrees/step
#define MOTOR_STEPS 200
#define RPM 10//10
// Since microstepping is set externally, make sure this matches the selected mode
// If it doesn't, the motor will move at a different RPM than chosen
// 1=full step, 2=half step etc.
#define MICROSTEPS 1

// All the wires needed for full functionality
#define DIR 4//2
#define STEP 5 //3 
//Uncomment line to use enable/disable functionality
//#define SLEEP 13

// 2-wire basic config, microstepping is hardwired on the driver
BasicStepperDriver stepper(MOTOR_STEPS, DIR, STEP);

//Uncomment line to use enable/disable functionality
//BasicStepperDriver stepper(MOTOR_STEPS, DIR, STEP, SLEEP);

volatile int contor=0;
int pin_v = 0;
volatile int plita_diferenta=0;
volatile int pasi_plita=0; 
volatile int plita=0;
volatile int val_v = 0;
volatile float val_t = 0;
volatile int pwm_m = 0;
volatile int pwm_gaz=0,pwm_gaz1=0;
volatile int pwm_gaz_temp=0,pwm_gaz_t_temp=0;
volatile int pwm_gaz_t=0;
volatile int f_pwm_gaz=0;
volatile int tempa=0;
volatile int start_pwm_m = 0;
volatile int start_pwm_g = 0;
volatile unsigned char recv[20];
char temp[20];
volatile unsigned char z[4], cont_recv=0;
volatile char *ptr;

#define BAUD 9600
#define ubrr 51

unsigned char r,c;
char buff[200];

int putere_plita(int pwm_gaz)
{
  int plita;
  if(pwm_gaz < 70)
    plita=200;
  else if(pwm_gaz < 140)
    plita=400;
  else if(pwm_gaz < 210)
    plita=600;
  else if(pwm_gaz < 280)
    plita=800;
  else if(pwm_gaz < 350)
    plita=1000;
  else if(pwm_gaz < 420)
    plita=1200;
  else if(pwm_gaz < 490)
    plita=1400;
  else if(pwm_gaz < 560)
    plita=1600;
  else if(pwm_gaz < 630)
    plita=1800;
  else if(pwm_gaz < 700)
    plita=2000;
  else if(pwm_gaz > 700)
    plita=2000;

  return plita;

}
 
void initserial(void){
SREG=SREG&127;
UCSR0B=(1<<RXEN0)|(1<<TXEN0);//enable transmiter and reciever
UBRR0H=(unsigned char) (ubrr>>8);//set transfer rate
UBRR0L=(unsigned char) ubrr ;
UCSR0C=0x86;//8 data bit+1 stop data

UCSR0B=UCSR0B|192;//enable USART interupt TXCIE/RXCIE
 
UCSR0A=UCSR0A|128;//activare recieve complete interupt
SREG=SREG|128;
}
 
int prints(char *string) 
{ 
 
   int count =0; 
   while ((string[count]) != '\0') 
   { 
      while ( !( UCSR0A & (1<<UDRE0)) );  // Wait for empty transmit buffer 
      UDR0 = (char)string[count++]; 
 
   }    
   //TxByte('_');
    UCSR0A=UCSR0A & 32;
   return 0; 
 
}
 
 
 
ISR (USART_TX_vect){
 
}
 
//****************** INTRERUPERE RECIEVE COMPLETE************
ISR (USART_RX_vect){
//ISR(USART_RXC_vect ){
 
unsigned char r;
 
 r=UDR0;
 if(r!=10 && r!=13)
 {
 recv[cont_recv]=r;
  cont_recv++;
 }  
if(r==35) //35=#
  {
    for(int j=cont_recv-1; j<=20; j++)
       recv[j]=0;

    
    if(recv[0]=='m')
    { 
       //sprintf(buff,"%s ",recv); 
    
      //prints(buff);
         
      z[0]=recv[1]-48;z[1]=recv[2]-48;z[2]=recv[3]-48;
      pwm_m=100*z[0]+10*z[1]+z[2];
  }
 
  if(recv[0]=='p')
    { 
       //sprintf(buff,"%s ",recv); 
    
      //prints(buff);
         
      z[0]=recv[1]-48;z[1]=recv[2]-48;z[2]=recv[3]-48;
      pwm_gaz=100*z[0]+10*z[1]+z[2];
      
    }
 /* if(recv[0]=='n')
    { 
       //sprintf(buff,"%s ",recv); 
    
      //prints(buff);
        
         
      z[0]=recv[1]-48;z[1]=recv[2]-48;z[2]=recv[3]-48;
      pwm_gaz=-100*z[0]+10*z[1]+z[2];
      
   }*/
 }

//la pwm_gaz diferenta dintre minim si maxim = 700
 
/*if (r==65) 
 { pwm_gaz=pwm_gaz+100;
   digitalWrite(LED_BUILTIN, HIGH);
 
   sprintf(buff,"A mare\n\r"); 
        prints(buff);
 }
 else
if (r==66)
  {pwm_gaz=pwm_gaz-100;
 sprintf(buff,"B mare\n\r "); 
        prints(buff);
    digitalWrite(LED_BUILTIN, LOW); 
 
 
  }
  
  else
  {sprintf(buff,"%c\n\r",r+1); 
   //     prints(buff);
    }
 */   
}
ISR (TIMER2_OVF_vect)    // Timer1 ISR
{//TCNT2 = 0;   // for 1 sec at 16 MHz
  contor++;
  //if(contor>30)
  //PORTB ^= (1 << LED);  

  if(contor>320)
  {PORTB ^= (1 << LED);  
  //pwm_m=75;
  val_v = analogRead(pin_v);
  //stepper.move();

  tempa=robojax.readCelsius();
  pwm_gaz_t_temp=pwm_gaz_t+1000;
   pwm_gaz_temp=pwm_gaz+1000;
  sprintf(buff,"vasc=%.3d,pwm_gaz_t=%.4d,pwm_gaz=%.4d,pwm_m=%.3d,tempa=%.3d,plita=%.4d \n\r ",val_v,pwm_gaz_t_temp,pwm_gaz_temp,pwm_m,tempa,plita); 
       prints(buff);
  contor=0;
  }
}

void setup() {
  // put your setup code here, to run once:

  pinMode(PinPuterePlus, OUTPUT);
  pinMode(PinPutereMinus, OUTPUT);
  digitalWrite(PinPuterePlus, HIGH);
  digitalWrite(PinPutereMinus, HIGH);
  pinMode(motor_pwm, OUTPUT);
  DDRB = (0x01 << LED);     //Configure the PORTD4 as output
  TCNT2 = 0;   // for 1 sec at 16 MHz  
  TCCR2A = 0x00;
  TCCR2B = (1<<CS10) | (1<<CS12);;  // Timer mode with 1024 prescler
  TIMSK2 = (1 << TOIE2) ;   // Enable timer1 overflow interrupt(TOIE1)
  //pinMode(LED_BUILTIN, OUTPUT);
  initserial();
  sprintf(buff,"  Mama Lena da-mi bani! \n\r "); 
        prints(buff);
  sei();        // Enable global interrupts by setting global interrupt enable bit in SREG

stepper.begin(RPM, MICROSTEPS);
    // if using enable/disable on ENABLE pin (active LOW) instead of SLEEP uncomment next line
    // stepper.setEnableActiveState(LOW);
 
  
  
 /* 
  DDRB = (0x01 << LED);     //Configure the PORTD4 as output
  
  //TCNT1 = 63974;   // for 1 sec at 16 MHz
  TCNT1 = 49910;   // for 1 sec at 16 MHz  

  TCCR1A = 0x00;
  TCCR1B = (1<<CS10) | (1<<CS12);;  // Timer mode with 1024 prescler
  //TIMSK1 = (1 << TOIE1) ;   // Enable timer1 overflow interrupt(TOIE1)
  //sei();        // Enable global interrupts by setting global interrupt enable bit in SREG
  */

  for(int j=0; j<5; j++)
  {
    digitalWrite(PinPutereMinus, LOW);
    for(int i=0;i<35;i++)
    delayMicroseconds(10000);
    digitalWrite(PinPutereMinus, HIGH);
    delay(500);
  }

}

void loop() {
  //PORTB=PORTB|32;
  // put your main code here, to run repeatedly:
 analogWrite(9, pwm_m);
  //delay (1000);
  //analogWrite(9, 0);
  //delay (10000);
  //TIMSK2 = TIMSK2 & 254; 
  
if(pwm_gaz_t!=pwm_gaz)
{
stepper.move(pwm_gaz_t-pwm_gaz);
plita_diferenta=putere_plita(pwm_gaz_t)-putere_plita(pwm_gaz);
pasi_plita=abs(plita_diferenta / 200);
pwm_gaz_t=pwm_gaz;

//sprintf(buff,"plita_diferenta=%d,pasi_plita=%d \n\r ",plita_diferenta,pasi_plita); 
  //     prints(buff);
if(plita_diferenta < 0 )
{
  for(int j=0; j<pasi_plita; j++)
  {  
    digitalWrite(PinPuterePlus, LOW);
    for(int i=0;i<35;i++)
    delayMicroseconds(10000);
    digitalWrite(PinPuterePlus, HIGH);
    delay(500);
  }
}
else 
{
  for(int j=0; j<pasi_plita; j++)
  {
    digitalWrite(PinPutereMinus, LOW);
    for(int i=0;i<35;i++)
    delayMicroseconds(10000);
    digitalWrite(PinPutereMinus, HIGH);
    delay(500);
  }
}
plita=putere_plita(pwm_gaz);
}
  
}
