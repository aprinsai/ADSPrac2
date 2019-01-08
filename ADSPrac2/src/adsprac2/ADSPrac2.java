package adsprac2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        
        ArrayList<boolean[]> models = findPossibleModels(students);
        if(models.size() == 1){
            boolean[] model = models.get(0);
            for(int i=0; i<model.length; i++){
                    if(model[i]){
                        System.out.print("1");
                    }
                    else{
                        System.out.print("0");
                    }
                
            }
        }
        else{
            System.out.print(models.size() + " solutions");
        } 
    }
    
    /**
     * 
     * @return Returns an array containing all students.
     * @throws FileNotFoundException 
     */
    private static Student[] readIn() throws FileNotFoundException {
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
            
           
            boolean[] ans = new boolean[number.length()];
            for (int i=0; i < number.length(); i++) {
                char c = number.charAt(i);
                if(c=='0'){
                    ans[i] = false;
                }
                else{
                    ans[i] = true;
                }
            }
            
            Student s = new Student(ans, score);
            students[pos] = s;
            
        }
        scan.close();
        return students;
    }
    
    
    /**
     * Returns an ArrayList containing all possible answer models (int arrays) for a given number of yes/no questions.
     * @param nrQuestions
     * @return all possible answer models
     */
    private static ArrayList<boolean[]> generateAnswerModels(int nrQuestions){
        
        int nrModels = (int) Math.pow(2, nrQuestions);
        ArrayList<boolean[]> models = new ArrayList<>();
        
        for(int i=0; i<nrModels; i++)
        {
            int temp = i;
            boolean[] answers = new boolean[nrQuestions];
            for (int j = 0; j < nrQuestions; j++)
            {
                if (temp%2 == 1){
                    answers[j]=true;
                }
                else{
                    answers[j] =false;
                    
                }
                temp = temp/2;
            }
            models.add(answers);
        }
        return models;
        
        
    }
    
    /**
     * Returns an ArrayList containing all the answer models that are consistent with the scores of all students.
     * @param students
     * @return all correct answer models
     */
    private static ArrayList<boolean[]> findPossibleModels(Student[] students){
       //Divide questions into halves
       sizeHalf1 = nrQuestions/2;
       sizeHalf2 = nrQuestions-sizeHalf1;

       //Generate all possible left and right models
       ArrayList<boolean[]> models1 = generateAnswerModels(sizeHalf1);
       ArrayList<boolean[]> models2;
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
       ArrayList<boolean[]> totalModels = findTotalModels(L1, L2, scores);

       return totalModels;
    }
    
    
    /**
     * Returns L1: an ArrayList containing the vectors of scores that students receive according to the corresponding submodels of the 
     * first half of the questions.
     * @param models
     * @param students
     * @return 
     */
    private static ArrayList<Vector> generateL1(ArrayList<boolean[]> models, Student[] students) {
        ArrayList<Vector> L1 = new ArrayList<>();
        for (boolean[] model : models){
            int [] vector = new int[nrOfStudents];
            for (int i=0; i<students.length; i++) {
                Student student = students[i];
                boolean[] answers = student.getAnswers();
                int score = computeSubScore(model, Arrays.copyOfRange(answers, 0, sizeHalf1));
                vector[i] = score;
            }
            L1.add(new Vector(vector, model));
        }

        return L1;
    }
    
    /**
     * Returns L2: an ArrayList containing the vectors of scores that students receive according to the corresponding submodels of the 
     * seconds half of the questions.
     * @param models
     * @param students
     * @return 
     */
    private static ArrayList<Vector> generateL2(ArrayList<boolean[]> models, Student[] students) {
        ArrayList<Vector> L2 = new ArrayList<>();
        for (boolean[] model : models){
            int[] vector = new int[nrOfStudents];
            for (int i=0; i<students.length; i++) {
                Student student = students[i];
                boolean[] answers = student.getAnswers();
                int score = computeSubScore(model, Arrays.copyOfRange(answers, sizeHalf1, nrQuestions));
                vector[i] = score;
            }
            L2.add(new Vector(vector, model));
        }

        return L2;
    }
    
    
    /**
     * Returns the score that a set of answers receive based on a certain model.
     * @param model
     * @param answers
     * @return 
     */
    private static int computeSubScore (boolean[] model, boolean[] answers) {
        int score = 0;
        for (int i =0; i <model.length; i++) {
            if (model[i]==answers[i]) {
                score++;
            }
        }
        return score;
    }
     
    
    /**
     * Returns all answer models that are consistent with the scores of all students, based on a sorted L1 and L2. 
     * @param L1
     * @param L2
     * @param models1
     * @param models2
     * @param scores
     * @return 
     */
    private static ArrayList<boolean[]> findTotalModels(ArrayList<Vector> L1, ArrayList<Vector> L2, int[] scores){
        ArrayList<boolean[]> totalModels = new ArrayList<>();
        int intL1 = 0;
        int intL2 = 0;
        int sizeL1 = L1.size();
        int sizeL2 = L2.size();

        while(intL1 < sizeL1 && intL2 < sizeL2){
            int[] v1 = L1.get(intL1).getVector();
            int[] v2 = L2.get(intL2).getVector();
            int s = 0;
            boolean possible = true;
            while(s < nrOfStudents && possible){
                int sum = v1[s] + v2[s];
                int score = scores[s];
                
                if(sum < score){ //sum is lexographically smaller than scores
                    possible = false;
                    intL1++;
                }
                else if(sum > score){ //sum is lexographically larger than scores
                    possible = false;
                    intL2++;
                }
                s++;
            }
            if(possible){
                //We have found a match, but there may be vectors of the same value right below our current v1 and v2
                int[] nextIndices = checkDuplicates(L1, L2, intL1, intL2, totalModels);
                intL1 = nextIndices[0];
                intL2 = nextIndices[1]; 
            }
        }

        return totalModels;
    } 
    
    
    /**
     * Starts at a given indices in the L1 and L2 ArrayLists, and checks for duplicate values at the following indices.
     * The vectors at the given indices and all duplicates are used to find answer models that are consistent with the students' scores.
     * These answer models are added to totalModels.
     * The function returns an array of size two containing the indices in L1 and L2 where the vectors stop being the same as the ones
     * at the starting indices.
     * @param L1
     * @param L2
     * @param models1
     * @param models2
     * @param intL1
     * @param intL2
     * @param totalModels
     * @return 
     */
    private static int[] checkDuplicates(ArrayList<Vector> L1, ArrayList<Vector> L2, int intL1, int intL2, ArrayList<boolean[]> totalModels){
        //Find duplicates in L1
        boolean same1 = true;
        int[] vector1 = L1.get(intL1).getVector();
        ArrayList<Vector> duplicates1 = new ArrayList<>();
        while(same1 && intL1 < L1.size()){
            if(!(Arrays.equals(L1.get(intL1).getVector(), vector1))){
                same1 = false;
            }
            else{
                duplicates1.add(L1.get(intL1));
                intL1++;
            }
        }
        
        //Find duplicates in L2
        boolean same2 = true;
        int[] vector2 = L2.get(intL2).getVector();
        ArrayList<Vector> duplicates2 = new ArrayList<>();
        while(same2 && intL2 < L2.size()){
            if(!(Arrays.equals(L2.get(intL2).getVector(), vector2))){
                same2 = false;
            }
            else{
                duplicates2.add(L2.get(intL2));
                intL2++;
            }
        }
        
        //Combine the duplicates into complete models and add these to the solutions
        addSolutions(duplicates1, duplicates2, totalModels);
        
        int[] nextIndices = {intL1, intL2};
        return nextIndices;
    }
      
    
    /**
     * Combines correct submodels into correct total models and adds these to the totalModels ArrayList.
     * @param L1
     * @param L2
     * @param models1
     * @param models2
     * @param duplicates1
     * @param duplicates2
     * @param totalModels 
     */
    private static void addSolutions (ArrayList<Vector> duplicates1, ArrayList<Vector> duplicates2, ArrayList<boolean[]> totalModels){
        ArrayList<boolean[]> toCombine1 = new ArrayList<>();
        for(Vector v : duplicates1){
            toCombine1.add(v.getModel());
        }
        
        ArrayList<boolean[]> toCombine2 = new ArrayList<>();
        for(Vector v : duplicates2){
            toCombine2.add(v.getModel());
        }
        
        for(int i=0; i<toCombine1.size(); i++){
            for(int j=0; j<toCombine2.size(); j++){
                boolean[] combined = new boolean[nrQuestions];
                System.arraycopy(toCombine1.get(i), 0, combined, 0, sizeHalf1);
                System.arraycopy(toCombine2.get(j), 0, combined, sizeHalf1, sizeHalf2);
                totalModels.add(combined);    
            }
        }
    } 
    
}