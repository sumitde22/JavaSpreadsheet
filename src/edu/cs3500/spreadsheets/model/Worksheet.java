package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.List;

/**
 * Represents a spreadsheet that contains Cells that can store either explicit values,
 * references to other Cells, or functions to be applied to one or multiple Cells.
 */
public interface Worksheet {

  /**
   * Edits the cell in this Worksheet at the specified coordinates with the specified
   * contents.
   * @param col the column where the desired Cell is.
   * @param row the row where the desired Cell is.
   * @param contents the contents for the desired Cell to be changed to.
   * @exception IllegalArgumentException if invalid cell #
   */
  void editCell(int col, int row, String contents) throws IllegalArgumentException;

  /**
   * Clears the contents of this cell if it currently holds values.
   * @param col the column where the desired Cell is.
   * @param row the row where the desired Cell is.
   * @exception IllegalArgumentException if invalid cell #
   */
  void clearCell(int col, int row) throws IllegalArgumentException;

  /**
   * Returns the explicit contents before (if it has a function) has been evaluated
   * of the Cell at the specified location.
   * @param col the column where the desired Cell is.
   * @param row the row where the desired Cell is.
   * @return the explicit contents of the desired Cell.
   * @exception IllegalArgumentException if invalid cell #
   */
  String getExplicitContents(int col, int row) throws IllegalArgumentException;

  /**
   * Returns the evaluated contents (if the Cell has a formula) of the Cell at the
   * specified location.
   * @param col the column where the desired Cell is.
   * @param row the row where the desired Cell is.
   * @return the evaluated contents of the desired Cell.
   * @exception IllegalArgumentException if invalid cell # or cell could not be evaluated
   */
  Value getEvaluatedContents(int col, int row) throws IllegalArgumentException;

  /**
   * Returns a list of the coordinates of all the Cells in this Worksheet whose Contents
   * are not Empty.
   * @return the list of Coord of the non-empty Cells.
   */
  List<Coord> getFilledCells();

}
