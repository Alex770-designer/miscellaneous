package LogProcessor.src;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogProcessor {
    final static int LATENCY_THRESHOLD = 400;
    final static int AUDIT_SIZE_LIMIT = 2048;

    public static void main(String[] args) {
        int totalRecord = 0;
        int successRequest = 0;
        int errorRequest = 0;
        double totalLatency = 0;

        ArrayList<ServerStats> server = new ArrayList<ServerStats>();

        try (Scanner scanner = new Scanner(new File("data/server_logs.csv"))) {
            scanner.nextLine();
            int lineNum = 1;

            while(scanner.hasNextLine()) {
                lineNum++;
                String line = scanner.nextLine();
                String[] fields = line.split(",");

                if(fields.length != 4) {
                    System.out.println("Warning in Line " + lineNum + ": Wrong number of fields. Getting skipped");
                    continue;
                }

                String serverID = fields[1];

                try {
                    double responseT = Double.parseDouble(fields[2]);
                    int status = Integer.parseInt(fields[3]);

                    if (responseT < 0) {
                        System.out.println("Warning in Line " + lineNum + ": Response time < 0. ");
                        continue;
                    }

                    totalRecord++;
                    totalLatency += responseT;

                    if (status == 200) {
                        successRequest++;
                    }
                    
                    if (status >= 400) {
                        errorRequest++;
                    }

                    int index = 0;

                    while (index < server.size()) {
                        if(server.get(index).serverID.equals(serverID)) {
                            break;
                        }
                        index++;
                    }

                    if (index == server.size()) {
                        server.add(new ServerStats(serverID));
                    }

                    server.get(index).totalLatency += responseT;
                    server.get(index).requestNum++;
                }
                catch (NumberFormatException f) {
                    System.out.println("Warning in Line " + lineNum + ": Wrong numeric value. Getting skipped");
                    continue;
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("input file not found. ");
            return;
        }

        double avg = 0.0;

        if (totalRecord > 0) {
            avg = (double) totalLatency / totalRecord;
        }

        try {
            FileWriter writer = new FileWriter("reports/summary_report.txt");
            writer.write("=== CLOUD SERVER LOG ANALYSIS REPORT ===\n");
            writer.write("Valid Records Processed: " + totalRecord + "\n");
            writer.write("Successful Requests (200 OK): " + successRequest + "\n");
            writer.write("Error Responses (4xx/5xx): " + errorRequest + "\n");
            writer.write("Overall Average Latency: " + avg + " ms\n");

            writer.write("\n");
            writer.write("--- Per-Server Latency Breakdown ---\n");
            
            for (ServerStats s : server) {
                double average = s.totalLatency / s.requestNum;

                writer.write("Server " + s.serverID + ": " + s.requestNum + " requests, Average Latency " + average + " ms\n");
            }

            writer.write("--- High Latency Alerts ---\n");

            for (ServerStats s : server) {
                double average = s.totalLatency / s.requestNum;
                if (average > LATENCY_THRESHOLD) {
                    writer.write("Alert: Server " + s.serverID + " exceeds the latency threshold\n");
                }
            }

            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error: Could not find the file. ");
        }

        try {
            File otherFile = new File("logs/audit.log");
            if (otherFile.exists()) {
                if (otherFile.length() > AUDIT_SIZE_LIMIT) {
                    String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                    File archive = new File("logs/audit_archive_" + stamp + ".log");
                    if (!otherFile.renameTo(archive)) {
                        System.out.println("Not able to archive audit log.");
                        return;
                    }
                }
            }
            FileWriter writer = new FileWriter("logs/audit.log", true);

            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + stamp + "] Success: Processed " + totalRecord + " logs. Average: " + avg + " ms\n");
            writer.close();
        }
        catch (IOException u) {
            System.out.println("File not found, could not write audit log.");
        }
    }
}