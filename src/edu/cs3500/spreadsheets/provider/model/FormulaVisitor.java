package edu.cs3500.spreadsheets.provider.model;

import edu.cs3500.spreadsheets.provider.model.IFunc;
import edu.cs3500.spreadsheets.provider.model.Reference;

/**
 * Visitor for each type of {@link Formula}.
 *
 * @param <R> the return type of this visitor.
 */
public interface FormulaVisitor<R> {
  /**
   * Process a reference.
   *
   * @param r the reference
   * @return the desired result
   */
  R visitReference(Reference r);

  /**
   * Process a function.
   *
   * @param f the function
   * @return the desired result
   */
  R visitFunction(IFunc f);

  /**
   * Process a boolean.
   *
   * @param b a boolean.
   * @return the desired result.
   */
  R visitBool(boolean b);

  /**
   * Process a Double.
   *
   * @param d the double.
   * @return the desired result.
   */
  R visitNum(double d);

  R visitString(String s);


}
