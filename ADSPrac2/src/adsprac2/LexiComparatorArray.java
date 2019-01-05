/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

import java.util.Comparator;

/**
 *
 * @author mlmla
 */
public class LexiComparatorArray implements Comparator<int[]>{

    @Override
    public int compare(int[] o1, int[] o2) {
        int length1 = o1.length;
        int length2 = o2.length;
        int length = Math.min(length1, length2);
        for(int i=0; i<length; i++){
            if(o1[i] < o2[i]){
                return -1;
            }
            if(o2[i] < o1[i]){
                return 1;
            }
        }
        return 0;
    }
    
}
