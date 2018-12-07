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
    
    private static int nrOfStudents;
    private static int nrQuestions;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // nothing yet
    }
    
    private int[][] makeMatrix(Student[] students) {
        int[][] matrix = new int[nrOfStudents][nrQuestions];
        for (int i=0; i<nrOfStudents; i++ ) {
            for (int j=0; j<nrQuestions; j++)
                matrix[i][j] = students[i].getAnswers()[j];
        }
        return matrix;
    }
    
    private Student[] readIn() {
        Scanner scan = new Scanner(System.in);
        
        nrOfStudents = scan.nextInt();
        nrQuestions = scan.nextInt();
        
        Student[] students;
        students = new Student[nrOfStudents];
        
        for (int pos = 0; pos<nrOfStudents; pos++) {
            int answers = scan.nextInt();
            int score = scan.nextInt();
            
            String number = String.valueOf(answers);
            int[] ans = new int[number.length()];
            for (int i=0; i < number.length(); i++) {
                ans[i] = number.charAt(i) - '0'; 
            }
            
            Student s = new Student(ans, score);
            students[pos] = s;
            
        }
        scan.close();
        return students;
    }
    
}
