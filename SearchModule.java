public class SearchModule {
    public static int linearSearch(int[] arr, int target) {
        int comparisons = 0;
        for (int i = 0; i < arr.length; i++) {
            comparisons++;

            if (arr[i] == target) {
                System.out.println(comparisons);
                return i;
            }
        }
        System.out.println(comparisons);
        return -1;
    }

    public static int binarySearch(int[] arr, int target) {
        int comparisons = 0;
        int low = 0;
        int high = arr.length-1;
        while (low <= high) {
            comparisons++;
            int middle = (low+high)/2;
            if (arr[middle] == target) {
                System.out.println(comparisons);
                return middle;
            }
            else if (arr[middle] < target) {
                low = middle + 1;
            }
            else {
                high = middle - 1;
            }
        }
        System.out.println(comparisons);
        return -1;
    }
}