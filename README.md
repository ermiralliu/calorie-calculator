## calorie-calculator

A MVC Spring Boot Web Application which tracks food calories and expenditure.

### Routes

#### Root Routes ```/```
```GET /``` redirects to ```/food``` which is the main page.

```GET /register``` redirects to ```/user/register```, because registering has mostly to do with the User model.

```GET /login``` leads to the login page, which is managed by Spring Security.

#### User Routes ```/user```
```GET /user/register``` returns the register view.

```POST /user/register``` -> if email exists, inserts a new user and redirects to ```/login```, else reloads the current page and displays a message.

```PUT /user``` updates current user (unimplemented).

#### Food Routes ```/food```
```GET /food``` returns the main view, with the food list and messages about calories and expenditure.

```POST /food/add``` adds a new food entry and redirects to ```/food```.

#### Admin Routes ```/admin```
Leads to the admin page (currently unimplemented).


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