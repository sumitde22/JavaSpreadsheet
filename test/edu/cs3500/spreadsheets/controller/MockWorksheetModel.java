package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.io.IOException;
import java.util.List;

/**
 * Represents a spreadsheet that just logs when it's methods are triggered.
 */
public class MockWorksheetModel implements Worksheet {

  private final Appendable log;

  /**
   * Constructs a mock worksheet model.
   * @param log message logger
   */
  MockWorksheetModel(Appendable log) {
    this.log = log;
  }

  public void editCell(int col, int row, String contents) throws IllegalArgumentException {
    addToOutput(
        String.format("Edited cell at row %d column %d with contents %s\n", row, col, contents));
  }

  @Override
  public void clearCell(int col, int row) throws IllegalArgumentException {
    addToOutput(String.format("Cleared cell at row %d column %d\n", row, col));
  }

  @Override
  public String getExplicitContents(int col, int row) throws IllegalArgumentException {
    return null;
  }

  @Override
  public Value getEvaluatedContents(int col, int row) throws IllegalArgumentException {
    return null;
  }

  @Override
  public List<Coord> getFilledCells() {
    return null;
  }

  private void addToOutput(String s) {
    try {
      log.append(s);
    } catch (IOException e) {
      throw new IllegalStateException("Could not append to output");
    }
  }
}
