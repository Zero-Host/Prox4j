package zero.host.Prox4j.v1.node;

import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeInfo {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String ssl_fingerprint;
    private String node;
    private long uptime;
    private Status status;
    private String id;
    private String type;
    private String level;


    private float cpu;
    private long maxcpu;

    private long mem;
    private long maxmem;

    private long disk;
    private long maxdisk;

    public NodeInfo(JSONObject data) {
        logger.info("New NodeInfo!", this);

        ssl_fingerprint = data.getString("ssl_fingerprint");

        maxdisk = data.getLong("maxdisk");
        maxcpu = data.getLong("maxcpu");
        maxmem = data.getLong("maxmem");

        disk = data.getLong("disk");
        cpu = data.getFloat("cpu");
        mem = data.getLong("mem");

        node = data.getString("node");
        uptime = data.getLong("uptime");
        status = data.getEnum(Status.class,  "status");
        id = data.getString("id");
        type = data.getString("type");
        level = data.getString("level");
    }


    public String getSsl_fingerprint() {
        return ssl_fingerprint;
    }

    public String getNode() {
        return node;
    }

    public long getUptime() {
        return uptime;
    }

    public Status getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLevel() {
        return level;
    }

    public float getCpu() {
        return cpu;
    }

    public long getMaxcpu() {
        return maxcpu;
    }

    public long getMem() {
        return mem;
    }

    public long getMaxmem() {
        return maxmem;
    }

    public long getDisk() {
        return disk;
    }

    public long getMaxdisk() {
        return maxdisk;
    }

    public String getName() {
        return node;
    }


    public enum Status {
        unknown,
        online,
        offline
    }
}
