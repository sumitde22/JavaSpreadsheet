package edu.cs3500.spreadsheets.provider.view;

import static edu.cs3500.spreadsheets.model.Coord.colIndexToName;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.model.ReadOnlyWorksheet;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 * An adapter class that will take in a read-only version of a Worksheet and create an
 * AbstractTableModel to be used by the JTable in the GUIView and EditableGUI.
 */
public class WSModelToAbstractTableModel extends AbstractTableModel {

  private final ReadOnlyWorksheet ws;
  private int numRows;
  private int numCol;

  /**
   * Creates a new instance of the adapter class from the given Worksheet.
   * @param ws a Worksheet which will be converted to an instance of AbstractTableModel.
   */
  public WSModelToAbstractTableModel(ReadOnlyWorksheet ws) {
    this.ws = ws;
    this.numRows = 100;
    this.numCol = 26;
  }

  // gets the number of rows in the spreadsheet, and adds the next multiple of 10 rows to the count.
  @Override
  public int getRowCount() {
    Set<Coord> set = this.ws.getAllCoords();
    int largest = 0;
    for (Coord c : set) {
      if (c.row > largest) {
        largest = c.row;
      }
    }
    if (this.numRows > (largest / 10 + 1) * 10) {
      return this.numRows;
    }
    else {
      this.numRows = (largest / 10 + 1) * 10;
      return this.numRows;
    }
  }

  // gets the number of columns in the spreadsheet, and adds the next multiple of 26 columns
  // to the count.
  @Override
  public int getColumnCount() {
    Set<Coord> set = this.ws.getAllCoords();
    int largest = 0;
    for (Coord c : set) {
      if (c.col > largest) {
        largest = c.col;
      }
    }
    if (this.numCol > (largest / 26 + 1) * 26 + 1) {
      return numCol;
    }
    else {
      this.numCol = (largest / 26  + 1) * 26 + 1;
      return this.numCol;
    }
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
      return rowIndex + 1;
    }
    try {
      return this.ws.evaluate(new Coord(columnIndex, rowIndex + 1));
    }
    catch (IllegalArgumentException e) {
      return "ERROR: " + e.getLocalizedMessage();
    }
  }

  @Override
  public String getColumnName(int col) {
    return colIndexToName(col);
  }


  /**
   * Adds 10 empty rows of cells to the bottom of the worksheet view.
   */
  public void add10Rows() {
    this.numRows += 10;
  }

  /**
   * Adds 10 empty columns of cells to the end of the worksheet view.
   */
  public void add10Cols() {
    this.numCol += 10;
  }

}
