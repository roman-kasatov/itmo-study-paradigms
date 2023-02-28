package search;

public class BinarySearchUni {

    // Pre: args.length > 0 && K in 0..args.length - 1: (args[i - 1] <= args[i] for i in 1..K) &&
    // (args[i - 1] >= args[i] for i in (K + 1)..(args.length - 1)) && (K = args.length - 1 ||
    // args[K] < args[args.length - 1])
    // Post:
    // R = K
    public static void main(String[] args) {
        int[] arr = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }

        System.out.println(uniSearch(arr));
    }

    // Pre: arr.length > 0 && K in 0..arr.length - 1: (arr[i - 1] <= arr[i] for i in 1..K) &&
    // (arr[i - 1] >= arr[i] for i in (K + 1)..(arr.length - 1)) && (K = arr.length - 1 ||
    // arr[K] < arr[arr.length - 1])
    // Post:
    // R = K
    private static int uniSearch(int[] arr) {
        int l = 0, r = arr.length;
        // l <= K && r > K

        // Invariant: 0 <= (r' - l') < (r - l) && l <= K <= r
        while (r - l > 1) {
            int m = l + (r - l) / 2;
            if (arr[m] >= arr[m - 1]) {
                // K >= m
                l = m;
            } else {
                // K < m
                r = m;
            }
        }
        return l;
    }
}
