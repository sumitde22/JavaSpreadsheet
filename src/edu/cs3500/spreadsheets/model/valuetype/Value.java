package edu.cs3500.spreadsheets.model.valuetype;

import edu.cs3500.spreadsheets.model.Contents;
import edu.cs3500.spreadsheets.model.formula.Formula;

/**
 * Represents a primitive type that a cell can be set equal to or be evaluated to.
 */
public interface Value extends Contents, Formula {

  /**
   * Allows a function objects to determine this value's type.
   * @param <R> the return type of the function object
   * @return the function object evaluated
   */
  <R> R accept(ValueVisitor<R> visitor);
}