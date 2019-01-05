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
import java.util.Collections;
import java.util.List;
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
        //File file = new File("C:\\Users\\mlmla\\Documents\\Y3\\Algorithms & Data Structures\\ADSPrac2\\ADSPrac2\\src\\samples\\sample-A.2.in");
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
        //Divide questions into halves
        sizeHalf1 = nrQuestions/2;
        sizeHalf2 = nrQuestions-sizeHalf1;
        
        //Generate all possible left and right models
        ArrayList<int[]> models1 = generateAnswerModels(sizeHalf1);
        ArrayList<int[]> models2;
        if (sizeHalf1==sizeHalf2) {
            models2 = new ArrayList<>(models1);
        }
        else {
            models2 = generateAnswerModels(sizeHalf2);
        }
        
        //Generate L1 and L2
        ArrayList<Vector> L1 = generateL1(models1, students);
        ArrayList<Vector> L2 = generateL2(models2, students);
        
        //Sort L1 and L2 lexicographically, each Vector still has access to the index of the corresponding model
        LexiComparatorVector ls = new LexiComparatorVector();
        Collections.sort(L1, ls);
        Collections.sort(L2, ls.reversed());
        
        //Create vector that contains all students' scores
        int[] scores = new int[nrOfStudents];
        for(int i=0; i<nrOfStudents; i++){
            scores[i] = students[i].getScore();
        }
        
        //Find matches of L1 and L2 items giving the correct scores
        ArrayList<int[]> totalModels = findTotalModels(L1, L2, models1, models2, scores);
        
        return totalModels;
     }
     
     /**
      * nu hebben we dubbele code dus moeten we nog ff mooimaken op t end
      * @param models
      * @param students
      * @return 
      */
     private static ArrayList<Vector> generateL1(ArrayList<int[]> models, Student[] students) {
         ArrayList<Vector> L1 = new ArrayList<>();
         for (int m=0; m<models.size(); m++){
             int[] model = models.get(m);
             int [] vector = new int[nrOfStudents];
             for (int i=0; i<students.length; i++) {
                 Student student = students[i];
                 int[] answers = student.getAnswers();
                 int score = computeSubScore(model, Arrays.copyOfRange(answers, 0, sizeHalf1));
                 vector[i] = score;
             }
            L1.add(new Vector(vector, m));
         }
         
         return L1;
     }
     
      private static ArrayList<Vector> generateL2(ArrayList<int[]> models, Student[] students) {
         ArrayList<Vector> L2 = new ArrayList<>();
         for (int m=0; m<models.size(); m++){
             int[] model = models.get(m);
             int [] vector = new int[nrOfStudents];
             for (int i=0; i<students.length; i++) {
                 Student student = students[i];
                 int[] answers = student.getAnswers();
                 int score = computeSubScore(model, Arrays.copyOfRange(answers, sizeHalf1, nrQuestions));
                 vector[i] = score;
             }
            L2.add(new Vector(vector, m));
         }
         
         return L2;
     }
     
    
     private static ArrayList<int[]> findTotalModels(ArrayList<Vector> L1, ArrayList<Vector> L2, ArrayList<int[]> models1, ArrayList<int[]> models2, int[] scores){
         ArrayList<int[]> totalModels = new ArrayList<>();
         LexiComparatorArray lc = new LexiComparatorArray();
         int intL1 = 0;
         int intL2 = 0;
         int sizeL1 = L1.size();
         int sizeL2 = L2.size();
         
         while(intL1 < sizeL1 && intL2 < sizeL2){
             int[] v1 = L1.get(intL1).getVector();
             int[] v2 = L2.get(intL2).getVector();
             int[] sum = sumVectors(v1, v2);
             int compare = lc.compare(sum, scores);
             
             if(compare == -1){ //sum is lexographically smaller than scores
                 intL1++;
             }
             else if(compare == 1){ //sum is lexographically larger than scores
                 intL2++;
             }
             else{ //sum and scores are equal
                 //we have found a match, but there may be vectors of the same value right below our current v1 and v2
                 int[] nextIndices = checkDuplicates(L1, L2, models1, models2, intL1, intL2, totalModels);
                 intL1 = nextIndices[0];
                 intL2 = nextIndices[1];
             }
         }
        
         return totalModels;
     } 
     
     
     
    private static int[] sumVectors(int[] v1, int[] v2){
        if(v1.length == v2.length){
            int[] sum = new int[v1.length];
            for(int i=0; i<v1.length; i++){
                sum[i] = v1[i] + v2[i];
            }
            return sum;
        }
        else{
            System.out.println("Given vectors had different lengths.");
            return null;
        }
    } 
    
    
    private static int[] checkDuplicates(ArrayList<Vector> L1, ArrayList<Vector> L2, ArrayList<int[]> models1, ArrayList<int[]> models2, int intL1, int intL2, ArrayList<int[]> totalModels){
        //Find duplicates in L1
        boolean same1 = true;
        int[] vector1 = L1.get(intL1).getVector();
        ArrayList<Integer> duplicates1 = new ArrayList<>();
        while(same1 && intL1 < L1.size()){
            if(!(Arrays.equals(L1.get(intL1).getVector(), vector1))){
                same1 = false;
            }
            else{
                duplicates1.add(intL1);
                intL1++;
            }
        }
        
        //Find duplicates in L2
        boolean same2 = true;
        int[] vector2 = L2.get(intL2).getVector();
        ArrayList<Integer> duplicates2 = new ArrayList<>();
        while(same2 && intL2 < L2.size()){
            if(!(Arrays.equals(L2.get(intL2).getVector(), vector2))){
                same2 = false;
            }
            else{
                duplicates2.add(intL2);
                intL2++;
            }
        }
        
        //Combine the duplicates into complete models and add these to the solutions
        addSolutions(L1, L2, models1, models2, duplicates1, duplicates2, totalModels);
        
        int[] nextIndices = {intL1, intL2};
        return nextIndices;
    }
      
      
    private static void addSolutions (ArrayList<Vector> L1, ArrayList<Vector> L2, ArrayList<int[]> models1, ArrayList<int[]> models2, ArrayList<Integer> duplicates1, ArrayList<Integer> duplicates2, ArrayList<int[]> totalModels){
        ArrayList<int[]> toCombine1 = new ArrayList<>();
        for(Integer i : duplicates1){
            toCombine1.add(models1.get(L1.get(i).getIndex()));
        }
        
        ArrayList<int[]> toCombine2 = new ArrayList<>();
        for(Integer i : duplicates2){
            toCombine2.add(models2.get(L2.get(i).getIndex()));
        }
        
        for(int i=0; i<toCombine1.size(); i++){
            for(int j=0; j<toCombine2.size(); j++){
                int[] combined = new int[nrQuestions];
                System.arraycopy(toCombine1.get(i), 0, combined, 0, sizeHalf1);
                System.arraycopy(toCombine2.get(j), 0, combined, sizeHalf1, sizeHalf2);
                totalModels.add(combined);    
            }
        }
    } 
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      /*
        // Tried this but already too slow at set B
        for(int s=0; s<students.length; s++){
            reduceBest(L1, L2, models1, models2, students[s], s);
            reduceWorst(L1, L2, models1, models2, students[s], s);
        } */
        /*
        Student best = students[0];
        reduceBest(L1, L2, models1, models2, best);
        Student worst = students[students.length-1];
        reduceWorst(L1, L2, models1, models2, worst, students.length-1); */
      
      
      
      
      /*private static ArrayList<int[]> findTotalModels(ArrayList<int[]> L1, ArrayList<int[]> L2, ArrayList<int[]> models1, ArrayList<int[]> models2, Student[] students) {
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
      } */
      
      private static ArrayList<int[]> findTotalModels2 (ArrayList<int[]> L1, ArrayList<ArrayList<int[]>> sortedL2, ArrayList<int[]> m1, ArrayList<ArrayList<int[]>> sortedModels2, Student[] students, int studentIndex){
          ArrayList<int[]> totalModels = new ArrayList<>();
          int score = students[studentIndex].getScore();
                  
          //for every vector in L1
          for(int v1=0; v1<L1.size(); v1++){
              int[] vector1 = L1.get(v1);
              int remaining = score - vector1[studentIndex];
              if(remaining <= sizeHalf2 && remaining >= 0){ //if the remaining number of points is larger than the number of questions in the second half, than vector1 is invalid
                //for every vector that corresponds to the remaining points that our student needs to get in the second half
                for(int v2=0; v2<sortedL2.get(remaining).size(); v2++){
                    int[] vector2 = sortedL2.get(remaining).get(v2);
                    boolean possible = true;
                    int s = 0;
                    while(possible == true && s<students.length){
                        if(vector1[s] + vector2[s] != students[s].getScore()){
                            possible = false;
                        }
                        s++;
                    }
                    //if possible still true, then vector1 and vector2 together make a correct answer model
                    if(possible) {
                        int[] model1 = m1.get(v1);
                        int[] model2 = sortedModels2.get(remaining).get(v2);
                        int[] model = new int[nrQuestions];
                        System.arraycopy(model1, 0, model, 0, sizeHalf1);
                        System.arraycopy(model2, 0, model, sizeHalf1, sizeHalf2);
                        totalModels.add(model);
                    }
                }
              }
          }
                
          return totalModels;
      }
     
      
     /*
      Returns an ArrayList "sortedL2" of length sizeHalf2+1
      At index i, the vectors are stored that give the particular student subscore i
      Also returns an ArrayList "sortedModels2" that puts the models corresponding vectors in the same positions as the vectors have in "sorted"
      */
     private static ArrayList<ArrayList<ArrayList<int[]>>> sortL2(ArrayList<int[]> L2, ArrayList<int[]> m2, int s){
         ArrayList<ArrayList<int[]>> sortedL2 = new ArrayList<>(); 
         ArrayList<ArrayList<int[]>> sortedModels2 = new ArrayList<>();
         //subscores can range from 0 up to and including sizeHalf2, create entries for all these values
         for(int i=0; i<sizeHalf2+1; i++){
             sortedL2.add(null);
             sortedModels2.add(null);
         }
         
         //go through all vectors in L2
         for(int i=0; i<L2.size(); i++){
             int[] vector = L2.get(i);
             int subscore = vector[s];
             
             if(sortedL2.get(subscore) == null){
                 ArrayList<int[]> a = new ArrayList<>();
                 sortedL2.set(subscore, a);
                 ArrayList<int[]> m = new ArrayList<>();
                 sortedModels2.set(subscore, m);
             }
             sortedL2.get(subscore).add(vector);
             sortedModels2.get(subscore).add(m2.get(i));
         }
         
         ArrayList<ArrayList<ArrayList<int[]>>> results = new ArrayList<>();
         results.add(sortedL2);
         results.add(sortedModels2);
         return results;
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