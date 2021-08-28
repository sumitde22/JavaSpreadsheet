package edu.cs3500.spreadsheets.provider.model;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.model.values.Val;
import java.util.Set;

/**
 * The model representing a spreadsheet. Allows for worksheets to be evaluated and edited.
 */
public interface Worksheet<R> {

  /**
   * Evaluates the cell at the given coordinate in the worksheet.
   *
   * @param c The coordinate of the cell to be evaluated.
   * @return a String representation of the result.
   */
  Val evaluate(Coord c);

  /**
   * Edits the cell at the given coordinate by replacing its previous contents with a new Formula.
   *
   * @param c the coordinate of the cell to be edited.
   * @param s the String to be converted into a Formula, which will be the contents of the cell.
   */
  void editCell(Coord c, String s);

  /**
   * Deletes the cell at the given coordinate (makes it blank).
   *
   * @param c the coordinate of the cell to be made blank.
   */
  void deleteCell(Coord c);


  /**
   * Gets the cell at the given coordinate in this worksheet.
   *
   * @param c the coordinate of the desired cell.
   * @return the cell at the given coordinate.
   */
  R getCellAt(Coord c);

  /**
   * Gets the s-expression contents of the cell at the given Coord. If there is no cell at the
   * given Coord, return the empty string.
   * @param c the Coord of the cell in the spreadsheet whose contents are being returned.
   * @return the S-Exp in String form of the cell at the given Coord.
   */
  String getCellContents(Coord c);


  /** Checks whether the model is empty or not.
   * @return true if the model is empty, false otherwise.
   */
  boolean isEmpty();

  /**
   * Gets the set of all the coordinates in the worksheet which contain non-empty cells.
   * @return a set of Coord representing all non-empty cells.
   */
  Set<Coord> getAllCoords();

  /**
   * Adds a row of blank cells at a certain index in the spreadsheet, by shifting all
   * cells with an index >= rowIndex down.
   * @param rowIndex the index where the new row will be added.
   */
  void addRow(int rowIndex);

  /**
   * Adds a column of blank cells at a certain index in the spreadsheet, by shifting all
   * cells with an index >= colIndex down.
   * @param colIndex the index where the new column will be added.
   */
  void addCol(int colIndex);

  /**
   * Deletes an entire row of cells, and shifts the other rows below it up.
   * @param rowIndex the index where the row will be deleted.
   */
  void deleteRow(int rowIndex);

  /**
   * Deletes an entire column of cells, and shifts the columns after it to the left.
   * @param colIndex the index of the column to be deleted.
   */
  void deleteCol(int colIndex);
}
