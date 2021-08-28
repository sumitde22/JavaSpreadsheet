package edu.cs3500.spreadsheets.provider.model;

import edu.cs3500.spreadsheets.provider.model.values.Val;

/**
 * Represents the formula of a Cell in a spreadsheet. A formula is one of: - a {@link Val}. - a
 * {@link edu.cs3500.spreadsheets.provider.model.IFunc}. - a {@link
 * edu.cs3500.spreadsheets.provider.model.Reference}.
 */
public interface Formula {

  <R> R accept(FormulaVisitor<R> visitor);

}