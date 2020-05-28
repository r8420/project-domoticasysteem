#####################
#      README       #
#####################

Om dit project te kunnen runnen zijn er een aantal stappen die gedaan moeten worden


# 1. Database
#####################
Start xampp en zet MySQL aan
Open MySQL Workbench en maak verbinding met xampp
Klik op "File > Run SQL Script" en navigeer naar deze projectmap.
In de projectmap ga je naar de map "database" en selecteer het "database.sql" bestand.
Als dit script voltooid is is de database goed ingesteld.

Om de database te vullen met een aantal muzieknummers kan ook het bestand "add_testnummers.sql" gebruikt worden.
Het is ook mogelijk om zelf een aantal nummers in te stellen, dit kan door "add_eigen_nummers.sql" te bewerken.
	
!! Hoewel stap 2 en 3 nodig zijn als je de metingen op het scherm wil zien werkt de GUI ook zonder verbinding met de Arduino of Pi.
!! Het is dus mogelijk om direct naar stap 4 te springen, de GUI zal alleen de meeste functionaliteit missen.


# 2. Arduino
#####################
Ga naar bladzijde 17 van het "TO Domotica-Systeem" en gebruik het elektrisch schema dat daat staat om je arduino/breadboard goed in te stellen.
Sluit de arduino aan op je computer
Navigeer naar de 'arduino' map en open het .ino bestand dat daar in staat.
Upload dit bestand naar de Arduino.
Sluit de Arduino IDE of zorg er in ieder geval voor dat de Serial comm niet open staat.


# 3. Pi
#####################
Sluit je Pi aan op je computer doormiddel van een netwerkkabel.
Open WinSCP of een vergelijkbaar programma
Navigeer naar de map 'pi' van dit project, en kopieer het "raspberryPi_comm.py" bestand over naar je Pi.
Als je eerder "add_testnummers.sql" hebt gebruikt kan je nu ook meteen de nummers uit de map Testnummers overzetten naar de Music folder op je Pi

Gebruik VNC Viewer of een vergelijkbaar programma om je Pi te besturen.
Zorg dat er een audio device is verbonden met de Pi. Dit kan via bluetooth of de aux poort. 
Zorg dat op de Pi het goede device geselecteerd is door op de volumeknop te rechtermuisklikken en Analog of de naam van je bluetooth speaker aan te klikken

Ga naar Start > Preferences > Raspberry Pi Configuration
Noteer de Hostname van jouw Pi ergens, je hebt deze later nodig.

! ALS ER EEN _ IN DE HOSTNAME VAN JE PI ZIT DAN MOET JE JE HOSTNAME VERANDEREN. !
We weten niet waarom het zo is, maar de java applicatie kan geen verbinding maken met een hostnaam met een _

Je kunt nu het "raspberryPi_comm.py" bestand vanaf de Pi runnen, de Pi wacht dan totdat de GUI verbinding maakt.
	
	
# 4. IntelliJ
#####################
Open intelliJ en selecteer Open...
Navigeer naar de projectfolder en open de folder "java" als een project.
Het kan zijn dat je een ander java JDK hebt dan wij, dan moet je dat even aanpassen in de projectsettings.

Navigeer naar het bestand MainInput en bekijk regel 19.
Pas de host aan naar de hostname die je in stap 3 genoteerd hebt.

Als dat werkt kan je de MainScherm klasse runnen om de GUI op te starten.


Als het goed is werkt het systeem nu!
