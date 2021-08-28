package edu.cs3500.spreadsheets.provider.view;

import edu.cs3500.spreadsheets.provider.controller.SpreadsheetListener;
import edu.cs3500.spreadsheets.provider.model.ReadOnlyWorksheet;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * A Visual view for a Worksheet, which uses a JTable GUI to render the cells. It takes in a
 * read-only version of a Worksheet and uses its data to render the table.
 */
public class GUIView extends JFrame implements IView {

  private static final int X_BOUND = 50;
  private static final int Y_BOUND = 10;
  private static final int WIDTH = 800;
  private static final int HEIGHT = 500;

  /**
   * Creates a new GUIView of a read-only Worksheet using a JTable, JScrollPane, and JPanels.
   * @param ws the Worksheet to be rendered.
   */
  public GUIView(ReadOnlyWorksheet ws) {

    // we set these as the bounds for when the view is first rendered. theoretically, if the
    // computer running this program had a really large screen, the cells would not fill up the
    // entire screen, but since our WSModelToAbstractTableModel starts with 100 rows and 26 cols,
    // the bounds of the data will always be larger than the maximum size of the screen for most
    // standard-sized screens.
    setBounds(X_BOUND, Y_BOUND, WIDTH, HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    MainPanel mainPanel = new MainPanel(ws);
    this.setLayout(new BorderLayout());
    this.add(mainPanel);
    this.pack();

  }

  @Override
  public void render() {
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void addListener(SpreadsheetListener sL) {
    // this method is empty because this is a non-editable, view-only GUI, so it does not need
    // a SpreadsheetListener to make any changes.
  }

}