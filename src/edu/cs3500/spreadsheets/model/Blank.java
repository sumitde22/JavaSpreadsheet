package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a null value for a spreadsheet's contents.
 */
public class Blank implements Contents {

  @Override
  public List<Coord> getReferences() {
    return new ArrayList<>();
  }

  @Override
  public Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals)
      throws IllegalArgumentException {
    throw new IllegalArgumentException("Blank cannot be evaluated");
  }

  @Override
  public <R> R accept(ContentsVisitor<R> visitor) {
    return visitor.visitBlank(this);
  }

  @Override
  public String toString() {
    return "";
  }
}