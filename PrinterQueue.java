import java.util.ArrayList;

public class PrinterQueue {
    private ArrayList<String> printJobs;

    public PrinterQueue() {
        printJobs = new ArrayList<String>();
    }

    public void enqueue(String printJob) {
        printJobs.add(printJob);
    }

    public String dequeue() {
        if (printJobs.isEmpty()) {
            System.out.println("Warning, queue is empty");
            return null;
        }
        return printJobs.remove(0);
    }

    public String peek() {
        if (printJobs.isEmpty()) {
            return null;
        }
        return printJobs.get(0);
    }

    public boolean isEmpty() {
        return printJobs.isEmpty();
    }

    public int size() {
        return printJobs.size();
    }
}