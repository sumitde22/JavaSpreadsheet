package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyWorksheet;
import edu.cs3500.spreadsheets.model.valuetype.PrintValue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;

class WorksheetGridPanel extends JPanel {

  private int rows;
  private int columns;
  private ReadOnlyWorksheet model;
  private Coord cellSelected;

  private final static int DEFAULT_STARTINGROWS = 30;
  private final static int DEFAULT_STARTINGCOLUMNS = 26;
  final static int CELLHEIGHT = 20;
  final static int CELLWIDTH = 80;
  private final static int PADDINGFROMSIDEOFCELL = CELLWIDTH / 40;
  private final static int PADDINGFROMTOPOFCELL = CELLHEIGHT * 3 / 4;

  WorksheetGridPanel(ReadOnlyWorksheet model) {
    super();
    this.model = model;
    this.cellSelected = null;
    this.rows = DEFAULT_STARTINGROWS;
    this.columns = DEFAULT_STARTINGCOLUMNS;
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    List<Coord> coordList = model.getFilledCells();
    for (Coord c : coordList) {
      this.rows = Math.max(this.rows, c.row);
      this.columns = Math.max(this.columns, c.col);
    }

    g2d.setColor(Color.LIGHT_GRAY);
    g2d.fillRect(0, 0,
        CELLWIDTH * (this.columns + 1), CELLHEIGHT * (this.rows + 1));

    g2d.setColor(Color.WHITE);
    g2d.fillRect(CELLWIDTH, CELLHEIGHT,
        CELLWIDTH * this.columns, CELLHEIGHT * this.rows);

    g2d.setColor(Color.BLACK);
    for (int i = 1; i <= this.columns; i++) {
      String toAdd = Coord.colIndexToName(i);
      g2d.drawString(toAdd,
          (CELLWIDTH / 2) + (i * CELLWIDTH) - (g2d.getFontMetrics().stringWidth(toAdd) / 2),
          PADDINGFROMTOPOFCELL);
    }
    for (int j = 1; j <= this.rows; j++) {
      String toAdd = Integer.toString(j);
      g2d.drawString(toAdd, (CELLWIDTH / 2) - (g2d.getFontMetrics().stringWidth(toAdd) / 2),
          PADDINGFROMTOPOFCELL + (j * CELLHEIGHT));
    }
    for (int i = 0; i <= this.columns + 1; i++) {
      g2d.drawLine(i * CELLWIDTH, 0,
          i * CELLWIDTH, (this.rows + 1) * CELLHEIGHT);
    }
    for (int j = 0; j <= this.rows + 1; j++) {
      g2d.drawLine(0, j * CELLHEIGHT,
          (this.columns + 1) * CELLWIDTH, j * CELLHEIGHT);
    }

    g2d.setColor(Color.BLUE);
    if (cellSelected != null) {
      g2d.drawLine(cellSelected.col * CELLWIDTH, cellSelected.row * CELLHEIGHT,
          (cellSelected.col) * CELLWIDTH, (cellSelected.row + 1) * CELLHEIGHT);
      g2d.drawLine(cellSelected.col * CELLWIDTH, cellSelected.row * CELLHEIGHT,
          (cellSelected.col + 1) * CELLWIDTH, (cellSelected.row) * CELLHEIGHT);
      g2d.drawLine((cellSelected.col + 1) * CELLWIDTH, (cellSelected.row) * CELLHEIGHT,
          (cellSelected.col + 1) * CELLWIDTH, (cellSelected.row + 1) * CELLHEIGHT);
      g2d.drawLine((cellSelected.col) * CELLWIDTH, (cellSelected.row + 1) * CELLHEIGHT,
          (cellSelected.col + 1) * CELLWIDTH, (cellSelected.row + 1) * CELLHEIGHT);
    }

    g2d.setColor(Color.BLACK);
    for (Coord c : coordList) {
      int leftx = CELLWIDTH * c.col + PADDINGFROMSIDEOFCELL;
      int y = CELLHEIGHT * c.row + PADDINGFROMTOPOFCELL;
      int rightx = CELLWIDTH * (c.col + 1) - PADDINGFROMSIDEOFCELL;
      String toPrint;
      try {
        toPrint = model.getEvaluatedContents(c.col, c.row).accept(new PrintValue());
      } catch (Exception e) {
        toPrint = "ERROR: " + e.getMessage();

      }
      // cuts off string to put in cell
      while (leftx + g2d.getFontMetrics().stringWidth(toPrint) > rightx) {
        toPrint = toPrint.substring(0, toPrint.length() - 1);
      }
      g2d.drawString(toPrint, leftx, y);
    }

  }

  @Override
  public Dimension getPreferredSize() {
    List<Coord> coordList = model.getFilledCells();
    for (Coord c : coordList) {
      this.rows = Math.max(this.rows, c.row);
      this.columns = Math.max(this.columns, c.col);
    }
    return new Dimension((1 + columns) * CELLWIDTH, (1 + rows) * CELLHEIGHT);
  }

  void setNewModel(ReadOnlyWorksheet model) {
    this.model = model;
    this.cellSelected = null;
    this.rows = DEFAULT_STARTINGROWS;
    this.columns = DEFAULT_STARTINGCOLUMNS;
  }

  int getRows() {
    return rows;
  }

  int getColumns() {
    return columns;
  }

  void addRows(int rows) {
    this.rows += Math.max(rows, 0);
  }

  void addColumns(int columns) {
    this.columns += Math.max(columns, 0);
  }

  void highlightCell(int col, int row) {
    if (col >= 1 && row >= 1) {
      cellSelected = new Coord(col, row);
    }
  }

  void unhighlightCell() {
    cellSelected = null;
  }
}
