int LDRValue = 0;
boolean Running = true;

void setup() {
  
  pinMode(A0,INPUT);
  Serial.begin(9600);
  while(!Serial){;}
}

void loop() {
  
  LDRValue = analogRead(A0); // read the value from the LDR
  int waarde = LDRValue;
  
  
  int getal;
  String msg = "....";
  
  // Deze for loop zorgt ervoor dat getal omgezet wordt naar een string van lengte 4. (51 wordt dus 0051)
  for (int i = 3; i>=0; i--) {
    getal = waarde % 10;
    waarde = (waarde - getal) / 10;
    msg[i] = getal + 48; // + 48 is getal to ascii
  }
  
  Serial.print(msg);      // print the value to the serial port
  delay(10000);  
    
}
