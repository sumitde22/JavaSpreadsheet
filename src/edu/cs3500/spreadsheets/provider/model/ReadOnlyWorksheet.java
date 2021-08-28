package edu.cs3500.spreadsheets.provider.model;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.model.values.Val;
import java.util.Set;

/**
 * Represents a read-only worksheet model interface which is immutable to changes.
 */
public interface ReadOnlyWorksheet<R> {

  /**
   * Gets the set of all the coordinates in the worksheet which contain non-empty cells.
   * @return a set of Coord representing all non-empty cells.
   */
  Set<Coord> getAllCoords();

  /**
   * Evaluates the cell at the given coordinate in the worksheet.
   *
   * @param c The coordinate of the cell to be evaluated.
   * @return a String representation of the result.
   */
  Val evaluate(Coord c);

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
}
