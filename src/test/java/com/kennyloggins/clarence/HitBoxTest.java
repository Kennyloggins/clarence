/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.geom.Ellipse2D;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;

/**
 *
 * @author Bill
 */
public class HitBoxTest {
    @Test
    public void ContainedShapesIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(25, 25, 50, 50);
        
        HitBox a = new HitBox(circle1);
        HitBox b = new HitBox(circle2);
        
        assertTrue(a.intersects(b, circle2.getX() - circle1.getX(), 
                circle2.getY() - circle1.getY()));
    }
    
    @Test
    public void EqualShapesIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(0, 0, 100, 100);
        
        HitBox a = new HitBox(circle1);
        HitBox b = new HitBox(circle2);
        
        assertTrue(a.intersects(b, circle2.getX() - circle1.getX(), 
                circle2.getY() - circle1.getY()));
    }
    
    @Test
    public void TangentShapesDoNotIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(100, 0, 100, 100);
        
        HitBox a = new HitBox(circle1);
        HitBox b = new HitBox(circle2);
        
        assertFalse(a.intersects(b, circle2.getX() - circle1.getX(), 
                circle2.getY() - circle1.getY()));
    }
    
    @Test
    public void DisjointShapesDoNotIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(200, 0, 100, 100);
        
        HitBox a = new HitBox(circle1);
        HitBox b = new HitBox(circle2);
        
        
        assertFalse(a.intersects(b, circle2.getX() - circle1.getX(), 
                circle2.getY() - circle1.getY()));
    }
}
