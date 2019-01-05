/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

/**
 *
 * @author mlmla
 */
public class Vector {
    private int[] vector;
    private int index;
    
    public Vector(int[] vector, int index){
        this.vector = vector;
        this.index = index;
    }
    
    public int[] getVector(){
        return vector;
    }
    
    public int getIndex(){
        return index;
    }
}
