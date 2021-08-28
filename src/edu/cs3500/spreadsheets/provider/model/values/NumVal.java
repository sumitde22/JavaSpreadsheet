package edu.cs3500.spreadsheets.provider.model.values;

import java.util.Objects;

/**
 * Represents a {@link Val} that is a Double.
 */
public class NumVal implements Val {
  public final double d;

  public NumVal(double d) {
    this.d = d;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    else if (! (other instanceof NumVal)) {
      return false;
    }
    else {
      return Math.abs(((NumVal)other).d - this.d) <= 0.00000001;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.d);
  }

  @Override
  public String toString() {
    /*
    DecimalFormat decimalFormat = new DecimalFormat("0.#");
    return String.valueOf(decimalFormat.format(this.d)); */
    return String.format("%f", this.d);
  }
}