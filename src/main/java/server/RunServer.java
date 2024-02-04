package server;

import org.mockserver.integration.ClientAndServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.LogManager;

import static helpers.ServerConfig.CONF;
import static io.netty.util.CharsetUtil.UTF_8;
import static server.Data.initEmployees;
import static server.Expectations.*;

public class RunServer {

    public static void main(String[] args) throws IOException {
        ClientAndServer.startClientAndServer(CONF.port());

        // Log to console if ran from main, to file if from test. Test sends 'testing' as arg.
        if(args.length != 0 && Objects.equals(args[0], "testing"))
            logToFile();
        else
            logToConsole();

        initEmployees();

        // Setting expectations
        ping();
        getEmployees();
        addEmployee();
        delEmployee();
        random();
    }

    private static void logToConsole() throws IOException {
        var loggingConfiguration = """
                java.util.logging.SimpleFormatter.format=[%1$tF %1$tT.%1$tL]  %3$s  %4$s  %5$s %6$s%n
                        handlers=org.mockserver.logging.StandardOutConsoleHandler
                        org.mockserver.logging.StandardOutConsoleHandler.level=ALL""";
        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(loggingConfiguration.getBytes(UTF_8)));
    }

    private static void logToFile() throws IOException {
        var loggingConfiguration = """
                java.util.logging.SimpleFormatter.format=[%1$tF %1$tT.%1$tL]  %3$s  %4$s  %5$s %6$s%n
                        handlers=java.util.logging.FileHandler
                        java.util.logging.FileHandler.level=ALL
                        java.util.logging.FileHandler.formatter=java.util.logging.SimpleFormatter
                        java.util.logging.FileHandler.pattern=""" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_Server.log";
        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(loggingConfiguration.getBytes(UTF_8)));
    }
}
