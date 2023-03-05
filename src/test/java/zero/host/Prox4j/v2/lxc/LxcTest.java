package zero.host.Prox4j.v2.lxc;


import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zero.host.Prox4j.v2.node.Credentials;
import zero.host.Prox4j.v2.node.Node;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LxcTest {

    static Node node;

    @BeforeAll
    static void beforeAll() throws IOException {
        Credentials credentials = new Credentials("hv01");
        node = new Node(credentials);


        Unirest.config().verifySsl(false);
    }


    @Test
    void Test() throws LXC.LXCException {
        LXC.Status status;

        LXC.Clone(node, 109,200, "Prox4j");
        do {
            status = LXC.Status(node, 200);
        } while (status != LXC.Status.Stopped);
        Assertions.assertEquals(status, LXC.Status.Stopped);


        LXC.Start(node, 200);
        do {
            status = LXC.Status(node, 200);
        } while (status != LXC.Status.Running);
        Assertions.assertEquals(status, LXC.Status.Running);


        LXC.Stop(node, 200);
        do {
            status = LXC.Status(node, 200);
        } while (status != LXC.Status.Stopped);
        Assertions.assertEquals(status, LXC.Status.Stopped);

        LXC.Delete(node, 200);
    }


    @AfterAll
    static void afterAll() {

    }
}
