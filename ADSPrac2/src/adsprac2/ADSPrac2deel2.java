/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Anouk
 */
public class ADSPrac2deel2 {
    private static int nrOfStudents;
    private static int nrQuestions;
    private static int sizeHalf1;
    private static int sizeHalf2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        Student[] students = readIn();
        
        StudentComparator sc = new StudentComparator();
        Arrays.sort(students, sc.reversed());
        
        ArrayList<int[]> models = findPossibleModels(students);
        if(models.size() == 1){
            int[] model = models.get(0);
            for(int i=0; i<model.length; i++){
                System.out.print(model[i]);
            }
        }
        else{
            System.out.print(models.size() + " solutions");
        }
        
    }
    
    private static Student[] readIn() throws FileNotFoundException {
        File file = new File("C:\\Users\\Anouk\\Documents\\Third year AI\\Algoritmen en Datastructuren\\ADSPrac2\\ADSPrac2\\src\\samples\\sample-A.1.in");
        Scanner scan = new Scanner(file);
        
        nrOfStudents = scan.nextInt();
        nrQuestions = scan.nextInt();
        scan.nextLine();

        
        Student[] students;
        students = new Student[nrOfStudents];
        
        for (int pos = 0; pos<nrOfStudents; pos++) {
            String line = scan.nextLine();
            String[] linelist = line.split(" ");
            String number = linelist[0];
            Integer score = Integer.parseInt(linelist[1]);
            
           
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
    private static ArrayList<int[]> generateAnswerModels(int nrQuestions){
        
        int nrModels = (int) Math.pow(2, nrQuestions);
        ArrayList<int[]> models = new ArrayList<>();
        
        for(int i=0; i<nrModels; i++)
        {
            int temp = i;
            int[] answers = new int[nrQuestions];
            for (int j = 0; j < nrQuestions; j++)
            {
                if (temp%2 == 1){
                    answers[j]=1;
                }
                else{
                    answers[j] = 0;
                    
                }
                temp = temp/2;
            }
            models.add(answers);
        }
        return models;
        
        
    }
    
     private static ArrayList<int[]> findPossibleModels(Student[] students){
        //divide into halves
        sizeHalf1 = nrQuestions/2;
        sizeHalf2 = nrQuestions-sizeHalf1;
        
        
        ArrayList<int[]> models1 = generateAnswerModels(sizeHalf1);
        ArrayList<int[]> models2;
        if (sizeHalf1==sizeHalf2) {
            models2 = new ArrayList<>(models1);
        }
        else {
            models2 = generateAnswerModels(sizeHalf2);
        }
        
        ArrayList<int[]> L1 = generateL1(models1, students);
        ArrayList<int[]> L2 = generateL2(models2, students);
        ArrayList<int[]> totalModels = findTotalModels(L1, L2, models1, models2, students);
        
        return totalModels;
     }
     
     /**
      * nu hebben we dubbele code dus moeten we nog ff mooimaken op t end
      * @param models
      * @param students
      * @return 
      */
     private static ArrayList<int[]> generateL1(ArrayList<int[]> models, Student[] students) {
         ArrayList<int[]> L1 = new ArrayList<>();
         for (int[] model : models) {
             int [] vector = new int[nrOfStudents];
             for (int i=0; i<students.length; i++) {
                 Student student = students[i];
                 int[] answers = student.getAnswers();
                 int score = computeSubScore(model, Arrays.copyOfRange(answers, 0, sizeHalf1));
                 vector[i] = score;
             }
            L1.add(vector);
         }
         
         return L1;
     }
     
      private static ArrayList<int[]> generateL2(ArrayList<int[]> models, Student[] students) {
         ArrayList<int[]> L2 = new ArrayList<>();
         for (int[] model : models) {
             int [] vector = new int[nrOfStudents];
             for (int i=0; i<students.length; i++) {
                 Student student = students[i];
                 int[] answers = student.getAnswers();
                 int score = computeSubScore(model, Arrays.copyOfRange(answers, sizeHalf1, nrQuestions));
                 vector[i] = score;
             }
            L2.add(vector);
         }
         
         return L2;
     }
     
    
      private static ArrayList<int[]> findTotalModels(ArrayList<int[]> L1, ArrayList<int[]> L2, ArrayList<int[]> models1, ArrayList<int[]> models2, Student[] students) {
          ArrayList<int[]> totalModels = new ArrayList<>();
          for (int i=0; i<L1.size(); i++) {
              int[] subscores1 = L1.get(i);
              for (int j=0; j<L2.size(); j++) {
                  int[] subscores2 = L2.get(j);
                  boolean possible = true;
                  int s = 0;
                  while(possible && s<nrOfStudents){
                      if (subscores1[s]+subscores2[s] != students[s].getScore()) {
                          possible = false;
                      }
                  }
                  if(possible) {
                      int[] model1 = models1.get(i);
                      int[] model2 = models2.get(j);//model1 en model2 samenvoegen
                      int[] model = new int[nrQuestions];
                      System.arraycopy(model1, 0, model, 0, sizeHalf1);
                      System.arraycopy(model2, 0, model, sizeHalf1, sizeHalf2);
                      totalModels.add(model);
                  }
              }
          }
          
          return totalModels;
      }
      
     private static int computeSubScore (int[] model, int[] answers) {
         int score = 0;
         for (int i =0; i <model.length; i++) {
             if (model[i]==answers[i]) {
                 score++;
             }
         }
         return score;
     }
    
}
