package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.ReadOnlyWorksheet;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * A visual representation of a Worksheet object that cannot be modified.
 */
public class DisplayOnlyGUI extends JFrame implements WorksheetView {

  private final static int DEFAULT_WIDTH = 1000;
  private final static int DEFAULT_HEIGHT = 600;

  /**
   * Constructs an instance to visually represent a Worksheet object.
   *
   * @param model the model to be displayed
   */
  public DisplayOnlyGUI(ReadOnlyWorksheet model) {
    super();
    Objects.requireNonNull(model);
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //setResizable(false);
    add(new JScrollPane(new WorksheetGridPanel(model)));
  }

  @Override
  public void render() {
    setVisible(true);
  }

  @Override
  public void refresh() {
    repaint();
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



