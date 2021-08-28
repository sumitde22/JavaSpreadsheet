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
 * Represents a function that attempts to multiply a set of values in a spreadsheet.
 */
public class Product extends Function {

  /**
   * Constructs an instance of a function that multiplies given arguments.
   *
   * @param args the arguments to the given function
   */
  public Product(List<Formula> args) {
    if (args.isEmpty()) {
      throw new IllegalArgumentException("Need at least one argument!");
    }

    arguments = args;
  }

  @Override
  public Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
    double answer = 1;
    EvalContentsForProduct visitor = new EvalContentsForProduct(data, savedEvals);
    for (Formula f : arguments) {
      try {
        answer *= f.accept(visitor);
      } catch (IllegalArgumentException e) {
        answer *= 1;
      }
    }
    if (visitor.countNumericTypes == 0) {
      return new DoubleValue(0);
    } else {
      return new DoubleValue(answer);
    }
  }

  @Override
  public Value getBaseReturnType() {
    return new DoubleValue(1);
  }

  /*

  @Override
  public boolean equals(Object o) {
    if(this == o) {
      return true;
    }
    if(!(o instanceof Product)) {
      return false;
    }
    Product that = (Product) o;

    if(arguments.size() != (that.arguments.size())) {
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
    return Objects.hash(2, arguments.toArray());
  }

   */

  /**
   * Helper function object that determines how to evaluate each argument of a product function.
   */
  private class EvalContentsForProduct implements ContentsVisitor<Double> {

    int countNumericTypes = 0;
    Map<Coord, Contents> data;
    Map<Contents, Value> savedEvals;

    /**
     * Constructs an instance of this function object.
     *
     * @param data a map of data needed to evaluate references.
     */
    EvalContentsForProduct(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
      this.data = data;
      this.savedEvals = savedEvals;
    }

    @Override
    public Double visitBlank(Blank that) {
      return 1.0;
    }

    @Override
    public Double visitBoolean(BooleanValue that) {
      return 1.0;
    }

    @Override
    public Double visitDouble(DoubleValue that) {
      countNumericTypes++;
      return that.getValue();
    }

    @Override
    public Double visitString(StringValue that) {
      return 1.0;
    }

    @Override
    public Double visitReference(Reference that) {
      double answer = 1.0;
      for (Coord c : that.getReferences()) {
        try {
          answer *= data.getOrDefault(c, new Blank()).accept(this);
        } catch (IllegalArgumentException e) {
          answer *= 1;
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
