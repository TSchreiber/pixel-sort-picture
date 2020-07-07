package pixelsortpicture;

import java.util.Arrays;
import javax.swing.*;

/**
 *
 * @author Daniel Schreiber
 * @date Jul 6, 2020
 * purpose: 
 *  
 */
public class VisualizeSort {
    
    public static int maxDepth = 10;
    
    public static boolean sorting;
    
    public static void sort(HSV_Color[] list) {
        sorting = true;
        int l = 0, m = list.length/2, r = list.length;
        mergeSort(list, l, m, r, 0);
        sorting = false;
        list.notify();
    }
    
    private static void mergeSort(HSV_Color[] list, int l, int m, int r, int depth) {
        // If there is only 1 or 0 values then the array is already sorted
        int size = r - l;
        if (size <= 1) {
            return;
        }
        // Left subarray
        int lSize = m - l;
        mergeSort(list, l, l + lSize/2, m, depth+1);
        // Right subarray
        int rSize = r - m;
        mergeSort(list, m, m + rSize/2, r, depth+1);
        // Now we know each sub array is sorted, so merge the sections
        merge(list, l, m, r);
        // 
        synchronized(list) {
            try {
                if(depth < maxDepth) {
                    list.notify();
                    list.wait();
                }
            } catch (InterruptedException ex) {ex.printStackTrace();}
        }
    }
    
    private static void merge(HSV_Color[] list, int l, int m, int r) {
        int a = l, b = m;
        // If b is past r, then the rest is from a and is already in the right place
        // If a passes b, then the rest is in order
        for(int i=l; i<r && b<r && a<b; i++) {
            // A is in the right position, inc index
            if (list[a].compareTo(list[b]) <= 0) {
                a++;
            }
            // B needs to be moved to a, shift array to the right one place
            else {
                HSV_Color val = list[b];
                for(int j=b; j>a; j--) {
                    list[j] = list[j-1];
                }
                list[a] = val;
                a++;
                b++;
            }
        }
    }
}