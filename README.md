## This is an old version of the project, which used Maven (replaced with Gradle) and Spring Boot (replaced with MockServer).

=================================

# REST API Testing
This project emulates a simple REST server and tests for it. It contains a small database of employees and supports basic modification methods like add and delete. New employee data is generated randomly for each run and stored locally between executions.

## Run
Needs JDK and Maven to be installed.
```bash
mvn clean test allure:serve
```
This will start an Apache Tomcat web server on localhost:8188, and all tests will be executed against it. Upon completion an Allure report will be generated and opened in the default browser.

## [Download sample report](https://github.com/nikmazur/REST-API-Testing/raw/oldVersionMavenSpringBoot/allure-report.zip)
![alt text](https://github.com/nikmazur/REST-API-Testing/blob/oldVersionMavenSpringBoot/allure_screen.png "Allure Report")

The report contains data on each test, parameters which were used for it, execution and retry history. All API requests and responses are also logged and attached to each test.
 
Tests were written with TestNG as the testing framework, REST Assured for managing requests and responses, and Allure for reporting. For the REST web server part Spring Boot was used: https://spring.io/guides/gs/rest-service/ The server part can also be run from the main class for manual testing (e.g. from Postman or Insomnia).
