/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Class used to divide the screen field into different 
 * partitions in order to facilitate quicker collision detection.
 */
public class ScreenPartition {
    private final Rectangle2D bounds;
    private boolean hasChildren;
    private final ScreenPartition[] childNodes;
    private final List<String2D> contained;

    ScreenPartition(double x, double y, double w, double h) {
        bounds = new Rectangle2D.Double(x, y, w, h);
        hasChildren = false;
        contained = new ArrayList<>();
        childNodes = new ScreenPartition[4];
    }

    /**
     * Adds strings to the appropriate partition, creating new child nodes
     * as needed to provide greater resolution for collision detection.
     * @param s2d
     */
    public void add(String2D s2d) {
        if(bounds.contains(s2d.getBounds())) {
            //Create new children if necessary
            if(!hasChildren) {
                double x = s2d.getX();
                double y = s2d.getY();
                double w = s2d.getWidth()/2;
                double h = s2d.getHeight()/2;
                hasChildren = true;
                childNodes[0] = new ScreenPartition(x       , y     , w, h);
                childNodes[1] = new ScreenPartition(x + w   , y     , w, h);
                childNodes[2] = new ScreenPartition(x       , y + h , w, h);
                childNodes[3] = new ScreenPartition(x + w   , y + h , w, h);
            }
            //Check children to see if they can contain the image,
            //otherwise add to set
            for(int i = 0; i < 4; i++) {
                if(childNodes[i].bounds.contains(s2d.getBounds())) {
                    childNodes[i].add(s2d);
                    return;
                }
            }
        }

        //Add it to the primary node anyway if it doesn't intersect. This 
        //will guarantee objects will get checked if they somehow fall out
        //of bounds.
        contained.add(s2d);
    }

    /**
     * Gets all String2Ds contained in the partitions that also contain the passed in String2D
     * @param s2d - The String2D whose partitions we wish to check.
     * @return All String2Ds that could possibly lie in the partition.
     */
    public List<String2D> potentialCollisions(String2D s2d) {
        List<String2D> potentialCollisions = new ArrayList<>();

        //Each partition contains strings that are contained within the 
        //partitions bounds, but not the bounds of its child nodes.
        Queue<ScreenPartition> queue = new LinkedList<>();
        queue.add(this);
        ScreenPartition curr;
        while(!queue.isEmpty()) {
            curr = queue.poll();
            if(curr.contained != null)
                potentialCollisions.addAll(curr.contained);
            if(curr.hasChildren) {
                for(ScreenPartition childNode : curr.childNodes) {
                    if(childNode.bounds.contains(s2d.getBounds()) 
                            || childNode.bounds.intersects(s2d.getBounds()))
                        queue.add(childNode);
                }
            }
        }
        return potentialCollisions;
    }
}
