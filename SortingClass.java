import java.util.Arrays;

public class SortingClass {
    public static int[] bubbleSort(int[] arr) {
        int comparisons = 0;
        int swaps = 0;
        boolean swapped;
        for (int i = 0; i < arr.length-1; i++) {
            swapped = false;
            for (int j = 0; j < arr.length-1-i; j++) {
                comparisons++;
                if(arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    swapped = true;
                    swaps++;
                }
            }
            if(!swapped) {
                break;
            }
        }
        System.out.println(comparisons);
        System.out.println(swaps);
        return arr;
    }

    public static int[] selectionSort(int[] arr) {
        int comparisons = 0;
        int swaps = 0;
        for (int i = 0; i < arr.length-1; i++) {
            int minIndex = i;
            for(int j = i+1; j<arr.length; j++) {
                comparisons++;
                if(arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                swaps++;
            }
        }

        System.out.println(comparisons);
        System.out.println(swaps);
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = {9, 65, 32, 13, 8, 18, 71, 61, 39, 70};

        int[] bubbleArray = Arrays.copyOf(arr, arr.length);
        int[] selectionArray = Arrays.copyOf(arr, arr.length);

        bubbleSort(bubbleArray);
        selectionSort(selectionArray);

        System.out.println(Arrays.toString(bubbleArray));
        System.out.println(Arrays.toString(selectionArray));
    }
}
