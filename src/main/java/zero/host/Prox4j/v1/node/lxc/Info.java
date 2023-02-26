package zero.host.Prox4j.v1.node.lxc;

import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Info {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private JSONObject rawData;

    private int vmid;
    private Status status;
    private long cpus;
    private Lock lock;
    private int maxdisk;
    private int maxmem;
    private int maxswap;
    private String name;
    private String tags;
    private int uptime;

    public Info(JSONObject j) {
        JSONObject data = j.getJSONObject("data");
        this.rawData = data;

        vmid = data.getInt("vmid");
        status = data.getEnum(Status.class, "status");

        try { cpus = data.getLong("cpus"); } catch (Exception e) { logger.info("cpus was not found"); }
        try { lock = LockConverter.fromString(data.getString("lock")); } catch (Exception e) { logger.info("lock was not found"); lock = Lock.unknow; }
        try { maxdisk = data.getInt("maxdisk"); } catch (Exception e) { logger.info("maxdisk was not found"); }
        try { maxmem = data.getInt("maxmem"); } catch (Exception e) { logger.info("maxmem was not found"); }
        try { maxswap = data.getInt("maxswap"); } catch (Exception e) { logger.info("maxswap was not found"); }
        try { name = data.getString("name"); } catch (Exception e) { logger.info("name was not found"); }
        try { tags = data.getString("tags"); } catch (Exception e) { logger.info("tags was not found"); }
        try { uptime = data.getInt("uptime"); } catch (Exception e) { logger.info("uptime was not found"); }

    }

    public int getVmid() {
        return vmid;
    }

    public Status getStatus() {
        return status;
    }

    public long getCpus() {
        return cpus;
    }

    public Lock getLock() {
        return lock;
    }

    public String getLockAsString() {
        return lock.toString();
    }

    public int getMaxdisk() {
        return maxdisk;
    }

    public int getMaxmem() {
        return maxmem;
    }

    public int getMaxswap() {
        return maxswap;
    }

    public String getName() {
        return name;
    }

    public String getTags() {
        return tags;
    }

    public int getUptime() {
        return uptime;
    }

    public JSONObject getRawData() {
        return rawData;
    }

    public enum Status {
        stopped,
        running,
    }

}
