package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.view.WorksheetView;
import java.io.IOException;

/**
 * Represents a spreadsheet displays that just logs when it's methods are triggered.
 */
public class MockWorksheetView implements WorksheetView {

  private final Appendable log;

  /**
   * Constructs a mock worksheet view.
   * @param log message logger
   */
  MockWorksheetView(Appendable log) {
    this.log = log;
  }

  @Override
  public void render() {
    // nothing significant to log
  }

  @Override
  public void refresh() {
    addToOutput("Refreshed view to account for changes\n");
  }

  @Override
  public void selectCell(int col, int row) {
    addToOutput(String.format("Selected cell at row %d column %d\n", row, col));
  }

  @Override
  public void unSelectCell() {
    addToOutput("Unselect any cell if selected\n");
  }

  @Override
  public void addFeatures(Features features) {
    // nothing significant to log
  }

  private void addToOutput(String s) {
    try {
      log.append(s);
    } catch (IOException e) {
      throw new IllegalStateException("Could not append to output");
    }
  }
}
