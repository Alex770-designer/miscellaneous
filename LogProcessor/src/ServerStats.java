package LogProcessor.src;

public class ServerStats {
    String serverID;
    double totalLatency;
    int requestNum;

    public ServerStats(String id) {
        serverID = id;
        totalLatency = 0;
        requestNum = 0;
    }
}
