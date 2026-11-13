public class GuitarPerformance {
    protected String songName;
    protected double songDuration;

    public GuitarPerformance(String songName, double songDuration) {
        this.songName = songName;
        this.songDuration = songDuration;
    }

    public void play() {
        System.out.println("Playing " + songName);
    }

    public void displayInfo() {
        System.out.println("Song: " + songName);
        System.out.println("Duration: " + songDuration + " minutes");
    }
}
