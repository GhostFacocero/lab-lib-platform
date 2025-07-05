# Requisiti
- JDK 17
- Maven Apache

# Build del progetto
- `mvn clean install` → installa le librerie presenti nel `pom.xml` (da eseguire in entrambi i progetti)
### Per VS Code:
- Se dopo `mvn install` il progetto dà problemi strani, provare a eseguire `Java: Clean the Java language server workspace` da `Ctrl+Shift+P`

# Avvio dell'app
- Inserire il **.env per la restapi** in `./lab-lib-restapi/src/main/resources`
- Inserire il **.env per il frontend** in `./lab-lib-frontend/src/main/resources`
- far partire il progetto **restapi** eseguendo `App.java` dentro `./src/Main`
- far partire il progetto **frontend** con il comando `mvn clean javafx:run` eseguito dentro `./lab-lib-frotnend`