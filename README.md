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
