package edu.cs3500.spreadsheets.provider.model;

import edu.cs3500.spreadsheets.provider.model.Formula;
import edu.cs3500.spreadsheets.provider.model.FormulaVisitor;
import edu.cs3500.spreadsheets.provider.model.values.Val;
import java.util.List;

/**
 * Represents a function, which can have an operator (representing what the function is doing,
 * and a list of formulas, which are the arguments to the function.
 */
public interface IFunc extends Formula {

  /**
   * Getter for the operator of a function.
   * @return the FormulaVisitor representing the Operator.
   */
  FormulaVisitor<Val> getOperator();

  /**
   * Getter for the arguments of a function.
   * @return the list of formulas representing arguments.
   */
  List<Formula> getArgs();
}