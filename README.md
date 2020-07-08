# REST API Testing
This project emulates a simple REST server and tests for it. It contains a small database of employees and supports basic modification requests like adding and deleting. New employee data is generated randomly for each run and stored locally between executions.

## Run
Needs JDK and Gradle to be installed.
```bash
gradle clean test downloadAllure allureServe
```
This will start a REST API server on localhost:8188, and all tests will be executed against it. Upon completion an Allure report will be generated and opened in the default browser.

## [Download sample report](https://github.com/nikmazur/REST-API-Testing/raw/master/files/allure-report.zip)
![alt text](https://github.com/nikmazur/REST-API-Testing/blob/master/files/allure_screen.png "Allure Report")

The report contains data on each test, parameters which were used for it, and execution and retry history. All API requests and responses are also logged and attached to the report. Requests are logged in cURL, so that they can be easily imported and reproduced for manual testing.

![alt text](https://github.com/nikmazur/REST-API-Testing/blob/master/files/test_screen.png "Individual Test Report")
 
Tests were written with TestNG as the testing framework, REST Assured for managing API requests and responses, and Allure for reporting. Besides Allure, logging is also supported through slf4j, and any test run will produce a .log file in the project directory. It will list all requests and expectations on the server side. 

For the REST server part I used the MockServer library: https://www.mock-server.com/ The server can also be run from the main class for manual testing (e.g. from Postman).
