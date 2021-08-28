package edu.cs3500.spreadsheets.model.valuetype;

import edu.cs3500.spreadsheets.model.ContentsVisitor;
import java.util.Objects;

/**
 * Represents a string that can be stored in a spreadsheet.
 */
public class StringValue extends AbstractValue {

  /**
   * Constructs a string value to stored in a spreadsheet.
   * @param value the string to be stored
   */
  public StringValue(String value) {
    this.value = value;
  }

  private final String value;

  /**
   * Gets the value of this String object.
   * @return a String
   */
  public String getValue() {
    return value;
  }

  @Override
  public <R> R accept(ContentsVisitor<R> visitor) {
    return visitor.visitString(this);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitString(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StringValue)) {
      return false;
    }
    StringValue that = (StringValue) o;
    return this.value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
