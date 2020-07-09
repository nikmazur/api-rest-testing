package helpers;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.Sources("file:src/main/resources/server.properties")
public interface ServerConfig extends Config {

    ServerConfig CONF = ConfigFactory.create(ServerConfig.class);

    String address();
    int port();
}
