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
        
        /*
        nrOfStudents=4;
        nrQuestions=5;
        //sizeHalf1=3;
        //sizeHalf2=2;
        
        Student[] students = new Student[4];
        //Student s1 = new Student(new int[]{0,1,1,0,1},0);
        //Student s2 = new Student(new int[]{1,0,1,0,0},3);
        //Student s3 = new Student(new int[]{0,0,0,1,1},2);
        Student s1 = new Student(new int[]{0,0,0,0},2);
        Student s2 = new Student(new int[]{1,0,1,0},2);
        Student s3 = new Student(new int[]{0,1,0,1},2);
        Student s4 = new Student(new int[]{1,1,1,1},2);
        
        students[0] = s1;
        students[1] = s2;
        students[2] = s3;
        students[3] = s4;
        */
        
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
        
        //make possible leftModels and possible rightModels (generateAnswerModels)
        ArrayList<int[]> models1 = generateAnswerModels(sizeHalf1);
        ArrayList<int[]> models2 = generateAnswerModels(sizeHalf2);
        
        //make possible totalModels where we save the possible combinations of left and right models
        ArrayList<int[]> totalModels = new ArrayList<>();
        
        boolean first = true;
        int s = 0;
        
        //while-loop goes through all students as long as there are more than 1 possible models
        while((totalModels.size() > 1 && !first || first) && s < students.length){
            first = false;
            Student student = students[s];
            
            //Each student will leave only a certain set of combinations of left and right models possible
            ArrayList<int[]> combsOfStudent = new ArrayList<>();
            
            //find all possible score divisions over the two halves
            ArrayList<int[]> divisions = computeDivisions(student.getScore(), sizeHalf1, sizeHalf2);
            
            //Divide student's answers into two sets
            int[] answers = student.getAnswers();
            int[] answers1 = Arrays.copyOfRange(answers, 0, sizeHalf1);
            int[] answers2 = Arrays.copyOfRange(answers, sizeHalf1, nrQuestions);
            
            //Keep track of which left and right models are left possible by this student
            ArrayList<int[]> allLeft = new ArrayList<>();
            ArrayList<int[]> allRight = new ArrayList<>();
            
            //for each score division
            for(int[] division : divisions){
                ArrayList<int[]> m1copy = new ArrayList<>(models1);
                ArrayList<int[]> m2copy = new ArrayList<>(models2);
                ArrayList<int[]> left = reduceModels(answers1, m1copy, division[0]);
                ArrayList<int[]> right = reduceModels(answers2, m2copy, division[1]);
                
                for(int[] l : left){
                    if(!allLeft.contains(l)){
                        allLeft.add(l);
                    }
                }
                
                for(int[] r : right){
                    if(!allRight.contains(r)){
                        allRight.add(r);
                    }
                }
                
                ArrayList<int[]> combos = findCombinations(left, right);
                
                //add combos to combsOfStudent
                combsOfStudent.addAll(combos);
                
            }
            
            //Reduce models1 and models2
            models1 = overlap(models1, allLeft);
            models2 = overlap(models2, allRight);
            
            //the first student determines the set of solutions to start out with
            if(s == 0){
                for(int[] combo : combsOfStudent){
                    totalModels.add(combo);
                }
            }
            //all subsequent students remove solutions from the original set
            else{
                //keep only the overlap between totalModels and combsOfStudent
                totalModels = overlap(totalModels, combsOfStudent);
            } 
            s++;
        }
        
        //As soon as only one possible model is left, we simply check for all remaining students if it gives the right score
        if(totalModels.size() == 1){
            int[] model = totalModels.get(0);
            for(int i=s; s<students.length; s++){
                Student student = students[i];
                int[] answers = student.getAnswers();
                boolean correct = checkModel(model, answers, student.getScore());
                if(!correct){
                    totalModels.remove(0);
                }
            }
        }
        
        return totalModels;
    }
    
    /**
     * per student will be called twice for both halves
     * remove answermodels from models if they do not give the score that is passed.
     * @param answers
     * @param models
     * @param score 
     */
    private static ArrayList<int[]> reduceModels (int[] answers, ArrayList<int[]> models, int subscore){
        ArrayList<int[]> toRemove = new ArrayList<>();
        for(int[] model : models){
            if(!checkModel(model, answers, subscore)){
                toRemove.add(model);
            }
        }
        models.removeAll(toRemove);
        return models;
    }
    
    private static ArrayList<int[]> computeDivisions(int totalScore, int left, int right){
        ArrayList<int[]> divisions = new ArrayList<>();
        int leftScore = totalScore;
        if(leftScore >= left){ //if the total score is larger than the left part, add the division where the complete first part is correct and the remaining correct answers are in the right half
            int[] subscores = {left, totalScore-left}; 
            divisions.add(subscores);
            leftScore = left - 1;
        }
        while(leftScore >= 0 && totalScore-leftScore <= right){
            int[] subscores = {leftScore, totalScore-leftScore};
            divisions.add(subscores);
            leftScore--;
        }
        return divisions;
    }
    
    private static boolean checkModel(int[] model, int[] answers, int subscore){
        int count = 0;
        for(int i=0; i<answers.length; i++){
            if(model[i] == answers[i]){
                count++;
            }
        }
        return count == subscore;
    }
    
    private static ArrayList<int[]> findCombinations(ArrayList<int[]> models1, ArrayList<int[]> models2) {
        ArrayList<int[]> combinations = new ArrayList<>();
        for(int i=0; i<models1.size(); i++){
            for(int j=0; j<models2.size(); j++){
                int[] combined = new int[nrQuestions];
                System.arraycopy(models1.get(i), 0, combined, 0, sizeHalf1);
                System.arraycopy(models2.get(j), 0, combined, sizeHalf1, sizeHalf2);
                combinations.add(combined);
                
            }
        }
        return combinations;
    }
    
    private static ArrayList<int[]> overlap(ArrayList<int[]> totalModels, ArrayList<int[]> combsOfStudent) {
        ArrayList<int[]> overlap = new ArrayList<>();
        for(int[] model : totalModels){
            if(isIn(model, combsOfStudent)){
                overlap.add(model);
            }
        }
        return overlap;
    }
    
    /*
    Returns true if model occurs in models.
    */
    private static boolean isIn(int[] model, ArrayList<int[]> models){
        for(int[] m : models){
            boolean temp = true;
            for(int i=0; i<m.length; i++){
                if(m[i] != model[i]){
                    temp = false;
                }
            }
            if(temp){
                return true;
            }
        }
        return false;
    }
}