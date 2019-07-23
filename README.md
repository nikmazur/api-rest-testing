# REST-API-Testing
This project emulates a simple REST server and tests for it. It contains a small database of employees and supports basic modification methods like add & delete. New employee data is generated randomly on each launch and stored locally.

To run the tests use:
$ mvn clean test allure:serve
This will start an Apache Tomcat web server on localhost port 8188, and then all tests will be executed against it.

Upon completion an Allure report will be generated & opened in the default browser.

For the REST web server part I used Spring Boot:
https://spring.io/guides/gs/rest-service/

The server part can also be run from the main class for manual testing (e.g. from Postman).

Tests were written with TestNG as the testing framework, REST Assured for managing requests and responses to the server and Allure for reporting.
