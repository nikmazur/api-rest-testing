package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Employee;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.mockserver.client.MockServerClient;

import static helpers.ServerConfig.CONF;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static server.Data.*;

public class Expectations {

    static ObjectMapper mapper = new ObjectMapper();
    static MockServerClient mockServerClient = new MockServerClient(CONF.address(), CONF.port());

    public static void ping() {
        mockServerClient
                .when(
                        request().withMethod("GET").withPath("/ping"))
                .respond(
                        response().withStatusCode(200));
    }

    public static void getEmployees() throws JsonProcessingException {
        mockServerClient
                .when(
                        request().withMethod("GET").withPath("/employees"))
                .respond(
                        response().withStatusCode(200).withBody(mapper.writeValueAsString(getComp())));
    }

    public static void addEmployee() {
        mockServerClient
                .when(
                        request().withMethod("POST").withPath("/employees"))
                .respond(
                        httpRequest -> {
                            // Deserialize from String in request body to POJO
                            Employee newEmpl = mapper.readValue((String) httpRequest.getBody().getValue(), Employee.class);
                            // Checks that Name is not empty, Title is not numeric
                            if (!newEmpl.getName().isEmpty() && newEmpl.getName().trim().length() > 0 &&
                                    !NumberUtils.isCreatable(newEmpl.getTitle())) {
                                newEmpl.setName(newEmpl.getName().replace(",", ""));
                                addEmpl(newEmpl);
                                return response().withStatusCode(201).withBody(mapper.writeValueAsString(getComp()));
                            } else {
                                return response().withStatusCode(400).withBody(mapper.writeValueAsString(getComp()));
                            }
                        }
                );
    }

    public static void delEmployee() {
        mockServerClient
                .when(
                        request().withMethod("DELETE").withPath("/employees").withHeaders(header("delete.*")))
                .respond(
                        httpRequest -> {
                            // Filter out the 'delete header' and get value
                            String value = httpRequest.getHeaderList().stream()
                                    .filter(x -> x.getName().toString().contains("delete"))
                                    .findFirst().get().getValues().get(0).toString();

                            // Check if number or text
                            if(NumberUtils.isCreatable(value)) {
                                delEmplIndex(NumberUtils.toInt(value));
                                return response().withStatusCode(200).withBody(mapper.writeValueAsString(getComp()));
                            } else if(!value.isEmpty()) {
                                delEmplName(value);
                                return response().withStatusCode(200).withBody(mapper.writeValueAsString(getComp()));
                            } else {
                                return response().withStatusCode(400).withBody(mapper.writeValueAsString(getComp()));
                            }
                        }
                );
    }

    public static void random() {
        mockServerClient
                .when(
                        request().withMethod("GET").withPath("/random"))
                .respond(
                        request -> {
                            if(RandomUtils.nextBoolean())
                                return response().withStatusCode(200);
                            else
                                return response().withStatusCode(500);
                        });
    }
}
