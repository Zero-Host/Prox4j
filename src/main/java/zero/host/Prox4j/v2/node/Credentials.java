package zero.host.Prox4j.v2.node;

import java.io.IOException;
import java.util.Properties;

public class Credentials {
    private String name;
    private String host;
    private int port;
    private String token;

    public Credentials(String file) throws IOException {
        Properties prop = new Properties();
        prop.load(Credentials.class.getClassLoader().getResourceAsStream("prox4j/" + file + ".properties"));

        name= prop.getProperty("proxmox.api.v2.name");
        host= prop.getProperty("proxmox.api.v2.host");
        port = Integer.parseInt(prop.getProperty("proxmox.api.v2.port"));
        token= prop.getProperty("proxmox.api.v2.token");
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getToken() {
        return token;
    }
}
