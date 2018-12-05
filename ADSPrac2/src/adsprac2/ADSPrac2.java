/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

import java.util.Scanner;

/**
 *
 * @author Anouk
 */
public class ADSPrac2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    private Student[] readIn() {
        Scanner scan = new Scanner(System.in);
        
        int listSize = scan.nextInt();
        Student[] students;
        students = new Student[listSize];
        
        for (int pos = 0; pos<listSize; pos++) {
            float length = Float.parseFloat(scan.next());
            float width = Float.parseFloat(scan.next());
            float height = Float.parseFloat(scan.next());
            Box box = new Box(length, width, height, pos);
            boxesList[pos] = box;
            
        }
        scan.close();
        return boxesList;
    }
    
}
