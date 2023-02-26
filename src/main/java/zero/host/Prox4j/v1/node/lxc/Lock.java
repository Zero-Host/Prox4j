package zero.host.Prox4j.v1.node.lxc;

public enum Lock {
    backup("Backup"),
    create("Create"),
    destroyed("Destroyed"),
    disk("Disk"),
    fstrim("FileSystemTrim"),
    migrate("Migrate"),
    mounted("Mounted"),
    rollback("Rollback"),
    snapshot("Snapshot"),
    snapshotdelete("SnapshotDelete"),
    unknow("Unknown");


    private String text;
    Lock(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
