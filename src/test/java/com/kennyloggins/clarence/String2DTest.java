/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * @author Bill
 */
public class String2DTest {
    Graphics2D g2d;
    
    public String2DTest() {
        //Need a graphics context to create String2D
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
    }
    
    @Test
    public void OverlappingStringsIntersect() {
        Font f = new Font("Rockwell", Font.PLAIN, 100);
        Color c = Color.BLACK;
        GlyphVector gv = f.createGlyphVector(g2d.getFontRenderContext(), "TEST");
        String2D a = new String2D("TEST", f, c, gv);
        String2D b = new String2D("TSET", f, c, gv);
        
        assertTrue(a.intersects(b));
    }
        
    @Test
    public void OverlappingShiftedStringsIntersect() {
        Font f = new Font("Rockwell", Font.PLAIN, 100);
        Color c = Color.BLACK;
        GlyphVector gv = f.createGlyphVector(g2d.getFontRenderContext(), "TEST");
        String2D a = new String2D("TEST", f, c, gv);
        String2D b = new String2D("TSET", f, c, gv);
        
        a.setCenterX(100);
        a.setCenterY(100);
        b.setCenterX(100);
        b.setCenterY(100);
        
        assertTrue(a.intersects(b));
    }
    
    @Test
    public void ShiftedIntersectingStringsIntersect() {
        Font f = new Font("Rockwell", Font.PLAIN, 100);
        Color c = Color.BLACK;
        GlyphVector gv = f.createGlyphVector(g2d.getFontRenderContext(), "TEST");
        String2D a = new String2D("TEST", f, c, gv);
        String2D b = new String2D("TSET", f, c, gv);
        
        a.setCenterX(550);
        a.setCenterY(500);
        b.setCenterX(575);
        b.setCenterY(500);
        
        assertTrue(a.intersects(b));
    }
}
