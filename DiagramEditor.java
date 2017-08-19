import ecs100.*;
import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.swing.JColorChooser;

public class DiagramEditor implements UIButtonListener, UIMouseListener, UITextFieldListener{

    // Fields
    public ArrayList<Shape> shapes = new ArrayList<Shape>();    // the collection of shapes

    // fields to store mouse positions, current action, current shape, current colour, etc

    private double pressedX;                 //where the mouse was pressed
    private double pressedY;  
    private String currentAction = "Line";   // current action ("Move", etc) or shape ("Line" etc)
    private Shape currentShape = null;      //  the current shape (or null if no shape)
    private Color currentColor = Color.red;
    private int currentShapeIndex = -1;

    /** Constructor sets up the GUI:
     *        sets the mouse listener and adds all the buttons
     */
    public DiagramEditor(){
        UI.initialise();
        UI.setMouseMotionListener(this);
        UI.addButton("New", this);
        UI.addButton("Open", this);
        UI.addButton("Save", this);
        UI.addButton("Colors", this);
        UI.addButton("Line", this);
        UI.addButton("Rect", this);
        UI.addButton("Oval", this);
        UI.addButton("Hexagon", this);
        UI.addTextField("Shape Text", this);
        UI.addButton("Move", this);
        UI.addButton("Resize", this);
        UI.addButton("Delete", this);
        UI.setImmediateRepaint(false);
    }

    /** Respond to button presses
     * For New, Open, Save, and Color, call the appropriate method (see below) to perform the action immediately.
     * For other buttons, store the button name in the currentAction field
     */
    public void buttonPerformed(String button){
        if (button.equals("New")){
            this.newDrawing();
        }
        else if (button.equals("Open")){
            this.openDrawing();
        }
        else if (button.equals("Save")){
            this.saveDrawing();
        }
        else if (button.equals("Colors")){
            this.selectColor();
        }
        else{
            this.currentAction = button;
        }
        UI.println("\n Current action is "+currentAction);
    }

    // Respond to mouse events 
    /** When mouse is pressed, remember the position in fields
    and also find the shape it is on (if any), and store
    the shape in a field (use the findShape(..) method)
     *  When the Mouse is released, depending on the currentAction,
     *  - perform the action (move, delete, or resize).
     *    move and resize are done on the shape  where the mouse was pressed,
     *    delete is done on the shape where the mouse was released 
     *  - construct the shape and add to the shapes array.
     *    (though the polygon is more complicated)
     *  It is easiest to call other methods (see below) to actually do the work,
     *  otherwise this method gets too big!
     */
    public void mousePerformed(String action, double x, double y) {
        if (action.equals("pressed")){
            this.pressedX = x;
            this.pressedY = y;
            this.currentShape = this.findShape(x, y);
        }
        else if (action.equals("released")){
            if (this.currentAction.equals("Move")) {
                this.moveShape(x-this.pressedX, y-this.pressedY);
            }
            else if (this.currentAction.equals("Delete")) {
                this.deleteShape(x, y);
            }
            else if (this.currentAction.equals("Resize")) {
                this.resizeShape(x-this.pressedX, y-this.pressedY);
            }
            else { // it is one of the shape
                this.addShape(this.pressedX, this.pressedY, x, y);
            }
            drawDrawing();
        }
    }

    /**Store text entered into currentShape, if any*/
    public void textFieldPerformed(String name, String Text) {
        if (name.equals("Shape Text")){
            shapeText(Text);
        }
    }

    // -----------------------------------------------------
    // Methods that actually do the work  
    // -----------------------------------------------------

    /** Draws all the shapes in the list on the graphics pane
    First clears the graphics pane, then draws each shape,
    Finally repaints the graphics pane
     */
    public void drawDrawing(){
        UI.clearGraphics();
        for (int i=0; i<shapes.size(); i++){
            this.shapes.get(i).redraw();
        }
        UI.repaintGraphics();
    }   

    /** Checks each shape in the list to see if the point (x,y) is on the shape.
    It returns the topmost shape for which this is true.
    Returns null if there is no such shape.
     */
    public Shape findShape(double x, double y){
        for (int i=0; i<shapes.size(); i++){
            if (this.shapes.get(i).on(x, y)) {
                currentShapeIndex = shapes.indexOf(shapes.get(i));
                return this.shapes.get(i);
            }
        }
        return null;  
    }

    /** Sets the current color.
     * Asks user for a new color using a JColorChooser (see MiniPaint, Assig 6)
     * As long as the color is not null, it remembers the color */
    private void selectColor(){
        Color newColor = JColorChooser.showDialog(null,"Choose new Color", this.currentColor);
        if (newColor!=null){    //make sure there is a selected color, if no selected color use currentColor
            this.currentColor=newColor;
        }
    }

    /** Start a new drawing -
    initialise the shapes array and clear the graphics pane. */
    public void newDrawing(){
        shapes.clear();
        UI.clearGraphics();
        UI.repaintGraphics();
    }

    /** Construct a new Shape object of the appropriate kind
    (depending on currentAction) using the appropriate constructor
    of the Line, Rectangle, Oval classes.
    Adds the shape to the end of the collection of shapes in the drawing, and
    Re-draws the drawing */
    public void addShape(double x1, double y1, double x2, double y2){
        Shape shape = null;
        boolean added = false;      //used to prevent line duplicates in arraylist
        if (this.currentAction.equals("Line")) {
            for (int i=0; i<shapes.size(); i++){
                if (this.shapes.get(i).on(x2, y2)) {    //constructor a line as connector
                    shape = new Line(x1, y1, x2, y2, this.currentColor, shapes.get(currentShapeIndex), shapes.get(i));
                    added = true;
                }
            }
            if (added == true){  //add the constructed connector to the TOP of arraylist so they will be drawn first later
                shapes.add(0,shape);
            }
            else if (added == false) {  //add line as a stand-alone
                shape = new Line(x1, y1, x2, y2, this.currentColor, null, null);
                shapes.add(shape);
            }
        }
        else{
            double left= Math.min(x1, x2);
            double top= Math.min(y1, y2);
            double width= Math.abs(x1-x2);
            double height= Math.abs(y1-y2);
            if (this.currentAction.equals("Rect")) {
                shape = new Rectangle(left, top, width, height, this.currentColor, null);
                shapes.add(shape);
            }
            else if (this.currentAction.equals("Oval")) {
                shape =new Oval(left, top, width, height, this.currentColor, null);
                shapes.add(shape);
            }
            else if (this.currentAction.equals("Hexagon")) {
                shape =new Hexagon(left, top, width, this.currentColor, null);
                shapes.add(shape);
            }
        }
    }

    /** Moves the current shape (if there is one)
    to where the mouse was released.
    Ie, change its position by (toX-fromX) and (toY-fromY)
     */
    public void moveShape(double changeX, double changeY){
        if (this.currentShape != null){     //make sure there is a selected shape
            this.shapes.get(this.currentShapeIndex).moveBy((changeX), (changeY));
        }
    }
    
    /** Set text on shape*/
    public void shapeText(String text){
        if (this.currentShape != null){     //make sure there is a selected shape
            this.currentShape.shapeText(text);
            this.drawDrawing();
        }
    }

    /** Finds the shape that was under the mouseReleased position (x, y)
    and then removes it from the array of shapes, moving later shapes down. 
     */
    public void deleteShape(double x, double y){
        if (currentShape != null){      //make sure there is a selected shape
            this.shapes.remove(currentShapeIndex);
            currentShape = null;
            currentShapeIndex = -1;
        }
    }

    /** Resizes the current shape by the amount that the mouse was moved
    (ie from (fromX, fromY) to (toX, toY)). 
    If the mouse is moved to the right, the shape should
    be made that much wider on each side; if the mouse is moved to
    the left, the shape should be made that much narrower on each side
    If the mouse is moved up, the shape should be made
    that much higher top and bottom; if the mouse is moved down, the shape 
    should be made that much shorter top and bottom.
    The effect is that if the user drags from the top right corner of
    the shape, the shape should be resized to whereever the dragged to.
     */
    public void resizeShape(double changeX, double changeY){
        if (this.currentShape != null){     //make sure there is a selected shape
            this.shapes.get(currentShapeIndex).resize((changeX), (changeY));
        }
    }

    /** Ask the user to select a file and save the current drawing to the file. */
    public void saveDrawing(){
        String fname = UIFileChooser.save();
        if (fname==null) return;
        try {
            PrintStream out = new PrintStream(new File(fname));
            for (int i=0; i<shapes.size(); i++){
                out.println(this.shapes.get(i).toString());
            }
            out.close();
        }
        catch(IOException e) {UI.println("File Saving failed: "+e);}
    }

    /** Ask the user for a file to open,
    then read all the shape descriptions into the current drawing. */
    public void openDrawing(){
        String description;
        String fname = UIFileChooser.open();
        if (fname==null) return;
        try {
            Scanner in = new Scanner(new File(fname));
            UI.printf("Opening file %s\n", fname);
            this.newDrawing();

            while (in.hasNext()){
                description = in.next();
                if (description.startsWith("Line")) {
                    this.shapes.add(new Line (in));
                }
                else if (description.startsWith("Rect")) {
                    this.shapes.add(new Rectangle (in));
                }
                else if (description.startsWith("Oval")) {
                    this.shapes.add(new Oval (in));
                }
                else if (description.startsWith("Hexa")) {
                    this.shapes.add(new Hexagon (in));
                }
            }
            in.close();

            this.drawDrawing();
        }
        catch(IOException e) {UI.println("File loading failed: "+e);}
    }

    public static void main(String args[]){
        new DiagramEditor();
    }
}