package edu.cs3500.spreadsheets.provider.view;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * Represents the column of row headers in a spreadsheet.
 * The boolean field "first" is set to true when a RowHeader is first constructed, and once the
 * first column in the table is seen, it is set to false.
 */
public class RowHeader extends DefaultTableColumnModel {

  private boolean first = true;

  @Override
  public void addColumn(TableColumn col) {
    // since this is the column of row headers, only the first column is needed, and it is
    // added to the spreadsheet. once it is added, "first" is set to false and all other columns
    // are ignored.
    if (first) {
      DefaultTableCellRenderer rowHeadersCenter = new DefaultTableCellRenderer();
      rowHeadersCenter.setHorizontalAlignment(JLabel.CENTER);
      col.setCellRenderer(rowHeadersCenter);
      col.setMaxWidth(col.getPreferredWidth());
      super.addColumn(col);
      first = false;
    }
  }
}
