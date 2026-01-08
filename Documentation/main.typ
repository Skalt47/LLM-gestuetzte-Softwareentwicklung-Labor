#import "static/lib.typ": *
#import "acronyms.typ": acronyms
#import "glossary.typ": glossary

#show: supercharged-dhbw.with(
  title: "LLM-gestuetzte-Softwareentwicklung-Labor",
  authors: (
    (name: "Tim Jauch", student-id: "763086", course: "SWB", course-of-studies: "LLM-gestuetzte-Softwareentwicklung"),
    (
      name: "Annabel Heberle",
      student-id: "770677",
      course: "SWB",
      course-of-studies: "LLM-gestuetzte-Softwareentwicklung",
    ),
  ),
  acronyms: acronyms, // displays the acronyms defined in the acronyms dictionary
  at-university: true, // if true the company name on the title page and the confidentiality statement are hidden
  city: "Esslingen",
  bibliography: bibliography("sources.bib"),
  date: datetime.today(),
  glossary: glossary, // displays the glossary terms defined in the glossary dictionary
  language: "de", // en, de
  // supervisor: (company: "John Appleseed"),
  supervisor: (university: "Prof. Dr. Jörg Nitzsche, Dr. Stefan Kaufmann, Klemens Morbe"),
  university: "Hochschule Esslingen University of Applied Sciences",
  university-location: "Campus Esslingen Flandernstraße",
  university-short: "HSE",
  // for more options check the package documentation (https://typst.app/universe/package/supercharged-dhbw)
)

// Edit this content to your liking
#let project-name = "Stack Attack"
#let project-slogan = "All or nothing!"

// Max 1 Page
= Einleitung
#project-name: #project-slogan
== Vorstellung des Projekts
#project-name ist ein Softwareprojekt, welches mit Hilfe von LLMs entwickelt und dokumentiert wird. Das Ziel des Projekts ist es, eine Online Version des beliebten Quartett Kartenspiels zu erstellen. Hierbei gilt es den Aufwand bei der Erstellung der Software mithilfe von LLMs zu minimieren und gleichzeitig eine qualitativ hochwertige Software zu entwickeln.

== Zielsetzung und Relevanz
Ziel dieses Softwareprojekts ist der Einsatz von LLMs zur Unterstützung bei der Softwareentwicklung und die Integration in selbigen. Durch den Einsatz von LLMs soll der Entwicklungsprozess effizienter gestaltet werden mit gleichbleibender Qualität.

Wichtig und relevant ist das Arbeiten mit LLMs, da von einer Steigerung der Produktivität ausgegangen wird. Dadurch kann der Aufwand und somit in der realen Welt kosten verringert werden. Außerdem ist der Vergleich von statischem Code, welcher eine Entscheidung trifft und dynamischen Entscheidungen durch LLMs betrachtenswert hinsichtlich Performance und Qualität.

== Aufbau der Arbeit
Nach einer Projektbeschreibung folgt die Softwarearchitektur und die Vorstellung der verwendeten Technologien. Im Zuge der Implementierung wird noch detailierter auf den Einsatz der LLMs und Prompt Engineering eingegangen. Abschließend geht es noch um Erfahrungen, Herausforderungen, Reflexion, sowie eine Zusammenfassung und das Fazit.

// ~3 Pages
= Projektbeschreibung
== Problemstellung und Anforderungen
Die Problemstellung besteht darin, eine Online Version des Kartenspiels Quartett zu erstellen und gegen eine LLM spielen zu können.  Die Anforderungen umfassen die Erstellung einer benutzerfreundlichen Oberfläche, die Implementierung der Spiellogik und die Integration einer LLM als Gegner.

== Zielgruppe und Personas
Die Zielgruppe sind Kinder und Erwachsene, welche gerne Quartett spielen und Dinos mögen. Für die Kinder steht das Spielen und Lernen im Vordergrund, während Erwachsene konkret efahren können, was kleine LLM-Modelle leisten können. Dabei kann die Qualität und Performance ganz gut beobachtet werden.

Von LLM erstelle passende Personas:
Persona 1: Dino-begeistertes Kind

Alter: 9-12
Motivation: spielen, lernen, Spaß an Dinos und gewinnen.
Ziele: neue Dino-Arten entdecken; Spielregeln schnell verstehen; Erfolgserlebnisse im Spiel.
Technik: Tablet/Smartphone, einfache Bedienung, wenig Geduld für komplexe Menüs.
Bedürfnisse: klare Anleitung, kurze Texte, große Buttons, bunte Visuals; schnelle Antworten; unkompliziert.
Schmerzpunkte: zu viel Text, langsame Ladezeiten, unklare Regeln, schwierige Begriffe.

Persona 2: Technik-affiner Erwachsener (z.B. Elternteil oder Lehrkraft)

Alter: 30-45; 
Motivation: Qualität und Performance kleiner LLM-Modelle beurteilen; Lern-/Unterhaltungswert für Kinder testen.
Ziele: sehen, wie stabil und fix die LLM-Antworten sind; Inhalte auf Korrektheit prüfen; didaktischen Nutzen für Unterricht/Spieleinsätze bewerten.
Technik: Desktop/Laptop/Tablet; erwartet Transparenz zu Modell, Daten und Limits.
Bedürfnisse: übersichtliches Monitoring (Antwortzeiten, Qualität), nachvollziehbare Quellen/Erklärungen, einfache Einrichtung (Docker/Setup), Datenschutz/Kinderschutz-Hinweise.
Schmerzpunkte: unklare Modellgrenzen, Halluzinationen ohne Kennzeichnung, fehlende Logs/Statistiken, aufwendiges Setup.


// ~3 Pages
= Softwarearchitektur und Technologie

== Gesamtübersicht der Architektur
#link("https://github.com/Skalt47/LLM-gestuetzte-Softwareentwicklung-Labor/tree/main/Documentation/Architecture.mmd")[
  Mermaid Chart der Softwarearchitektur in Github
]

#figure(
  image("/documentation/assets/Architecture_V1.png", width: 100%),
  caption: [
    Architektur der Software
  ],
)

== Frontend/Benutzeroberfläche

Beim ersten Aufruf blockiert ein kleines Dialogfenster das Spiel, bis ein Spielername eingetragen und gespeichert ist. Name und Profil bleiben für spätere Sitzungen erhalten. Von dort lässt sich jederzeit ein neues Match über den Startknopf beginnen. Während des Spiels stehen oben die aktuellen Kartenzahlen beider Decks, darunter die oberste Spielerkarte mit Name, Bild, Gruppencode und den sechs spielrelevanten Attributen Lifespan, Length, Speed, Intelligence, Attack und Defense, die du per Klick zum Vergleich auswählst. Ist der Mensch am Zug, sind die Buttons aktiv. Gewinnt die KI eine Runde und das Spiel läuft weiter, spielt sie nach kurzer Bedenkzeit automatisch erneut. Auf Wunsch holst du dir bis zu dreimal pro Match per Ask the LLM Knopf einen Tipp, der angezeigt und direkt genutzt wird. Nach jedem Zug zeigt die Oberfläche das Rundenergebnis, beide Werte und die aktualisierten Deckgrößen. Ist das Spiel vorbei, erscheint ein Game Over Bereich mit Gesamtsieger und Play Again Option. Lade und Fehlersituationen machen sich durch deaktivierte Buttons und eine sichtbare Fehlermeldung bemerkbar. Typescript und React bilden das Fundament.


== Backend, Datenbank und Schnittstellen

Im Backend läuft eine Java Spring Boot Anwendung, die ihre REST API bereitstellt und den Spielablauf steuert. Persistente Daten wie Dinosaurier und Spieler landen in PostgreSQL, während laufende Matches im schnellen Redis In Memory State gehalten werden. Die Infrastruktur startet bequem über Docker, inklusive Adminer zur Datenbankinspektion. Datenbankänderungen werden mit Flyway versioniert. Für Entscheidungen im Spiel fragt der Server ein Ollama Modell (phi3:mini) an, und greift so auf LLM Unterstützung zurück. Insgesamt bildet das Backend das Fundament zwischen Frontend, Datenbank und KI Helfer.

Java, Spring Boot, REST API, PostgreSQL and Redis, Docker, Flyway, Ollama, phi3:mini, Adminer

== LLM-Integration (MCP, Wrapper, API)

Die LLM-Integration sitzt im LlmClientService.java: Das Backend ruft über einen konfigurierten RestTemplate den Ollama-Container an, der das Modell phi3:mini bereitstellt. Für jede Entscheidung baut der Service einen klaren Prompt mit den sechs zulässigen Attributen (lifespan, length, speed, intelligence, attack, defense) und schickt ihn an die Chat-API des Containers. Falls die LLM eine unbrauchbare Antwort liefert, was bei LLMs eine zu berücksichtigende Möglichkeit ist, so greift die Fallback-Logik. Fallback-Logik wählt den höchsten Wert, sollte auch dies nicht funktionieren, so wird einfach das Angriffs-Attribut ausgewählt. Das Timeout ist auf 2 Minuten (120000 ms) gesetzt, wodurch phi3:mini die Inferenz durchzuführen und eine Antwort zu generieren. Kommt etwas Unerwartetes zurück oder gar nichts, greift die bereits erwähnte Fallback-Logik, sodass das Spiel auch ohne LLM-Antwort weiterläuft.

== Sicherheits- und Authentifizierungsmechanismen
Entfallen, da kein Login sondern bisher nur Spielername eingegeben werden kann und keine sensible Daten verarbeitet werden.
-
== Sonstiges
Die Dokumentation erfolgt mit Typst und Mermaid Chart für das Architekturdiagramm.

// ~3 Pages
= Implementierung

== Vorgehensmodell und Projektmanagement
Während dieses Projektes wurden keine festen Vorgehensmodelle und Projektmanagement-Tools eingesetzt und stattdessen mehr auf flexibles Arbeiten gesetzt. In wöchentlichen Meetings wurden die nächsten Schritte geplant, Aufgaben verteilt und der Fortschritt überprüft. Während der Entwicklung wurde der main Branch geschützt,sodass nur Änderungen eines überprüften Pull Requests durch ein anderes Teammitglied in diesen zusammengeführt werden konnten. Dadurch konnte zudem eine höhere Codequalität gewährleistet werden.

== Code-Generierung und Entwicklung
Nach anfänglicher Konfiguration und testen, wurde anhand von abgesprochenen User Stories zunächst die Grundfunktionen des Spiels implementiert. Hierbei wurde der Fokus auf die Spiellogik und die Kommunikation zwischen Frontend und Backend gelegt. Die Backendfunktionen, also die Spiellogik ist das Herzstück und wurde dementsprechend auch so behandelt. Das Frontend wurde dann häufig, abhängig von dem Backend, mit LLMs zunächst Test mäßig generiert. Anschließend wurde die LLM-Integration implementiert und getestet. Während der Entwicklung wurden immer wieder kleinere Anpassungen vorgenommen, um die Benutzerfreundlichkeit zu verbessern und Fehler zu beheben.

== Datenmodelle und Schnittstellenimplementierung

=== Domain Model Diagramm

#link("https://github.com/Skalt47/LLM-gestuetzte-Softwareentwicklung-Labor/tree/main/Documentation/DomainModel.mmd")[
  Mermaid Chart des Domain Model Diagramms in Github
]

#figure(
  image("/documentation/assets/Domain_Model_Diagramm_Color.png", width: 100%),
  caption: [
    Domain Model Diagramm
  ],
)

== Versionskontrolle, Build- und CI-Prozesse
Als Versionskontrolle werden Git und GitHub verwendet.

// ~4 Pages
= Einsatz der LLMs und Prompt Engineering

== Auswahl der LLMs und Dienste
phi3:mini als LLM für die Spiellogik
Github Copilot, Codex und ChatGPT 5.2 für die Codegenerierung
Bei der Entwicklung des Codes wurden die KI-Agenten Codex und GitHub Copilot verwendet, die der Unterstützung während des Programmierens dienten. Ein zentrales Ziel des Projekts war die Implementierung von Large Language Models (LLMs) in zwei verschiedenen Anwendungsbereichen: Erstens die automatisierte Generierung von Dinosaurier-Abbildungen für die Quartettkarten über eine externe API und zweitens die Realisierung eines KI-Gegners als Kernfunktion des Spiels. Bei der Wahl dieser LLMs war das ausschlaggebenste Kriterium, dass sie kostenlos verwendbar sein mussten, was sich schnell als Herausforderung erwies.

Um den KI-Gegener zu realisieren, wurde das kleine lokale Modell phi3:mini gewählt. Die Bereitstellung des Modells erfolgt über Ollama, ein Framework zur Ausführung verschiedener Sprachmodelle auf lokaler Hardware. Wobei um die Skalierbarkeit zu erhöhen Ollama in einen Docker-Container gekapselt wurde. 

Ollama fungiert hierbei als Schnittstelle, über die das Backend Spielzustände an das phi3:mini Modell sendet und darauf Entscheidungen der KI zurückerhält. Phi3:mini hat sich für dieses Projekt geeignet da es sowohl kostenlos, sowie relativ kompakt und somit von unserer Hardware stemmbar war.

Die Wahl eines API-Endpunktes war etwas eingeschränkt, da nicht viele kostenlos zur Verfügung stehen. Prinzipiell war die Wahl allerdings nicht von zu großer Bedeutung, da der Code so strukturiert wurde, dass lediglich der API-Endpunkt geändert werden muss, um ein anderes Modell nach Belieben zu verwenden. Über Huggingface-Inference-API wurde das Modell Stable Diffusion XL (SDXL) 1.0 von Stabilityai für die Dinosaurier-Bildgenerierung angebunden. Huggingface ist ein tokenbasierter Service, der API-Endpunkte für verschiedene Modelle zur Verfügung stellt. Das SDXL Modell erzeugt eine hohe Ausgabequlität und ist aufgeteilt in ein Base und ein Refiner Modell. Das Base Modell erzeugt die erste Grundlage des Bildes und kann optional durch das Refiner-Modell hinischtlich Detailgrad und Prompttreue optimiert werden. Da Huggingface tokenbasiert ist und somit die Bildgenerierung limitiert war, erwies sich SDXL als effizient, da das Base Modell ausreichend war und gezielt einzelne Bilder durch den Refiner optimiert werden konnten, um das Token-Kontingent zu schonen.

== Aufbau und Dokumentation der Prompts

== Generierte Assets (Code, Bilder, Audio, Video, Text)
Code, Stammdaten in Form von JSON, Bilder für die Karten
Hier Abschnitt der Dino JSON einfügen.

== Fehleranalyse und Optimierung
Frontend Code: Anpassung durch visuelle Überprüfung und manuelles testen. Fehlertoleranz bei Stammdaten, Bilder manuelle Überprüfung auf Halluzinationen

== Qualitätssicherung bei der LLM-Nutzung
Besonders bei der Nutzung der KI-Agenten musste auf Richtigkeit und Qualität des Codes geachtet werden. Um Qualität zu verbessern, erhielten die Agenten bei Verwendung nur kleinere Aufgabenpäckchen, da  das Ergebnis des Outputs dadurch verbessert werden konnte. Überreichen mehrerer Aufgaben gleichzeitig führte dazu, dass die Agenten eine Aufgabe besser und die andere schlechter beziehungsweise gar nicht bearbeiteten. Des Weiteren wurden die eingefügten Codeabschnitte überprüft und getestet bevor diese beibehalten wurden.

// ~1 Page
= Anpassungen nach der Präsentation
Im Zuge der Präsentation gab es wertvolles Feedback, welches in die Weiterentwicklung des Projekts einfloss. Basierend auf den Anregungen der Präsentation, sowie eigener Ideen wurden folgende Anpassungen vorgenommen:
== Frontend
Zum Zeitpunkt der Präsentation hatte das Frontend Ausbaupotential. Ursprünglich wurde nach jedem Spielzug ein Feld angezeigt, in welchem der Gewinner, die vergleichten Attribute und deren Werte angezeigt wurden, nicht aber die gesamte Gegnerkarte. Durch Anregung aus dem Feedback wurde das Frontend weitgehend optimiert, sodass nun auch die Karte des KI-Gegners angezeigt wird. Während jeder Matchrunde wird die Karte dabei verschwommen angezeigt und nach Matchentscheidung offenbart, sodass der User die Karten vergleichen kann.

Um die User Experience zu optimieren wurden des Weiteren Animationen der Kartenränder eingefügt, um ganz einfach Matchgewinner und Verlierer zu erkennen. Darüber hinaus wurde das Layout des Frontends angepasst um ansprechender zu sein.  Zusätzlich wurde ein Responsive-Design eingeführt, sodass das Spiel auf kleinen und großen Bildschirmen funktionert. Hinzu kommt eine Userseite auf welcher ein User Profil angezeigt wird, welches ein Diagramm über die totalen Gewinne und Verluste anzeigt.

== Backend
Damit der KI-Gegner eine fundierte Entscheidung treffen kann, müssen dem LLM die möglichen Minimum- und Maximumwerte der Attribute bekannt sein, wie dem Feedback der Präsentation entnommen wurde. Daher wurde der Prompt, der an das LLM-Modell weitergereicht wird in dieser Hinsicht optmiert. Bei erstmaliger Optimierung erhielten alle Attribute die Minimum- und Maximumwerte 0 bis 100.

Eine detaillierte Analyse der zugrundeliegenden DinoData.json Datei ergab jedoch, dass die tatsächlichen Wertebereiche stark variieren. So liegt beispielsweise der Wert für „Speed“ konstant zwischen 18 und 60, während das Attribut „Angriff“ Werte zwischen 40 und 98 aufweist. Ohne diese Information würde die KI eine Fehlentscheidung treffen: Ein Geschwindigkeitswert von 60 stellt das absolute Maximum dar und garantiert einen Sieg, wohingegen ein Angriffswert von 70 lediglich im mittleren Bereich liegt. Bei Beibehalten der starren Grenzen von 0-100 im Prompt, würde das Modell die Siegwahrscheinlichkeit von Geschwindigkeit niedriger einschätzen als die von Angriff, was fehlerhaft wäre. Um die Entscheidungsqualität zu optimieren, wurden die Minimum- und Maximumwerte eines jeden Attributs einzeln im Prompt definiert. Erst durch diesen Kontext ist das Modell dazu in der Lage eine fundierte Entscheidung zu treffen. Diese Promptanpassung funktioniert allerdings nur, da es in dieser Version nur ein zu spielendes Dinoset gibt.

== Allgemein
Im Anschluss an die Präsentation erfolgte neben der Optimierung der Features eine umfassende Optimierung der Codestruktur und der Namenskonventionen. Ein kritischer Punkt war die ursprüngliche Architektur des Frontends, bei der die gesamte Programmlogik in einer einzigen, unübersichtlichen Datei konzentriert war.

Um die Wartbarkeit und Lesbarkeit zu verbessern, wurde ein Refactoring durchgeführt. Dabei wurde die Logik in modulare Komponenten unterteilt und eine klare Verzeichnisstruktur eingeführt.

// ~3 Pages
= Erfahrung, Herausforderungen und Reflexion

== Positive Erfahrungen und Erfolgsgeschichten
Durch den gezielten und bewussten Einsatz von LLMs ist ein schneller Einstieg in neue Technologien möglich gewesen, insbesondere auch bei der Technologienfindung hat der Einsatz der LLMs sich als positiv erwiesen. Bei simplen Fehlern konnten LLMs schnell Abhilfe schaffen und den Entwicklungsprozess beschleunigen. 

Als überwiegend positiv hat sich auch die Frage nach Beispielen herausgestellt, wenn es beispielsweise darum ging wie man eine neue Technologie einsetzt.


== Schwierigkeiten und Problemstellung
Falschinformationen durch LLMs und unklare Grenzen der LLMs haben zu Schwierigkeiten und teils längeren Umwegen geführt. Diese Umwege mussten dann menschlich korrigiert werden und die Fehlerbehebung fand dann mithilfe der traditionellen Fehlersuche statt (Google, Stackoverflow, Reddit usw.). Als Problematisch hat sich deshalb erwiesen, das die LLM bei fast jeder Frage eine Antwort generiert wird, auch wenn diese falsch ist oder die Antwort unbekannt. Daher haben wir auch gezielt eine Überprüfung der Antworten von phi3:mini vorgenommen, sowie deren Fallback-Logik, damit das Spiel nicht einfach in der Mitte eines Durchlaufs stehen bleibt.

== Grenzen und Risiken von LLMs
Besonders beim debugging und der Fehlersuche wurden die Schwächen und Grenzen der LLMs deutlich. Hierbei ist ein Kreislauf des Todes bei Problemen nicht unüblich. Exemplarisch Darstellung dieser Prompts: 
- "Bitte löse das Problem ...""
- "Ja, klar ich hier die verbesserte Version:" 
Und es ist dann entweder genau der gegebene Code, es ist ein zuvor gegebener Code mit Fehlern oder es ist neuer Code mit neuen Fehlern.

Deutlich wurde die Fehlerhäufigkeit bei größerem Scope, also z.B. langen Codeabschnitten. Hierbei ist unserer Beobachtung nach die Fehlerquote deutlich höher als bei kleineren Codeabschnitten.

== Lessons Learned und Empfehlungen
Fantastisch beim Einstieg in neue Technologien, Brainstorming, einfachen kreativen Aufgaben aber nicht als alleinige Lösung geeignet.

Ist allerdings ein bereits fertiges Backend vorhanden und man möchte sich die Arbeit für das Frontend sparen, so ist der Einsatz von LLMs durchaus sinnvoll. Wichtig ist dabei in kleinen Schritten vorzugehen und klare Anweisungen zu geben. 

Ist man in einer Technologie versiert/erfahren, so kann man sich den Einsatz von LLMs durchaus sparen, da der Aufwand den man für die Qualitätssicherung und Fehlerbehebung betreiben muss, den Nutzen übersteigt.

== Zukunft der LLM-gestützten Entwicklung
Kurzfristig: Hauptsächlich als Unterstützung für Entwickler, nicht als Ersatz (Effizienzsteigerung, Ideenfindung, Dokumentationserstellung).

Mittel- bis langfristig: LLM als hauptsächlicher Entwickler (Code-Generierung) und Entwickler als Architekt und Qualitätsmanager

// 1-2 Pages
= Zusammenfassung und Fazit

== Zusammenfassung der Arbeit

== Persönliche Reflexion und Fazit

/*
= Acronyms

Use the `acr` function to insert acronyms, which looks like this #acr("HTTP").

#acrlpl("API") are used to define the interaction between different software systems.

#acrs("REST") is an architectural style for networked applications.

== Glossary

Use the `gls` function to insert glossary terms, which looks like this:

A #gls("Vulnerability") is a weakness in a system that can be exploited.

== Lists

Create bullet lists or numbered lists.

- This
- is a
- bullet list

+ It also
+ works with
+ numbered lists!

== Figures and Tables

Create figures or tables like this:

=== Figures

#figure(caption: "Image Example", image(width: 4cm, "assets/ts.svg"))

=== Tables

#figure(
  caption: "Table Example",
  table(
    columns: (1fr, 50%, auto),
    inset: 10pt,
    align: horizon,
    table.header(
      [],
      [*Area*],
      [*Parameters*],
    ),

    text("cylinder.svg"),
    $ pi h (D^2 - d^2) / 4 $,
    [
      $h$: height \
      $D$: outer radius \
      $d$: inner radius
    ],

    text("tetrahedron.svg"), $ sqrt(2) / 12 a^3 $, [$a$: edge length],
  ),
)<table>

== Code Snippets

Insert code snippets like this:

#figure(
  caption: "Codeblock Example",
  sourcecode[```ts
    const ReactComponent = () => {
      return (
        <div>
          <h1>Hello World</h1>
        </div>
      );
    };

    export default ReactComponent;
    ```],
)

#pagebreak()

== References

Cite like this #cite(form: "prose", <iso18004>).
Or like this @iso18004.

You can also reference by adding `<ref>` with the desired name after figures or headings.

For example this @table references the table on the previous page.

*/
