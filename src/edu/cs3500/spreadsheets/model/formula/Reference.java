package edu.cs3500.spreadsheets.model.formula;

import edu.cs3500.spreadsheets.model.Blank;
import edu.cs3500.spreadsheets.model.Contents;
import edu.cs3500.spreadsheets.model.ContentsVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a group of coordinates.
 */
public class Reference implements Formula {
  private final Coord topLeft;
  private final Coord bottomRight;

  /**
   * Constructs a Reference.
   * @param topLeft the top left coordinate of this group of cells
   * @param bottomRight the bottom right coordinate of this group of cells
   */
  public Reference(Coord topLeft, Coord bottomRight) {
    if (topLeft.row > bottomRight.row || topLeft.col > bottomRight.col) {
      throw new
          IllegalArgumentException("Illegal reference: first coordinate must be above second");
    }

    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  /**
   * Helper constructor for reference to one coordinate.
   * @param coord coordinate that this reference stores
   */
  public Reference(Coord coord) {
    this(coord, coord);
  }

  @Override
  public List<Coord> getReferences() {
    ArrayList<Coord> answer = new ArrayList<>();
    for (int i = topLeft.row; i <= bottomRight.row; i++) {
      for (int j = topLeft.col; j <= bottomRight.col; j++) {
        answer.add(new Coord(j, i));
      }
    }
    return answer;
  }

  @Override
  public Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
    // a "reference" by itself can only be evaluated if it's a reference to one cell only
    if (topLeft.equals(bottomRight)) {
      Contents toEvaluate = data.getOrDefault(topLeft, new Blank());
      if (savedEvals.containsKey(toEvaluate)) {
        return savedEvals.get(toEvaluate);
      } else {
        Value toReturn = toEvaluate.evaluate(data, savedEvals);
        savedEvals.put(toEvaluate, toReturn);
        return toReturn;
      }
    } else {
      throw new
          IllegalArgumentException("Cannot evaluate reference to multiple cells without function");
    }
  }

  @Override
  public <R> R accept(ContentsVisitor<R> visitor) {
    return visitor.visitReference(this);
  }

  /*

  @Override
  public boolean equals(Object o) {
    if(this == o) {
      return true;
    }
    if(!(o instanceof Reference)) {
      return false;
    }
    return topLeft.equals((((Reference) o).topLeft))
    && bottomRight.equals(((Reference) o).bottomRight);
  }


  @Override
  public int hashCode() {
    return Objects.hash(topLeft, bottomRight);
  }

 */


}
