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
    
    private int nrOfStudents;
    private int nrQuestions;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n=3;
        int[][] models = generateAnswerModels(n);
        for(int i=0; i<models.length; i++){
            for(int j=0; j<n; j++){
                System.out.print(" "+ models[i][j]);
            }
            System.out.println("");
        }
    }
    
    //nodig?
    private int[][] makeMatrix(Student[] students, int nrOfStudents, int nrQuestions) {
        int[][] matrix = new int[nrQuestions][nrOfStudents];
        
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
    
    /*
    Generates all possible answer models for a given number of yes/no questions
    */
    private static int[][] generateAnswerModels(int nrQuestions){
        int nrModels = (int) Math.pow(2, nrQuestions);
        int[][] models = new int[nrModels][nrQuestions];
        
        for(int i=0; i<nrModels; i++)
        {
            int temp = i;
            for (int j = 0; j < nrQuestions; j++)
            {
                if (temp%2 == 1)
                    models[i][j] = 1;
                else
                    models[i][j] = 0;
                    temp = temp/2;
            }
         }
        
        return models;
    }
    
}
