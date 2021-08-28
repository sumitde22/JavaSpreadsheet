ASSIGNMENT 5

To represent a worksheet model, we have a Worksheet interface whose purpose is to both store and
evaluate spreadsheet data. On this Worksheet, you can edit cells, clear cells, and then get either
the exact string you typed in or the evaluated version of that contents. To draw the worksheet,
you can get a list of the filled cells to determine how to do that.

We implement this interface in BasicWorksheet.

Our Contents interface represents one of the three types of things that can be stored in a cell,
including being blank, containing a value, or containing a formula. Hence, the Contents interface
is implemented by Blank and extended by Value and Formula.

A ContentsVisitor is a function object that takes in a contents and returns the desired type.

Blank is a representation for the contents of a cell that hasn't been initialized.

The Value interface represents the different primitive types that can be stored in our spreadsheet
including booleans, doubles, and strings, which are accordingly represented by the BooleanValue,
DoubleValue, and StringValue classes. The AbstractValue class factors out all the common code
for amongst the representations of Value when it implements methods for Contents.

A ValueVisitor is a function object that takes in a value and returns the desired type.

The Formula interface represents the different formulas that our spreadsheets can handle, including
values, references, and functions. Hence, the Formula interface is implemented by Reference and
Function and extended by Value.

A Reference is a class that simply represents a region of cells in a spreadsheet.

A Function is an abstract class that factors out all of the common code amongst implementations of
different functions that our spreadsheet supports. Some of these implementations include Sum,
Product, LessThan, and Concat.

Lastly, WorksheetBuilderImpl is a class that implements a WorksheetBuilder and allows the builder
to edit cells and construct a worksheet out of our BasicWorksheet implementation.

ASSIGNMENT 6

We made no changes to our model interface from Assignment 5. However, the one small detail we
changed about our model implementation is that now, instead of throwing an error immediately
when inputting an invalid cell, we only throw an exception when trying to evaluate an invalid cell.
This makes it easier to show error messages in the model, and also handles some of edge cases where
a cell at a time could be invalid but could later turn valid.

We also made a ReadOnlyWorksheet interface and a ViewModel that gives the view a version of the
model that can't be modified.

For the design of our View, our view interface is pretty simple. It has a render method that allows
the user to officially display that view, whether it be making a visual view visible or printing
out a textual view. The interface also has a refresh method that orders itself to be redrawn in the
case that the data that it's drawing has been modified. This would be useful for a edu.cs3500.spreadsheets.controller.
Lastly, the view interface allows a client to get text that may have been inputted or get the cell
that is currently selected, things that may be relevant to determining how to change a model. Note
the textual view provides some default implementations for the last two methods, and these methods
have no effect on the actual functionality of the textual view which is in render.

ASSIGNMENT 7

We made no changes to our model interface from Assignment 6. However, one small detail we changed
about our internal implementation is that instead of doing all the work to evaluate a cell when we
call getEvaluatedContents, we calculate the value of a cell right when it is edited and then
appropriately recalculate any cells that might changed due to the original cell change. This
essentially just turns getEvaluatedContents into a getter, making it more efficient for the view
to draw, and the functionality of the model remains the same.

We did modify the view interface a little bit because our original conception for our MVC design was
that the controller would be "getting" information from the view, using it to modify the model,
which in turn would update the view. However, we later realized it would better design if the
action listeners, key listeners, etc were added in the view instead of the controller so the controller
wouldn't need to know about the internal implementation of the view and could work with any WorksheetView.
Hence, our WorksheetView instead has an addFeatures method that encompasses all the possible functionality
that a Worksheet would need to support inside of a Features interface.

Our controller supports functionality for "high-level" events that would modify a visual worksheet,
including highlighting a cell to edit, clearing a selected cell's contents, saving the text typed
into a cell, and also being able to save/load outside files. Our controller simply has to add itself
to the view given in the constructor, and the view takes care of knowing when to notify the controller
when certain actions have been taken. Then, every time a relevant event like a mouse click or a arrow key
happens, the controller will have its method called if applicable, and then will make the appropriate
changes to the view and model.

ASSIGNMENT 8
We were able to get all the required features of our provider's code working, namely being able to
select and modify and evaluate cells, and being able to navigate through the spreadsheet using arrows
and being able to remove cells using the delete key. We didn't implement saving/loading files
because it wasn't required.