package edu.cs3500.spreadsheets.provider.controller;

/**
 * A Controller is a SpreadsheetListener (so it has the ability to update a spreadsheet based on
 * calls from the IView) and also has the ability to render a Worksheet into a visual view.
 */
public interface Controller extends SpreadsheetListener {

  /**
   * Renders the controller's spreadsheet, so that the table can be used (and edited, if it is an
   * editable version of the view).
   */
  void useTable();
}