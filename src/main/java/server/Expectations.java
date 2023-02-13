package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import models.Employee;
import org.apache.commons.lang3.math.NumberUtils;
import org.mockserver.client.MockServerClient;

import java.security.SecureRandom;

import static helpers.ServerConfig.CONF;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static server.Data.*;
import static server.Expectations.Methods.*;

@UtilityClass
public class Expectations {

    static ObjectMapper mapper = new ObjectMapper();
    static MockServerClient mockServerClient = new MockServerClient(CONF.address(), CONF.port());

    @AllArgsConstructor
    public enum Methods {
        GET("GET"), POST("POST"), DELETE("DELETE");

        @Getter
        private final String value;
    }

    private static final String EMPLOYEES = "/employees";
    private static final String PING = "/ping";
    private static final String RANDOM = "/random";

    public static void ping() {
        mockServerClient
                .when(
                        request().withMethod(GET.value).withPath(PING))
                .respond(
                        response().withStatusCode(200));
    }

    public static void getEmployees() throws JsonProcessingException {
        mockServerClient
                .when(
                        request().withMethod(GET.value).withPath(EMPLOYEES))
                .respond(
                        response().withStatusCode(200).withBody(mapper.writeValueAsString(getComp())));
    }

    public static void addEmployee() {
        mockServerClient
                .when(
                        request().withMethod(POST.value).withPath(EMPLOYEES))
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
                        request().withMethod(DELETE.value).withPath(EMPLOYEES).withHeaders(header("delete.*")))
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
                        request().withMethod(GET.value).withPath(RANDOM))
                .respond(
                        request -> {
                            if(new SecureRandom().nextBoolean())
                                return response().withStatusCode(200);
                            else
                                return response().withStatusCode(500);
                        });
    }
}
