package edu.cs3500.spreadsheets.model.valuetype;

import edu.cs3500.spreadsheets.model.ContentsVisitor;
import java.util.Objects;

/**
 * Represents a boolean that can be stored in a spreadsheet.
 */
public class BooleanValue extends AbstractValue {

  private final boolean value;

  /**
   * Constructs a boolean value to stored in a spreadsheet.
   * @param value the boolean to be stored
   */
  public BooleanValue(boolean value) {
    this.value = value;
  }

  /**
   * Gets the value of this boolean object.
   * @return a boolean
   */
  public boolean getValue() {
    return value;
  }

  @Override
  public <R> R accept(ContentsVisitor<R> visitor) {
    return visitor.visitBoolean(this);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitBoolean(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BooleanValue)) {
      return false;
    }
    BooleanValue that = (BooleanValue) o;
    return this.value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return Boolean.toString(value);
  }
}
