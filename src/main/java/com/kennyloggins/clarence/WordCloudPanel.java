/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Bill
 */
public class WordCloudPanel extends JPanel {
    private static final long serialVersionUID = -3009020538577863914L;
    //Component attributes.
    private int width, height;
    private int highestCount;
    private int maxIterations;
    private int maxIterationsPerWord;
    private int maxWords;
    private final List<Word> wordCounts;

    //String attributes.
    private double defaultFontSize;
    private double fontScaleFactor;
    private int minimumFontSize;

    //List of placed string objects.
    private List<String2D> strPositions;

    //Shapes to check against for collision.
    private CollidableShape screenBoundary;
    private Shape outliningShape;
    private Shape transformedOutliningShape;
    private String2D cache;
    private double screenArea;
    private double enclosedArea;
    private AffineTransform outlineTransform;
    private List<Point> outliningPoints;
    private double stringPadding;
    
    private List<String> fonts;

    //Partitioned field to facilitate quicker collision detection
    private ScreenPartition partitionRoot;
    private int iterations;

    /**
     * Constructs the Word Cloud Panel.
     */
    public WordCloudPanel() {
        initComponents();
        
        this.maxIterations = 5000;
        this.maxIterationsPerWord = 250;
        this.maxWords = 500;
        this.wordCounts = new ArrayList<>();
        
        defaultFontSize = 200;
        fontScaleFactor = 0.8;
        screenArea = 0;
        enclosedArea = 0;
        minimumFontSize = 7;
        
        strPositions = new ArrayList<>();
        screenBoundary = null;
        transformedOutliningShape = null;
        outliningShape = null;
        outlineTransform = new AffineTransform();
        outliningPoints = new ArrayList<>();
        stringPadding = 1.0;
                
        fonts = new ArrayList<>();
        fonts.addAll(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames()));
    }
    
    /**
     * Sets the maximum number of iterations before terminating the overall generation process.
     * @param maxIterations The maximum number of iterations.
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    /**
     * Sets the maximum number of iterations before skipping the generation of a single word.
     * @param maxIterationsPerWord The maximum number of iterations to spend generating a single word.
     */
    public void setMaxIterationsPerWord(int maxIterationsPerWord) {
        this.maxIterationsPerWord = maxIterationsPerWord;
    }
    
    /**
     * The maximum number of words to include in the final word cloud image.
     * @param maxWords The maximum number of words.
     */
    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }
    
    /**
     * Sets the scale factor, which controls how much the font size is scaled.
     * 
     * Lower scale factor means font size is reduced at a slower rate as frequency decreasing.
     * @param fontScaleFactor The desired scale factor.
     */
    public void setFontScaleFactor(double fontScaleFactor) {
        this.fontScaleFactor = fontScaleFactor;
    }
    
    /**
     * Sets the default font size. This is the font size for the largest word.
     * @param fontSize The default font size.
     */
    public void setFontSize(double fontSize) {
        this.defaultFontSize = fontSize;
    }
    
    /**
     * Sets the minimum font size.
     * @param minimumFontSize - The minimum font size.
     */
    public void setMinimumFontSize(int minimumFontSize) {
        this.minimumFontSize = minimumFontSize;
    }
    
    /**
     * Sets the padding between strings. This is the minimum distance allowed between strings.
     * @param padding The minimum distance allowed between strings.
     */
    public void setStringPadding(double padding) {
        this.stringPadding = padding;
    }
    
    /**
     * @return The maximum iterations this word cloud instance will run before terminating.
     */
    public int getMaxIterations() {
        return this.maxIterations;
    }
    
    /**
     * @return The maximum iterations this word cloud will attempt to generate a single word.
     */
    public int getMaxIterationsPerWord() {
        return this.maxIterationsPerWord;
    }
    
    /**
     * @return The maximum number of words this word cloud will generate.
     */
    public int getMaxWords() {
        return this.maxWords;
    }
    
    /**
     * @return The coefficient determining how much font size decreases as word frequency decreases. Lower means slower font size decrease.
     */
    public double getFontScaleFactor() {
        return this.fontScaleFactor;
    }
    
    /**
     * @return The font size of the largest word in the word cloud.
     */
    public double getFontSize() {
        return this.defaultFontSize;
    }
    
    /**
     * @return The current iteration stage of the word cloud.
     */
    public int getCurrentIterationCount() {
        return iterations;
    }
    
    /**
     * @return The number of words currently positioned in the word cloud.
     */
    public int getCurrentWordCount() {
        return strPositions.size();
    } 
    
    /**
     * @return The minimum font size possible in the word cloud.
     */
    public int getMinimumFontSize() {
        return minimumFontSize;
    }
    
    /**
     * @return The padding between strings, i.e. the minimum distance allowed between two strings.
     */
    public double getStringPadding() {
        return stringPadding;
    }
    
    /**
     * Loads the word counts that will be used to generate the word cloud. Higher values means larger key text.
     * @param wordCounts The map containing the word of counts used to generate the word cloud.
     */
    public void setWordCounts(Map<String, Integer> wordCounts) {
        if(this.wordCounts.size() > 0)
            this.wordCounts.clear();
        
        for(Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            this.wordCounts.add(new Word(key, value));
        }
        //Sort by count
        Collections.sort(this.wordCounts, (Word lhs, Word rhs) -> 
                lhs.count > rhs.count ? -1 : lhs.count == rhs.count ? 0 : 1);
    }
    
    /**
     * Sets the shape that will constrain the positioning of the words of the word cloud.
     * @param s The shape that all words should be contained inside.
     */
    public void setOutliningShape(Shape s) {
        outliningShape = transformedOutliningShape = s;
    }
    
    /**
     * Transforms the outlining shape, if set, from its origin point.
     * @param scaleX The amount to scale in the X direction.
     * @param scaleY The amount to scale in the Y direction.
     * @param translateX The amount to translate in the X direction.
     * @param translateY The amount to translate in the Y direction.
     */
    public void setOutlineTransform(double scaleX, double scaleY, double translateX, double translateY) {
        if(transformedOutliningShape == null)
            return;
        
        outlineTransform = new AffineTransform();
        outlineTransform.scale(scaleX, scaleY);
        outlineTransform.translate(translateX, translateY);
        transformedOutliningShape = outlineTransform.createTransformedShape(outliningShape);
        outliningPoints = getPointsFromShape(transformedOutliningShape);
    }
    
    /**
     * Sets the available fonts that will be used when generating the word cloud.
     * @param fonts The list of available fonts. Contents will be randomly chosen in the generation process.
     */
    public void setFonts(List<String> fonts) {
        this.fonts = fonts;
    }
    
    /**
     * Simple pair class to keep track of each string's frequency.
     */
    private class Word implements Comparable<Word> {
        String name;
        int count;

        Word(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public int compareTo(Word w) {
            return w.count - this.count;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Entry point to creating the word cloud layout.
     */
    public void generateWordCloud() {
        this.getGraphics().dispose();
        
        initialize();
        
        createBoundingShapes();
        
        if(wordCounts.isEmpty())
            return;
        
        int i = 0;
        Word w = wordCounts.get(0);
        highestCount = w.count;
        while(i < maxWords && i < wordCounts.size()) {
            if(iterations >= maxIterations) break;
            createAndPositionWord(wordCounts.get(i));
            i++;
        }
        repaint();
    }
    
    /**
     * Resets the necessary fields for subsequent generations.
     */
    public void initialize() {
        strPositions = new ArrayList<>();
        screenBoundary = null;
        iterations = 0;
        width = getSize().width;
        height = getSize().height;
        screenArea = width * height;
        partitionRoot = new ScreenPartition(-width/2, -height/2, width, height);
        System.gc();
    }
    
    /**
     * Constructs objects to be used in collision detection from outlining shapes.
     */
    private void createBoundingShapes() {
        //Constrain to the screen
        screenBoundary = new CollidableShape(new Rectangle2D.Double(-width / 2, -height / 2, width, height));
        
        //If no outliningShape is provided, use a rectangle the size of the screen.
        if(transformedOutliningShape == null) {
            int resolution = 50;
            
            double dX = width / resolution;
            double dY = height / resolution;
            
            for(int i = 0; i < resolution; i++) {
            	outliningPoints.add(new Point((int)(i * dX - width / 2), - height / 2));
            	outliningPoints.add(new Point((int)(i * dX - width / 2), height / 2));
            	outliningPoints.add(new Point(- width / 2, (int)(i * dY - height / 2)));
            	outliningPoints.add(new Point(width / 2, (int)(i * dY - height / 2)));
            }
            
            enclosedArea = width * height;
        }
        else {
            //Compute area
            enclosedArea = getEnclosedArea(transformedOutliningShape);
            outliningPoints = getPointsFromShape(transformedOutliningShape);
        }
        
    }
    
    /**
     * Calculates the enclosed area of a shape.
     * @param s the shape whose area will be calculated.
     * @return the area enclosed by the provided shape.
     */
    private double getEnclosedArea(Shape s) {
        double area = 0;
        double[] coords = new double[6];
        double prevX = 0, prevY = 0;
        double firstX = 0, firstY = 0;
        boolean first = true;
        for(PathIterator pi = s.getPathIterator(null); !pi.isDone(); pi.next()) {
            pi.currentSegment(coords);
            if(!first)
                area += prevX * coords[1] - coords[0] * prevY;
            else {
                firstX = coords[0];
                firstY = coords[1];
                first = false;
            }
            prevX = coords[0];
            prevY = coords[1];
        }
        area += prevX * firstY - prevY * firstX;
        return Math.abs(area / 2);
    }
    
    /**
     * Creates the Font, Color, and Graphic representation (GlyphVector) for the
     * given string.
     * 
     * Checks 
     * @param w - String and word count
     */
    private void createAndPositionWord(Word w) {
        String2D s2d = buildString2D(w);
        int currentIterations = 1;
        do {
            if(currentIterations >= maxIterationsPerWord)
                return;
            
            if(currentIterations % 10 == 0)
                s2d = buildString2D(w);
            
            Point2D origin = findStartingPoint(s2d);
            s2d.setCenterX(origin.getX());
            s2d.setCenterY(origin.getY());
            
            cache = null;
            while(intersectsOtherStrings(s2d)) 
                relocate(s2d, origin);
            
            iterations++;
            currentIterations++;
        } while(!withinBounds(s2d));
        
        strPositions.add(s2d);
        partitionRoot.add(s2d);
    }
    
    /**
     * Helper function to build a string2d.
     * @param w - the string & associated word count.
     * @return randomized string2d object.
     */
    private String2D buildString2D(Word w) {
        Font f = createFont(w);
        Color c = createColor(w);
        GlyphVector v = f.createGlyphVector(getFontMetrics(f).getFontRenderContext(), w.name);
        
        return new String2D(w.name, f, c, v);
    }
    
    /**
     * Creates the appropriately sized and styled font for the given word
     * @param w - The word for which to create a font
     * @return New appropriately sized and rotated font
     */
    private Font createFont(Word w) {
        //Base size of the word on frequency
        int fontSize = Math.max((int)(defaultFontSize * Math.sqrt(enclosedArea / screenArea) * 
                Math.pow((double)w.count / (double)highestCount, fontScaleFactor)), minimumFontSize);
        //Select font name
        int index = (int)(Math.random() * fonts.size());
        String name = fonts.get(index);
        Font f = new Font(name, Font.PLAIN, fontSize); 
        
        f = rotateFont(f);
        
        return f;    
    }
    
    /**
     * Rotates the word.
     * @param f - The font that will be rotated;
     * @return Rotated font.
     */
    private Font rotateFont(Font f) {
        FontRenderContext frc = getFontMetrics(f).getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(f.getName(), frc);
        AffineTransform at = new AffineTransform();
        double angle = Math.PI / 2 * Math.round((2 * Math.random() - 1));
        
        //Rotate around the center of the string box
        at.rotate(angle, bounds.getCenterX(), bounds.getCenterY());
        
        return f.deriveFont(at);
    }
    
    /**
     * Creates the appropriate color for the given word
     * @param w - The word for which to create a color
     * @return Appropriate color for the word
     */
    private Color createColor(Word w) {
        /*int red = 125 - (int)(125 * Math.random());
        int green = 150 - (int)(100 * Math.random());
        int blue = 150 - (int)(100 * Math.random());*/
        
        int offset = (int)(80 * Math.random());
        int red = 80 - offset;
        int green = 95 - offset;
        int blue = 135 - offset;
        
        return new Color(red, green, blue);
    }
    
    /**
     * Finds a satisfactory starting point for the placement of the string image.
     * @param s2d - The string2d to place.
     * @return A point deemed satisfactory for initial positioning of the string.
     */
    private Point2D findStartingPoint(String2D s2d) {
        Point start;
        Point end;
        if(outliningPoints != null) {
            do {
                start = outliningPoints.get((int)(outliningPoints.size() * Math.random()));
                end = outliningPoints.get((int)(outliningPoints.size() * Math.random()));
            } while(start.x == end.x && start.y == end.y);
        }
        else {
            //If no outlining shape, get a random start and end point on the screen perimeter
            start = new Point((int)(width * Math.random()), (int)(height * Math.random()));
            end = new Point((int)(width * Math.random()), (int)(height * Math.random()));
            if(Math.random() > 0.5)
                start.x = (int)(width * Math.round(Math.random()));
            else
                start.y = (int)(height * Math.round(Math.random()));
            if(Math.random() > 0.5)
                end.x = (int)(width * Math.round(Math.random()));
            else
                end.y = (int)(height * Math.round(Math.random()));
        }
        
        //Higher chance of being at the end points
        double mult = Math.sqrt(Math.random()) / 2;
        if(Math.random() > 0.5)
            mult = 0.5 - mult;
        else
            mult = 0.5 + mult;
        
        double dX = end.x - start.x;
        double dY = end.y - start.y;
        return new Point((int)(start.x + mult * dX), (int)(start.y + mult * dY));
    }
    
    /**
     * Checks all relevant, previously placed strings for collisions. 
     * @param s2d - The string2d to check.
     * @param cache - The previously intersecting string, if any.
     * @return True if there are collisions, false otherwise.
     */
    private boolean intersectsOtherStrings(String2D s2d) {
        if(cache != null && s2d.intersects(cache, stringPadding)) 
            return true;

        for(String2D target : partitionRoot.potentialCollisions(s2d)) {
            if(s2d.intersects(target, stringPadding)) {
                cache = target;
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Calls the necessary movement function for the string.
     * @param s2d - The string2d to move.
     */
    private void relocate(String2D s2d, Point2D origin) {
        s2d.moveAlongSpiral(origin, 10.0, 10.0, 2 * Math.PI * (Math.random() - 0.5));
    }
    
    /**
     * Checks if the string is within bounds of all bounding shapes, such as
     * the UI screen.
     * @param s2d - The string to check.
     * @return Whether the string lies inside all bounding shapes.
     */
    private boolean withinBounds(String2D s2d) {
        Rectangle2D bounds = new Rectangle2D.Double(screenBoundary.getX(), screenBoundary.getY(), 
                screenBoundary.getWidth(), screenBoundary.getHeight());
        if(!bounds.contains(s2d.getBounds()))
            return false;
        if(transformedOutliningShape != null && !transformedOutliningShape.contains(s2d.getBounds()))
            return false;
        return true;
    }
    
    /**
     * Takes an image, converts it to a set of points based on provided color & 
     * tolerance, and constructs an outlining shape from these points. 
     * @param filePath - the filepath to the image.
     * @param targetRGB - the target color to extract.
     * @param tolerance - allowed distance in rgb values from target color. 
     * @return true if successful, false otherwise
     * @throws java.io.IOException
     */
    public static Shape getShapeFromFile(String filePath, Color targetRGB, int tolerance) throws IOException {
        tolerance = Math.min(tolerance, 255);
        
        BufferedImage img;
        img = ImageIO.read(new File(filePath));

        //Convert the image to a set of points
        List<Point> points = getPointsFromImage(img, targetRGB, tolerance);
        if(points.isEmpty())
            return null;

        //Initialize the creation of the concave hull
        ConcaveHull ch = new ConcaveHull(points);

        //Construct the hull points
        List<Point> outlineHull = ch.getHull(20);
        if(outlineHull == null)
            return null;

        //Build the path from the set of points
        GeneralPath gp = new GeneralPath();
        gp.moveTo(outlineHull.get(0).x, outlineHull.get(0).y);
        for(Point p : outlineHull) 
            gp.lineTo(p.x, p.y);
        gp.closePath();

        //Construct the shape and center the image
        Area newArea = new Area(gp);
        Rectangle2D bounds = newArea.getBounds2D();
        AffineTransform at = new AffineTransform();
        at.translate(-bounds.getX() - bounds.getWidth()/2, -bounds.getY() - bounds.getHeight()/2);
        newArea = newArea.createTransformedArea(at);

        return newArea;
    }
   
    /**
     * Gets points provided an image, target color, and allowed tolerance.
     * @param img - the image to decode.
     * @param target - the target color.
     * @param tolerance - the allowed distance between target color and image color.
     * @return set of points indicating the specific pixels matching the criteria.
     */
    public static List<Point> getPointsFromImage(BufferedImage img, Color target, int tolerance) {
        List<Point> ret = new ArrayList<>();
        int w = img.getWidth();
        int h = img.getHeight();
        int targetRGB = target.getRGB();
        
        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                if(withinTolerance(img.getRGB(i, j), targetRGB, tolerance))
                    ret.add(new Point(i, j));
            }
        }
        
        return ret;
    }
    
    /**
     * Unrolls the path iterator of a Shape into a list of points.
     * @param s - Shape to extract.
     * @return List of points taken from the Shape's PathIterator.
     */
    public static List<Point> getPointsFromShape(Shape s) {
        List<Point> points = new ArrayList<>();
        for(PathIterator pi = s.getPathIterator(null); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            pi.currentSegment(coords);
            points.add(new Point((int)coords[0], (int)coords[1]));
        }
        
        List<Point> newPoints = new ArrayList<>();
        for(int i = 1; i < points.size(); i++) {
            newPoints.add(new Point((points.get(i).x + points.get(i-1).x)/2, 
                    (points.get(i).y + points.get(i-1).y)/2));
        }
        points.addAll(newPoints);
        return points;
    }
    
    /**
     * Returns whether the color is within the tolerance value.
     * @param sourceRGB - the RGB value to check.
     * @param targetRGB - the target RGB value.
     * @param tolerance - the allowed tolerance.
     * @return true if the source color is within the tolerable range of the target color.
     */
    public static boolean withinTolerance(int sourceRGB, int targetRGB, int tolerance) {
        //blue
        if(Math.abs((0xFF & sourceRGB) - (0xFF & targetRGB)) > tolerance)
            return false;
        //red
        if(Math.abs((0xFF & (sourceRGB >> 8)) - (0xFF & (targetRGB >> 8))) > tolerance)
            return false;
        //green
        return Math.abs((0xFF & (sourceRGB >> 16)) - (0xFF & (targetRGB >> 16))) <= tolerance;
    }
    
    /**
     * Paints the elements to the screen.
     * @param g - the graphics context. 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        if(strPositions.size() > 0)
            g2d.scale((double)getSize().width / (double)width, (double)getSize().height / (double)height);

        g2d.translate(width/2, height/2);
        
        if(strPositions.isEmpty() && transformedOutliningShape != null) 
            g2d.draw(transformedOutliningShape);
        
        for(String2D s2d : strPositions) {
            g2d.setFont(s2d.getFont());
            g2d.setColor(s2d.getColor());
            g2d.drawGlyphVector(s2d.getGlyphVector(), (int)s2d.getX(), (int)s2d.getY());
        }
    }
    
    public void saveImage(File filePath) throws IOException {
        BufferedImage img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();
        this.paint(g);
        ImageIO.write(img, "png", filePath);
    }
}

