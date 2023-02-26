package zero.host.Prox4j.v1.node;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ticket {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final long TicketTimeout = 2000;

    private Node node;
    private Credentials credentials;

    private long ticketDate = 0;
    private String ticket = "";
    private String csrfPreventionToken = "";


    // ==================== HEADER ==================== //

    public Ticket(Node node, Credentials credentials) {
        this.node = node;
        this.credentials = credentials;
    }

    // ==================== CODE ==================== //
    private boolean hasTicket() {
        if(ticketDate == 0 || ticket == "" || csrfPreventionToken == "") {
            logger.info("Has ticket? No - Ticket not initialized!");
            return false;
        }

        if((System.currentTimeMillis()-ticketDate) > TicketTimeout) {
            logger.info("Has ticket? No - Out of date!");
            return false;
        }

        logger.info("Has ticket? Yes");
        return true;
    }

    private Ticket request() throws AuthenticationException {
        String reqUrl = node.getUrl() + "/api2/json/access/ticket";

        logger.info("Requesting api ticket from: " + reqUrl);
        HttpResponse<JsonNode> response = Unirest.post(reqUrl)
                .queryString("username", credentials.getUsername())
                .queryString("password", credentials.getPassword())
                .queryString("realm", credentials.getRealm().getText())
                .asJson();

        if(response.getStatusText().equals("authentication failure")) throw new AuthenticationException("Authentication Failure!");
        if(response.getBody().getObject().isNull("data")) throw new AuthenticationException("Data was Null! " + response.getStatusText());

        this.ticket = response.getBody().getObject().getJSONObject("data").getString("ticket");
        this.csrfPreventionToken = response.getBody().getObject().getJSONObject("data").getString("CSRFPreventionToken");
        this.ticketDate = System.currentTimeMillis();

        logger.info("Ticket request successful!");

        logger.debug("Ticket: " + ticket);
        logger.debug("CSRFPreventionToken: " + csrfPreventionToken);
        return this;
    }

    public Ticket requestTicket() throws AuthenticationException {
        if(hasTicket()) {return this;}

        return request();
    }


    // ==================== GETTER / SETTER ==================== //

    public String getTicket() throws AuthenticationException {
        requestTicket();
        return ticket;
    }

    public String getCsrfPreventionToken() throws AuthenticationException {
        requestTicket();
        return csrfPreventionToken;
    }


    // ==================== EXCEPTIONS ==================== //

    public class AuthenticationException extends Exception {
        public AuthenticationException(String errorMessage) {
            super(errorMessage);
        }
    }

    class DataNullException extends Exception {
        public DataNullException(String errorMessage) {
            super(errorMessage);
        }
    }

}
