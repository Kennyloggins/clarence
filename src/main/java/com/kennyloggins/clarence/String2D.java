/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
    
/**
 * Represents the two-dimensional string that will be drawn.
 */
public class String2D extends CollidableShape {
    //Display attributes
    private final String name;
    private final Font font;
    private final Color color;
    private final GlyphVector gv;

    //Movement functions
    private int movementIteration;

    String2D(String name, Font font, Color color, GlyphVector gv) {
        super(gv.getOutline());

        this.name = name;
        this.font = font;
        this.color = color;
        this.gv = gv;

        movementIteration = 0;
    }

    public String getString() {
        return name;
    }

    public Font getFont() {
        return font;
    }

    public Color getColor() {
        return color;
    }

    public GlyphVector getGlyphVector() {
        return gv;
    }
    
    /**
     * Incrementally moves the string along a spiral path.
     * @param origin - The point the spiral is propagating from.
     * @param xConst - Constant to be applied to the horizontal component of 
     * the spiral.
     * @param yConst - Constant to be applied to the vertical component of
     * the spiral.
     * @param startingAngle - The starting angle of rotation.
     */
    public void moveAlongSpiral(Point2D origin, double xConst, double yConst, double startingAngle) {
        movementIteration++;
        double angle = Math.sqrt(movementIteration);

        double xOffSet = xConst * angle * Math.cos(startingAngle + angle);
        double yOffSet = yConst * angle * Math.sin(startingAngle + angle);
        this.setCenterX(xOffSet + origin.getX());
        this.setCenterY(yOffSet + origin.getY());
    }

    /**
     * Resets the movement state of the String.
     */
    public void resetMovementState() {
        movementIteration = 0;
    }
}