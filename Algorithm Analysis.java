/**
 * @author Jesse Atkinson
 * @date 2024-10-06
 * @version 1.0
 *
 * // Analysis
 * a) There seems to be some variance in the order of fastest to slowest (for size 20 dataset)
 *    The fastest is nearly always Insertion and the slowest is always Merge.
 *    The other sorts are in the middle and their order varies slightly.
 *    I ran multiple tests and had these results:
 *    I, S, B, R, Q, M
 *    I, Q, R, B, S, M
 *    Q, I, R, B, S, M
 *    I, R, S, Q, B, M
 *    R, I, Q, B, S, M
 *
 * b) Consistent order of fastest to slowest for all sorting methods (for size 8000 dataset).
 *    Radix, Quick, Merge, Insertion, Selection, Bubble
 *
 * d)
 *    Algorithm     Name          BIG O (time - avg)   BIG O (space - worst)
 *    aSort         Selection     O(n^2)               O(1)
 *    bSort         Radix         O(2n+r)              O(r)
 *    cSort         Merge         O(n log n)           O(n)
 *    dSort         Insertion     O(n^2)               O(1)
 *    eSort         Quick         O(n log n)           O(1)
 *    fSort         Bubble        O(n^2)               O(1)
 */
import java.util.Random;

public class Assignment2_Start_F24 {
    /**
     * The swap method swaps the contents of two elements in an int array.
     *
     * @param array where elements are to be swapped.
     * @param a The subscript of the first element.
     * @param b The subscript of the second element.
     */
    private static void swap(int[] array, int a, int b) {
        int temp;
        temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    /**
     * Validate that an array is sorted,
     *
     * @param array array that might or might not be sorted
     * @return a 6 digit checksum if sorted, -1 if not sorted.
     */
    public static int ckSumSorted(int[] array) {
        int sum = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return -1;
            }
            sum += array[i] * i;
        }
        return Math.abs(sum % 1000_000);
    }

    /**
     * Randomly re-arrange any array, if sorted will unsort the array, or shuffle it
     *
     * @param array array that might or might not be sorted
     */
    public static void shuffle(int[] array) {
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            int j = rand.nextInt(array.length);
            swap(array, i, j);
        }
    }

    /**
     * ---------------------------- Selection Sort ---------------------------------------
     */

    public static long selectionSort(int[] array) {
        int startScan;   // Starting position of the scan
        int index;       // To hold a subscript value
        int minIndex;    // Element with smallest value in the scan
        int minValue;    // The smallest value found in the scan
        int counter = 0; // Counter for the number of operations

        // The outer loop iterates once for each element in the
        // array. The startScan variable marks the position where
        // the scan should begin.
        for (startScan = 0; startScan < (array.length - 1); startScan++) {
            // Assume the first element in the scannable area
            // is the smallest value.
            minIndex = startScan;
            minValue = array[startScan];

            // Scan the array, starting at the 2nd element in
            // the scannable area. We are looking for the smallest
            // value in the scannable area.
            for (index = startScan + 1; index < array.length; index++) {
                counter++;
                if (array[index] < minValue) {
                    minValue = array[index];
                    minIndex = index;
                }
            }
            // Swap the element with the smallest value
            // with the first element in the scannable area.
            array[minIndex] = array[startScan];
            array[startScan] = minValue;
        }
        return counter;
    }

    /**
     * ---------------------------- Radix Sort ---------------------------------------
     */
    public static long radixSort(int[] array) {
        int count = 0;
        int counter = 0;

        int min = array[0];
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min)
                min = array[i];
            else if (array[i] > max)
                max = array[i];
        }
        int b[] = new int[max - min + 1];
        for (int i = 0; i < array.length; i++) {
            b[array[i] - min]++;
        }
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i]; j++) {
                counter++; // Increment counter for number of assignments
                array[count++] = i + min;
            }
        }
        return counter;
    }
    /**
     * ---------------------------- Merge Sort ---------------------------------------
     */
    public static long mergeSort(int[] inputArray) {
        int length = inputArray.length;
        // Create array only once for merging
        int[] workingArray = new int[inputArray.length];
        long count = 0;
        count = doMergeSort(inputArray, workingArray, 0, length - 1, count);
        return count;
    }

    private static long doMergeSort(int[] inputArray, int[] workingArray, int lowerIndex, int higherIndex, long count) {
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            count = doMergeSort(inputArray, workingArray, lowerIndex, middle, count);
            // Below step sorts the right side of the array
            count = doMergeSort(inputArray, workingArray, middle + 1, higherIndex, count);
            // Now merge both sides
            count += part2(inputArray, workingArray, lowerIndex, middle, higherIndex);
        }
        return count;
    }

    private static long part2(int[] inputArray, int[] workingArray, int lowerIndex, int middle, int higherIndex) {
        long count = 0;
        for (int i = lowerIndex; i <= higherIndex; i++) {
            workingArray[i] = inputArray[i];
        }
        int i1 = lowerIndex;
        int i2 = middle + 1;
        int newIndex = lowerIndex;
        while (i1 <= middle && i2 <= higherIndex) {
            count++;
            if (workingArray[i1] <= workingArray[i2]) {
                inputArray[newIndex] = workingArray[i1];
                i1++;
            } else {
                inputArray[newIndex] = workingArray[i2];
                i2++;
            }
            newIndex++;
        }
        while (i1 <= middle) {
            inputArray[newIndex] = workingArray[i1];
            newIndex++;
            i1++;
        }
        return count;
    }

    /**
     * ---------------------------- Insertion Sort ---------------------------------------
     */
    public static long insertionSort(int[] array) {
        int unsortedValue;  // The first unsorted value
        int scan;           // Used to scan the array
        int counter = 0;   // Counter for the number of operations

        // The outer loop steps the index variable through
        // each subscript in the array, starting at 1. The portion of
        // the array containing element 0  by itself is already sorted.
        for (int index = 1; index < array.length; index++) {
            // The first element outside the sorted portion is
            // array[index]. Store the value of this element
            // in unsortedValue.
            unsortedValue = array[index];

            // Start scan at the subscript of the first element
            // outside the sorted part.
            scan = index;

            // Move the first element in the still unsorted part
            // into its proper position within the sorted part.
            while (scan > 0 && array[scan - 1] > unsortedValue) {
                counter++;
                array[scan] = array[scan - 1];
                scan--;
            }

            // Insert the unsorted value in its proper position
            // within the sorted subset.
            array[scan] = unsortedValue;
        }
        return counter;
    }

    /**
     * ---------------------------- Quick Sort ---------------------------------------
     */
    public static long quickSort(int array[]) {

        return doQuickSort(array, 0, array.length - 1, 0);
    }

    /**
     * The doQuickSort method uses the quick sort algorithm to sort an int array.
     *
     * @param array The array to sort.
     * @param start The starting subscript of the list to sort
     * @param end The ending subscript of the list to sort
     */
    private static long doQuickSort(int array[], int start, int end, long numberOfCompares) {
        int pivotPoint;

        if (start < end) {
            // Get the pivot point.
            pivotPoint = part1(array, start, end);
            // Note - only one +/=
            numberOfCompares += (end - start);
            // Sort the first sub list.
            numberOfCompares = doQuickSort(array, start, pivotPoint - 1, numberOfCompares);

            // Sort the second sub list.
            numberOfCompares = doQuickSort(array, pivotPoint + 1, end, numberOfCompares);
        }
        return numberOfCompares;
    }

    /**
     * The partition method selects a pivot value in an array and arranges the
     * array into two sub lists. All the values less than the pivot will be
     * stored in the left sub list and all the values greater than or equal to
     * the pivot will be stored in the right sub list.
     *
     * @param array The array to partition.
     * @param start The starting subscript of the area to partition.
     * @param end The ending subscript of the area to partition.
     * @return The subscript of the pivot value.
     */
    private static int part1(int array[], int start, int end) {
        int pivotValue;    // To hold the pivot value
        int endOfLeftList; // Last element in the left sub list.
        int mid;           // To hold the mid-point subscript

        // Find the subscript of the middle element.
        // This will be our pivot value.
        mid = (start + end) / 2;

        // Swap the middle element with the first element.
        // This moves the pivot value to the start of
        // the list.
        swap(array, start, mid);

        // Save the pivot value for comparisons.
        pivotValue = array[start];

        // For now, the end of the left sub list is
        // the first element.
        endOfLeftList = start;

        // Scan the entire list and move any values that
        // are less than the pivot value to the left
        // sub list.
        for (int scan = start + 1; scan <= end; scan++) {
            if (array[scan] < pivotValue) {
                endOfLeftList++;
                swap(array, endOfLeftList, scan);
            }
        }

        // Move the pivot value to end of the
        // left sub list.
        swap(array, start, endOfLeftList);

        // Return the subscript of the pivot value.
        return endOfLeftList;
    }

    /**
     * ---------------------------- Bubble Sort ---------------------------------------
     */
    public static long bubbleSort(int[] array) {
        int lastPos;     // Position of last element to compare
        int index;       // Index of an element to compare
        int counter = 0; // Counter for the number of operations

        // The outer loop positions lastPos at the last element
        // to compare during each pass through the array. Initially
        // lastPos is the index of the last element in the array.
        // During each iteration, it is decreased by one.
        for (lastPos = array.length - 1; lastPos >= 0; lastPos--) {
            // The inner loop steps through the array, comparing
            // each element with its neighbor. All of the elements
            // from index 0 through lastPos are involved in the
            // comparison. If two elements are out of order, they
            // are swapped.
            for (index = 0; index <= lastPos - 1; index++) {
                // Compare an element with its neighbor.
                counter++;
                if (array[index] > array[index + 1]) {
                    // Swap the two elements.

                    swap(array, index, index + 1);
                }
            }
        }
        return counter;
    }

    /**
     * A demonstration of recursive counting in a Binary Search
     * @param array - array to search
     * @param low - the low index - set to 0 to search whiole array
     * @param high - set to length of array to search whole array
     * @param value - item to search for
     * @param count - Used in recursion to accumulate the number of comparisons made (set to 0 on initial call)
     *
     * @return
     */
    private static int[] binarySearchR(int[] array, int low, int high, int value, int count)
    {
        int middle;     // Mid point of search

        // Test for the base case where the value is not found.
        if (low > high)
            return new int[] {-1,count};

        // Calculate the middle position.
        middle = (low + high) / 2;

        // Search for the value.
        if (array[middle] == value)
            // Found match return the index
            return new int[] {middle, count};

        else if (value > array[middle])
            // Recursive method call here (Upper half of remaining data)
            return binarySearchR(array, middle + 1, high, value, count+1);
        else
            // Recursive method call here (Lower half of remaining data)
            return binarySearchR(array, low, middle - 1, value, count+1);
    }

    /**
     * Interface for the sorting methods
     */
    interface SortMethod {
        long sort(int[] array);
    }

    /**
     * Runs a sorting method on an array and outputs the time it takes and the number of comparisons
     * @param sortMethod The sort method to use
     * @param sortName The name of the sort method
     * @param array The array to sort
     * @param totalRuns The number of runs to perform per sort method
     */
    private static void runSortMethod(SortMethod sortMethod, String sortName, int[] array, int totalRuns) {
        long totalTime = 0; // Counter for the total time spent taken to run each method
        long totalCompares = 0; // Counter for the number of comparisons

        // Ensure array is sorted
        if (ckSumSorted(array) != -1) {
            throw new IllegalArgumentException("Array is not sorted");
        }

        int[] arrayClone = new int[0];
        for (int i = 0; i < totalRuns; i++) {
            arrayClone = array.clone(); // Create a copy of the array to keep the original unsorted
            long start = System.currentTimeMillis();
            long compares = sortMethod.sort(arrayClone);
            long end = System.currentTimeMillis();

            totalTime += (end - start);
            totalCompares += compares;
        }

        // Calculate the average time and number of comparisons
        double avgTime = totalTime / (double) totalRuns;
        double avgCompares = totalCompares / (double) totalRuns;
        // Output
        System.out.printf("%-10s", sortName);
        System.out.printf("%,10.7f ms", avgTime);
        System.out.printf("%,14d ops", (long) avgCompares);
        System.out.printf("%,14.7f ms / op", avgTime / avgCompares);
        System.out.printf("%,12d \n", ckSumSorted(arrayClone));
    }
    /**
     * Calls each sorting method for the given array
     * @param array The array to run the sort methods on
     * @param sortMethods The array of sort methods to use
     * @param sortNames The array of sort names
     * @param totalRuns The number of runs to perform per sort method
     */
    private static void runArraySort(int[] array, SortMethod[] sortMethods, String[] sortNames, int totalRuns) {
        System.out.printf("\nComparison of sorts, Array size = %,d total runs = %,d\n", array.length, totalRuns);
        System.out.println("==============================================================");
        System.out.println("Algorithm      Run time     # of compares         ms / compares    checksum");

        // Run each sort method on the array
        for (int i = 0; i < sortMethods.length; i++) {
            runSortMethod(sortMethods[i], sortNames[i], array, totalRuns);
        }
    }
    /**
     * Generates an array of random integers
     * @param studentID The number used to seed the random number generator
     * @param arraySize THe size of the array to generate
     * @return The generated array
     */
    private static int[] generateArray(long studentID, int arraySize) {
        Random rand = new Random(studentID);
        int[] array = new int[arraySize];
        for(int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(1, arraySize);
        }
        return array;
    }

    /**
     * Main method
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        // PART 1:
        long myStudentID = 886545;
        // Generate arrays of set sizes
        int[][] arrays = {
                generateArray(myStudentID, 20),
                generateArray(myStudentID, 400),
                generateArray(myStudentID, 8000)
        };
        // Array of sort names to be used for output
        String[] sortNames = {
                "Selection", "Radix", "Merge", "Insertion", "Quick", "Bubble"
        };
        // Initialize an array of all sorting methods for method referencing
        SortMethod[] sortMethods = {
                array -> selectionSort(array),
                array -> radixSort(array),
                array -> mergeSort(array),
                array -> insertionSort(array),
                array -> quickSort(array),
                array -> bubbleSort(array),
        };
        // For each array, shuffle it and run each sorting method on it
        for (int[] array : arrays) {
            shuffle(array); // Shuffle the array
            int totalRuns = (1_000_000 / array.length);
            runArraySort(array, sortMethods, sortNames, totalRuns); // Run every sorting method on the array
        }
        System.out.println("\n==============================================================\n");

        // PART 2:
        // Create an array of 100,000 random integers
        int[] array = new int[100_000];
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(1, 100_000);
        }
        int result;
        // Perform linear search for value '-1' in array and track how long it takes
        long start = System.currentTimeMillis();
        for (int i : array) {
            if (i == -1) {
                result = i;
            }
        }
        long end = System.currentTimeMillis();
        long linearSearchTime = (end - start);
        // Output linear search time
        System.out.println("Linear Search: " + linearSearchTime + " ms");

        // Track merge sort time
        start = System.currentTimeMillis();
        mergeSort(array);
        end = System.currentTimeMillis();
        long sortTime = (end - start);

        // Track binary search time
        start = System.currentTimeMillis();
        binarySearchR(array, 0, array.length - 1, -1, 0);
        end = System.currentTimeMillis();
        long binarySearchTime = (end - start);

        // Output merge sort and binary search time
        System.out.println("Merge Sort: " + sortTime + " ms");
        System.out.println("Binary Search: " + binarySearchTime + " ms");

        // Find the number of linear searches required to justify sorting the array
        double numLinearSearches = (double) (sortTime + binarySearchTime) / linearSearchTime;
        System.out.println("Linear searches to justify sorting: " + numLinearSearches);
    }
}
