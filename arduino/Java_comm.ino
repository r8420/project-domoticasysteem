int LDRValue = 0;
boolean Running = true;
void setup() {
  // put your setup code here, to run once:
  pinMode(A0,INPUT_PULLUP);
  Serial.begin(9600);
  while(!Serial){
    ;
}
}
void loop() {
  // put your main code here, to run repeatedly:
    delay(1000);
   LDRValue = analogRead(A0); // read the value from the LDR
   if (LDRValue < 1000) {
    Serial.print("0");
    if (LDRValue < 100) {
        Serial.print("0");
        if (LDRValue < 10) {
            Serial.print("0");
        }
    }
}
  Serial.println(LDRValue);      // print the value to the serial port
    
    
}
