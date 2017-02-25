/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Bill
 */
public class GridTest {
    /*
    Constructor
    */
    @Test(expected=IllegalArgumentException.class)
    public void ZeroRowColumnCountThrowsException() {
        Grid g = new Grid(0, 0, 100, 100, 0, 0);
    }
    
    @Test
    public void PointInsideGridCanBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point a = new Point(47, 87);
        
        assertTrue(g.add(a));
    }
    
    @Test
    public void PointOnLeftEdgeCanBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point left = new Point(0, 23);
        
        assertTrue(g.add(left));
    }
    @Test
    public void PointOnBottomEdgeCanBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point bottom = new Point(47, 100);
        
        assertTrue(g.add(bottom));
    }
    
    @Test
    public void PointOnRightEdgeCanBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point right = new Point(100, 61);
        
        assertTrue(g.add(right));
    }
    
    @Test
    public void PointOnTopEdgeCanBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
      
        Point top = new Point(71, 0);
        
        assertTrue(g.add(top));
    }
    
    @Test
    public void PointAboveGridCannotBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point above = new Point(45, -1);
        
        assertFalse(g.add(above));
    }
    
    @Test
    public void PointBelowGridCannotBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point below = new Point(23, 101);
        
        assertFalse(g.add(below));
    }
    
    @Test
    public void PointLeftOfGridCannotBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point left = new Point(-1, 49);
        
        assertFalse(g.add(left));
    }
    
    @Test
    public void PointRightOfGridCannotBeAdded() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        
        Point right = new Point(101, 97);
        
        assertFalse(g.add(right));
    }
    
    /*
    getNearestNeighbors
    */
    @Test
    public void nnSinglePointReturnsEmptyList() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        Point a = new Point(50, 50);
        g.add(a);
        
        List<Point> neighbors = g.getNearestNeighbors(a, 1, null);
        assertTrue(neighbors.isEmpty());
    }
    
    @Test
    public void nnReturnsTotalPointsIfLessThanNum() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        Point a = new Point(50, 50);
        Point b = new Point(45, 55);
        Point c = new Point(40, 60);
        Point d = new Point(30, 0);
        g.add(a);
        g.add(b);
        g.add(c);
        g.add(d);
        
        List<Point> neighbors = g.getNearestNeighbors(a, 5, null);
        assertTrue(neighbors.size() == 3 && neighbors.get(0) == b 
                && neighbors.get(1) == c && neighbors.get(2) == d);
    }
    
    @Test
    public void nnReturnsNumPoints() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        Point a = new Point(50, 50);
        Point b = new Point(45, 55);
        Point c = new Point(40, 60);
        Point d = new Point(30, 0);
        g.add(a);
        g.add(b);
        g.add(c);
        g.add(d);
        
        List<Point> neighbors = g.getNearestNeighbors(a, 2, null);
        assertTrue(neighbors.size() == 2 && neighbors.get(0) == b 
                && neighbors.get(1) == c);
    }
    
    @Test
    public void nnTestMinimumRowsAndColumns() {
        Grid g = new Grid(0, 0, 100, 100, 1, 1);
        Point[] points = new Point[11];
        for(int i = 0; i <= 10; i++) {
            points[i] = new Point(10 * i, 10 * i);
            g.add(points[i]);
        }
        
        List<Point> neighbors = g.getNearestNeighbors(points[0], 4, null);
        assertTrue(neighbors.size() == 4 && neighbors.get(0) == points[1] 
                && neighbors.get(1) == points[2] && neighbors.get(2) == points[3] 
                && neighbors.get(3) == points[4]);
    }
    
    @Test
    public void nnTestManyRowsAndColumns() {
        Grid g = new Grid(0, 0, 100, 100, 1000, 1000);
        Point[] points = new Point[11];
        for(int i = 0; i <= 10; i++) {
            points[i] = new Point(10 * i, 10 * i);
            g.add(points[i]);
        }
        
        List<Point> neighbors = g.getNearestNeighbors(points[0], 4, null);
        assertTrue(neighbors.size() == 4 && neighbors.get(0) == points[1] 
                && neighbors.get(1) == points[2] && neighbors.get(2) == points[3] 
                && neighbors.get(3) == points[4]);
    }
    
    @Test
    public void nnTestOneRowManyColumns() {
        Grid g = new Grid(0, 0, 100, 100, 1, 1000);
        Point[] points = new Point[11];
        for(int i = 0; i <= 10; i++) {
            points[i] = new Point(10 * i, 10 * i);
            g.add(points[i]);
        }
        
        List<Point> neighbors = g.getNearestNeighbors(points[0], 4, null);
        assertTrue(neighbors.size() == 4 && neighbors.get(0) == points[1] 
                && neighbors.get(1) == points[2] && neighbors.get(2) == points[3] 
                && neighbors.get(3) == points[4]);
    }
    
    @Test
    public void nnTestManyRowsOneColumn() {
        Grid g = new Grid(0, 0, 100, 100, 1000, 1);
        Point[] points = new Point[11];
        for(int i = 0; i <= 10; i++) {
            points[i] = new Point(10 * i, 10 * i);
            g.add(points[i]);
        }
        
        List<Point> neighbors = g.getNearestNeighbors(points[0], 4, null);
        assertTrue(neighbors.size() == 4 && neighbors.get(0) == points[1] 
                && neighbors.get(1) == points[2] && neighbors.get(2) == points[3] 
                && neighbors.get(3) == points[4]);
    }
    
    @Test
    public void nnTestEqualPoints() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        Point a = new Point(40, 40);
        Point b = new Point(40, 40);
        Point c = new Point(50, 50);
        Point d = new Point(50, 50);
        g.add(a);
        g.add(b);
        g.add(c);
        g.add(d);
        
        List<Point> neighbors = g.getNearestNeighbors(a, 3, null);
        assertTrue(neighbors.size() == 3 && neighbors.get(0) == b 
                && neighbors.get(1) == c && neighbors.get(2) == d);
    }
    
    @Test
    public void nnTestAllVisited() {
        Grid g = new Grid(0, 0, 100, 100, 10, 10);
        Set<Point> visited = new HashSet<>();
        Point a = new Point(40, 40);
        Point b = new Point(50, 50);
        Point c = new Point(60, 60);
        g.add(a);
        g.add(b);
        g.add(c);
        visited.add(a);
        visited.add(b);
        visited.add(c);
        
        List<Point> neighbors = g.getNearestNeighbors(a, 2, visited);
        assertTrue(neighbors.isEmpty());
    }
}
