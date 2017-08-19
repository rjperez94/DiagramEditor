import java.util.*;
import ecs100.*;
import java.awt.Color;
import java.io.*;

/** Hexagon represents a solid hexagon shape*/
public class Hexagon implements Shape {
    //fields
    private double x;   //represents top-left corner of hexagon
    private double y;

    private double wd;  //distance of left side from right side
    private Color col;
    private String text;    //text inside hexagon

    /** Constructor with explicit values*/
    public Hexagon (double x, double y, double wd, Color col, String text) {
        this.x = x;
        this.y = y;
        this.wd = wd;
        this.col = col;
        this.text = text;
    }

    /**Constructor which reads values from a String that contains the specification of the Hexagon.*/
    public Hexagon (Scanner in) {
        String description = in.nextLine();     //reads a whole line using previous scanner
        Scanner data = new Scanner(description);    //new scanner that reads tokens
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        this.col = new Color(red, green, blue);
        this.x = data.nextDouble();
        this.y = data.nextDouble();
        this.wd = data.nextDouble();
        if (data.hasNext()) {   //reads till the end of a line -- text can have spaces, IF there is some form of string
            this.text = data.nextLine();
        }
    }

    /** Returns true if the point (u, v) is on top of the shape 
    Selects a elliptical area INSIDE the hexagon*/
    public boolean on(double u, double v) {
        double centerX = this.wd/2 + this.x;
        double centerY = this.wd/2 + this.y;

        double xDiff = Math.pow(u - centerX, 2);
        double yDiff = Math.pow(v - centerY, 2);
        double sum = xDiff + yDiff;
        double radius = Math.pow(this.wd/2, 2);
        if(sum <= radius) {
            return true;
        }
        return false;
    }

    /** Changes the position of the shape by dx and dy.
    If it was positioned at (x, y), it will now be at (x+dx, y+dy)*/
    public void moveBy(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    /** Sets text inside hexagon*/
    public void shapeText(String text) {
        this.text = text;
    }

    /** Draws the hexagon on the graphics pane. It draws a (this.col) border and fills it white.*/
    public void redraw() {
        //for border
        double [] xPoints = {
                this.x, 
                this.wd/2+this.x,
                this.wd+this.x,
                this.wd+this.x,
                this.wd/2+this.x,
                this.x};
        double [] yPoints = {
                this.wd/4+this.y, 
                this.y, 
                this.wd/4+this.y, 
                3*this.wd/4+this.y,
                this.wd+this.y, 
                3*this.wd/4+this.y};

        //for hexagon fill
        double [] xFill = {
                this.x+1, 
                this.wd/2+this.x+1,
                this.wd+this.x-1,
                this.wd+this.x-1,
                this.wd/2+this.x,
                this.x+1};
        double [] yFill = {
                this.wd/4+this.y+1, 
                this.y+1, 
                this.wd/4+this.y+1, 
                3*this.wd/4+this.y-1,
                this.wd+this.y-1, 
                3*this.wd/4+this.y-1};

        UI.setColor(Color.white);   //fill shape
        UI.fillPolygon(xFill, yFill, 6, false);

        UI.setColor(this.col);  //draw border
        UI.drawPolygon(xPoints, yPoints, 6, false);

        if (text != null) { //if there is text
            double centerY = this.wd/2 + this.y;    // text Y-point
            int middleChar = (int)(this.text.length()/2);   //get index of 'mid-point' of text
            double centerX = this.wd/2 + this.x;    //determines letter spacing -- initial X-point
            for (int i = middleChar; i<this.text.length(); i++) {   //draw text from 'mid-point' to end 
                UI.drawString(""+text.charAt(i), centerX, centerY,false);
                centerX+=8;     //for letter spacing LtoR
            }

            centerX = this.wd/2 + this.x;   //reset X-point counter
            for (int i = middleChar-1; i>-1; i--) {     //draw text from 'mid-point' to beginning
                centerX-=8;     //for letter spacing RtoL
                UI.drawString(""+text.charAt(i), centerX, centerY,false);
            }
        }
    }

    /**Changes the width and height (top-centre to bottom-centre) of the shape by the specified amounts.*/
    public void resize (double changeWd, double changeHt) {
        this.wd = this.wd + changeWd/2 + changeWd/2;
    }

    /** Returns a string description of the hexagon in a form suitable for
    writing to a file in order to reconstruct the hexagon later
    The first word of the string must be Hexa */
    public String toString() {
        //if there is no text, leave out text at the end
        if (this.text != null) {
            return ("Hexa "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+this.x+" "+this.y+" "+this.wd+" "+this.text);
        } else {
            return ("Hexa "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+this.x+" "+this.y+" "+this.wd);
        }
    }
}