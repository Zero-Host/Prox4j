package zero.host.Prox4j.v1.node.lxc;

public class LockConverter {

    public static Lock fromString(String data) {
        if (data == null) {
            return Lock.unknow;
        }

        switch (data) {
            case "snapshot-delete": return Lock.snapshotdelete;
            default:
                try {
                    return Enum.valueOf(Lock.class, data);
                } catch (Exception e) {
                    return Lock.unknow;
                }
        }
    }


}
