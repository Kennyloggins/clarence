package com.kennyloggins.clarence;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class WordCloudTest extends TestCase
{
    private WordCloudPanel wcp;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WordCloudTest( String testName )
    {
        super( testName );
        
        wcp = new WordCloudPanel();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( WordCloudTest.class );
    }

    public void testWithinTolerance()
    {
        assertTrue(wcp.withinTolerance(0, 0, 0));
        assertTrue(wcp.withinTolerance(0, 0, 1));
    }
    
    public void testGetPoints() {
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File("star.png"));
            List<Point> points = wcp.getPointsFromImage(img, new Color(0, 0, 0, 255), 5);
            for(Point p : points) {
                System.out.println(p.x + " " + p.y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
