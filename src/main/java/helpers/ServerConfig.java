package helpers;

import org.aeonbits.owner.Config;

@Config.Sources("file:src/main/resources/server.properties")
public interface ServerConfig extends Config {
    String address();
    int port();
}
