package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.valuetype.Value;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of a read-only spreadsheet model that a view cannot mutate.
 */
public class ViewModel implements ReadOnlyWorksheet {
  private final Worksheet delegate;

  /**
   * Constructs a read-only model that delegates all function calls to a given Worksheet.
   * @param delegate the given Worksheet that is only read and not modified
   */
  public ViewModel(Worksheet delegate) {
    Objects.requireNonNull(delegate);
    this.delegate = delegate;
  }

  @Override
  public String getExplicitContents(int col, int row) throws IllegalArgumentException {
    return delegate.getExplicitContents(col, row);
  }


  @Override
  public Value getEvaluatedContents(int col, int row) throws IllegalArgumentException {
    return delegate.getEvaluatedContents(col, row);
  }

  @Override
  public List<Coord> getFilledCells() {
    return delegate.getFilledCells();
  }

}
