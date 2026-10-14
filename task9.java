import java.util.Scanner;

public class task9 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner (System.in);
        boolean success = false;

        while(!success) {
            try {
                System.out.print("Enter a value for the numerator: ");
                int numerator = scanner.nextInt();

                System.out.print("Enter a value for the denominator: ");
                int denominator = scanner.nextInt();

                int result =  numerator/ denominator;
                System.out.println(result);
                success = true;
            } catch (ArithmeticException e) {
                System.out.println("Please enter a denominator other than 0");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}