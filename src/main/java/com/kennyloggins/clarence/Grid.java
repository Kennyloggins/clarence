/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class Grid {
    private final GridCell[][] cells;
    private final double x, y, w, h;
    private final double cellW, cellH;
    private static final int MAX_ROWS = 1000;
    private static final int MAX_COLUMNS = 1000;
    
    /**
     * Constructs a new Grid with the given position and row/column count.
     * @param x horizontal coordinate of the top left point of the grid
     * @param y vertical coordinate of the top left point of the grid
     * @param w width of the grid
     * @param h height of the grid
     * @param rows The number of rows in the grid. Clamped to 1000 rows or less.
     * @param columns The number of columns in the grid. Clamped to 1000 columns or less.
     * @throws IllegalArgumentException when rows or columns is equal or less than 0
     */
    public Grid(double x, double y, double w, double h, int rows, int columns) throws IllegalArgumentException {
        if(rows <= 0 || columns <= 0)
            throw new IllegalArgumentException();
        
        rows = Math.min(rows, MAX_ROWS);
        columns = Math.min(columns, MAX_COLUMNS);
        
        cells = new GridCell[rows][columns];
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        cellW = w / rows;
        cellH = h / columns;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) 
                cells[i][j] = new GridCell(x + i * cellW, y + j * cellH, cellW, cellH);
        }
    }
    
    /**
     * Returns the {@code GridCell} where {@code p} would be placed.
     * @param p The point to check.
     * @return The {@code GridCell} where {@code p} would be placed.
     */
    private GridCell findCell(Point p) {
        if(p.x < this.x || p.x > (this.x + this.w) || p.y < this.y || p.y > (this.y + this.h))
            return null;
        
        int i = (int)Math.min(((p.x - this.x) / cellW), cells.length - 1);
        int j = (int)Math.min(((p.y - this.y) / cellH), cells[0].length - 1);
        
        return cells[i][j];
    }
    
    /**
     * Adds the point to the grid.
     * @param p The point to add.
     * @return True if the point was added, False if not (e.g. due to being 
     * outside the grid's bounds).
     */
    public boolean add(Point p) {
        if(p == null)
            return false;
        
        GridCell cell = findCell(p);
        
        if(cell == null)
            return false;
        
        return cell.add(p);
    }
    
    /**
     * Returns up to {@code num} points closest to {@code p}.
     * @param p The point distance to neighbors is based on.
     * @param num The maximum number of neighbors to return.
     * @param visited Set of points to exclude from the search.
     * @return Up to {@code num} points closest to {@code p}.
     */
    public List<Point> getNearestNeighbors(Point p, int num, Set<Point> visited) {
        if(visited == null) 
            visited = new HashSet<>();
        
        int i = 0;
        List<Point> ret = new ArrayList<>();
        List<GridCell> surroundingCells;    
        //Points in surrounding cell can be closer than those in the same cell,
        //so get those first.
        //Keeping adding surrounding cells until we have a big enough list, or
        //run out of points.
        while(ret.size() < num || i < 2) {
            surroundingCells = getSurroundingCells(p, i);
            if(surroundingCells == null)
                break;
            for(GridCell cell : surroundingCells) {
                for(Point point : cell.getPoints()) {
                    if(p != point && !visited.contains(point))
                        ret.add(point);
                }
            }
            i++;
        } 
        
        //Sort based on distance to p.
        Collections.sort(ret, (Point lhs, Point rhs) -> {
            double l = Math.pow(lhs.x - p.x, 2) + Math.pow(lhs.y - p.y, 2);
            double r = Math.pow(rhs.x - p.x, 2) + Math.pow(rhs.y - p.y, 2);
            return l < r ? -1 : l == r ? 0 : 1;
        });
        
        //Pare down the list
        if(ret.size() > num)
            ret = new ArrayList<>(ret.subList(0, num));
        
        return ret;
    }
    
    /**
     * Returns a list of all cells within {@code radius} linear units of 
     * {@code cell}.
     * @param center The cell that serves as the center of the search.
     * @param radius The number of horizontal and/or vertical units the returned
     * list of cells should be from {@code center}. A radius of 0 will return 
     * {@code center}.
     * @return A list of all cells within {@code radius} linear units of 
     * {@code cell}. Returns {@code center} if radius is 0, and null if the 
     * radius is entirely outside the grid.
     */
    private List<GridCell> getSurroundingCells(Point p, int radius) {
        if(p == null || radius < 0)
            return null;
        
        if(p.x < this.x || p.x > (this.x + this.w) || p.y < this.y || p.y > (this.y + this.h))
            return null;
        
        List<GridCell> ret = new ArrayList<>();
        
        //Locate the center
        int i = (int)(((double)p.x - this.x) / cellW);
        int j = (int)(((double)p.y - this.y) / cellH);
        if(i == cells.length)
            i = cells.length - 1;
        if(j == cells[0].length)
            j = cells[0].length - 1;
        
        if(radius == 0) {
            ret.add(cells[i][j]);
            return ret;
        }
        
        //Find left, right, top and bottom indices. -1 if outside the grid.
        int left = i - radius >= 0 ? i - radius : -1;
        int right = i + radius < cells.length ? i + radius : -1;
        int top = j - radius >= 0 ? j - radius : -1;
        int bottom = j + radius < cells[0].length ? j + radius : -1;
        
        //Quit if entirely out if radius
        if(left == -1 && right == -1 && top == -1 && bottom == -1)
            return null;
        
        /*
        r = 1, 3x3, c at (2, 2)
        | l | t | t | 
        | l | c | r |
        | b | b | r |
        
        r = 2, 5x5, c at (3, 0)
        | l |   | c |   | r | 
        | l |   |   |   | r | 
        | b | b | b | b | r |
        */
        //Get the left slice, top to bottom
        if(left != -1) {
            int start = (top != -1 ? top : 0);
            int end = (bottom != -1 ? bottom : cells[0].length);
            for(int index = start; index < end; index++)
                ret.add(cells[left][index]);
        }
        //Get the bottom slice, left to right
        if(bottom != -1) {
            int start = (left != -1 ? left : 0);
            int end = (right != -1 ? right : cells.length);
            for(int index = start; index < end; index++)
                ret.add(cells[index][bottom]);
        }
        //Get the right slice, bottom to top
        if(right != -1) {
            int start = (bottom != -1 ? bottom : cells[0].length - 1);
            int end = top; //if top is -1, we want to go to the edge
            for(int index = start; index > end; index--)
                ret.add(cells[right][index]);
        }
        //Get the top slice, right to left
        if(top != -1) {
            int start = (right != -1 ? right : cells.length - 1);
            int end = left;
            for(int index = start; index > end; index--)
                ret.add(cells[index][top]);
        }
        
        return ret;
    }
    
    private class GridCell {
        private final double x, y, w, h;
        private final List<Point> points;
        public GridCell(double x, double y, double w, double h) {
            points = new ArrayList<>();
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
        
        public boolean add(Point p) {
            if(p == null || !contains(p)) 
                return false;
            
            points.add(p);
            return true;
        }
        
        public boolean contains(Point p) {
            return p.x >= x && p.x <= (x + w) && p.y >= y && p.y <= (y + h); 
        }
        
        public List<Point> getPoints() {
            return points;
        }
    }
}
