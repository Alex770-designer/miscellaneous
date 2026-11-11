public class Pitcher extends BaseballPlayer {
    private double ERA;
    private int strikeouts;
    private double inningsPitched;

    public Pitcher(String name, int gamesPlayed, String team, double ERA, int strikeouts, double inningsPitched) {
        super(name, gamesPlayed, team);
        this.ERA = ERA;
        this.strikeouts = strikeouts;
        this.inningsPitched = inningsPitched;
    }

    public double getERA() {
        return ERA;
    }
    
    public int getStrikeouts() {
        return strikeouts;
    }

    public double getInningsPitched() {
        return inningsPitched;
    }

    public void setERA(double ERA) {
        if(ERA >= 0) {
            this.ERA = ERA;
        }
    }

    public void setStrikeouts(int strikeouts) {
        if(strikeouts >= 0) {
            this.strikeouts = strikeouts;
        }
    }

    public void setInningsPitched(double inningsPitched) {
        if (inningsPitched >= 0) {
            this.inningsPitched = inningsPitched;
        }
    }

    public double calcKPerNine() {
        return (strikeouts/inningsPitched) * 9.0;
    }

    public void recordStrikeout() {
        strikeouts++;
        System.out.println(getName() + " got a strikeout!");
    }

    @Override 
    public double calcPerformance() {
        return (strikeouts * 0.3) + (inningsPitched * 0.5) - (ERA * 2);
    }

    @Override 
    public void displayStats() {
        super.displayStats();
        System.out.println("ERA: " + ERA);
        System.out.println("Strikeouts: " + strikeouts);
        System.out.println("Innings Pitched: " + inningsPitched);
        System.out.println("Performance score: " + calcPerformance()); 
    }
}
