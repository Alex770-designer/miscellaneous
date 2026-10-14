import java.util.Scanner;

public class DiscreteMath
{
    public static String gameShowSimulator() {
        int stayWins = 0;
        int switchWins = 0;
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("How many simulations do you wish to make? ");
        int n = scanner.nextInt();
        
        for (int i = 0; i < n; i++) {
            int carDoor = (int) (Math.random() * 3) + 1;
            int doorSelected = (int) (Math.random() * 3) + 1;
            
            if (carDoor == doorSelected) {
                stayWins++;
            }
            else {
                switchWins++;
            }
        }
        double stayPercent = (double) stayWins / n * 100;
        double switchPercent = (double) switchWins / n * 100;
        
        scanner.close();

        return "Stay wins: " + stayWins + "\nSwitch wins: " + switchWins + "\nStay win percentage: " + stayPercent + "%" + "\nSwitch win percentage: " + switchPercent + "%";
    }
    
    public static void main(String[] args)
    {
        System.out.println(gameShowSimulator());
    }
}       

