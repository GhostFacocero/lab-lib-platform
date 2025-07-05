# Requisiti
- JDK 17
- Apache Maven

# Build del progetto
Eseguire il seguente comando in entrambi i progetti (`lab-lib-restapi` e `lab-lib-frontend`):

```bash
mvn clean install
```

Questo comando installerà le dipendenze specificate nel `pom.xml`.

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
Questa architettura permette di mantenere il codice modulare, testabile e facilmente estendibile, separando chiaramente responsabilità e facilitando la sostituzione o estensione di componenti.