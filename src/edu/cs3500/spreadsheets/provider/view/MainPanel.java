package edu.cs3500.spreadsheets.provider.view;

import edu.cs3500.spreadsheets.provider.model.ReadOnlyWorksheet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumnModel;

/**
 * The main JPanel which contains the JScrollPane containing our JTable representation of a
 * Worksheet, as well as a ButtonPanel containing buttons for adding blank rows and columns to the
 * end of a spreadsheet.
 */
public class MainPanel extends JPanel implements ActionListener {

  private JTable mainTable;
  private WSModelToAbstractTableModel dataModel;

  MainPanel(ReadOnlyWorksheet ws) {
    this.dataModel = new WSModelToAbstractTableModel(ws);
    ButtonPanel buttonPanel = new ButtonPanel(this);

    // sets the main table's data model to the worksheet
    TableColumnModel columnModel = new ColumnModel();
    this.mainTable = this.getMainTable(dataModel, columnModel);

    // Controls the layout of our main panel, and makes sure that the panels resize properly
    // with the JFrame.
    this.setLayout(new BorderLayout());
    this.add(buttonPanel.getButtons(), BorderLayout.SOUTH);
    this.add(this.getScrollPane(dataModel));

  }

  /**
   * Returns the JScrollPane that surrounds the JTable in this JPanel.
   * @param dataModel the AbstractTableModel representing the information in the spreadsheet.
   * @return the JScrollPane which contains the spreadsheet in it.
   */
  private JScrollPane getScrollPane(WSModelToAbstractTableModel dataModel) {

    // sets up the header column for row headers
    TableColumnModel rowHeaders = new RowHeader();

    // Creates a single-column JTable, which consists of the row headers.
    JTable headerCol = this.getHeaderTable(dataModel, rowHeaders);

    // Creates the columns in the table and the header column.
    mainTable.createDefaultColumnsFromModel();
    headerCol.createDefaultColumnsFromModel();

    // Makes sure that the table and row headers stay in sync by sharing the same SelectionModel.
    mainTable.setSelectionModel(headerCol.getSelectionModel());

    // Puts the row header column in a viewport, which will be put in the scroll pane.
    JViewport viewport = new JViewport();
    viewport.setView(headerCol);
    viewport.setPreferredSize(headerCol.getMaximumSize());

    // initializes the scroll pane
    JScrollPane scrollPane = new JScrollPane(mainTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // attaches the row headers to the scroll pane, so that when the user scrolls horizontally,
    // the row headers will always be visible on the left of the screen.
    scrollPane.setRowHeader(viewport);
    scrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerCol.getTableHeader());

    return scrollPane;
  }

  /**
   * Returns the JTable which renders the Cells in a Worksheet.
   * @param dataModel the AbstractTableModel representing the information in the spreadsheet.
   * @param columnModel The TableColumnModel representing each column of cells making up the
   *                    Worksheet.
   * @return the JTable representing the information in the dataModel.
   */
  private JTable getMainTable(WSModelToAbstractTableModel dataModel, TableColumnModel columnModel) {

    JTable mainTable = new JTable(dataModel, columnModel);
    mainTable.setRowHeight(20);

    // gets the column headers of the table and centers the text.
    JLabel colHeader = (JLabel) mainTable.getTableHeader().getDefaultRenderer();
    colHeader.setHorizontalAlignment(JLabel.CENTER);

    mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    mainTable.setGridColor(new Color(215, 215, 215));

    // disallows selection of full rows or reordering of the columns
    mainTable.setRowSelectionAllowed(false);
    mainTable.setCellSelectionEnabled(true);
    mainTable.getTableHeader().setReorderingAllowed(false);

    mainTable.setColumnModel(columnModel);

    return mainTable;
  }

  /**
   * Returns the single-column JTable displaying row headers.
   * @param dataModel The AbstractTableModel representing the information in the spreadsheet.
   * @param rowHeaders the TableColumnModel representing the row headers for the table.
   * @return the column of row headers to as a JTable.
   */
  private JTable getHeaderTable(WSModelToAbstractTableModel dataModel,
                                TableColumnModel rowHeaders) {
    JTable headerCol = new JTable(dataModel, rowHeaders);
    headerCol.setRowHeight(20);

    // displays the row headers in a visually appealing way
    headerCol.setBackground(new Color(246, 246, 246));
    headerCol.setColumnSelectionAllowed(false);
    headerCol.setRowSelectionAllowed(false);
    headerCol.setGridColor(Color.LIGHT_GRAY);
    headerCol.getTableHeader().setReorderingAllowed(false);

    return headerCol;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Add 10 Rows Button")) {
      this.dataModel.add10Rows();
      this.mainTable.createDefaultColumnsFromModel();
    }
    else if (e.getActionCommand().equals("Add 10 Columns Button")) {
      this.dataModel.add10Cols();
      this.mainTable.createDefaultColumnsFromModel();
    }
  }

  /**
   * Returns the JTable in this JPanel.
   * @return the main JTable which displays the information about the spreadsheet.
   */
  JTable getTable() {
    return this.mainTable;
  }
}
