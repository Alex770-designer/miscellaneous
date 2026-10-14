import java.util.Scanner;
import java.util.InputMismatchException;

public class task10 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;

        while(!success) {
            try {
                System.out.print("Enter a decimal value: ");
                double decimal = scanner.nextDouble();

                if (decimal > Integer.MAX_VALUE || decimal < Integer.MIN_VALUE) {
                    throw new ArithmeticException();
                }

                int integer = (int) decimal;
                System.out.println(integer);

                success = true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a decimal value");
                scanner.nextLine();
            } catch (ArithmeticException e) {
                System.out.println("Please enter a max 32-bit decimal");
                scanner.nextLine();
            } 
        }
        scanner.close();
    }
}
