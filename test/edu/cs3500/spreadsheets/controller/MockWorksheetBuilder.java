package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;

/**
 * Represents a builder that just returns a standard mock model.
 */
class MockWorksheetBuilder implements WorksheetBuilder<Worksheet> {
  private final Appendable log;

  /**
   * Constructs a mock worksheet builder.
   * @param log message logger
   */
  MockWorksheetBuilder(Appendable log) {
    this.log = log;
  }

  public WorksheetBuilder<Worksheet> createCell(int col, int row, String contents) {
    return this;
  }

  @Override
  public MockWorksheetModel createWorksheet() {
    return new MockWorksheetModel(log);
  }
}
