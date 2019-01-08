package adsprac2;

/**
 *
 * @author mlmla
 */
public class Vector {
    private int[] vector;
    private boolean[] model;
    
    public Vector(int[] vector, boolean[] model){
        this.vector = vector;
        this.model = model;
    }
    
    public int[] getVector(){
        return vector;
    }
    
    public boolean[] getModel(){
        return model;
    }
}