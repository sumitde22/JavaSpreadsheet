package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;

/**
 * Implementation of a Builder for a BasicWorksheet object used in WorksheetReader.
 */
public class WorksheetBuilderImpl implements WorksheetBuilder<Worksheet> {

  private final Worksheet model;

  /**
   * Constructs a builder object for a basic worksheet.
   */
  public WorksheetBuilderImpl() {
    model = new BasicWorksheet();
  }

  /**
   * Creates a new cell at the given coordinates and fills in its raw contents.
   *
   * @param col the column of the new cell (1-indexed)
   * @param row the row of the new cell (1-indexed)
   * @param contents the raw contents of the new cell: may be {@code null}, or any string. Strings
   *        beginning with an {@code =} character should be treated as formulas; all other strings
   *        should be treated as number or boolean values if possible, and string values otherwise.
   * @return this {@link WorksheetBuilder}
   */
  @Override
  public WorksheetBuilder<Worksheet> createCell(int col, int row, String contents) {
    model.editCell(col, row, contents);
    return this;
  }

  /**
   * Finalizes the construction of the worksheet and returns it.
   *
   * @return the fully-filled-in worksheet
   */
  @Override
  public Worksheet createWorksheet() {
    return model;
  }
}
