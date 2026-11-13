public class ElectricGuitar extends GuitarPerformance {
    public ElectricGuitar(String songName, int songDuration) {
        super(songName, songDuration);
    }

    @Override 
    public void play() {
        System.out.println("Playing " + songName + " on electric guitar with the amp and distortion");
    }
}
