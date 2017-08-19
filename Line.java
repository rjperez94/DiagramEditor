import java.util.*;
import ecs100.*;
import java.awt.Color;
import java.io.*;

/** Line represents a straight line.
Can be a line by-itself OR can be used as a connector*/
public class Line implements Shape{
    //fields

    //when line by itself uses
    private double x1;  // one end
    private double y1;
    private double x2;  // the other end
    private double y2;

    //when line is a cnnector
    private Shape shape1;   //shapes that the line is connecting and their respective centre-points
    private Shape shape2;
    private double shape1X;     //centre-point values
    private double shape1Y;
    private double shape2X;
    private double shape2Y;

    private Color col;  // the colour of the line
    boolean isConnected = false; //when used as connector

    /** Constructor with explicit values*/
    public Line (double x1, double y1, double x2, double y2, Color col, Shape shape1, Shape shape2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.col = col;

        this.shape1 = shape1;
        this.shape2 = shape2;

        //if it is a connector
        if (shape1!=null && shape2 !=null) {    //only allows being a connector when both shapes exist
            isConnected = true;

            String[] shape1String = shape1.toString().split(" ");
            String[] shape2String = shape2.toString().split(" ");

            try {   //code inside try used for oval and rectangle shapes
                this.shape1X = Double.parseDouble(shape1String[6])/2+Double.parseDouble(shape1String[4]);
                this.shape1Y = Double.parseDouble(shape1String[7])/2+Double.parseDouble(shape1String[5]);

                this.shape2X = Double.parseDouble(shape2String[6])/2+Double.parseDouble(shape2String[4]);
                this.shape2Y = Double.parseDouble(shape2String[7])/2+Double.parseDouble(shape2String[5]);
            }
            //throws index-out-of-bounds because hexagon representation has no height component
            //height = width
            catch(ArrayIndexOutOfBoundsException e) {   //used for hexagon
                this.shape1X = Double.parseDouble(shape1String[6])/2+Double.parseDouble(shape1String[4]);
                this.shape1Y = Double.parseDouble(shape1String[6])/2+Double.parseDouble(shape1String[5]);

                this.shape2X = Double.parseDouble(shape2String[6])/2+Double.parseDouble(shape2String[4]);
                this.shape2Y = Double.parseDouble(shape2String[6])/2+Double.parseDouble(shape2String[5]);
            }
        }
    }

    /** Constructor which reads values from a String that contains the specification of the Line.*/
    public Line (Scanner data){
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        this.col = new Color(red, green, blue);
        this.x1 = data.nextDouble();
        this.y1 = data.nextDouble();
        this.x2 = data.nextDouble();
        this.y2 = data.nextDouble();
    }

    /** Returns true if the point (u, v) is on top of the shape*/
    public boolean on(double u, double v){
        if (isConnected == false) {     //if line is by itself
            double threshold = 3;
            // first check if it is past the ends of the line...
            if (u < Math.min(this.x1,this.x2)-threshold ||     
            u > Math.max(this.x1,this.x2)+threshold ||
            v < Math.min(this.y1,this.y2)-threshold ||
            v > Math.max(this.y1,this.y2)+threshold) {
                return false;
            }
            // then check the distance from the point to the line
            double wd = this.x2-this.x1;
            double ht = this.y2-this.y1;
            return (Math.abs(((v-this.y1)*wd - (u-this.x1)*ht)/Math.hypot(wd, ht)) <= threshold);
            // distance of a point from a line, from linear algebra
        } else {    //if it is a connector
            double threshold = 3;
            // first check if it is past the ends of the line...
            if (u < Math.min(this.shape1X,this.shape2X)-threshold ||     
            u > Math.max(this.shape1X,this.shape2X)+threshold ||
            v < Math.min(this.shape1Y,this.shape2Y)-threshold ||
            v > Math.max(this.shape1Y,this.shape2Y)+threshold) {                
                return false;
            }
            // then check the distance from the point to the line
            double wd = this.shape2X-this.shape1X;
            double ht = this.shape2Y-this.shape1Y;
            return (Math.abs(((v-this.shape1Y)*wd - (u-this.shape1X)*ht)/Math.hypot(wd, ht)) <= threshold);
            // distance of a point from a line, from linear algebra
        }
    }

    /** Changes the position of the shape by dx and dy.
    If it was positioned at (x, y), it will now be at (x+dx, y+dy)
     */
    public void moveBy(double dx, double dy){
        if (isConnected == false) {
            this.x1 += dx;
            this.y1 += dy;
            this.x2 += dx;
            this.y2 += dy;
        }
    }

    //Not used
    public void shapeText(String text) {
    }

    /** Draws the line on the graphics pane.*/
    public void redraw(){
        UI.setColor(this.col);

        //if line is not a connector
        if (isConnected == false) {
            UI.drawLine(this.x1, this.y1, this.x2, this.y2);
        } else {
            String[] shape1String = shape1.toString().split(" ");
            String[] shape2String = shape2.toString().split(" ");

            try {   //code inside try used for oval and rectangle shapes
                this.shape1X = Double.parseDouble(shape1String[6])/2+Double.parseDouble(shape1String[4]);
                this.shape1Y = Double.parseDouble(shape1String[7])/2+Double.parseDouble(shape1String[5]);

                this.shape2X = Double.parseDouble(shape2String[6])/2+Double.parseDouble(shape2String[4]);
                this.shape2Y = Double.parseDouble(shape2String[7])/2+Double.parseDouble(shape2String[5]);
            }
            //throws index-out-of-bounds because hexagon representation has no height component
            //height = width
            catch(ArrayIndexOutOfBoundsException e) {
                this.shape1X = Double.parseDouble(shape1String[6])/2+Double.parseDouble(shape1String[4]);
                this.shape1Y = Double.parseDouble(shape1String[6])/2+Double.parseDouble(shape1String[5]);

                this.shape2X = Double.parseDouble(shape2String[6])/2+Double.parseDouble(shape2String[4]);
                this.shape2Y = Double.parseDouble(shape2String[6])/2+Double.parseDouble(shape2String[5]);
            }

            UI.setLineWidth(4);    //sets line width -- thicker for connectors
            UI.drawLine(this.shape1X, this.shape1Y, this.shape2X, this.shape2Y);
            UI.setLineWidth(1);     //resets line width
        }
    }

    /** Changes the width and height of the shape by the specified amounts.*/
    public void resize(double changeWd, double changeHt){
        if (isConnected == false) {
            if (this.x1 < this.x2){
                this.x1 = this.x1 - changeWd/2;
                this.x2 = this.x2 + changeWd/2;
            }
            else{
                this.x1 = this.x1 + changeWd/2;
                this.x2 = this.x2 - changeWd/2;
            }
            if (this.y1 < this.y2){
                this.y1 = this.y1 - changeHt/2;
                this.y2 = this.y2 + changeHt/2;
            }
            else{
                this.y1 = this.y1 + changeHt/2;
                this.y2 = this.y2 - changeHt/2;
            }
        }
    }

    /** Returns a string description of the line in a form suitable for
    writing to a file in order to reconstruct the line later */
    public String toString(){
        if (isConnected == false) {
            return ("Line "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+this.x1+" "+this.y1+" "+this.x2+" "+this.y2);
        } else {
            return ("Line "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+this.shape1X+" "+this.shape1Y+" "+this.shape2X+" "+this.shape2Y);
        }
    }
}
