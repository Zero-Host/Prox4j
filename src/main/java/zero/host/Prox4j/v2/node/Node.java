package zero.host.Prox4j.v2.node;

import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Credentials credentials;

    public Node(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getUrl() {
        return  "https://" + credentials.getHost() + ":" + credentials.getPort();
    }

    // ###### Unirest ###### //

    public GetRequest getUnirestGet(String url) {
        return Unirest.get(url).header("Authorization", "PVEAPIToken="+credentials.getToken());
    }

    public HttpRequestWithBody getUnirestPost(String url) {
        return Unirest.post(url).header("Authorization", "PVEAPIToken="+credentials.getToken());
    }

    public HttpRequestWithBody getUnirestPut(String url) {
        return Unirest.put(url).header("Authorization", "PVEAPIToken="+credentials.getToken());
    }

    public HttpRequestWithBody getUnirestDelete(String url) {
        return Unirest.delete(url).header("Authorization", "PVEAPIToken="+credentials.getToken());
    }

    // ###### Getter / Setter ###### //


    public Credentials getCredentials() {
        return credentials;
    }

    public String getHost() {
        return credentials.getHost();
    }

    public int getPort() {
        return credentials.getPort();
    }

    public String getName() {
        return credentials.getName();
    }

}
