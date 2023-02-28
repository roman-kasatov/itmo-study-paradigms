package search;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class BinarySearch {

    // Pred: args.length > 0 && args[i - 1] >= args[i] for i in 2..(args.length - 1)
    // Post:
    // ((R = args.length - 1) || (args[R + 1] <= args[0])) && args[i] > args[0] for i in 1..R
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] arr = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            arr[i - 1] = Integer.parseInt(args[i]);
        }

        // ((R = args.length - 1) || (args[R + 1] <= args[0])) && args[i] > args[0] for i in 1..R
        if (Arrays.stream(arr).sum() % 2 == 1) {
            System.out.println(iterativeSearch(arr, x));
        } else {
            System.out.println(recursiveSearch(arr, x));
        }
    }

    // Let sorted(n): for i in 1..(n - 1): arr[i - 1] >= arr[i]

    // Pred: sorted(arr.length)
    // Post:
    // ((R = arr.length) || (arr[R] <= x)) && arr[i] > x for i in 1..(R - 1)
    public static int iterativeSearch(int[] arr, int x) {
        int l = -1, r = arr.length;

        // Invariant: 0 <= (r' - l') < (r - l)
        while (r - l > 1) {
            int m = (r + l) / 2;
            if (arr[m] > x) {
                // arr[i] > x for i in 0..m && arr[i] <= x for x in r..(arr.length - 1)
                l = m;
            } else {
                // arr[i] > x for i in 0..l && arr[i] <= x for x in m..(arr.length - 1)
                r = m;
            }
        }
        return r;
    }

    // Pred: sorted(arr.length)
    // Post:
    // ((R = arr.length) || (arr[R] <= x)) && arr[i] > x for i in 1..(R - 1)
    public static int recursiveSearch(int[] arr, int x) {
        // arr[i] > x for i in 0..l(==-1) && arr[i] <= x for x in r(==arr.length)..(arr.length - 1)
        return recursiveSearch(arr, x, -1, arr.length);
    }

    // Pred: sorted(arr.length) && r >= l &&
    // arr[i] > x for i in 0..l && arr[i] <= x for x in r..(arr.length - 1)
    // Invariant: 0 <= (r' - l') < (r - l)
    // Post:
    // ((R = arr.length) || (arr[R] <= x)) && arr[i] > x for i in 1..(R - 1)
    public static int recursiveSearch(int[] arr, int x, int l, int r) {
        if (r - l <= 1) {
            return r;
        }

        int m = (r + l) / 2;
        if (arr[m] > x) {
            // Pred: arr[i] > x for i in 0..m && arr[i] <= x for x in r..(arr.length - 1)
            return recursiveSearch(arr, x, m, r);
        } else {
            // Pred: arr[i] > x for i in 0..l && arr[i] <= x for x in m..(arr.length - 1)
            return recursiveSearch(arr, x, l, m);
        }
    }
}
