/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Bill
 */
public class ConcaveHullTest {
    @Test
    public void AllPointsInHullOrContained() {
        List<Point> points = new ArrayList<>();
        
        //Generate 20 random points
        for(int i = 0; i < 20; i++) 
            points.add(new Point((int)(100 * Math.random()), (int)(100 * Math.random())));
        
        ConcaveHull ch = new ConcaveHull(points);
        
        List<Point> hull = ch.getHull((int)(100 * Math.random() + 1));
        
        //Build the shape
        GeneralPath gp = new GeneralPath();
        gp.moveTo(hull.get(0).x, hull.get(0).y);
        for(Point p : hull)
            gp.lineTo(p.x, p.y);
        gp.closePath();
        Area s = new Area(gp);
        
        //Make sure every point is contained or on the edge
        boolean allContained = true;
        for(Point p : points) {
            if(!s.contains(p)) {
                boolean contained = false;
                for(Point hullPoint : hull) {
                    if(hullPoint == p)
                        contained = true;
                }
                if(!contained) {
                    allContained = false;
                    break;
                }
            }
        }
        
        assertTrue(allContained);
    }
}
