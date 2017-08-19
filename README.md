# DiagramEditor

## Compiling Java files using Eclipse IDE

1. Download this repository as ZIP
2. Create new `Java Project` in `Eclipse`
3. Right click on your `Java Project` --> `Import`
4. Choose `General` --> `Archive File`
5. Put directory where you downloaded ZIP in `From archive file`
6. Put `ProjectName/src` in `Into folder`
7. Click `Finish`

### Linking the UI Library

8. Right click on your `Java Project` --> `Build Path` --> `Add External Archives`
9. Select `ecs100.jar` and link it to the project. That JAR will be in the directory where you downloaded ZIP

## Running the program

1. Right click on your `Java Project` --> `Run As` --> `Java Application` --> `DiagramEditor`

## Features

### Adding shapes
- click on the button that represents the shape
- press mouse anywhere on graphics pane (don't release) -- this will be x and y
- drag mouse to where you want the bottom right of shape to be
- Current action will be remembered
NOTE: for hexagon, treat methodology as if you are drawing a rectangle
/ \	<-- Hexagon draws like so
!  !
!  !
\ /

### Removing shapes
- Click on delete button
- Click on shape to delete
NOTE: currentShape will be set to null and currentShapeIndex to -1

### Selecting shapes/lines
- Click / Mouse-press on a shape/line 
(Provided that line, rectangle, oval or hexagon buttons were not clicked before)
NOTE: currentShape selected will be remebered
NOTE: Lines used as 'connectors' to shapes can only be selected to be deleted

### Resizing
- select a shape or a stand-alone line
- if mouse is moved to right, shape should be made that much wider on each side
- if the mouse is moved to left, the shape should be made that much narrower on each side
- if the mouse is moved up, the shape should be made that much higher top and bottom
- if the mouse is moved down, the shape should be made that much shorter top and bottom.
NOTE: The effect is that if the user drags from the top right corner of
    the shape, the shape should be resized to whereever the dragged to.
NOTE: currentShape selected will be remebered

### Moving
- select a shape or a stand-alone line
- drag and release the mouse to where you want to move the shape
NOTE: currentShape selected will be remebered

### Setting text
- select a shape
- type text in the 'shape text' textbox
NOTE: It is not possible to set text on a line

### Adding lines
- Click on line button
	- if you select a shape by mouse-press then drag and release it to another shape
	line will serve as a 'connector' -- connect the two shapes
	- if you mouse-press then drag and release on an empty space, 
	it will draw a stand-alone line.
	- if you mouse-press on a shape then drag and release on an empty space,
	line won't draw (and vice-versa)
NOTE: stand-alone lines can be moved and resized
NOTE: lines used as connectors will automatically adjust to where their
connected shapes are. Connectors have triple the width of stand-alone lines

### Removing lines
- Click on delete button
- Click on line to delete
NOTE: currentShape will be set to null and currentShapeIndex to -1
NOTE: lines used as connectors are independent to the two shapes they connect to.

E.g. shape1 is connected by line1 to shape2. If I delete shape 1 and 2,
line1 won't deleted and will still be there. There would be no way to move
or resize line1. The only thing you can do is to delete it.

## Bugs
- May be difficult for some people to resize with ultimate precision
No values to adjust or sliders to explore from (resize is just by trial and 
error)
- Text inside a shape will go off to the sides (overflow) if shape width is far
more less than the 'text width'
- No indication of what the currentShape is
	- The most recent selected shape, if any, is not 'highlighted'.
	User will have to remember what and where he last preseed the mouse
