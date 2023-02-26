package zero.host.Prox4j.v1.node;

import zero.host.Prox4j.v1.node.lxc.CreateConfig;
import zero.host.Prox4j.v1.node.lxc.LinuxContainer;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String url;
    private String host;
    private int port;

    private Credentials credentials;
    private Ticket ticket;
    private NodeInfo info;

    // ===================== HEADER =====================

    public Node(String host, int port, Credentials credentials) throws NodeException {
        this.url = "https://" + host + ":" + port;
        this.host = host;
        this.port = port;
        this.credentials = credentials;

        this.ticket = new Ticket(this, credentials);

        testConnection();
        connect();
    }

    // ===================== CODE =====================

    /**
     * Tests the connection and credentials of the node
     * @return true if connection and credentials are ok
     */
    public boolean testConnection() {
        logger.info("Testing connection...");
        try {
            this.ticket.requestTicket();
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Connection test failed!");
            return false;
        }

        logger.info("Connection test successful!");
        return true;
    }

    public LinuxContainer createLXC(CreateConfig createConfig) throws LinuxContainer.LXCException {
        return new LinuxContainer(this, createConfig);
    }

    public LinuxContainer connectLXC(int vmid) throws LinuxContainer.LXCException {
        return new LinuxContainer(this, vmid);
    }

    private void connect() throws NodeException {
        try {

            Unirest.config().addDefaultCookie("PVEAuthCookie", ticket.getTicket());
            HttpResponse<JsonNode> response = Unirest.get(url + "/api2/json/nodes")
                    .header("CSRFPreventionToken", ticket.getCsrfPreventionToken())
                    .asJson();


            JSONObject data = response.getBody().getObject().getJSONArray("data").getJSONObject(0);

            info = new NodeInfo(data);
        } catch (Exception e) {
            throw new NodeException(e.getMessage());
        }
    }





    // ===================== GETTER / SETTER ===================== //

    public Credentials getCredentials() {
        return credentials;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public Ticket getTicket() throws Ticket.AuthenticationException {
        return ticket.requestTicket();
    }

    public NodeInfo getInfo() {
        return info;
    }

    // ===================== EXCEPTIONS ===================== //


    public class NodeException extends Exception {
        public NodeException(String errorMessage) {
            super(errorMessage);
        }
    }

}
