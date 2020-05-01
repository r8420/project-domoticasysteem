  int LDRValue = 0;
boolean Running = true;
void setup() {
  // put your setup code here, to run once:
  pinMode(A0,INPUT);
  Serial.begin(9600);
  while(!Serial){
    ;
}
}
void loop() {
  // put your main code here, to run repeatedly:
  
   LDRValue = analogRead(A0); // read the value from the LDR
   int waarde = LDRValue;
  
  
  int getal;
  String msg = "....";
  
  for (int i = 3; i>=0; i--) {
    getal = waarde % 10;
    waarde = (waarde - getal) / 10;
    msg[i] = getal + 48; // + 48 is getal to ascii
  }
  
  Serial.print(msg);      // print the value to the serial port
  delay(10000);  
    
}
