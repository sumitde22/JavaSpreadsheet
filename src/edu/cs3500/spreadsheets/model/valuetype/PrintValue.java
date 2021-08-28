package edu.cs3500.spreadsheets.model.valuetype;

/**
 * Function object that correctly prints out the given value.
 */
public class PrintValue implements ValueVisitor<String> {

  @Override
  public String visitString(StringValue s) {
    StringBuilder builder = new StringBuilder();
    String str = s.getValue();
    builder.append("\"");
    builder.append(str);
    builder.append("\"");
    return builder.toString();
  }

  @Override
  public String visitDouble(DoubleValue d) {
    return String.format("%f", d.getValue());
  }

  @Override
  public String visitBoolean(BooleanValue b) {
    return b.toString();
  }
}

