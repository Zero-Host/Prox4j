package zero.host.Prox4j.v1.node.lxc;


public enum OSTemplate {

    Ubuntu20("local:vztmpl/ubuntu-20.04-standard_20.04-1_amd64.tar.gz"),
    Unknown("");

    private String iso;

    OSTemplate(String iso) {
        this.iso = iso;
    }

    public String getIso() {
        return iso;
    }
}
