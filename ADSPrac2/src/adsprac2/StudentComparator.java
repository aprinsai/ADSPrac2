/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsprac2;

import java.util.Comparator;

/**
 *
 * @author Anouk
 */
public class StudentComparator implements Comparator<Student>{

    @Override
    public int compare(Student o1, Student o2) {
        return (o1.getScore() - o2.getScore());
    }
    
}
