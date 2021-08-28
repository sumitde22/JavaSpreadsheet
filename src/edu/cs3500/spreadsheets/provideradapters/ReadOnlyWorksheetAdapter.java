package edu.cs3500.spreadsheets.provideradapters;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.model.ReadOnlyWorksheet;
import edu.cs3500.spreadsheets.provider.model.Worksheet;
import edu.cs3500.spreadsheets.provider.model.values.Val;
import java.util.Set;

/**
 * Provides wrapper around Worksheet so that data can be extracted but the model itself
 * cannot be modified.
 */
public class ReadOnlyWorksheetAdapter implements ReadOnlyWorksheet<String> {

  private final Worksheet<String> delegate;

  /**
   * Wraps the given worksheet with read-only functionality.
   * @param delegate worksheet to be wrapped
   */
  public ReadOnlyWorksheetAdapter(Worksheet<String> delegate) {
    this.delegate = delegate;
  }

  /**
   * Gets the set of all the coordinates in the worksheet which contain non-empty cells.
   *
   * @return a set of Coord representing all non-empty cells.
   */
  @Override
  public Set<Coord> getAllCoords() {
    return delegate.getAllCoords();
  }

  /**
   * Evaluates the cell at the given coordinate in the worksheet.
   *
   * @param c The coordinate of the cell to be evaluated.
   * @return a String representation of the result.
   */
  @Override
  public Val evaluate(Coord c) {
    return delegate.evaluate(c);
  }

  /**
   * Gets the cell at the given coordinate in this worksheet.
   *
   * @param c the coordinate of the desired cell.
   * @return the cell at the given coordinate.
   */
  @Override
  public String getCellAt(Coord c) {
    return delegate.getCellAt(c);
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
    return delegate.getCellContents(c);
  }
}
