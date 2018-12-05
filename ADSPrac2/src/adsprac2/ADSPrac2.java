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
        int nrQuestions = scan.nextInt();
        
        Student[] students;
        students = new Student[listSize];
        
        for (int pos = 0; pos<listSize; pos++) {
            int answers = scan.nextInt();
            int score = scan.nextInt();
            
            String number = String.valueOf(answers);
            char[] ans = number.toCharArray(); //of naar string????????????????????????? of int???????????
            
            Student s = new Student(ans, score);
            students[pos] = s;
            
        }
        scan.close();
        return students;
    }
    
}
