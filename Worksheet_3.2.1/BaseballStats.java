public class BaseballStats {
    public static void main(String[] args) {
        Hitter hitter = new Hitter("Jackson Chourio", 127, "Brewers", .291, 25, 75);
        Pitcher pitcher = new Pitcher("Jacob Misiorowski", 30, "Brewers", 1.80, 252, 174.2);

        hitter.displayStats();
        pitcher.displayStats();

        hitter.hitHomeRun();
        System.out.println("TCI: " + hitter.calcTCI());

        pitcher.recordStrikeout();
        System.out.printf("K/9: %.2f%n", pitcher.calcKPerNine());

        BaseballPlayer hitterDuplicate = hitter;
        BaseballPlayer pitcherDuplicate = pitcher;

        hitterDuplicate.displayStats();
        pitcherDuplicate.displayStats();

        System.out.println("Hitter Performance: " + hitterDuplicate.calcPerformance());
        System.out.println("Pitcher Performance: " + pitcherDuplicate.calcPerformance());

        BaseballPlayer[] players = {hitter, pitcher};

        for (BaseballPlayer player : players) {
            System.out.println("Player: " + player.getName());
            player.displayStats();
            System.out.printf("Performance score: %.2f%n", player.calcPerformance());
        }
    }
}
