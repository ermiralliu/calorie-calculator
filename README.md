## calorie-calculator

A MVC Spring Boot Web Application which tracks food calories and expenditure.

### Routes

#### Root Routes ```/```
- ```GET /``` redirects to ```/food``` which is the main page (or to login if the user is not logged in).
- ```GET /register``` returns the register page.
- ```GET /login``` leads to the login page, which is managed by Spring Security.

#### User Routes ```/user```
- ```POST /user/register``` -> if email exists, inserts a new user and redirects to ```/login```, else reloads the current page and displays a message.
---
The routes above can be accessed unauthorized.

For the routes below, you need to be logged in:

#### Food Routes ```/food```
- ```GET /food``` returns the main view, with the food list and messages about calories and expenditure.
- ```POST /food/add``` adds a new food entry and redirects to ```/food```.
- ```GET /food/get-interval``` returns a page with foodEntries filtered by start-date and end-date.

#### Food Api Routes ```/api/food```
- ```GET /api/food``` return a json containing paginated food entries. Used by ```/food```.
---
The routes below you need to have an <strong>admin</strong> role:
#### Admin Routes ```/admin``` (Only accessible by admin)
- ```GET /admin ``` leads to the admin page.
- ```GET /admin/user``` gets the food entries for a specific user (by user_id parameter) so you can view and select to edit them.
- ```GET /admin/food/update``` shows a view (with current entry data by food entry id) to edit or delete them.
- ```POST /admin/food/update``` sends the data to update food entry and redirects to current page.
- ```POST /admin/food/delete``` deletes current food entry by id and redirects to ```/admin``` and shows a dialog.


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

### Usage

You have to wait a bit for the database seeding to be done (would be removed in a full production build).

Then you can register your own user, or use one of the 5 seeded users:
| email | password|
|-|-|
|admin@example.com| adminpassword|
|ermir@example.com|passwordErmir|
|holta@example.com|passwordHolta|
|orkida@example.com|passwordOrkida|
|selma@example.com|passwordSelma|

admin@example.com is the only one which has access to `/admin`.
Admin can select a user and view their food entries and edit and delete those food entries.

For more, check the pdf-s.

### Todo

- Admin reports: List users which have exceeded the expenditure limit of the last month.