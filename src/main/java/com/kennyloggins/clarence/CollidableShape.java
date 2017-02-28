package com.kennyloggins.clarence;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * 
 */
public class CollidableShape {
    //Position
    protected double x, y, w, h;
    private final Rectangle2D bounds;

    //Needed to align any future position changes.
    protected double dX, dY; 
    
    //Collision
    private final HitBox root;
    
    //Contains
    private final Shape outline;
    
    CollidableShape(Shape outline) {
        Rectangle2D bounds2D = outline.getBounds2D();
        this.outline = outline;
        
        this.x = this.dX = bounds2D.getX();
        this.y = this.dY = bounds2D.getY();
        this.w = bounds2D.getWidth();
        this.h = bounds2D.getHeight();
        this.bounds = new Rectangle2D.Double(x, y, w, h);
        
        root = new HitBox(outline);
    }
    
     /**
     * Checks if target shape intersects with this shape.
     * @param target - the target shape to check for intersection.
     * @return True if the shapes intersect, false otherwise.
     */
    public boolean intersects(CollidableShape target) {
        return this.intersects(target, 0);
    }
    
    /**
     * Checks if target shape intersects with this shape.
     * @param target - the target shape to check for intersection.
     * @param padding - the distance between the objects that is considered "intersecting."
     * @return True if the shapes intersect, false otherwise.
     */
    public boolean intersects(CollidableShape target, double padding) {
        return root.intersects(target.root, target.x - this.x, target.y - this.y, padding);
    }  
    
    public double getX() {
        return x;
    }
    
    public double getCenterX() {
        return x + w/2;
    }
    
    public double getY() {
        return y;
    }
    
    public double getCenterY() {
        return y + h/2;
    }
    
    public double getWidth() {
        return w;
    }
    
    public double getHeight() {
        return h;
    }
    
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(bounds.getX() + x, bounds.getY() + y, w, h);
    }
    
    public void setCenterX(double x) {
        this.x = x  - w/2 + dX;
    }
    
    public void setCenterY(double y) {
        this.y = y + h + dY;
    }
    
    public boolean contains(Rectangle2D r) {
        return outline.contains(r);
    }
    
    public boolean contains(double x, double y, double w, double h) {
        return outline.contains(x, y, w, h);
    }
    
    public boolean contains(double x, double y) {
        return outline.contains(x, y);
    }
    
    public boolean contains(Point2D p) {
        return outline.contains(p);
    }
}
