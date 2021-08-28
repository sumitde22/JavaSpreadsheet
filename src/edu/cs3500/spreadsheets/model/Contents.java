package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.List;
import java.util.Map;

/**
 * A representation of what can be stored inside a cell.
 */
public interface Contents {

  /**
   * Determines all the references to other coordinates in this contents.
   * @return a list of references found in this cell
   */
  List<Coord> getReferences();

  /**
   * Solves for the value of this contents using the parameters provided by the map.
   *
   * @param data the values for parameters in this contents
   * @return the evaluated form of this contents
   * @throws IllegalArgumentException if the contents cannot be simplified to a value
   */
  Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals)
      throws IllegalArgumentException;

  /**
   * Allows a function objects to determine this content's type.
   * @param visitor a function object
   * @param <R> the return type of the function object
   * @return the function object evaluated
   */
  <R> R accept(ContentsVisitor<R> visitor);

}
