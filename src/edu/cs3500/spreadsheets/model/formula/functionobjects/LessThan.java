package edu.cs3500.spreadsheets.model.formula.functionobjects;

import edu.cs3500.spreadsheets.model.Blank;
import edu.cs3500.spreadsheets.model.Contents;
import edu.cs3500.spreadsheets.model.ContentsVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.Function;
import edu.cs3500.spreadsheets.model.formula.Reference;
import edu.cs3500.spreadsheets.model.valuetype.BooleanValue;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.model.valuetype.StringValue;
import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.List;
import java.util.Map;

/**
 * Represents a function that attempts to compare two values in a spreadsheet.
 */
public class LessThan extends Function {

  /**
   * Constructs an instance of a function that compares two given arguments.
   *
   * @param args the arguments to the given function
   */
  public LessThan(List<Formula> args) throws IllegalArgumentException {
    if (args.size() != 2) {
      throw new IllegalArgumentException("Less than must have two arguments");
    }

    if (!args.get(0).accept(new IsValidArg()) || !args.get(1).accept(new IsValidArg())) {
      throw new IllegalArgumentException("Less than must take in integer or reference to one cell");
    }

    this.arguments = args;
  }

  @Override
  public Value getBaseReturnType() {
    return new BooleanValue(false);
  }

  @Override
  public Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
    return new BooleanValue(
        arguments.get(0).accept(new EvalContentsForLessThan(data, savedEvals)) < arguments.get(1)
            .accept(new EvalContentsForLessThan(data, savedEvals)));
  }

  /*

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LessThan)) {
      return false;
    }
    LessThan that = (LessThan) o;

    if (arguments.size() != (that.arguments.size())) {
      return false;
    }

    for (int i = 0; i < arguments.size(); i++) {
      if (!arguments.get(i).equals(that.arguments.get(i))) {
        return false;
      }
    }
    return true;
  }



  @Override
  public int hashCode() {
    return Objects.hash(3, arguments.toArray());
  }

   */

  /**
   * Function object that validates that each argument for a less than function.
   */
  private class IsValidArg implements ContentsVisitor<Boolean> {

    @Override
    public Boolean visitBlank(Blank that) {
      return false;
    }

    @Override
    public Boolean visitBoolean(BooleanValue that) {
      return false;
    }

    @Override
    public Boolean visitDouble(DoubleValue that) {
      return true;
    }

    @Override
    public Boolean visitString(StringValue that) {
      return false;
    }

    @Override
    public Boolean visitReference(Reference that) {
      return that.getReferences().size() == 1;
    }

    @Override
    public Boolean visitFunction(Function that) {
      return that.getBaseReturnType().accept(this);
    }
  }

  /**
   * Helper function object that determines how to evaluate each argument of a Less Than function.
   */
  private class EvalContentsForLessThan implements ContentsVisitor<Double> {

    Map<Coord, Contents> data;
    Map<Contents, Value> savedEvals;

    /**
     * Constructs an instance of this function object.
     * @param data a map of data needed to evaluate references.
     */
    EvalContentsForLessThan(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
      this.data = data;
      this.savedEvals = savedEvals;
    }

    @Override
    public Double visitBlank(Blank that) {
      throw new IllegalArgumentException("Cannot apply less than to blank");
    }

    @Override
    public Double visitBoolean(BooleanValue that) {
      throw new IllegalArgumentException("Cannot apply less than to boolean");
    }

    @Override
    public Double visitDouble(DoubleValue that) {
      return that.getValue();
    }

    @Override
    public Double visitString(StringValue that) {
      throw new IllegalArgumentException("Cannot apply less than to string");
    }

    @Override
    public Double visitReference(Reference that) {
      List<Coord> coords = that.getReferences();
      if (coords.size() != 1) {
        throw new IllegalArgumentException(
            "Cannot apply less than to reference to more than one value");
      }

      Coord c = coords.get(0);

      return data.getOrDefault(c, new Blank()).accept(this);
    }

    @Override
    public Double visitFunction(Function that) {
      if (savedEvals.containsKey(that)) {
        return savedEvals.get(that).accept(this);
      } else {
        Value toReturn = that.evaluate(data, savedEvals);
        savedEvals.put(that, toReturn);
        return toReturn.accept(this);
      }
    }
  }
}
