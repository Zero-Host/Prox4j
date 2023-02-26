package zero.host.Prox4j.v1.node;


public enum Realm {
    pve("pve"),
    pam("pam");

    private String text;
    Realm(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}