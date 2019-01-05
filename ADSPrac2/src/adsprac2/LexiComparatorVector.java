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
public class LexiComparatorVector implements Comparator<Vector>{

    @Override
    public int compare(Vector o1, Vector o2) {
        int[] v1 = o1.getVector();
        int[] v2 = o2.getVector();
        int length1 = v1.length;
        int length2 = v2.length;
        int length = Math.min(length1, length2);
        for(int i=0; i<length; i++){
            if(v1[i] < v2[i]){
                return -1;
            }
            if(v2[i] < v1[i]){
                return 1;
            }
        }
        return 0;
    }
    
}
