

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
public class ADSPrac2 {
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
        //File file = new File("C:\\Users\\Anouk\\Documents\\Third year AI\\Algoritmen en Datastructuren\\ADSPrac2\\ADSPrac2\\src\\samples\\sample-A.1.in");
        //File file = new File("C:\\Users\\mlmla\\Documents\\Y3\\Algorithms & Data Structures\\ADSPrac2\\ADSPrac2\\src\\samples\\sample-A.3.in");
        Scanner scan = new Scanner(System.in);
        
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
        
        //Find the total number of points scored over all students
        int total = 0;
        for(Student student : students){
            total += student.getScore();
        }
        
        //Generate all possible left and right models
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
        
        //Sort L2 based on sum of vector
        ArrayList<ArrayList<int[]>> sortedL2 = sortVectorList(L2, total);
        
        // Tried this but already too slow at set B
        for(int s=0; s<students.length; s++){
            reduceBest(L1, L2, models1, models2, students[s], s);
            reduceWorst(L1, L2, models1, models2, students[s], s);
        }
        /*
        Student best = students[0];
        reduceBest(L1, L2, models1, models2, best);
        Student worst = students[students.length-1];
        reduceWorst(L1, L2, models1, models2, worst, students.length-1); */
        
        
        
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
                      s++;
                  }
                  if(possible) {
                      int[] model1 = models1.get(i);
                      int[] model2 = models2.get(j);
                      int[] model = new int[nrQuestions];
                      System.arraycopy(model1, 0, model, 0, sizeHalf1);
                      System.arraycopy(model2, 0, model, sizeHalf1, sizeHalf2);
                      totalModels.add(model);
                  }
              }
          }
          
          return totalModels;
      }
      
      private static ArrayList<int[]> findTotalModels2 (ArrayList<int[]> L1, ArrayList<ArrayList<int[]>> sortedL2, ArrayList<int[]> m1, ArrayList<int[]> m2, Student[] students, int total){
          //for every vector in L1
                //compute sum of vector
                //remaining = total - sum
                //for every vector in sortedL2.get(remaining)
                        //for every student
                                //check if vectorL1 + vectorL2 == score of student
                                //if so: corresponding model is correct
          
          
          
          return null;
      }
     
      
     /*
      Returns an ArrayList of length the total number of points scored over all students.
      At each index, the vectors are stored that give the sum equal to this index.
      */
     private static ArrayList<ArrayList<int[]>> sortVectorList(ArrayList<int[]> L, int length){
         ArrayList<ArrayList<int[]>> sorted = new ArrayList(length);
         for(int[] vector : L){
             int sum = 0;
             for(int score : vector){
                 sum += score;
             }
             
             if(sorted.get(sum) == null){
                 ArrayList<int[]> a = new ArrayList<>();
             }
             sorted.get(sum).add(vector);
         }
         
         return sorted;
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
     
     /*
     Remove all L1/models1 and L2/models2 entries that make it impossible for the best student to obtain its total score
     */
     private static void reduceBest(ArrayList<int[]> L1, ArrayList<int[]> L2, ArrayList<int[]> models1, ArrayList<int[]> models2, Student best, int index){
         int score = best.getScore();
         ArrayList<Integer> toRemove1 = new ArrayList<>();
         ArrayList<Integer> toRemove2 = new ArrayList<>();
         for(int i=0; i<L1.size(); i++){
             if(L1.get(i)[index] < score - sizeHalf2){
                 toRemove1.add(i);
             }
         }
         for(int i=0; i<L2.size(); i++){
             if(L2.get(i)[index] < score - sizeHalf1){
                 toRemove2.add(i);
             }
         }
         
         //Remove entries from L1, L2, models1 and models2
         for(int i=toRemove1.size()-1; i>=0; i--){
             int r = toRemove1.get(i);
             L1.remove(r);
             models1.remove(r);
         }
         for(int i=toRemove2.size()-1; i>=0; i--){
             int r = toRemove2.get(i);
             L2.remove(r);
             models2.remove(r);
         }
         //System.out.println("Size toRemove1: " + toRemove1.size());
         //System.out.println("Size toRemove2: " + toRemove2.size());
     }
     
     private static void reduceWorst(ArrayList<int[]> L1, ArrayList<int[]> L2, ArrayList<int[]> models1, ArrayList<int[]> models2, Student worst, int index){
         int score = worst.getScore();
         ArrayList<Integer> toRemove1 = new ArrayList<>();
         ArrayList<Integer> toRemove2 = new ArrayList<>();
         for(int i=0; i<L1.size(); i++){
             if(L1.get(i)[index] > score){
                 toRemove1.add(i);
             }
         }
         for(int i=0; i<L2.size(); i++){
             if(L2.get(i)[index] > score){
                 toRemove2.add(i);
             }
         }
         
         //Remove entries from L1, L2, models1 and models2
         for(int i=toRemove1.size()-1; i>=0; i--){
             int r = toRemove1.get(i);
             L1.remove(r);
             models1.remove(r);
         }
         for(int i=toRemove2.size()-1; i>=0; i--){
             int r = toRemove2.get(i);
             L2.remove(r);
             models2.remove(r);
         }
         //System.out.println("Size toRemove1: " + toRemove1.size());
         //System.out.println("Size toRemove2: " + toRemove2.size());
     }
    
}

