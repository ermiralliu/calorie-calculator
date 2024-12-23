## calorie-calculator

A MVC Spring Boot Web Application which tracks food calories and expenditure.

### Setting-up the project

#### Using an IDE:

- Open the project folder in your IDE of choice.
- Enable annotation processing in your project properties.
- Run ```maven install``` through your IDE's maven toolkit to install Spring Boot and other dependencies.
- Run the app itself

Notes:
- For Eclipse, you might have to manually install [lombok](https://projectlombok.org/download) for some of the annotation processing. Simply run the jar file after downloading and it will automatically find the IDE. Then proceed with the installation.

- Other IDE-s contain lombok by default.

#### Terminal-only:

You'll have to use maven package manager.
- Open the folder in your terminal of choice.
- Build the project with ```./mvnw clean install```.
- Then run it through: ```java -jar target/softi-0.0.1-SNAPSHOT.jar```