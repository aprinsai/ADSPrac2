/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

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
    public static void main(String[] args) {
        nrOfStudents=3;
        nrQuestions=5;
        sizeHalf1=3;
        sizeHalf2=2;
        
        ArrayList<Student> students = new ArrayList<>();
        Student s1 = new Student(new int[]{0,1,1,0,1},4);
        Student s2 = new Student(new int[]{1,0,1,0,0},5);
        Student s3 = new Student(new int[]{0,0,0,1,1},3);
        
        students.add(s1);
        students.add(s2);
        students.add(s3);
        
        StudentComparator sc = new StudentComparator();
        students.sort(sc.reversed());
        
        /* TESTING findCombinations ---------------------------------------------*/
        /*
        int[] left1 = {1,1,0};
        ArrayList<int[]> models1 = new ArrayList<>();
        models1.add(left1);
        
        int[] right1 = {1,1};
        int[] right2 = {0,0};
        ArrayList<int[]> models2 = new ArrayList<>();
        models2.add(right1);
        models2.add(right2);
        
        ArrayList<int[]> testCombos = findCombinations(models1, models2);
        System.out.println(testCombos.size());
        for(int k=0; k<testCombos.size(); k++){
            int[] combo = testCombos.get(k);
            System.out.println("test2");
            for(int z=0; z<combo.length; z++){
                System.out.print(combo[z]);
            }
            System.out.println("");
        } */
        /*-----------------------------------------------------------------------*/
        
        //ArrayList<int[]> models = generateAnswerModels(nrQuestions);
        
        //for (int i=0; i<models.length; i++){
        //    if (checkModel(models[i], students))
        //        System.out.print(models[i].toString()); //helaas niks leesbaars
        //}
        //int m=3;
        //Student[] students = readIn();
        //Divide the questions in half
        
        
        //Find models for both halfs
        
        /*
        int totalScore = 4;
        int left = 3;
        int right = 2;
        ArrayList<int[]> divisions = computeDivisions(totalScore, left, right);
        for(int[] division : divisions){
            System.out.println("["+division[0]+","+division[1]+"]");
        } */
        
        /*
        //Check models for both halfs 
        ArrayList<int[]> correctModels1 = new ArrayList<>();
        for(int i=0; i<models1.length; i++){
            if(checkModel(models1[i], students, 0, half1)){
                correctModels1.add(models1[i]);
            }
        }
        ArrayList<int[]> correctModels2 = new ArrayList<>();
        for(int i=0; i<models2.length; i++){
            if(checkModel(models2[i], students, half1+1, m)){
                correctModels2.add(models2[i]);
            }
        } */
        
        //Combine results
        
    }

    
    
    //niet nodig #sad
    private int[][] makeMatrix(Student[] students) {
        int[][] matrix = new int[nrOfStudents][nrQuestions];
        for (int i=0; i<nrOfStudents; i++ ) {
            for (int j=0; j<nrQuestions; j++)
                matrix[i][j] = students[i].getAnswers()[j];
        }
        return matrix;
    }
    
    private static Student[] readIn() {
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
                    temp = temp/2;
                }
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
        //first student will add to this, all following students will make the set smaller
        ArrayList<int[]> totalModels = new ArrayList<>();
        
        //for each student
        for(int s=0; s<students.length; s++){
            Student student = students[s];
            //Each student will leave only a certain combinations of left and right models possible
            ArrayList<int[]> combsOfStudent = new ArrayList<>();
            
            //find all possible score divisions over the two halves
            ArrayList<int[]> divisions = computeDivisions(student.getScore(), sizeHalf1, sizeHalf2);
            
            //Divide student's answers into two sets
            int[] answers = student.getAnswers();
            int[] answers1 = Arrays.copyOfRange(answers, 0, sizeHalf1);
            int[] answers2 = Arrays.copyOfRange(answers, sizeHalf1, nrQuestions);
            
            //for each score division
            for(int[] division : divisions){
                //reduce leftModels and rightModels
                models1 = reduceModels (answers1, models1, division[0]);
                models2 = reduceModels (answers2, models2, division[1]);
                ArrayList<int[]> combos = findCombinations(models1, models2);
                //add combos to combsOfStudent
            }
            
            
            if(s == 0){
                for(int[] combo : combinations){
                    
                }
            }
            
        }
        
        
        
        
        return null;
    }
    
    /**
     * per student will be called twice for both halves
     * remove answermodels from models if they do not give the score that is passed.
     * @param answers
     * @param models
     * @param score 
     */
    private static ArrayList<int[]> reduceModels (int[] answers, ArrayList<int[]> models, int subscore) { //nu nog maar voor 1 subscore
        for(int[] model : models){
            if(!checkModel(model, answers, subscore)){
                models.remove(model);
            }
        }
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
        System.out.println("models1 size: " + models1.size());
        System.out.println("models2 size: " + models2.size());
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
}
