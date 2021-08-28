package edu.cs3500.spreadsheets.sexp;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.Function;
import edu.cs3500.spreadsheets.model.formula.Reference;
import edu.cs3500.spreadsheets.model.valuetype.BooleanValue;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.model.valuetype.StringValue;
import java.util.List;

/**
 * Function object that takes in a S-expression and returns a formula.
 */
public class FormulaBuilder implements SexpVisitor<Formula> {

  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  @Override
  public Formula visitBoolean(boolean b) {
    return new BooleanValue(b);
  }

  /**
   * Process a numeric value.
   *
   * @param d the value
   * @return the desired result
   */
  @Override
  public Formula visitNumber(double d) {
    return new DoubleValue(d);
  }

  /**
   * Process a list value.
   *
   * @param l the contents of the list (not yet visited)
   * @return the desired result
   */
  @Override
  public Formula visitSList(List<Sexp> l) {
    // in general, if there are more than 1 arguments in the list, we interpret it as a function,
    // else, it could just be a single type of any s-expression

    // note that the only time a function name is a valid symbol is if it's the first argument in a
    // list, which we handle here
    if (l.size() > 1) {
      SSymbol function = (SSymbol) l.get(0);
      Function.Builder builder = Function.builder();
      builder.setType(function.name);

      for (int i = 1; i < l.size(); i++) {
        builder.addArgument(l.get(i).accept(this));
      }
      return builder.build(); // could throw IllegalArgumentException
    } else if (l.size() == 1) {
      return l.get(0).accept(this);
    }
    throw new IllegalArgumentException("List cannot have 0 args");
  }

  /**
   * Process a symbol.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Formula visitSymbol(String s) {
    // a symbol by itself can only be reference, as the case where it's a function is handled by
    // Slist

    // if the reference has a colon, we try to interpret it as two references,
    // otherwise we trying to interpret the whole string as a single reference
    if (s.contains(":")) {
      String[] arguments = s.split(":");
      if (arguments.length != 2) {
        throw new IllegalArgumentException("More than one colon");
      }
      Coord cell1;
      Coord cell2;
      if (isValidCellName(arguments[0])) {
        cell1 = stringToCoord(arguments[0]);
      } else {
        throw new IllegalArgumentException("Invalid cell 1 name");
      }
      if (isValidCellName(arguments[1])) {
        cell2 = stringToCoord(arguments[1]);
      } else {
        throw new IllegalArgumentException("Invalid cell 2 name");
      }
      return new Reference(cell1, cell2); // throws illegal arg if first cell below second cell
    } else {
      if (isValidCellName(s)) {
        return new Reference(stringToCoord(s));
      }
      throw new IllegalArgumentException("Invalid cell name");
    }
  }

  /**
   * Helper method that takes a string representation of a cell and determines if it's a valid cell.
   *
   * @param s the string rep of a cell
   * @return is it a valid rep for a cell
   */
  public static boolean isValidCellName(String s) {
    int i = 0;
    while (i < s.length() && (s.charAt(i) >= 65 && s.charAt(i) <= 90)) {
      i++;
    }
    if (i == s.length()) {
      return false; // "All Strings, no numbers"
    }
    if (s.charAt(i) < 48 || s.charAt(i) > 57) {
      return false; // "The next character isn't a number"
    }

    while (i < s.length() && (s.charAt(i) >= 48 && s.charAt(i) <= 57)) {
      i++;
    }
    return i == s.length();
  }

  /**
   * Converts the given string to a coordinate if it represents a valid coordinate.
   *
   * @param s the string to be converted
   * @return the coordinate
   */
  public static Coord stringToCoord(String s) {
    int i = 0;
    StringBuilder builder = new StringBuilder();
    while (i < s.length() && (s.charAt(i) >= 65 && s.charAt(i) <= 90)) {
      builder.append(s.charAt(i));
      i++;
    }

    int row = 0;
    while (i < s.length()) {
      row *= 10;
      row += (s.charAt(i) - 48);
      i++;
    }

    return new Coord(Coord.colNameToIndex(builder.toString()), row);
  }

  /**
   * Process a string value.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Formula visitString(String s) {
    return new StringValue(s);
  }
}
