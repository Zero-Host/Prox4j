package zero.host.Prox4j.v1.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Credentials {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String username;
    private String password;
    private Realm realm;


    public Credentials(String username, String password, Realm realm) {
        this.username = username;
        this.password = password;
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Realm getRealm() {
        return realm;
    }

}
