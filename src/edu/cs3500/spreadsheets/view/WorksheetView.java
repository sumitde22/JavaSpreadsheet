package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * Represents a set of methods that render an image of a Worksheet.
 */
public interface WorksheetView {

  /**
   * Appropriately presents an image representation of a Worksheet based on implementation.
   */
  void render();

  /**
   * Refreshes the image in the case that certain aspects have been updated.
   */
  void refresh();

  /**
   * Indicates that given cell has been selected in view.
   *
   * @param col column of cell selected
   * @param row row of cell selcted
   */
  void selectCell(int col, int row);

  /**
   * If any cell is highlighted right now, unhighlight it.
   */
  void unSelectCell();

  /**
   * Add the functionality that these features support to the view.
   *
   * @param features the different methods that are fired off given actions
   */
  void addFeatures(Features features);

}
