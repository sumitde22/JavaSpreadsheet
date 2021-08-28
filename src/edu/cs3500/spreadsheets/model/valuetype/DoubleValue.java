package edu.cs3500.spreadsheets.model.valuetype;

import edu.cs3500.spreadsheets.model.ContentsVisitor;
import java.util.Objects;

/**
 * Represents a double that can be stored in a spreadsheet.
 */
public class DoubleValue extends AbstractValue {

  private final double value;

  /**
   * Constructs a double value to stored in a spreadsheet.
   * @param value the double to be stored
   */
  public DoubleValue(double value) {
    this.value = value;
  }

  /**
   * Gets the value of this double object.
   * @return a double
   */
  public double getValue() {
    return value;
  }

  @Override
  public <R> R accept(ContentsVisitor<R> visitor) {
    return visitor.visitDouble(this);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitDouble(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DoubleValue)) {
      return false;
    }
    DoubleValue that = (DoubleValue) o;
    return Math.abs(this.value - that.value) < 0.001;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return Double.toString(value);
  }
}

