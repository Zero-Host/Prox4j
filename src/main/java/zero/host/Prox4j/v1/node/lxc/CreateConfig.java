package zero.host.Prox4j.v1.node.lxc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private int vmid;
    private OSTemplate osTemplate;


    // ==================== HEADER ==================== //

    public CreateConfig(int vmid, OSTemplate osTemplate) throws InvalidConfigException {
        this.vmid = vmid*100;
        this.osTemplate = osTemplate;

        if(this.vmid < 100) {
            logger.warn("VMID must have 3 digits!");
            throw new InvalidConfigException("VMID must have 3 digits! VMID = " + this.vmid);
        }
    }


    // ==================== CODE ==================== //


    // ==================== GETTER / SETTER ==================== //


    public int getVmid() {
        return vmid;
    }

    public String getVmidString() {
        return String.valueOf(vmid);
    }

    public OSTemplate getOsTemplate() {
        return osTemplate;
    }


    //

    class InvalidConfigException extends Exception {
        public InvalidConfigException(String message) {
            super(message);
        }
    }

}
