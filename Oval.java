import java.util.*;
import ecs100.*;
import java.awt.Color;
import java.io.*;

/** Oval represents a solid oval shape*/
public class Oval implements Shape {
    //fields
    private double x;   //represents top-left corner of oval
    private double y;
    private double wd;
    private double ht;
    private Color col;
    private String text;    //text inside rectangle

    /** Constructor with explicit values
    Arguments are the x and y of the top left corner,
    the width and height, and the color. */
    public Oval(double x, double y, double wd, double ht, Color col, String text) {
        this.x = x;
        this.y = y;
        this.wd = wd;
        this.ht = ht;
        this.col = col;
        this.text = text;
    }

    /** [Completion] Constructor which reads values from a String
    that contains the specification of the Rectangle. 
    The format of the String is determined by the toString method.
    The first 3 integers specify the color;
    the following four numbers specify the position and the size.
     */
    public Oval(Scanner in) {
        String description = in.nextLine();     //reads a whole line using previous scanner
        Scanner data = new Scanner(description);    //new scanner that reads tokens
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        this.col = new Color(red, green, blue);
        this.x = data.nextDouble();
        this.y = data.nextDouble();
        this.wd = data.nextDouble();
        this.ht = data.nextDouble();
        if (data.hasNext()) {       //reads till the end of a line -- text can have spaces, IF there is some form of string
            this.text = data.nextLine();
        }
    }

    /** Returns true if the point (u, v) is on top of the shape */
    public boolean on(double u, double v) {
        double centerX = this.x + this.wd/2;
        double centerY = this.y + this.ht/2;
        double xDiff = Math.pow(u - centerX, 2);
        double yDiff = Math.pow(v - centerY, 2);
        double dx = xDiff/Math.pow(wd/2, 2);
        double dy = yDiff/Math.pow(ht/2, 2);
        double sum = dx + dy;
        if(sum <= 1) {
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

    /** Sets text inside oval*/
    public void shapeText(String text) {
        this.text = text;
    }

    /** Draws the rectangle on the graphics pane. It draws a black border and
    fills it with the color of the rectangle.
    It uses the drawing methods with the extra last argument of "false"
    so that the shape will not actually appear until the 
    graphics pane is redisplayed later. This gives much smoother redrawing.*/
    public void redraw() {
        UI.setColor(Color.white);   //fill shape
        UI.fillOval(this.x+1, this.y+1, this.wd-2, this.ht-2, false);
        
        UI.setColor(this.col);  //draw border
        UI.drawOval(this.x, this.y, this.wd, this.ht, false);

        if (text != null) {
            double centerY = this.ht/2 + this.y;    // text Y-point
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

    /** Changes the width and height of the shape by the specified amounts.*/
    public void resize (double changeWd, double changeHt) {
        this.x = this.x - changeWd/2;
        this.wd = this.wd + changeWd/2 + changeWd/2;

        this.y = this.y + changeHt/2;
        this.ht = this.ht - changeHt/2 - changeHt/2;
    }

    /** Returns a string description of the oval in a form suitable for
    writing to a file in order to reconstruct the rectangle later
    The first word of the string must be Oval */
    public String toString() {
        //if there is no text, leave out text at the end
        if (this.text != null) {
            return ("Oval "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+this.x+" "+this.y+" "+this.wd+" "+this.ht+" "+this.text);
        } else {
            return ("Oval "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+this.x+" "+this.y+" "+this.wd+" "+this.ht);
        }
    }
}