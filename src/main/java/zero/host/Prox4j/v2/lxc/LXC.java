package zero.host.Prox4j.v2.lxc;

import zero.host.Prox4j.v2.node.Node;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LXC {
    private static final Logger logger = LoggerFactory.getLogger(LXC.class);

    private Node node;
    private int lxcID;

    public LXC(Node node, int lxcID) throws LXCException {
        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);

        this.node = node;
        this.lxcID = lxcID;
    }

    public String getMacAddress() throws LXCException {
        logger.info("LXC MacAddress " + lxcID);

        String url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID + "/config";
        HttpResponse<JsonNode> response = node.getUnirestGet(url).asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC MacAddress Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }

        String data = response.getBody().getObject().getJSONObject("data").getString("net0");

        String[] parts = data.split(",");

        for(String part : parts) {
            String[] parts2 = part.split("=");
            if(parts2[0].equals("hwaddr")) return parts2[1];
        }

        throw  new LXCException("No MacAddress found! \n" + response.getBody().toPrettyString());
    }

    // ####### STATICS WRAPPER ####### //

    public LXC clone(int newID, String newName) throws LXCException {
        Clone(node, lxcID, newID, newName);
        return new LXC(node, newID);
    }

    public LXC clone(int newID) throws LXCException {
        Clone(node, lxcID, newID);
        return new LXC(node, newID);
    }

    public void delete() throws LXCException {
        Delete(node , lxcID);
    }

    public void start() throws LXCException {
        Start(node, lxcID);
    }

    public void stop() throws LXCException {
        Stop(node, lxcID);
    }

    public void shutdown() throws LXCException {
        Shutdown(node, lxcID);
    }

    public void reboot() throws LXCException {
        Reboot(node, lxcID);
    }

    public Status status() throws LXCException {
        return Status(node, lxcID);
    }

    // ####### STATICS ####### //

    public static void Clone(Node node, int templateID, int lxcID, String lxcName) throws LXCException {
        logger.info("LXC Clone " + templateID + "  --> " + lxcID);

        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);
        if(templateID < 100) throw  new LXCException("templateID < 100 --> templateID = " + templateID);

        String url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + templateID + "/clone";
        HttpResponse<JsonNode> response = node
                .getUnirestPost(url)
                .field("newid", lxcID)
                .field("hostname", lxcName)
                .asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Clone Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }
    }

    public static void Clone(Node node, int templateID, int lxcID) throws LXCException {
        Clone(node, templateID,lxcID,"Unkown");
    }

    public static void Delete(Node node, int lxcID) throws LXCException {
        logger.info("LXC Delete " + lxcID);

        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);

        String url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID;
        HttpResponse<JsonNode> response = node.getUnirestDelete(url).asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Delete Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }
    }

    public static void Start(Node node, int lxcID) throws LXCException {
        logger.info("LXC Start " + lxcID);

        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);

        String url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID + "/status/start";
        HttpResponse<JsonNode> response = node.getUnirestPost(url).asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Start Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }
    }

    public static void Stop(Node node, int lxcID) throws LXCException {
        logger.info("LXC Stop " + lxcID);

        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);

        String url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID + "/status/stop";
        HttpResponse<JsonNode> response = node.getUnirestPost(url).asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Start Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }
    }

    public static void Shutdown(Node node, int lxcID) throws LXCException {
        logger.info("LXC Shutdown " + lxcID);

        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);

        String url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID + "/status/shutdown";
        HttpResponse<JsonNode> response = node.getUnirestPost(url).asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Shutdown Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }
    }

    public static void Reboot(Node node, int lxcID) throws LXCException {
        logger.info("LXC Reboot " + lxcID);

        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);

        String url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID + "/status/reboot";
        HttpResponse<JsonNode> response = node.getUnirestPost(url).asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Reboot Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }
    }

    public static Status Status(Node node, int lxcID) throws LXCException {
//        logger.info("LXC Status " + lxcID + " Requesting...");

        if(lxcID < 100) throw  new LXCException("lxcID < 100 --> lxcID = " + lxcID);

        String url;
        HttpResponse<JsonNode> response;

        url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID + "/config";
        response = node.getUnirestGet(url).asJson();
        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Status Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }


        try {
//            logger.info("\n"+response.getBody().toPrettyString());
            response.getBody().getObject().getJSONObject("data").getString("lock");
            logger.info("LXC Status " + lxcID + " Locked");
            return Status.Locked;
        } catch (JSONException e) {}


        url = node.getUrl() + "/api2/json/nodes/" + node.getName() + "/lxc/" + lxcID + "/status/current";
        response = node.getUnirestGet(url).asJson();

        if(!response.getStatusText().equals("OK")) {
            String warnText = "LXC Status Failed! " + response.getStatusText();
            throw new LXCException(warnText);
        }


        try {
            String dataStatus = response.getBody().getObject().getJSONObject("data").getString("lock");
            logger.info("LXC Status " + lxcID + " Locked");
            return Status.Locked;
        } catch (Exception e) {}

        try {
            String dataStatus = response.getBody().getObject().getJSONObject("data").getString("status");

            if(dataStatus.equals("running")) {
                logger.info("LXC Status " + lxcID + " Running");
                return Status.Running;
            }
            if(dataStatus.equals("stopped")) {
                logger.info("LXC Status " + lxcID + " Stopped");
                return Status.Stopped;
            }

            throw new LXCException("Unknown Status!");
        } catch (JSONException e) {
            throw new LXCException(e.getMessage());
        }
    }

    public enum Status {
        Running,
        Stopped,
        Locked
    }

    public static class LXCException extends Exception {
        public LXCException(String message) {
            super(message);
        }
    }
}
