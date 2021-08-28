package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.ReadOnlyWorksheet;

/**
 * An interface that consolidates all of functionality that a WorksheetView is expected to provide
 * actions or implementations for and allows a user to interact with a Worksheet.
 */
public interface Features {

  /**
   * Replaces the model currently stored with a new model to be read in from the given file name.
   *
   * @param fileName the file containing a text representation of a Worksheet
   * @return a read-only version of this worksheet to be used
   */
  ReadOnlyWorksheet setNewModel(String fileName);

  /**
   * Provide some way of indicating that this cell has been picked by the user to modify in some
   * way.
   *
   * @param col column of cell that user wants to modify
   * @param row row of cell that user wants to modify
   */
  void selectCell(int col, int row);

  /**
   * Delete the cell at the given indices and update all representations of this cell accordingly.
   *
   * @param col column of cell that user wants to delete
   * @param row row of cell that user wants to delete
   */
  void clearCell(int col, int row);

  /**
   * Modify the cell at the given indices and update all representations of this cell accordingly.
   *
   * @param col column of cell that user wants to modify
   * @param row row of cell that user wants to modify
   */
  void editCell(int col, int row, String contents);

  /**
   * Save the current state of this representation to the given text field.
   *
   * @param fileName the file that will contain a text representation of this Worksheet
   */
  void saveToFile(String fileName);

}
