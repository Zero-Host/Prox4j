package zero.host.Prox4j.v1;

import zero.host.Prox4j.v1.node.Node;
import kong.unirest.Unirest;

import java.util.ArrayList;

public class Proxmox {

    private ArrayList<Node> nodes = new ArrayList<Node>();

    public Proxmox addNode(Node node) {
        nodes.add(node);
        return this;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }



    public static void verifySSL(boolean ssl) {
        Unirest.config().verifySsl(ssl);
    }

}
