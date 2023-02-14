package helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Attachment;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllureFilter implements OrderedFilter {

    private final String line = System.lineSeparator();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        logRequest("Request", requestTextBuilder(requestSpec).getBytes(StandardCharsets.UTF_8));
        logRequest("Request cURL", requestCurlBuilder(requestSpec).getBytes(StandardCharsets.UTF_8));
        Response response = ctx.next(requestSpec, responseSpec);
        logResponse(responseTextBuilder(response).getBytes(StandardCharsets.UTF_8));
        return response;
    }

    private String requestTextBuilder(FilterableRequestSpecification req){
        StringBuilder text = new StringBuilder();

        text.append("Method: ").append(req.getMethod()).append(line);
        text.append("URI: ").append(req.getURI()).append(line);
        text.append("Headers: ");
        req.getHeaders().asList().forEach(x ->
                text.append(x.getName()).append("=").append(x.getValue()).append(", "));
        text.append(line);

        if(req.getBody() != null && !req.getBody().toString().isEmpty())
            text.append("Body: ").append(line).append(textToJson(req.getBody().toString())).append(line);

        return text.toString();
    }

    private String requestCurlBuilder(FilterableRequestSpecification req){
        StringBuilder text = new StringBuilder();

        text.append("curl -X").append(req.getMethod());
        req.getHeaders().asList().forEach(x ->
                text.append(" -H '").append(x.getName()).append(": ").append(x.getValue()).append("'"));

        if(req.getBody() != null && !req.getBody().toString().isEmpty())
            text.append(" -d '").append(req.getBody().toString()).append("'");

        text.append(" '").append(req.getURI()).append("' ");

        return text.toString();
    }

    private String responseTextBuilder(Response resp) {
        return resp.statusLine() + line + textToJson(resp.asString());
    }

    private String textToJson(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(text, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (JsonProcessingException jpe) {
            Logger.getGlobal().log(Level.SEVERE, jpe.getMessage());
        }
        return "";
    }

    @Attachment(value = "{0}", type = "application/json")
    private byte[] logRequest(String name, byte[] stream) {
        return stream;
    }

    @Attachment(value = "Response", type = "application/json")
    private byte[] logResponse(byte[] stream) {
        return stream;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
