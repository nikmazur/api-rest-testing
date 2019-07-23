# REST-API- Testing
This project emulates a simple REST server and tests for it. It contains a small database of employees and supports basic modification methods like add & delete. New employee data is generated randomly for each run and stored locally between executions.

To run the tests use:
```bash
mvn clean test allure:serve
```
This will start an Apache Tomcat web server on localhost port 8188, and all tests will be executed against it. Upon completion an Allure report will be generated & opened in the default browser. 

## [Download sample report](https://github.com/nikmazur/REST-API-Testing/raw/master/allure-report.zip)
Screenshot:
![alt text](https://github.com/nikmazur/REST-API-Testing/blob/master/allure_screen.png "Allure Report")

The report contains data on each test, parameters which were used for it, execution & retry history. All API requests & responses are also logged in the report.
 
Tests were written with TestNG as the testing framework, REST Assured for managing requests and responses, and Allure for reporting. For the REST web server part I used Spring Boot: https://spring.io/guides/gs/rest-service/ The server part can also be run from the main class for manual testing (e.g. from Postman or Insomnia).
