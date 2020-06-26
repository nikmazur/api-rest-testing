package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.mockserver.integration.ClientAndServer;

import static server.Expectations.*;

public class RunServer {

    public static void main(String[] args) throws JsonProcessingException {

        ClientAndServer.startClientAndServer(8188);
        new Data();

        ping();
        getEmployees();
        addEmployee();
        delEmployee();
        random();
    }
}
