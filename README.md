# REST-API-Testing
This project emulates a simple REST server and unit tests for it. 

When started with 'mvn test' an Apache Tomcat web server on localhost HTTP port 8188 is launched, and then all tests are executed against it.

For the REST web server part I used Spring Boot and this short tutorial:
https://spring.io/guides/gs/rest-service/

The server part can also be run from the main class for manual testing (e.g. from Postman).

Tests were written with TestNG as the testing framework and REST Assured for managing requests and responses to the server. 

Overall there are currently 6 positive and 6 negative tests.
