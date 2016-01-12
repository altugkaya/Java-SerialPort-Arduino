/*
 *  A Basic programme that controls a RGB LED via JAVA
*/

//  These pins can be changed. However, do not forget 
//  to check that desired pins are capable of sending PWM signals
int redPin = 3;
int greenPin = 5;
int bluePin = 6;

int redVal = 0;
int greenVal = 0;
int blueVal = 0;

unsigned char input[3];

void setup() {
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  // if serial port is available, read incoming bytes 
    if(Serial.available() > 0 ) {
    delay(5);
    
    Serial.println("Datas are arrived");
    
    for(int i = 0; i < 3; i++){
      delay(10);
      input[i] = Serial.read();
      Serial.println(input[i]);
    }
    
    delay(30);
    analogWrite(redPin, input[0]);
    analogWrite(greenPin, input[1]);
    analogWrite(bluePin, input[2]);
  } 
}
  
  
  
  
  

  
  
  
  
  
  
  
  
  /*mappedRedVal = map(redVal, 0, 255, 0, 400);// Berk's idea, a sort of PWM signal. However, does not do what I want.
  
  for(int i = 0; i < 4; i++){
    digitalWrite(redPin, HIGH);
    delay(mappedRedVal);
    digitalWrite(redPin, LOW);
    delay(400 - mappedRedVal)
}
