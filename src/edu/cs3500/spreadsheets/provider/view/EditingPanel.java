package edu.cs3500.spreadsheets.provider.view;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A JPanel which contains two JButtons for accepting and declining a change, as well as a
 * JTextField which displays the raw contents of a cell, and which the user can type new contents
 * into in order to edit a cell.
 */
class EditingPanel extends JPanel {

  private JButton accept;
  private JButton decline;
  private JTextField textField;
  private JButton save;
  private JButton load;

  EditingPanel getEditingPanel() {
    this.accept = new JButton("✓");
    accept.setActionCommand("Accept");

    this.decline = new JButton("X");
    decline.setActionCommand("Decline");

    this.textField = new JTextField();
    textField.setActionCommand("Textfield");
    textField.setPreferredSize(new Dimension(350, 30));

    this.save = new JButton("Save");
    save.setActionCommand("Save");

    this.load = new JButton("Load");
    load.setActionCommand("Load");

    this.add(accept);
    this.add(decline);
    this.add(textField);
    this.add(save);
    this.add(load);
    return this;
  }

  /**
   * Gets the textfield used for editing cells.
   * @return the JTextField used in this spreadsheet for editing.
   */
  JTextField getTextField() {
    return this.textField;
  }

  /**
   * Gets the accept button on the spreadsheet.
   * @return the checkmark JButton.
   */
  JButton getAccept() {
    return this.accept;
  }

  /**
   * Gets the decline button on the spreadsheet.
   * @return the X JButton.
   */
  JButton getDecline() {
    return this.decline;
  }

  /**
   * Clear the textfield after a the checkmark or X buttons are clicked.
   */
  void clearTextField() {
    this.textField.setText("");
  }

  /**
   * Gets the save button on the spreadsheet.
   * @return the save JButton.
   */
  JButton getSave() {
    return this.save;
  }

  /**
   * Gets the load button on the spreadsheet.
   * @return the load JButton.
   */
  JButton getLoad() {
    return this.load;
  }
}