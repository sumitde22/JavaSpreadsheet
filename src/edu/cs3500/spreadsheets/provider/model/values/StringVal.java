package edu.cs3500.spreadsheets.provider.model.values;

import java.util.Objects;

/**
 * Represents a {@link Val} that is a String.
 */
public class StringVal implements Val {
  public final String s;

  public StringVal(String s) {
    this.s = s;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    else if (! (other instanceof StringVal)) {
      return false;
    }
    else {
      return ((StringVal) other).s.equals(this.s);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.s);
  }

  @Override
  public String toString() {
    String ans = this.s;
    return "\"" + ans.replaceAll("\\\\", "\\\\\\\\")
        .replaceAll("\"", "\\\\\\\"") + "\"";
  }
}
