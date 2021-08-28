package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyWorksheet;
import java.util.List;
import java.util.Objects;

/**
 * A textual edu.cs3500.spreadsheets.model.view implementation of Worksheet
 * edu.cs3500.spreadsheets.model.view which represents the contents of the passed in Worksheet to be
 * rendered.
 */
public class TextualView implements WorksheetView {

  private Appendable app;
  private ReadOnlyWorksheet w;

  /**
   * Constructs a textual edu.cs3500.spreadsheets.model.view that represents the contents of the
   * passed in edu.cs3500.spreadsheets.model.
   *
   * @param app the Appendable to be rendered
   * @param w the Worksheet edu.cs3500.spreadsheets.model which contains the data
   */
  public TextualView(Appendable app, ReadOnlyWorksheet w) {
    Objects.requireNonNull(app);
    Objects.requireNonNull(w);
    this.app = app;
    this.w = w;
  }

  @Override
  public void render() {
    StringBuilder s = new StringBuilder();
    List<Coord> cells = w.getFilledCells();
    for (int i = 0; i < cells.size(); i++) {
      Coord c = cells.get(i);
      s.append(c.toString());
      s.append(" ");
      s.append(w.getExplicitContents(c.col, c.row));
      if (i != cells.size() - 1) {
        s.append("\n");
      }
    }
    try {
      app.append(s.toString());
    } catch (Exception e) {
      // just don't render to appendable
    }
  }

  @Override
  public void refresh() {
    // nothing to refresh, only work is done when printing
  }

  @Override
  public void selectCell(int col, int row) {
    // doesn't respond to events
  }

  @Override
  public void unSelectCell() {
    // doesn't respond to events
  }

  @Override
  public void addFeatures(Features features) {
    // doesn't respond to events
  }
}
