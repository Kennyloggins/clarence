/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.geom.Ellipse2D;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Bill
 */
public class CollidableShapeTest {
    
    @Test
    public void ContainedShapesIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(25, 25, 50, 50);
        
        CollidableShape a = new CollidableShape(circle1);
        CollidableShape b = new CollidableShape(circle2);
        
        assertTrue(a.intersects(b) && b.intersects(a));
    }
    
    @Test
    public void EqualShapesIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(0, 0, 100, 100);
        
        CollidableShape a = new CollidableShape(circle1);
        CollidableShape b = new CollidableShape(circle2);
        
        assertTrue(a.intersects(b) && b.intersects(a));
    }
    
    @Test
    public void TangentShapesDoNotIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(100, 0, 100, 100);
        
        CollidableShape a = new CollidableShape(circle1);
        CollidableShape b = new CollidableShape(circle2);
        
        assertFalse(a.intersects(b) || b.intersects(a));
    }
    
    @Test
    public void DisjointShapesDoNotIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(200, 0, 100, 100);
        
        CollidableShape a = new CollidableShape(circle1);
        CollidableShape b = new CollidableShape(circle2);
        
        assertFalse(a.intersects(b) || b.intersects(a));
    }
    
    @Test
    public void ShiftedShapesIntersect() {
        Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 100, 100);
        Ellipse2D circle2 = new Ellipse2D.Double(0, 0, 90, 90);
        
        CollidableShape a = new CollidableShape(circle1);
        CollidableShape b = new CollidableShape(circle2);
        
        a.setCenterX(550);
        a.setCenterY(500);
        b.setCenterX(575);
        b.setCenterY(500);
        
        assertTrue(a.intersects(b));
    }
}
