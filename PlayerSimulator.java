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
        this("Unknown", 70, 40);
    }
    public PlayerSimulator(String name, int rating, int stamina) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException ("Player name cannot be empty.");
        }

        if (rating < 1 || rating > 99) {
            throw new IllegalArgumentException("Overall rating must be between 1 and 99.");
        }

        if (stamina < 0 || stamina > 100) {
            throw new IllegalArgumentException("Stamina must be between 0 and 100.");
        }

        totalPlayers++;

        this.jerseyNum = totalPlayers;
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
        if (name == null || name.isEmpty()) {
            System.out.println("Error: Invalid player name");
            return;
        }
        this.name = name;
    }
    public void setRating(int rating) {
        if (rating < 1 || rating > 99) {
            System.out.println("Error: Rating must be between 1 and 99");
            return;
        }
        if (!isPlaying) {
            System.out.println("Error: Player is inactive");
            return;
        }
        
        overallRating += rating - this.rating;
        this.rating = rating;
    }
    public void setStamina(int stamina) {
        if (stamina < 0 || stamina > 100) {
            System.out.println("Error: Stamina must be between 0 and 100");
            return;
        }
        this.stamina = stamina;
    }

    public void updatePlayer(int amount) {
        if(!isPlaying) {
            System.out.println("Error: Player is inactive");
            return;
        }
        if(amount <= 0) {
            System.out.println("Error: Upgrade should be positive");
            return;
        }
        setRating(rating+amount);
    }
    public void useStamina(int amount) {
        if (amount <= 0 || amount > stamina) {
            System.out.println("Error: Invalid stamina amount");
            return;
        }
        stamina -= amount;
    }
    public void deactivatePlayer() {
        if (!isPlaying) {
            System.out.println("Error: Player is already inactive");
            return;
        }
        isPlaying = false;
        playingPlayers--;
        overallRating -= rating;

        System.out.println("Player " + jerseyNum + " inactive");
    }

    public void displayPlayerStats() {
        System.out.println("[Jersey: " + jerseyNum + ", Name: " + name + ", Rating: " + rating + ", Stamina: " + stamina + ", Playing: " + isPlaying + "]");
    }
    public static void displayGlobalStats() {
        System.out.println("[Total Players: " + totalPlayers + ", Playing Players: " + playingPlayers + ", Combined Ratings: " + overallRating + "]");
    }

    public static void main(String[] args) {
        System.out.println("Basketball Player Simulator: ");

        System.out.println("Instantiation:");
        PlayerSimulator player1 = new PlayerSimulator();
        PlayerSimulator player2 = new PlayerSimulator("Michael Jordan", 93, 95);
        PlayerSimulator player3 = new PlayerSimulator("LeBron James", 97, 90);

        player1.displayPlayerStats();
        player2.displayPlayerStats();
        player3.displayPlayerStats();
        displayGlobalStats();

        System.out.println("Mutator Bounds Validation: ");

        player2.setRating(-10);
        player3.setName("");
        displayGlobalStats();

        System.out.println("State Transformation: ");

        player2.updatePlayer(4);
        player2.displayPlayerStats();
        displayGlobalStats();

        System.out.println("Lifecycle Transformation");

        player3.deactivatePlayer();
        displayGlobalStats();

        System.out.println("Redundant Safeguards");

        player3.deactivatePlayer();
        displayGlobalStats();

        System.out.println("Test Complete!");
    }
}