package edu.cs3500.spreadsheets.provider.view;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * Represents all of the columns of cells which make up a spreadsheet.
 * The boolean field "first" is set to true when a ColumnModel is first constructed, and once the
 * first column in the table is seen, it is set to false.
 */
public class ColumnModel extends DefaultTableColumnModel {

  private boolean first = true;

  @Override
  public void addColumn(TableColumn col) {
    // ignore the first column (which will be the row header)
    if (first || col.getModelIndex() == 0) {
      first = false;
      return;
    }
    // if the column is not the first in the model, add the column to the table.
    col.setPreferredWidth(col.getWidth());
    super.addColumn(col);
  }
}
