package edu.cs3500.spreadsheets.model.valuetype;

import edu.cs3500.spreadsheets.model.Contents;
import edu.cs3500.spreadsheets.model.Coord;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Factors out common code that all primitive types in a spreadsheet share.
 */
public abstract class AbstractValue implements Value {

  @Override
  public List<Coord> getReferences() {
    return new ArrayList<>();
  }

  @Override
  public Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
    return this;
  }

}
