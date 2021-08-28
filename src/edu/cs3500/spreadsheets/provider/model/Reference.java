package edu.cs3500.spreadsheets.provider.model;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.model.Formula;
import edu.cs3500.spreadsheets.provider.model.values.Val;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a reference to an arbitrary number of cells in a spreadsheet. A Reference can be one
 * of: - A SingleRef, which is a reference to one cell. - A RectangleRef, which is a
 * reference to a rectangular region of cells.
 */
public interface Reference<R> extends Formula {

  /**
   * A method which returns a list of the Values corresponding to the cells in this reference.
   *
   * @param spreadsheet a mapping from coord to cell of the spreadsheet.
   * @return A List<Value></Value> representing all the values of the cells in this reference.
   */
  List<Val> getRefValues(Map<Coord, R> spreadsheet, Map<Coord, Val> acc);

  /**
   * Checks to make sure that there are no cycles in a reference.
   *
   * @param cell        the cell referred to in this reference.
   * @param spreadsheet The mapping of coords to cells representing this worksheet.
   * @return true if there are no cycles in this reference.
   */
  boolean checkNoCycles(Coord cell, Map<Coord, R> spreadsheet);

  /**
   * Gets a list of all the coordinates in this reference.
   *
   * @return a list of Coord of the cells in this reference.
   */
  ArrayList<Coord> getAllCoords();


}