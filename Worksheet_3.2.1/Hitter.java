public class Hitter extends BaseballPlayer {
    private double battingAverage;
    private int homeRuns;
    private int runsBattedIn;

    public Hitter (String name, int gamesPlayed, String team, double battingAverage, int homeRuns, int runsBattedIn) {
        super(name, gamesPlayed, team);
        this.battingAverage = battingAverage;
        this.homeRuns = homeRuns;
        this.runsBattedIn = runsBattedIn;
    }

    public double getBattingAvg() {
        return battingAverage;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public int getRunsBattedIn() {
        return runsBattedIn;
    }

    public void setBattingAvg(double battingAverage) {
        if(battingAverage >= 0 && battingAverage <= 1) {
            this.battingAverage = battingAverage;
        }
    }

    public void setHomeRuns(int homeRuns) {
        if(homeRuns >= 0) {
            this.homeRuns = homeRuns;
        }
    }

    public void setRunsBattedIn(int runsBattedIn) {
        if(runsBattedIn >= 0) {
            this.runsBattedIn = runsBattedIn;
        }
    }

    public double calcTCI() {
        return (battingAverage * homeRuns * runsBattedIn);
    }

    public void hitHomeRun() {
        homeRuns++;
        System.out.println(getName() + " hit a home run!");
    }

    @Override 
    public double calcPerformance() {
        return (battingAverage * 100) + (homeRuns * 0.5) + (runsBattedIn * 0.1);
    }

    @Override 
    public void displayStats() {
        super.displayStats();
        System.out.println("Batting Average: " + battingAverage);
        System.out.println("Home Runs: " + homeRuns);
        System.out.println("Runs Batted In: " + runsBattedIn);
        System.out.println("Performance score: " + calcPerformance());
    }
}