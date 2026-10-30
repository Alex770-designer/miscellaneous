public class PlayerSimulator {
    private int jerseyNum;
    private String name;
    private int rating;
    private int stamina;
    private boolean isPlaying;
    private static int totalPlayers = 0;
    private static int playingPlayers = 0;
    private static int overallRating = 0;

    public PlayerSimulator() {
        name = "Unknown";
        rating = 70;
        stamina = 40;
    }
    public PlayerSimulator(String name, int rating, int stamina) {
        if(name == null) {
            throw new IllegalArgumentException ("Player name cannot be empty.");
        }

        if (overallRating < 1 || overallRating > 99) {
            throw new IllegalArgumentException("Overall rating must be between 1 and 99.");
        }

        if (stamina < 0 || stamina > 100) {
            throw new IllegalArgumentException("Stamina must be between 0 and 100.");
        }

        totalPlayers++;

        this.jerseyNum = (int) (Math.random() * 101);
        this.name = name;
        this.rating = rating;
        this.stamina = stamina;
        this.isPlaying = true;
        
        playingPlayers++;
        overallRating += rating;
    }

    public String getName() {
        return name;
    }
    public int getJerseyNum() {
        return jerseyNum;
    }
    public int getRating() {
        return rating;
    }
    public int getStamina() {
        return stamina;
    }
    public boolean isActive() {
        return isPlaying;
    }
    
    public static int getTotalPlayers() {
        return totalPlayers;
    }
    public static int getPlayingPlayers() {
        return playingPlayers;
    }
    public static int getOverallRating() {
        return overallRating;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void 

}