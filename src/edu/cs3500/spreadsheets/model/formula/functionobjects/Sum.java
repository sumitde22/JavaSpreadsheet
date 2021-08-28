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
 * Represents a function that attempts to add a set of values in a spreadsheet.
 */
public class Sum extends Function {

  /**
   * Constructs an instance of a function that adds given arguments.
   *
   * @param args the arguments to the given function
   */
  public Sum(List<Formula> args) {
    if (args.isEmpty()) {
      throw new IllegalArgumentException("Need at least one argument!");
    }
    arguments = args;
  }

  @Override
  public Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
    double answer = 0;
    for (Formula f : arguments) {
      try {
        answer += f.accept(new EvalContentsForSum(data, savedEvals));
      } catch (IllegalArgumentException e) {
        answer += 0;
      }
    }
    return new DoubleValue(answer);
  }

  @Override
  public Value getBaseReturnType() {
    return new DoubleValue(0);
  }

  /*

  @Override
  public boolean equals(Object o) {
    if(this == o) {
      return true;
    }
    if(!(o instanceof Sum)) {
      return false;
    }
    Sum that = (Sum) o;

    if(arguments.size() != that.arguments.size()) {
      return false;
    }

    for (int i = 0; i < arguments.size(); i++) {
      if(!arguments.get(i).equals(that.arguments.get(i))) {
        return false;
      }
    }
    return true;
  }


  @Override
  public int hashCode() {
    return Objects.hash(1, arguments.toArray());
  }

 */





  /**
   * Helper function object that determines how to evaluate each argument of a sum function.
   */
  private class EvalContentsForSum implements ContentsVisitor<Double> {
    Map<Coord, Contents> data;
    Map<Contents, Value> savedEvals;

    /**
     * Constructs an instance of this function object.
     *
     * @param data a map of data needed to evaluate references.
     */
    EvalContentsForSum(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
      this.data = data;
      this.savedEvals = savedEvals;
    }

    @Override
    public Double visitBlank(Blank that) {
      return 0.0;
    }

    @Override
    public Double visitBoolean(BooleanValue that) {
      return 0.0;
    }

    @Override
    public Double visitDouble(DoubleValue that) {
      return that.getValue();
    }

    @Override
    public Double visitString(StringValue that) {
      return 0.0;
    }

    @Override
    public Double visitReference(Reference that) {
      double answer = 0.0;
      for (Coord c : that.getReferences()) {
        try {
          answer += data.getOrDefault(c, new Blank()).accept(this);
        } catch (IllegalArgumentException e) {
          answer += 0;
        }


      }
      return answer;
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
