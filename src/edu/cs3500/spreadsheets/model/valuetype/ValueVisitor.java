package edu.cs3500.spreadsheets.model.valuetype;

/**
 * A function object that takes in a value and return desired type.
 * @param <R> return type of function object
 */
public interface ValueVisitor<R> {

  /**
   * Determines what to do with a string value.
   * @param s a string value
   * @return the function applied to string
   */
  R visitString(StringValue s);

  /**
   * Determines what to do with a double value.
   * @param d a double value
   * @return the function applied to double
   */
  R visitDouble(DoubleValue d);

  /**
   * Determines what to do with a boolean value.
   * @param b a boolean value
   * @return the function applied to boolean
   */
  R visitBoolean(BooleanValue b);
}
