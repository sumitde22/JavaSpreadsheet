package edu.cs3500.spreadsheets.provideradapters;

import edu.cs3500.spreadsheets.model.valuetype.BooleanValue;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.model.valuetype.StringValue;
import edu.cs3500.spreadsheets.model.valuetype.ValueVisitor;
import edu.cs3500.spreadsheets.provider.model.values.BoolVal;
import edu.cs3500.spreadsheets.provider.model.values.NumVal;
import edu.cs3500.spreadsheets.provider.model.values.StringVal;
import edu.cs3500.spreadsheets.provider.model.values.Val;

/**
 * Converts return types of our model to be compatible with return types of provider's model.
 */
public class ValueAdapter implements ValueVisitor<Val> {

  /**
   * Determines what to do with a string value.
   *
   * @param s a string value
   * @return the function applied to string
   */
  @Override
  public Val visitString(StringValue s) {
    return new StringVal(s.getValue());
  }

  /**
   * Determines what to do with a double value.
   *
   * @param d a double value
   * @return the function applied to double
   */
  @Override
  public Val visitDouble(DoubleValue d) {
    return new NumVal(d.getValue());
  }

  /**
   * Determines what to do with a boolean value.
   *
   * @param b a boolean value
   * @return the function applied to boolean
   */
  @Override
  public Val visitBoolean(BooleanValue b) {
    return new BoolVal(b.getValue());
  }
}
