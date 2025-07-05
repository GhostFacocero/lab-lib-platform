# Requisiti
- JDK 17
- Apache Maven

# Build del progetto
Eseguire il seguente comando in entrambi i progetti (`lab-lib-restapi` e `lab-lib-frontend`):

```bash
mvn clean install
```

Questo comando installerà le dipendenze specificate nel `pom.xml`.

# Librerie utilizzate:

### Backend

| Libreria                              | Versione | Scopo                 | Descrizione                                                           |
| ------------------------------------- | -------- | --------------------- | --------------------------------------------------------------------- |
| `junit:junit`                         | 3.8.1    | `test`                | Framework per i test unitari.                                         |
| `spring-boot-starter-web`             | 3.2.0    | -                     | Starter Spring Boot per applicazioni web e REST API.                  |
| `spring-boot-starter-data-jpa`        | 3.2.0    | -                     | Starter per Spring Data JPA (accesso e gestione DB relazionali).      |
| `org.postgresql:postgresql`           | 42.7.1   | `runtime`             | Driver JDBC per PostgreSQL.                                           |
| `spring-boot-devtools`                | 3.2.0    | `runtime` (opzionale) | Strumenti per hot-reload e sviluppo rapido con Spring Boot.           |
| `org.projectlombok:lombok`            | 1.18.28  | `provided`            | Libreria per ridurre il boilerplate in Java (getter, setter, ecc.).   |
| `io.github.cdimascio:dotenv-java`     | 3.0.0    | -                     | Caricatore di file `.env` in Java.                                    |
| `springdoc-openapi-starter-webmvc-ui` | 2.3.0    | -                     | Generatore di documentazione Swagger/OpenAPI per progetti Spring MVC. |

### Frontend

| Libreria                                      | Versione | Scopo  | Descrizione                                                            |
| --------------------------------------------- | -------- | ------ | ---------------------------------------------------------------------- |
| `junit:junit`                                 | 3.8.1    | `test` | Framework di test unitario per Java.                                   |
| `org.openjfx:javafx-controls`                 | 20       | -      | Componenti UI di JavaFX come bottoni, liste, layout, ecc.              |
| `org.openjfx:javafx-fxml`                     | 20       | -      | Supporto per la definizione dell'interfaccia utente tramite file FXML. |
| `com.fasterxml.jackson.core:jackson-databind` | 2.15.2   | -      | Parser/mapper JSON per Java, parte di Jackson.                         |
| `io.github.cdimascio:dotenv-java`             | 3.0.0    | -      | Lettura di file `.env` per caricare variabili ambiente.                |
| `com.google.inject:guice`                     | 5.1.0    | -      | Dependency injection framework sviluppato da Google.                   |

| Plugin                            | Versione | Descrizione                                             |
| --------------------------------- | -------- | ------------------------------------------------------- |
| `org.openjfx:javafx-maven-plugin` | 0.0.8    | Permette di eseguire applicazioni JavaFX tramite Maven. |



### Per Visual Studio Code
Se dopo aver eseguito `mvn install` il progetto presenta problemi strani, provare a eseguire:

> `Java: Clean the Java language server workspace`

(digitare `Ctrl+Shift+P` per aprire la palette comandi)

---

# Avvio dell'app

## 1. Variabili d'ambiente
- Inserire il file **`.env` per la REST API** in:
  ```
  ./lab-lib-restapi/src/main/resources
  ```

- Inserire il file **`.env` per il frontend** in:
  ```
  ./lab-lib-frontend/src/main/resources
  ```

## 2. Avvio dei progetti

### REST API
Avviare il file `App.java` dentro la cartella:

```
./lab-lib-restapi/src/main/java/com/lab_lib/restapi
```

  dopo di che sarà visibile l'anteprima degli endpoint Swagger all'indirizzo:

  [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

### Frontend JavaFX
Da terminale, eseguire il seguente comando nella cartella `lab-lib-frontend`:

```bash
mvn clean javafx:run
```
# Architettura e Design Pattern

### Backend (`lab-lib-restapi`)

Al momento il backend adotta una struttura semplice e diretta senza un layer di service dedicato. La divisione è organizzata per entità, con:

- Un **Controller** specifico per ogni entità (es. `BookController`) che espone gli endpoint REST.
- Un corrispondente **Repository** (es. `BookRepository`) che gestisce l’accesso al database e le operazioni CRUD.
- Una **classe modello** (es. `Book`) che rappresenta l’entità stessa.

Questa struttura mantiene il codice chiaro e facile da seguire, anche se in futuro si potrà introdurre un layer di service per separare meglio la logica di business.

---

### Frontend (`lab-lib-frontend`)

Il frontend utilizza un approccio basato su dependency injection (DI) tramite la libreria di Google (Guice):

- I **controller JavaFX** associati a ogni pagina o componente non interagiscono direttamente con i dati, ma tramite **servizi iniettati** che implementano interfacce.
- Il modulo `Utils` contiene classi concrete senza astrazioni, utilizzate come implementazioni dirette da iniettare nei servizi.
- Nel modulo `DI` si trova la classe `AppModule`, dove vengono configurate le iniezioni, associando interfacce alle loro implementazioni o direttamente classi a istanze concrete, ad esempio:
  ```java
  // Collegamento implementazione a interfaccia
  bind(IBookService.class).to(BookService.class);
        
  // Collegamento a implementazioni concrete
  bind(HttpUtil.class).toInstance(new HttpUtil());
  ```

- e in seguito per utilizzare l'implementazione in qualsiasi classe basterà scrive qualcosa di simile:
  ```java
  private final IBookService bookService;

  @Inject
  public BookListPage(IBookService bookService) {
      this.bookService = bookService;
  }
  ```

Questa architettura permette di mantenere il codice modulare, testabile e facilmente estendibile, separando chiaramente responsabilità e facilitando la sostituzione o estensione di componenti.

## Linee guida per i commit e la gestione dei branch

### Convenzioni per i messaggi di commit

Per mantenere il repository ordinato e leggibile, si seguono queste regole per scrivere i messaggi di commit:

- Il messaggio deve iniziare con uno di questi prefissi standard:
  - `feat:` per una nuova funzionalità
  - `fix:` per una correzione di bug
  - `docs:` per modifiche alla documentazione
  - `style:` per modifiche di formattazione o stile senza impatto sul codice
  - `refactor:` per ristrutturazioni del codice senza cambiamenti funzionali
  - `test:` per aggiunta o modifica di test
  - `chore:` per altre attività di manutenzione (es. aggiornamento dipendenze)

- Dopo il prefisso si può scrivere liberamente il messaggio di commit, in **qualsiasi lingua**.

---
### Creazione di branch

- Per creare un nuovo branch, usare sempre il formato:

`<tipo>/<descrizione-breve-in-minuscolo-e-trattini>`

dove `<tipo>` può essere `feat`, `fix`, `docs`, `chore`, ecc., coerente con i prefissi dei commit.

La descrizione dovrebbe sintetizzare chiaramente la modifica o la feature su cui si lavora, ad esempio:

  feat/aggiunta-paginazione-tabella <br />
  fix/correzione-colore-bottoni <br />
  docs/aggiornamento-readme <br />

È consigliato scrivere la descrizione del branch in inglese per uniformità, ma non è obbligatorio: va bene anche un’altra lingua se più comoda.


### Branch principali

- Il repository deve sempre contenere due branch principali, **`main`** e **`development`**:
  - `main` è il ramo stabile con le versioni di produzione
  - `development` è il ramo dove si integrano le nuove feature e correzioni prima di andare in `main`

- **Entrambi i branch devono essere sempre presenti** e non vanno mai eliminati, nemmeno dopo merge o pulizie.

---

### Merge e descrizioni

- Ogni merge verso `development` o `main` deve essere accompagnato da un titolo chiaro e una descrizione esaustiva, scritti **in italiano**.
- Questa descrizione sarà la prima cosa che verrà letta e analizzata, quindi deve spiegare chiaramente cosa è stato fatto, perché e come.
- Scrivere sempre in modo completo e comprensibile per facilitare la revisione e la tracciabilità delle modifiche.
