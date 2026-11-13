public class AcousticGuitar extends GuitarPerformance {
    public AcousticGuitar(String songName, int songDuration) {
        super(songName, songDuration);
    }

    @Override 
    public void play() {
        System.out.println("Playing " + songName + " on acoustic guitar with boom chuck strumming");
    }

    public void play(int strummingSpeed) {
        System.out.println("Playing " + songName + " on acoustic guitar at " + strummingSpeed + " strums per minute");
    }
}
