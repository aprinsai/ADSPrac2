/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

import java.util.BitSet;

/**
 *
 * @author mlmla
 */
public class Vector {
    private int[] vector;
    private BitSet model;
    
    public Vector(int[] vector, BitSet model){
        this.vector = vector;
        this.model = model;
    }
    
    public int[] getVector(){
        return vector;
    }
    
    public BitSet getModel(){
        return model;
    }
}
