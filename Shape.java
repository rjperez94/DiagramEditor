/** The interface for all Shape objects*/

public interface Shape{
  /** Returns true if the point (x, y) is on top of the shape*/
  public boolean on(double x, double y);

  /** Changes the position of the shape by dx and dy.
   If it was positioned at (x, y), it will now be at (x+dx, y+dy)
  */
  public void moveBy(double dx, double dy);
  
  /** Sets text inside shape*/
  public void shapeText(String text);

  /** Draws the shape on the graphics pane and its text, if any.
      It uses the drawing methods with the extra last argument of "false"
      so that the shape will not actually appear until the 
      graphics pane is redisplayed later. This gives much smoother redrawing.
  */
  public void redraw();

  /** Changes the width and height of the shape by the specified amounts.*/
  public void resize(double changeWd, double changeHt);

  /** Returns a string description of the shape in a form suitable for
      writing to a file in order to reconstruct the shape later
  */
  public String toString();
}