package zero.host.Prox4j.v2.lxc;

import zero.host.Prox4j.v2.node.Node;
import kong.unirest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ticket {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String ticket;
    private String csrfPreventionToken;

    public Ticket(Node node, String username, String password, String realm) throws AuthenticationException {
        String reqUrl = node.getUrl() + "/api2/json/access/ticket";

        logger.info("Requesting api ticket from: " + reqUrl);
        HttpResponse<JsonNode> response;

        try {
            response = Unirest.post(reqUrl)
                    .queryString("username", username)
                    .queryString("password", password)
                    .queryString("realm", realm)
                    .asJson();

        } catch (UnirestException e) {
            throw new AuthenticationException(e.getMessage());
        }

        if(response.getStatusText().equals("authentication failure")) throw new AuthenticationException("Authentication Failure!");
        if(response.getBody().getObject().isNull("data")) throw new AuthenticationException("Data was Null! " + response.getStatusText());

        this.ticket = response.getBody().getObject().getJSONObject("data").getString("ticket");
        this.csrfPreventionToken = response.getBody().getObject().getJSONObject("data").getString("CSRFPreventionToken");
    }

    public String getTicket() {
        return ticket;
    }

    public String getCsrfPreventionToken() {
        return csrfPreventionToken;
    }

    public class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}