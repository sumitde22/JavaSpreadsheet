package edu.cs3500.spreadsheets.provider.view;

import edu.cs3500.spreadsheets.provider.controller.SpreadsheetListener;

/**
 * An interface for the various types of views that can be rendered from a single spreadsheet.
 */
public interface IView {

  /**
   * Renders the view of the spreadsheet.
   */
  void render();

  /**
   * Sets the spreadsheet listener of an implementation of IView, which the IView will call
   * upon whenever the spreadsheet should be changed.
   * @param sL the SpreadsheetListener that the View will use to make changes.
   */
  void addListener(SpreadsheetListener sL);

}