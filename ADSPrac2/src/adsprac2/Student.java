/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

import java.util.BitSet;

/**
 *
 * @author Anouk
 */
public class Student{
    
    private BitSet answers;
    private int score;
    
    Student(BitSet answers, int score) {
        this.answers = answers;
        this.score = score;
    }
    
    public BitSet getAnswers() {
        return this.answers;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void setAnswers(BitSet a) {
        this.answers = a;
    }
    
    public void setScore(int s) {
        this.score = s;
    }

}
