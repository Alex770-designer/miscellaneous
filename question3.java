public class question3 {

    public static int primarySum (int[][] arr) {
        int counter1diag = 0;
        for (int i = 0; i < arr.length; i++) {
            counter1diag += arr[i][i];
        }
        return counter1diag;
    }

    public static int secondSum (int[][] arr) {
        int counter2diag = 0;
        for (int i = arr.length-1; i >= 0; i--){
            counter2diag += arr[i][arr[i].length-1-i];
        }
        return counter2diag;
    }
    
    public static void main(String[] args) {
        int maxVal = Integer.MIN_VALUE;
        int[][] arr = {{42,17,89}, {33,64,5}, {91,28,76}};
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] >= maxVal) {
                    maxVal = arr[i][j];
                }
            }
        }
        System.out.println(primarySum(arr));
        System.out.println(secondSum(arr));
        System.out.println(maxVal);
    }
}