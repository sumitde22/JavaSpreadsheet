package edu.cs3500.spreadsheets.model.formula;

import edu.cs3500.spreadsheets.model.Contents;
import edu.cs3500.spreadsheets.model.ContentsVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.functionobjects.Concat;
import edu.cs3500.spreadsheets.model.formula.functionobjects.LessThan;
import edu.cs3500.spreadsheets.model.formula.functionobjects.Product;
import edu.cs3500.spreadsheets.model.formula.functionobjects.Sum;
import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An abstract class that factors out all common code present in representation of functions in this
 * spreadsheet.
 */
public abstract class Function implements Formula {

  protected List<Formula> arguments;

  protected Function() {
    this.arguments = new ArrayList<>();
  }

  @Override
  public List<Coord> getReferences() {
    List<Coord> references = new ArrayList<>();
    for (Formula f : arguments) {
      for (Coord c : f.getReferences()) {
        if (!references.contains(c)) {
          references.add(c);
        }
      }
    }
    return references;
  }

  /**
   * Determines the base case for this function.
   * @return a base value to be used return type of this function
   */
  public abstract Value getBaseReturnType();

  @Override
  public abstract Value evaluate(Map<Coord, Contents> data, Map<Contents, Value> savedEvals);

  @Override
  public <R> R accept(ContentsVisitor<R> visitor) {
    return visitor.visitFunction(this);
  }

  /**
   * Returns a builder object that can indirectly build functions.
   * @return a function builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * A class that allows functions to be built based on a name without details about implementation.
   */
  public static final class Builder {

    String type;
    List<Formula> args;

    Builder() {
      this.type = null;
      this.args = new ArrayList<>();
    }

    /**
     * Set the type of this function.
     * @param t the function name
     * @return updated builder object
     */
    public Builder setType(String t) {
      this.type = t;
      return this;
    }

    /**
     * Add an argument to this function.
     * @param f the formula to added to this function call
     * @return updated builder object
     */
    public Builder addArgument(Formula f) {
      args.add(f);
      return this;
    }

    // constructors may throw illegalArg

    /**
     * Attempts to construct desired function based on String arguments passed.
     * @return the desired function object
     */
    public Function build() {
      switch (this.type) {
        case "SUM":
          return new Sum(args);
        case "PRODUCT":
          return new Product(args);
        case "<":
          return new LessThan(args);
        case "CONCAT":
          return new Concat(args);
        default:
          throw new IllegalArgumentException();
      }
    }
  }
}
