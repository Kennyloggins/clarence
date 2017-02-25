/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * ConcaveHull provides methods for calculating a concave hull of a given set
 * of points.
 * 
 * @author Bill
 */
public class ConcaveHull {
    private final Grid grid;
    private final List<Point> points;
    private static final int CH_ROWS = 100, CH_COLUMNS = 100;
    ConcaveHull(List<Point> points) {
        assert(points != null && !points.isEmpty());
        
        this.points = new LinkedList<>(points);
        
        removeDuplicates(this.points);
        sortByDescendingY(this.points);
        
        //Build the grid
        int maxX = 0, maxY = this.points.get(0).y;
        int minX = 0, minY = this.points.get(this.points.size() - 1).y;
        for(Point p : this.points) {
            if(p.x > maxX) maxX = p.x;
            if(p.x < minX) minX = p.x;
        }
        //Add 1 to account for rounding errors when adding the last point
        int w = maxX - minX + 1;
        int h = maxY - minY + 1;
        
        //TODO: come up with some logic to determine optimal grid layout
        //Around 100 seems good most of the time though
        grid = new Grid(minX, minY, w, h, CH_ROWS, CH_COLUMNS);
        for(Point p : this.points) {
            boolean success = grid.add(p);
            assert(success);
        }
    }
    
    /**
     * Removes duplicate points in the provided list, keeping order.
     * the provided points were already pre-sorted. 
     * @param points The list of points to edit.
     */
    private void removeDuplicates(List<Point> points) {
        //Preserve order
        Set<Point> uniquePoints = new LinkedHashSet<>(points);
        if(uniquePoints.size() != points.size()) {
            points.clear();
            for(Point p : uniquePoints) 
                points.add(p);
        }
    }
    
    /**
     * Sorts the provided list in descending order of Y coordinates.
     * @param points The list of points to sort.
     */
    private void sortByDescendingY(List<Point> points) {
        Collections.sort(points, (Point lhs, Point rhs) -> 
                lhs.y > rhs.y ? -1 : lhs.y == rhs.y ? 0 : 1);
    }
    
    /**
     * Sorts the list of points by order of right-hand turn from the provided point.
     * @param points the list of points to sort.
     * @param origin the point to base the angle calculation off.
     * @param prevAngle base offset of right-hand turn calculation.
     */
    public void sortByRightHandTurn(List<Point> points, Point origin, double prevAngle) {
        if(points == null)
            return;
        
        Collections.sort(points, (Point lhs, Point rhs) -> {
            double l = getRightHandTurn(lhs, origin, prevAngle);
            double r = getRightHandTurn(rhs, origin, prevAngle);
            return l > r ? -1 : l == r ? 0 : 1;
        });
    }
    
    /**
     * Returns the "right-hand turn" angle, in rectangular units.
     * 
     *    offsetX (~3.25)
     *   /
     *  / 
     * /  
     * \  )result (~1.5) 
     *  \  
     *   \ 
     *    end - start (~0.75)
     * 
     * @param start Initial point of the vector.
     * @param end End point of the vector.
     * @param offsetX The amount to offset (from x-axis) the result
     * @return The angle of right hand turn from offsetX.
     */
    public static double getRightHandTurn(Point start, Point end, double offsetX) {
        double x = end.x - start.x; 
        double y = end.y - start.y; 
        double angle;
        
        //Compute the angle in rectangular units (i.e. 0-4 instead of 0-2pi)
        if(y >= 0) angle = (x >= 0 ? y/(x+y) : 1-x/(-x+y));
        else angle = (x < 0 ? 2-y/(-x-y) : 3+x/(x-y));
        
        //Convert to "right hand turn"
        if(angle > 0) angle = 4 - angle;
        else if(angle < 0) angle = -angle;
        
        //Offset by the result by the provided x-axis-based angle
        angle -= offsetX;
        
        //Keep within 0-4 range
        while(angle < 4) angle += 4;
        while(angle >= 4) angle -= 4;
        
        return angle;
    }
    
    /**
     * Returns an ordered list of points that represent -a- concave hull. Since
     * there is more than one such hull, the result will be based on the 
     * {@code smoothness} parameter. This determines the number of neighboring nodes
     * the algorithm checks at each point along the hull. A greater smoothness will
     * result in a smoother hull.
     * 
     * @param smoothness The number of neighboring nodes the algorithm searches for
     * each point along the hull. Higher smoothness results in longer computation
     * time, but a smoother curve.
     * @return An ordered list of points that represent -a- concave hull.
     */
    public List<Point> getHull(int smoothness) {
        int numNeighbors = Math.max(smoothness, 3);
        
        //Needs at least 3 points
        if(points.size() < 3)
            return null;
        
        //If exactly 3 points, return the triangle
        if(points.size() == 3)
            return new ArrayList<>(points);
        
        //Initialize
        double prevAngle = 0;
        int step = 1;
        List<Point> hull = new ArrayList<>();
        Set<Point> visited = new HashSet<>();
        numNeighbors = Math.min(numNeighbors, points.size());
        
        Point first = points.get(points.size() - 1);
        Point curr = first;
        
        hull.add(first);
        visited.add(curr);
        
        while(((curr != first) || step == 1) && points.size() > step) {
            //Add back the initial point once it's possible to finish the hull
            if(step == 4)
                visited.remove(first);
            
            //Get the nearest neighbors, then sort by right-hand angle in
            //ascending right-hand turn
            List<Point> nearestPoints = grid.getNearestNeighbors(curr, numNeighbors, visited);
            sortByRightHandTurn(nearestPoints, curr, prevAngle);
            
            //Check each point in order for any intersections with the already
            //created hull.
            int i = 0;
            boolean intersects = true;
            while(intersects && i < nearestPoints.size()) {
                int last = 0;
                if(nearestPoints.get(i) == first)
                    last = 1;
                int j = 1;
                intersects = false;
                while(!intersects && (j < (hull.size() - last))) {
                    intersects = intersects(hull.get(step - 1), nearestPoints.get(i), 
                            hull.get(step - 1 - j), hull.get(step - j));
                    j++;
                }
                i++;
            }
            
            //failure, all intersect, increase the smoothness
            if(intersects)
                return getHull(smoothness + 1);
            
            curr = nearestPoints.get(i - 1);
            hull.add(curr);
            visited.add(curr);
            prevAngle = getRightHandTurn(hull.get(step-1), hull.get(step), 0);
            step++;
        }
        
        return hull;
    }
    
    private boolean intersects(Point a1, Point a2, Point b1, Point b2) {
        Line2D l1 = new Line2D.Double(a1, a2);
        return l1.intersectsLine(b1.x, b1.y, b2.x, b2.y) && !a1.equals(b2) && !a2.equals(b1);
    }
}

