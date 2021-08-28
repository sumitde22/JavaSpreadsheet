package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.formula.Function;
import edu.cs3500.spreadsheets.model.formula.Reference;
import edu.cs3500.spreadsheets.model.valuetype.BooleanValue;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.model.valuetype.StringValue;

/**
 * An abstract function object that takes something of type Contents and determines how to evaluate
 * it.
 * @param <R> the return type of this function object
 */
public interface ContentsVisitor<R> {

  /**
   * Determines what to do with a blank contents.
   * @param that a blank contents
   * @return the function applied to blank
   */
  R visitBlank(Blank that);

  /**
   * Determines what to do with a boolean contents.
   * @param that a boolean contents
   * @return the function applied to boolean
   */
  R visitBoolean(BooleanValue that);


  /**
   * Determines what to do with a double contents.
   * @param that a double contents
   * @return the function applied to double
   */
  R visitDouble(DoubleValue that);


  /**
   * Determines what to do with a string contents.
   * @param that a string contents
   * @return the function applied to string
   */
  R visitString(StringValue that);

  /**
   * Determines what to do with a reference contents.
   * @param that a reference contents
   * @return the function applied to reference
   */
  R visitReference(Reference that);


  /**
   * Determines what to do with a function contents.
   * @param that a function contents
   * @return the function applied to function
   */
  R visitFunction(Function that);
}