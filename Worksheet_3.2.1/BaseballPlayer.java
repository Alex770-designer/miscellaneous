public class BaseballPlayer {
    private String name;
    protected int gamesPlayed;
    private String team;

    public BaseballPlayer(String name, int gamesPlayed, String team) {
        this.name = name;
        this.gamesPlayed = gamesPlayed;
        this.team = team;
    }

    public BaseballPlayer() {
        name = "Unknown";
        gamesPlayed = 50;
        team = "Free agent";
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGamesPlayed(int gamesPlayed) {
        if (gamesPlayed >= 0) {
            this.gamesPlayed = gamesPlayed;
        }
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void displayStats() {
        System.out.println("This player is " + name + ", he has played " + gamesPlayed + " games, and he plays for the " + team + "!");
    }

    public double calcPerformance() {
        return gamesPlayed;
    }
}