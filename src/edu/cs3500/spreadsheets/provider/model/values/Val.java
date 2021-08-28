package edu.cs3500.spreadsheets.provider.model.values;

import edu.cs3500.spreadsheets.provider.model.Formula;
import edu.cs3500.spreadsheets.provider.model.FormulaVisitor;

/**
 * Represents a value in a Cell.
 * A Value is a type of {@link Formula}, and it can be one of:
 * - a {@link BoolVal}, which is a boolean.
 * - a {@link NumVal}, which is a double.
 * - a {@link StringVal}, which is a String.
 */
public interface Val extends Formula {

  @Override
  <R> R accept(FormulaVisitor<R> visitor);

  @Override
  String toString();

}

