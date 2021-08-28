package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyWorksheet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * Provides an implementation of a visual representation of a Worksheet that can be modified by user
 * and in turn modify the model itself.
 */
public class EditableGUI extends JFrame implements WorksheetView {

  private final static int DEFAULT_WIDTH = 1000;
  private final static int DEFAULT_HEIGHT = 600;
  private final static String cellSavingMessage
      = "Press ✓ to save changes to cell, ✖ to undo change";
  private final static String fileSavingMessage
      = "Type here to save or load file";

  private final WorksheetGridPanel grid;
  private ReadOnlyWorksheet model;
  private Coord cellSelected;
  private final JPanel interactiveGrid;
  private final JPanel toolBar;
  private final JButton saveTextButton = new JButton("✓");
  private final JButton undoTextButton = new JButton("✖");
  private final JLabel cellBeingEdited = new JLabel("Cell");
  private final JTextField contentsEditingTextBox = new JTextField(cellSavingMessage, 30);
  private final JButton saveFileButton = new JButton("Save");
  private final JButton loadFileButton = new JButton("Load");
  private final JTextField fileManagingTextBox = new JTextField(fileSavingMessage, 15);
  private final JButton addRows = new JButton("Add 10 rows");
  private final JButton addColumns = new JButton("Add 10 cols");

  /**
   * Creates an instance to visually display and modify a Worksheet object.
   *
   * @param model the model to be displayed
   */
  public EditableGUI(ReadOnlyWorksheet model) {
    super();
    Objects.requireNonNull(model);
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //setResizable(false);

    this.grid = new WorksheetGridPanel(model);
    this.model = model;
    this.cellSelected = null;
    this.interactiveGrid = new JPanel();
    interactiveGrid.setLayout(null);

    toolBar = new JPanel();
    toolBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    toolBar.setBackground(Color.WHITE);
    toolBar.add(saveTextButton);
    toolBar.add(undoTextButton);
    toolBar.add(cellBeingEdited);
    toolBar.add(contentsEditingTextBox);
    toolBar.add(saveFileButton);
    toolBar.add(loadFileButton);
    toolBar.add(fileManagingTextBox);
    toolBar.setBounds(0, 0, DEFAULT_WIDTH, WorksheetGridPanel.CELLHEIGHT * 2);
    toolBar.setPreferredSize(new Dimension(DEFAULT_WIDTH, WorksheetGridPanel.CELLHEIGHT * 2));

    grid.setBounds(0, WorksheetGridPanel.CELLHEIGHT * 2, grid.getPreferredSize().width,
        grid.getPreferredSize().height);
    addRows.setBounds(0, grid.getPreferredSize().height + toolBar.getPreferredSize().height,
        WorksheetGridPanel.CELLWIDTH, WorksheetGridPanel.CELLHEIGHT);
    addColumns.setBounds(grid.getPreferredSize().width, toolBar.getPreferredSize().height,
        WorksheetGridPanel.CELLWIDTH, WorksheetGridPanel.CELLHEIGHT);
    addRows.addActionListener(e -> {
      grid.addRows(10);
      refresh();
    });
    addColumns.addActionListener(e -> {
      grid.addColumns(10);
      refresh();
    });

    interactiveGrid.add(toolBar);
    interactiveGrid.add(grid);
    interactiveGrid.add(addRows);
    interactiveGrid.add(addColumns);
    interactiveGrid.setPreferredSize(
        new Dimension(grid.getPreferredSize().width + WorksheetGridPanel.CELLWIDTH,
            grid.getPreferredSize().height + WorksheetGridPanel.CELLHEIGHT * 3));
    add(new JScrollPane(interactiveGrid));
  }

  @Override
  public void render() {
    setVisible(true);
  }

  @Override
  public void refresh() {
    grid.setBounds(0, WorksheetGridPanel.CELLHEIGHT * 2, grid.getPreferredSize().width,
        grid.getPreferredSize().height);
    addRows.setBounds(0, grid.getPreferredSize().height + toolBar.getPreferredSize().height,
        WorksheetGridPanel.CELLWIDTH, WorksheetGridPanel.CELLHEIGHT);
    addColumns.setBounds(grid.getPreferredSize().width, toolBar.getPreferredSize().height,
        WorksheetGridPanel.CELLWIDTH, WorksheetGridPanel.CELLHEIGHT);
    interactiveGrid.setPreferredSize(
        new Dimension(grid.getPreferredSize().width + WorksheetGridPanel.CELLWIDTH,
            grid.getPreferredSize().height + WorksheetGridPanel.CELLHEIGHT * 3));
    repaint();
  }

  @Override
  public void selectCell(int col, int row) {
    if (col >= 1 && row >= 1) {
      interactiveGrid.grabFocus();
      grid.highlightCell(col, row);
      cellSelected = new Coord(col, row);
      cellBeingEdited.setText(cellSelected.toString());
      contentsEditingTextBox.setText(model.getExplicitContents(col, row));
      //refresh();
    }
  }

  @Override
  public void unSelectCell() {
    grid.unhighlightCell();
    cellSelected = null;
    cellBeingEdited.setText("Cell");
    contentsEditingTextBox.setText(cellSavingMessage);
    //refresh();
  }


  @Override
  public void addFeatures(Features features) {
    interactiveGrid.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = (e.getY() / WorksheetGridPanel.CELLHEIGHT) - 2;
        int col = e.getX() / WorksheetGridPanel.CELLWIDTH;
        if (row >= 1 && col >= 1) {
          features.selectCell(col, row);
        }
      }
    });

    saveTextButton.addActionListener(e -> {
          if (cellSelected != null) {
            features.editCell(cellSelected.col, cellSelected.row, contentsEditingTextBox.getText());
            //refresh()
          }
        }
    );

    undoTextButton.addActionListener(e -> {
      if (cellSelected != null) {
        contentsEditingTextBox
            .setText(model.getExplicitContents(cellSelected.col, cellSelected.row));
        //refresh()
      }
    });

    interactiveGrid.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "Delete cell");
    interactiveGrid.getActionMap().put("Delete cell", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (cellSelected != null) {
          features.clearCell(cellSelected.col, cellSelected.row);
          // refresh()
        }
      }
    });

    BiFunction<Integer, Integer, AbstractAction> tryMoveCell = (xChange, yChange) ->
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            if (cellSelected != null) {
              int row = cellSelected.row + yChange;
              int col = cellSelected.col + xChange;
              if (row > grid.getRows()) {
                grid.addRows(row - grid.getRows());
              }
              if (col > grid.getColumns()) {
                grid.addColumns(col - grid.getColumns());
              }
              if (row >= 1 && col >= 1) {
                features.selectCell(col, row);
              }
            }
          }
        };

    interactiveGrid.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Move down one cell");
    interactiveGrid.getActionMap().put("Move down one cell", tryMoveCell.apply(0, 1));

    interactiveGrid.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "Move right one cell");
    interactiveGrid.getActionMap().put("Move right one cell", tryMoveCell.apply(1, 0));

    interactiveGrid.getInputMap().put(KeyStroke.getKeyStroke("UP"), "Move up one cell");
    interactiveGrid.getActionMap().put("Move up one cell", tryMoveCell.apply(0, -1));

    interactiveGrid.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "Move left one cell");
    interactiveGrid.getActionMap().put("Move left one cell", tryMoveCell.apply(-1, 0));

    saveFileButton.addActionListener(e -> {
      features.saveToFile(fileManagingTextBox.getText());
    });

    loadFileButton.addActionListener(e -> {
      try {
        ReadOnlyWorksheet newModel = features.setNewModel(fileManagingTextBox.getText());
        model = newModel;
        grid.setNewModel(newModel);
        cellBeingEdited.setText("Cell");
        contentsEditingTextBox.setText(cellSavingMessage);
        fileManagingTextBox.setText(fileSavingMessage);
        refresh();
      } catch (IllegalArgumentException exception) {
        // don't do anything if file not found
      }
    });
  }
}
