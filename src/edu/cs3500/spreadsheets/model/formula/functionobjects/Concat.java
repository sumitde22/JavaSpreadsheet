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
 * Represents a function that attempts to combine strings together.
 */
public class Concat extends Function {

  /**
   * Constructs a concat function object.
   *
   * @param args the list of arguments for this concat function
   */
  public Concat(List<Formula> args) {
    if (args.isEmpty()) {
      throw new IllegalArgumentException("Needs at least 1 argument");
    }

    for (Formula f : args) {
      if (!f.accept(new IsValidArg())) {
        throw new IllegalArgumentException("Every formula must evaluate to string");
      }
    }

    this.arguments = args;
  }

  @Override
  public Value getBaseReturnType() {
    return new StringValue("");
  }

  @Override
  public Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
    StringBuilder answer = new StringBuilder();
    for (Formula f : arguments) {
      answer.append(f.accept(new EvalContentsForConcat(data, savedEvals)));
    }
    return new StringValue(answer.toString());
  }


  /*
  @Override
  public boolean equals(Object o) {
    if(this == o) {
      return true;
    }
    if(!(o instanceof Concat)) {
      return false;
    }
    Concat that = (Concat) o;

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
    return Objects.hash(4, arguments.toArray());
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
      return false;
    }

    @Override
    public Boolean visitString(StringValue that) {
      return true;
    }

    @Override
    public Boolean visitReference(Reference that) {
      return true;
    }

    @Override
    public Boolean visitFunction(Function that) {
      return that.getBaseReturnType().accept(this);
    }
  }

  /**
   * Helper function object that determines how to evaluate each argument of a Concat function.
   */
  private class EvalContentsForConcat implements ContentsVisitor<String> {

    Map<Coord, Contents> data;
    Map<Contents, Value> savedEvals;

    /**
     * Constructs an instance of this function object.
     * @param data a map of data needed to evaluate references.
     */
    EvalContentsForConcat(Map<Coord, Contents> data, Map<Contents, Value> savedEvals) {
      this.data = data;
      this.savedEvals = savedEvals;
    }

    @Override
    public String visitBlank(Blank that) {
      throw new IllegalArgumentException("Cannot apply concat to blank");
    }

    @Override
    public String visitBoolean(BooleanValue that) {
      throw new IllegalArgumentException("Cannot apply concat to boolean");
    }

    @Override
    public String visitDouble(DoubleValue that) {
      throw new IllegalArgumentException("Cannot apply concat to double");
    }

    @Override
    public String visitString(StringValue that) {
      return that.getValue();
    }

    @Override
    public String visitReference(Reference that) {
      StringBuilder answer = new StringBuilder();
      for (Coord c : that.getReferences()) {
        answer.append(data.getOrDefault(c, new Blank()).accept(this));
      }

      return answer.toString();
    }

    @Override
    public String visitFunction(Function that) {
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
