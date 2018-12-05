/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

/**
 *
 * @author Anouk
 */
public class Student {
    
    private char[] answers;
    private int score;
    
    Student(char[] answers, int score) {
        this.answers = answers;
        this.score = score;
    }
    
    public char[] getAnswers() {
        return this.answers;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void setAnswers(char[] a) {
        this.answers = a;
    }
    
    public void setScore(int s) {
        this.score = s;
    }
}
