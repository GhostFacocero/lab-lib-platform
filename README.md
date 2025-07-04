# Requisiti
- JDK 17

# Build del progetto
- `mvn clean install` → installa le librerie presenti nel `pom.xml` (da eseguire in entrambi i progetti)

# Avvio dell'app
- Inserire il .env in `./lab-lib-restapi/src/main/resources`
- Eseguire `App.java` dentro `./src/Main`:
  1. prima far partire il progetto **restapi**
  2. poi il progetto **frontend**

# Consigli utili
### VS Code
- Se dopo `mvn install` il progetto dà problemi strani, provare a eseguire `Java: Clean the Java language server workspace` da `Ctrl+Shift+P`
