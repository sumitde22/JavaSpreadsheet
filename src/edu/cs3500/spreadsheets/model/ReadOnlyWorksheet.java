package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.List;

/**
 * Represents a spreadsheet that contains Cells that can only retrieve values but not modify the
 * sheet itself.
 */
public interface ReadOnlyWorksheet {

  /**
   * Returns the explicit contents before (if it has a function) has been evaluated of the Cell at
   * the specified location.
   *
   * @param col the column where the desired Cell is.
   * @param row the row where the desired Cell is.
   * @return the explicit contents of the desired Cell.
   * @throws IllegalArgumentException if invalid cell #
   */
  String getExplicitContents(int col, int row) throws IllegalArgumentException;

  /**
   * Returns the evaluated contents (if the Cell has a formula) of the Cell at the specified
   * location.
   *
   * @param col the column where the desired Cell is.
   * @param row the row where the desired Cell is.
   * @return the evaluated contents of the desired Cell.
   * @throws IllegalArgumentException if invalid cell # or cell could not be evaluated
   */
  Value getEvaluatedContents(int col, int row) throws IllegalArgumentException;

  /**
   * Returns a list of the coordinates of all the Cells in this Worksheet whose Contents are not
   * Empty.
   *
   * @return the list of Coord of the non-empty Cells.
   */
  List<Coord> getFilledCells();

}
