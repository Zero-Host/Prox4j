package zero.host.Prox4j.v1.node.lxc;

import zero.host.Prox4j.v1.node.Node;
import zero.host.Prox4j.v1.node.Ticket;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinuxContainer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Node node;
    private CreateConfig createConfig;
    private Info info;

    private String upid;

    // ================== HEADER ================== //

    /**
     * Creates a LinuxContainer
     * @param node
     * @param createConfig
     * @throws LXCException
     */
     public LinuxContainer(Node node, CreateConfig createConfig) throws LXCException {
        this.node = node;
        this.createConfig = createConfig;

        create();
         getInfo();
     }

    /**
     * Connect to a LinuxContainer
     * @param node
     * @param vmid
     * @throws LXCException
     */
     public LinuxContainer(Node node, int vmid) throws LXCException {
         this.node = node;
         try {
             this.createConfig = new CreateConfig(vmid, OSTemplate.Unknown);
         } catch (Exception e) { throw new LXCException(e.getMessage()); }

         connect();
         getInfo();
     }

    // ================== LIVECYLE ================== //

    /**
     * Creates a LinuxContainer
     * @throws LXCException
     */
     private void create() throws LXCException {
         logger.info("Creating LXC " + createConfig.getVmidString());
         try {

             String url = node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc";

//             Unirest.config().addDefaultCookie("PVEAuthCookie", node.getTicket().getTicket());
             HttpResponse<JsonNode> response = Unirest.post(url)
                     .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                     .field("ostemplate", createConfig.getOsTemplate().getIso())
                     .field("vmid", createConfig.getVmidString())
                     .field("rootfs", "local")
                     .asJson();

             if(response.getStatusText().contains("already exists on node")) {
                 String warnText = "Container already exists";
                 logger.warn(warnText);
                 throw new LXCException(warnText);
             }

             if(!response.getStatusText().equals("OK")) {
                 String warnText = "Creation of LXC failed! " + response.getStatusText();
                 logger.warn(warnText);
                 throw new LXCException(warnText);
             }

//             logger.info("create resp --> " + response.getBody().toPrettyString());
             this.upid = response.getBody().getObject().getString("data");
         } catch (LXCException e) {
             logger.warn(e.getMessage());
             throw e;
         } catch (Exception e) {
             logger.warn(e.getMessage());
             throw new LXCException(e.getMessage());
         }

         logger.info("Successfully created LXC " + createConfig.getVmidString());
     }

    /**
     * Connect to a LinuxContainer (instead of creating one)
     * @throws LXCException
     */
     private void connect() throws LXCException {
        logger.info("Connecting to LXC " + createConfig.getVmidString());
         try {
             HttpResponse<JsonNode> response = Unirest.get(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + this.createConfig.getVmidString())
                     .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                     .asJson();

             if(!response.getStatusText().equals("OK")) throw new LXCException("Connection failed!");

//             logger.info("connect resp --> " + response.getBody().toPrettyString());
         } catch (Ticket.AuthenticationException e) {
             logger.warn(e.getMessage());
             throw new LXCException(e.getMessage());
         }

         logger.info("Connecting successful!");
     }

    /**
     * Deletes a LinuxContainer
     * @throws LXCException
     */
     public void delete() throws LXCException {
        logger.info("Deleting LXC " + createConfig.getVmidString());
        try {
            HttpResponse response = Unirest.delete(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + createConfig.getVmidString())
                    .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                    .asEmpty();
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Deletion of " + createConfig.getVmidString() + " failed!");
            throw new LXCException(e.getMessage());
        }
        logger.info("Deletion successful!");
    }

    /**
     * Start the LinuxContainer
     * @throws LXCException
     */
    public void start() throws LXCException {
        logger.info("Starting LXC " + createConfig.getVmidString());
        try {
            HttpResponse<JsonNode> response = Unirest.post(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + createConfig.getVmidString() + "/status/start")
                    .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                    .asJson();

            if(!response.getStatusText().equals("OK")) {
                logger.error(response.getBody().toPrettyString());
                throw new LXCException("Response was not OK! " + response.getStatusText());
            }
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Starting of " + createConfig.getVmidString() + " failed!");
            throw new LXCException(e.getMessage());
        }
        logger.info("Start successful!");
    }

    /**
     * Abruptly Shutdown the LinuxContainer
     * @throws LXCException
     */
    public void stop() throws LXCException {
        logger.info("Stopping LXC " + createConfig.getVmidString());
        try {
            HttpResponse response = Unirest.post(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + createConfig.getVmidString() + "/status/stop")
                    .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                    .asEmpty();
            if(!response.getStatusText().equals("OK")) throw new LXCException("Response was not OK! " + response.getStatusText());
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Stopping of " + createConfig.getVmidString() + " failed!");
            throw new LXCException(e.getMessage());
        }
        logger.info("Stop successful!");

    }

    /**
     * Pauses the LinuxContainer (EXPERIMENTAL)
     * @throws LXCException
     */
    public void suspend() throws LXCException {
        logger.info("Suspending LXC " + createConfig.getVmidString());
        try {
            HttpResponse response = Unirest.post(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + createConfig.getVmidString() + "/status/suspend")
                    .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                    .asEmpty();
            if(!response.getStatusText().equals("OK")) throw new LXCException("Response was not OK! " + response.getStatusText());
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Suspending of " + createConfig.getVmidString() + " failed!");
            throw new LXCException(e.getMessage());
        }
        logger.info("Suspend successful!");

    }

    /**
     * Unpauses the LinuxContainer (EXPERIMENTAL)
     * @throws LXCException
     */
    public void resume() throws LXCException {
        logger.info("Resuming LXC " + createConfig.getVmidString());
        try {
            HttpResponse response = Unirest.post(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + createConfig.getVmidString() + "/status/resume")
                    .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                    .asEmpty();
            if(!response.getStatusText().equals("OK")) throw new LXCException("Response was not OK! " + response.getStatusText());
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Resuming of " + createConfig.getVmidString() + " failed!");
            throw new LXCException(e.getMessage());
        }
        logger.info("Resum successful!");

    }

    /**
     * Reboots the LinuxContainer
     * @throws LXCException
     */
    public void reboot() throws LXCException {
        logger.info("Rebooting LXC " + createConfig.getVmidString());
        try {
            HttpResponse response = Unirest.post(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + createConfig.getVmidString() + "/status/reboot")
                    .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                    .asEmpty();
            if(!response.getStatusText().equals("OK")) throw new LXCException("Response was not OK! " + response.getStatusText());
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Rebooting of " + createConfig.getVmidString() + " failed!");
            throw new LXCException(e.getMessage());
        }
        logger.info("Reboot successful!");

    }

    /**
     * Save Shutdown the LinuxContainer
     * @throws LXCException
     */
    public void shutdown() throws LXCException {
        logger.info("Shutdown LXC " + createConfig.getVmidString());
        try {
            HttpResponse response = Unirest.post(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + createConfig.getVmidString() + "/status/shutdown")
                    .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                    .asEmpty();
            if(!response.getStatusText().equals("OK")) throw new LXCException("Response was not OK! " + response.getStatusText());
        } catch (Ticket.AuthenticationException e) {
            logger.warn("Shutdown of " + createConfig.getVmidString() + " failed!");
            throw new LXCException(e.getMessage());
        }
        logger.info("Shutdown successful!");

    }

    // ================== IS ================== //

    /**
     * Is true if LXC is created
     * @return
     * @throws LXCException
     */
    public boolean isCreated() throws LXCException {
         if(upid == null) return true;

         try {

             HttpResponse<JsonNode> response = Unirest.get(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/tasks/"+upid+"/status")
                     .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                     .asJson();
             if(!response.getStatusText().equals("OK")) throw new LXCException("Response was not OK! " + response.getStatusText());

             if(response.getBody().getObject().getJSONObject("data").getString("status").equals("stopped")) {
                 return true;
             }


         } catch (Ticket.AuthenticationException e) {
             throw new LXCException(e.getMessage());
         }
        return false;
    }

    /**
     * Is true if LXC is locked
     * @return
     * @throws LXCException
     */
    public boolean isLocked() throws LXCException {
        getInfo();
        if(info.getLock() != Lock.unknow) return true;
        return false;
    }

    /**
     * Is true if LXC is unlucked
     * @return
     * @throws LXCException
     */
    public boolean isUnocked() throws LXCException {
         getInfo();
         if(info.getLock() == Lock.unknow) return true;
         return false;
    }

    /**
     * Is true if LXC is running
     * @return
     * @throws LXCException
     */
    public boolean isRunning() throws LXCException {
         getInfo();
         if(info.getStatus() == Info.Status.running) return true;
         return false;
    }

    /**
     * Is true if LXC is stopped
     * @return
     * @throws LXCException
     */
    public boolean isStopped() throws LXCException {
        getInfo();
        if(info.getStatus() == Info.Status.stopped) return true;
        return false;
    }

    // ================== GETTER / SETTER ================== //

    /**
     * Returns information aboute the LXC
     * @return
     * @throws LXCException
     */
     public Info getInfo() throws LXCException {
         try {
             HttpResponse<JsonNode> response = Unirest.get(node.getUrl() + "/api2/json/nodes/" + node.getInfo().getName() + "/lxc/" + this.createConfig.getVmidString() + "/status/current")
                     .header("CSRFPreventionToken", node.getTicket().getCsrfPreventionToken())
                     .asJson();

             if(!response.getStatusText().equals("OK")) throw new LXCException("Response code not OK! " + response.getStatusText());

             this.info = new Info(response.getBody().getObject());


         } catch (Ticket.AuthenticationException e) {
             logger.warn(e.getMessage());
             throw new LXCException(e.getMessage());
         }
         return this.info;
     }

    /**
     * Get node on witch the LXC runs
     * @return
     */
     public Node getNode() {
        return node;
    }

    /**
     * Get the creation configuration of this LXC
     * @return
     */
    public CreateConfig getCreateConfig() {
        return createConfig;
    }

    /**
     * Used to save LXC to database
     * @return
     */
    public String getData() {
        JSONObject data = new JSONObject()
                .put("vmid", createConfig.getVmid());

        String jsonString = new JSONObject()
                .put("type", "lxc")
                .put("data", data)
                .toString();

        return jsonString;
    }

    public class LXCException extends Exception {
         public LXCException(String errorMessage) {
            super(errorMessage);
        }
     }


}
