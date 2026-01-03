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
#link("https://example.com")[
  See example.com
]
Hier Diagramm einfügen mit mermaidchart VSCode Erweiterung und das Chart in Github docu speichern.
== Frontend/Benutzeroberfläche

Von dem Startscreen aus kann ein Spiel gestartet werden. Nach dem Start, kann aber auch jederzeit ein neues Spiel begonnen werden. Im Spiel sind oben die Anzahl der Karte pro Deck zu sehen. Darunter die obersterste Karte des Spielers, diese beinhaltet Infos zu dem Dino. Dazu gehören, Name, Bild und die 6 Attribute (Lifespan in years, length, speed in km/h, Intelligence, Attack, Defense) für die Spielmechanik. Diese Attribute können durch einen Klick für den Vergleich ausgewählt werden. 

...Spielabflauf weiter beschreiben...

Typescript, React

== Backend, Datenbank und Schnittstellen

Java, Spring Boot, REST API, PostgreSQL and Redis, Docker, Flyway, Ollama, phi3:mini

== LLM-Integration (MCP, Wrapper, API)

REST-Wrapper auf Ollama.

== Sicherheits- und Authentifizierungsmechanismen
Entfallen, da kein Login oder sensible Daten verarbeitet werden.

// ~3 Pages
= Implementierung

== Vorgehensmodell und Projektmanagement

== Code-Generierung und Entwicklung

== Datenmodelle und Schnittstellenimplementierung

== Versionskontrolle, Build- und CI-Prozesse

// ~4 Pages
= Einsatz der LLMs und Prompt Engineering

== Auswahl der LLMs und Dienste

== Aufbau und Dokumentation der Prompts

== Generierte Assets (Code, Bilder, Audio, Video, Text)
Stammdaten in Form von JSON, Bilder für die Karten

== Fehleranalyse und Optimierung

== Qualitätssicherung bei der LLM-Nutzung

// ~3 Pages
= Erfahrung, Herausforderungen und Reflexion

== Positive Erfahrungen und Erfolgsgeschichten
Schneller Einstieg in Technologien, durch den Einsatz von LLMs

== Schwierigkeiten und Problemstellung

== Grenzen und Risiken von LLMs
Kreislauf des Todes bei Problemen

== Lessons Learned und Empfehlungen
Fantastisch beim Einstieg in neue Technologien, aber nicht als alleinige Lösung geeignet

== Zukunft der LLM-gestützten Entwicklung
Hauptsächlich als Unterstützung für Entwickler, nicht als Ersatz

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
