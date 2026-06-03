Egy Java alapú városépítő és logisztikai szimulációs játék. A projekt célja a városi infrastruktúra kiépítése, a járművek útvonalának optimalizálása, valamint az utasok és nyersanyagok hatékony szállítása a gazdaság fenntartása érdekében.
Főbb funkciók

    Infrastruktúra építése: Utak, kisebb és nagyobb hidak, valamint megállók elhelyezése egy rács alapú térképen.

    Logisztika és szállítás: Különböző kapacitású járművek (buszok és teherautók) kezelése. A teherautók specifikus nyersanyagokat (szén, arany, vas, kő, fa) szállítanak a gyárakból a felhasználási helyekre.

    Dinamikus szimuláció: A városok népessége idővel növekszik, a gyárak folyamatosan termelnek.

    Útvonaltervezés: A járművek a megállók között kijelölt útvonalon, automatikus ütközéselkerüléssel közlekednek.

    Mentés és betöltés: A játékállás perzisztens tárolása a SaveHandler segítségével.

Technológiai verem

    Nyelv: Java (JDK 17+)

    Grafikus felület (GUI): Java Swing / AWT

    Tesztelés: JUnit 5, Mockito

    Kódlefedettség: JaCoCo

    CI/CD: GitLab CI

Tesztelés és CI/CD architektúra

A projekt szigorú, 80% feletti tesztlefedettségi elvárások (Instruction & Branch coverage) mentén készült.

    Headless tesztkörnyezet: A GitLab CI szervereken való sikeres futtatás érdekében a tesztek grafikuskártya/monitor nélkül futnak. Ezt a System.setProperty("java.awt.headless", "true"); beállítás és a Swing ablakok mockolása (Mockito) biztosítja.

    Humble Object minta: A tisztán vizuális megjelenítésért felelős osztályok (pl. GameGUI, MenuGUI) le vannak választva a központi játéklogikáról (GameEngine), és a JaCoCo konfigurációban (pom.xml) ki lettek zárva a lefedettségi mérésből a pontosabb eredmények érdekében.

Futtatás és Használat
Játék indítása

A program belépési pontja a citybuilder.Main osztály, amely elindítja a főmenüt.
Tesztek futtatása (Maven)

A tesztesetek és a JaCoCo kódlefedettségi riport generálása az alábbi paranccsal indítható:
Bash

mvn clean test

Felépítés / Architektúra

    citybuilder.GameEngine: A fő logikai vezérlő. Felelős az építési módokért, a kattintások feldolgozásáért és a járművek mozgatásáért.

    citybuilder.Map és citybuilder.map.tile.*: A pálya modellje és a rajta lévő mezők (Road, Bridge, City, Factory, Stop).

    citybuilder.vehicles.*: A járművek viselkedését és kapacitását leíró modellek.

    citybuilder.GameGUI: A felhasználói felület megjelenítése.