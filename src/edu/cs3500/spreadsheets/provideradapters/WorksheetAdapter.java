package edu.cs3500.spreadsheets.provideradapters;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.model.Worksheet;
import edu.cs3500.spreadsheets.provider.model.values.Val;
import java.util.HashSet;
import java.util.Set;

/**
 * Adapts an instance of our Worksheet interface to support the functionality of our provider's
 * Worksheet interface.
 */
public class WorksheetAdapter implements Worksheet<String> {

  private final edu.cs3500.spreadsheets.model.Worksheet delegate;

  /**
   * Wraps given Worksheet to support functionality of provider's Worksheet interface.
   *
   * @param model our model to be wrapped
   */
  public WorksheetAdapter(edu.cs3500.spreadsheets.model.Worksheet model) {
    delegate = model;
  }

  /**
   * Evaluates the cell at the given coordinate in the worksheet.
   *
   * @param c The coordinate of the cell to be evaluated.
   * @return a String representation of the result.
   */
  @Override
  public Val evaluate(Coord c) {
    if (delegate.getFilledCells().contains(c)) {
      return delegate.getEvaluatedContents(c.col, c.row).accept(new ValueAdapter());
    } else {
      return null;
    }
  }

  /**
   * Edits the cell at the given coordinate by replacing its previous contents with a new Formula.
   *
   * @param c the coordinate of the cell to be edited.
   * @param s the String to be converted into a Formula, which will be the contents of the cell.
   */
  @Override
  public void editCell(Coord c, String s) {
    delegate.editCell(c.col, c.row, s);
  }

  /**
   * Deletes the cell at the given coordinate (makes it blank).
   *
   * @param c the coordinate of the cell to be made blank.
   */
  @Override
  public void deleteCell(Coord c) {
    delegate.clearCell(c.col, c.row);
  }

  /**
   * Gets the cell at the given coordinate in this worksheet.
   *
   * @param c the coordinate of the desired cell.
   * @return the cell at the given coordinate.
   */
  @Override
  public String getCellAt(Coord c) {
    if (!delegate.getFilledCells().contains(c)) {
      return null;
    } else {
      return delegate.getExplicitContents(c.col, c.row);
    }
  }

  /**
   * Gets the s-expression contents of the cell at the given Coord. If there is no cell at the given
   * Coord, return the empty string.
   *
   * @param c the Coord of the cell in the spreadsheet whose contents are being returned.
   * @return the S-Exp in String form of the cell at the given Coord.
   */
  @Override
  public String getCellContents(Coord c) {
    return delegate.getExplicitContents(c.col, c.row);
  }

  /**
   * Checks whether the model is empty or not.
   *
   * @return true if the model is empty, false otherwise.
   */
  @Override
  public boolean isEmpty() {
    return delegate.getFilledCells().isEmpty();
  }

  /**
   * Gets the set of all the coordinates in the worksheet which contain non-empty cells.
   *
   * @return a set of Coord representing all non-empty cells.
   */
  @Override
  public Set<Coord> getAllCoords() {
    return new HashSet<>(delegate.getFilledCells());
  }

  /**
   * Adds a row of blank cells at a certain index in the spreadsheet, by shifting all cells with an
   * index >= rowIndex down.
   *
   * @param rowIndex the index where the new row will be added.
   */
  @Override
  public void addRow(int rowIndex) {
    // method not relevant
  }

  /**
   * Adds a column of blank cells at a certain index in the spreadsheet, by shifting all cells with
   * an index >= colIndex down.
   *
   * @param colIndex the index where the new column will be added.
   */
  @Override
  public void addCol(int colIndex) {
    // method not relevant
  }

  /**
   * Deletes an entire row of cells, and shifts the other rows below it up.
   *
   * @param rowIndex the index where the row will be deleted.
   */
  @Override
  public void deleteRow(int rowIndex) {
    // method not relevant
  }

  /**
   * Deletes an entire column of cells, and shifts the columns after it to the left.
   *
   * @param colIndex the index of the column to be deleted.
   */
  @Override
  public void deleteCol(int colIndex) {
    // method not relevant
  }
}
