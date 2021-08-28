package edu.cs3500.spreadsheets.provider.controller;

import java.io.File;

/**
 * Represents the controller's listener which makes changes to the Worksheet model. Updates the
 * spreadsheet according to changes made in the view whenever it is called on by the view.
 */
public interface SpreadsheetListener {
  /**
   * Update the model based on the given coordinates and the new contents.
   * @param row the zero-indexed row of the cell to be updated.
   * @param col the zero-indexed column of the cell to be updated.
   * @param s the new contents of the cell.
   */
  void update(int col, int row, String s);

  /**
   * Loads the given file chosen in the file chooser as an Editable GUI.
   * @param selectedFile the selected file in the file chooser
   */
  void openFile(File selectedFile);

  /**
   * Saves the current worksheet as a new file with the given file name using a Textual View.
   * @param selectedFile the user inputted file name
   */
  void saveFile(File selectedFile);
}
