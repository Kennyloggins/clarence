package com.kennyloggins.clarence;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class HitBox {
    //Hierarchical bounding boxes
    private double x, y, w, h;
    private boolean isLeaf = false;
    private boolean hasChildren;
    private HitBox[] childNodes;
    
    HitBox(Shape outline) {
        this(outline, outline.getBounds2D().getX(), outline.getBounds2D().getY(), 
                outline.getBounds2D().getWidth(), outline.getBounds2D().getHeight());
    }
    
    private HitBox(Shape outline, double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        
        childNodes = new HitBox[4];
        
        //Limit node creation to minimum of 1 pixels
        if(Math.max(w, h) < 2 || outline.contains(x, y, w, h)) {
            isLeaf = true;
            return;
        }

        //Create new nodes if the outline intersects with the bounds
        if(outline.intersects(x, y, w, h)) {
            this.hasChildren = true;
            double newW = w / 2;
            double newH = h / 2;
            childNodes[0] = new HitBox(outline, x, y, newW, newH);
            childNodes[1] = new HitBox(outline, x + newW, y, newW, newH);
            childNodes[2] = new HitBox(outline, x, y + newH, newW, newH);
            childNodes[3] = new HitBox(outline, x + newW, y + newH, newW, newH);
        }
    }
    
    /**
     * Checks for intersection between two HitBox objects.
     * @param target - the HitBox to check against.
     * @param xOffSet - the horizontal distance to offset the target hitbox.
     * @param yOffSet - the vertical distance to offset the target hitbox.
     * @return true if the HBoxes intersect, given the offsets.
     */
    public boolean intersects(HitBox target, double xOffSet, double yOffSet)  {
        return this.intersects(target, xOffSet, yOffSet, 0);
    }
    /**
     * Checks for intersection between two HitBox objects.
     * @param target - the HitBox to check against.
     * @param xOffSet - the horizontal distance to offset the target hitbox.
     * @param yOffSet - the vertical distance to offset the target hitbox.
     * @param padding - the minimum allowed distance between HitBoxes before they're considered intersecting.
     * @return true if the HBoxes intersect, given the offsets.
     */
    public boolean intersects(HitBox target, double xOffSet, double yOffSet, double padding)  {
        //Check the actual geometry for an intersection
        Rectangle2D bounds = new Rectangle2D.Double(x - padding/2, y - padding/2, w + padding, h + padding);
        if(bounds.intersects(target.x + xOffSet - padding/2, target.y + yOffSet - padding/2, target.w + padding, target.h + padding)) {
            //A "true" intersection only occurs when two leaf nodes intersect 
            if(this.isLeaf && target.isLeaf)
                return true;
            
            if(this.hasChildren) {
                for(int i = 0; i < 4; i++) {
                    if(target.hasChildren) {
                        for(int j = 0; j < 4; j++) {
                            if(this.childNodes[i].intersects(target.childNodes[j], xOffSet, yOffSet, padding)) 
                                return true;
                        }
                    } else if(target.isLeaf) {
                        if(this.childNodes[i].intersects(target, xOffSet, yOffSet, padding))
                            return true;
                    }
                }
            } else if(this.isLeaf) {
                if(target.hasChildren) {
                    for(int j = 0; j < 4; j++) {
                        if(this.intersects(target.childNodes[j], xOffSet, yOffSet, padding))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
