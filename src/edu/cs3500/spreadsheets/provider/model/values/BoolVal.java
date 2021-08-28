package edu.cs3500.spreadsheets.provider.model.values;

/**
 * Represents a boolean value in a cell.
 */
public class BoolVal implements Val {
  private boolean b;

  public BoolVal(boolean b) {
    this.b = b;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    else if (! (other instanceof BoolVal)) {
      return false;
    }
    else {
      return ((BoolVal) other).b == this.b;
    }
  }

  @Override
  public int hashCode() {
    if (this.b) {
      return 1;
    }
    else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return String.valueOf(this.b);
  }
}
