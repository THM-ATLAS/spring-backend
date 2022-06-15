# ATLAS Backend

---
## Projekt lokal installieren

- Wir empfehlen die Verwendung von IntelliJ IDEA, da Spring und Kotlin dort out of the Box 
 supportet werden
  - https://www.jetbrains.com/de-de/idea/download/
- Repo clonen:
 ````git clone git@github.com:THM-ATLAS/spring-backend.git````
- Projekt in IntelliJ importieren
- Umgebungsvariablen ````db_source, db_username, db_password```` auf die URL der Datenbank, das Passwort der Datenbank und den Nutzernamen der Datenbank setzen, die Umgebungsvariable `mode` auf den Pfad setzen, auf dem die doc starten soll
- oben rechts auf Play drücken, die Anwendung startet auf http://localhost:8080
- Eine Dokumentation der API startet automatisch auf http://localhost:8080/${mode}/docs

## Frontend, API-Doc, etc

- Das Frontend zu ATLAS finden Sie hier: https://github.com/THM-ATLAS/vue-frontend
- Eine Dokumentation der API, wie sie auf http://brueckenkurs-programmieren.thm.de/api läuft, finden Sie hier: http://brueckenkurs-programmieren.thm.de/api/docs (nur aus dem THM Netz erreichbar).