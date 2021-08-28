package edu.cs3500.spreadsheets.provider.view;

import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * The JPanel containing the JButtons to add rows and add columns to the spreadsheet.
 */
class ButtonPanel extends JPanel {

  private MainPanel mainPanel;

  ButtonPanel(MainPanel mainPanel) {
    this.mainPanel = mainPanel;
  }

  /**
   * Adds the two JButtons, sets their action commands, and sets their action listeners to the
   * main JPanel.
   * @return this JPanel with the two JButtons added.
   */
  ButtonPanel getButtons() {
    JButton addColsButton = new JButton("Add 10 Columns");
    addColsButton.setActionCommand("Add 10 Columns Button");
    addColsButton.addActionListener(mainPanel);

    JButton addRowsButton = new JButton("Add 10 Rows");
    addRowsButton.setActionCommand("Add 10 Rows Button");
    addRowsButton.addActionListener(mainPanel);

    this.add(addColsButton);
    this.add(addRowsButton);
    return this;
  }

}