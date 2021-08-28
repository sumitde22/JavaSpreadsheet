package edu.cs3500.spreadsheets.provider.view;

import edu.cs3500.spreadsheets.provider.controller.SpreadsheetListener;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.model.ReadOnlyWorksheet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 * An editable GUI, which is a JFrame and implements both IView and ActionListener, so that when
 * JButtons on the EditingPanel in this JFrame are clicked, this GUI can call on the
 * SpreadsheetListener to make changes.
 */
public class EditableGUI extends JFrame implements IView, ActionListener, KeyListener {

  private EditingPanel editingPanel;
  private MainPanel mainPanel;
  private SpreadsheetListener sL;
  private final ReadOnlyWorksheet ws;
  private static final int X_BOUND = 50;
  private static final int Y_BOUND = 10;
  private static final int WIDTH = 800;
  private static final int HEIGHT = 500;

  /**
   * Constructs an instance of an EditableGUI with the given Read-only worksheet.
   *
   * @param ws a ReadOnlyWorksheet to be rendered as a JTable.
   */
  public EditableGUI(ReadOnlyWorksheet ws) {
    this.ws = ws;

    // we set these as the bounds for when the view is first rendered. theoretically, if the
    // computer running this program had a really large screen, the cells would not fill up the
    // entire screen, but since our WSModelToAbstractTableModel starts with 100 rows and 26 cols,
    // the bounds of the data will always be larger than the maximum size of the screen for most
    // standard-sized screens.
    setBounds(X_BOUND, Y_BOUND, WIDTH, HEIGHT);

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    editingPanel = new EditingPanel().getEditingPanel();

    this.setLayout(new BorderLayout());
    this.mainPanel = new MainPanel(ws);
    JTable table = this.mainPanel.getTable();
    mainPanel.add(editingPanel, BorderLayout.NORTH);
    mainPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    table.addKeyListener(this);
    this.setMinimumSize(mainPanel.getMinimumSize());

    // add a mouse listener to the JTable, so that when a cell is clicked, the table automatically
    // highlights it and knows which cell is selected.
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        int col = table.columnAtPoint(e.getPoint());
        int row = table.rowAtPoint(e.getPoint());

        setContents(ws.getCellContents(new Coord(col + 1, row + 1)));
      }
    });

    this.editingPanel.getAccept().addActionListener(this);
    this.editingPanel.getDecline().addActionListener(this);
    this.editingPanel.getSave().addActionListener(this);
    this.editingPanel.getLoad().addActionListener(this);

    this.add(mainPanel);
    this.pack();

  }

  /**
   * Set the contents of the editing panel's JTextField.
   *
   * @param contents the contents of a cell to be put into the JTextField.
   */
  private void setContents(String contents) {
    this.editingPanel.getTextField().setText(contents);
  }

  @Override
  public void render() {
    this.setVisible(true);
    this.mainPanel.getTable().createDefaultColumnsFromModel();
  }

  @Override
  public void addListener(SpreadsheetListener sL) {
    this.sL = sL;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Accept")) {
      this.acceptWasClicked();
    }
    if (e.getActionCommand().equals("Decline")) {
      this.declineWasClicked();
    }
    if (e.getActionCommand().equals("Save")) {
      this.openSave();
    }
    if (e.getActionCommand().equals("Load")) {
      this.openLoad();
    }
  }

  /**
   * Opens a JFileChooser for loading a file, which shows all possible files in the computer's
   * system when the Load button is clicked. Calls the openFile method on the SpreadsheetListener
   * and passes in the selected file from the JFileChooser.
   */
  private void openLoad() {
    JFileChooser load = new JFileChooser();
    int loadApprove = load.showOpenDialog(this);
    if (loadApprove == JFileChooser.APPROVE_OPTION) {
      try {
        this.sL.openFile(load.getSelectedFile());
      } catch (Exception e) {
        System.out.println("The file could not be loaded: " + e.getMessage());
      }
    }
  }

  /**
   * Opens a file chooser for saving a file with a given file name when the Save JButton is clicked.
   * Calls the saveFile method on the SpreadsheetListener and passes in the File given by the user
   * to the JFileChooser.
   */
  private void openSave() {
    JFileChooser save = new JFileChooser();
    int saveApprove = save.showSaveDialog(this);
    if (saveApprove == JFileChooser.APPROVE_OPTION) {
      try {
        this.sL.saveFile(save.getSelectedFile());
      } catch (Exception e) {
        System.out.println("The file could not be saved: " + e.getMessage());
      }
    }
  }


  /**
   * Called when the accept JButton is clicked, supposed to tell the SpreadsheetListener that the
   * accept JButton was clicked. The SL needs to know that the check button was clicked and what the
   * contents of the Text box are. It also need the row and column of the cell that it is updating.
   */
  private void acceptWasClicked() {
    int col = mainPanel.getTable().getSelectedColumn() + 1;
    int row = mainPanel.getTable().getSelectedRow() + 1;
    String contents = editingPanel.getTextField().getText();
    editingPanel.clearTextField();

    if (this.sL != null && row >= 1 && col >= 1) {
      this.sL.update(col, row, contents);
    }
  }

  /**
   * Method to tell the editing panel to clear the text field when the decline button is clicked.
   */
  private void declineWasClicked() {
    editingPanel.clearTextField();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // this method body is empty because we do not have any key events that are handled
    // when a key is typed. All our key events are handled by keyPressed and keyReleased.
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int col = mainPanel.getTable().getSelectedColumn() + 1;
    int row = mainPanel.getTable().getSelectedRow() + 1;

    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
        || e.getKeyCode() == KeyEvent.VK_DELETE) {
      editingPanel.clearTextField();

      if (this.sL != null && row >= 1 && col >= 1) {
        this.sL.update(col, row, "");
      }
    }

    if (e.getKeyCode() == KeyEvent.VK_UP
        || e.getKeyCode() == KeyEvent.VK_DOWN
        || e.getKeyCode() == KeyEvent.VK_LEFT
        || e.getKeyCode() == KeyEvent.VK_RIGHT) {
      if (this.ws.getCellAt(new Coord(col, row)) == null) {
        this.editingPanel.clearTextField();
      } else {
        String contentsOfSelectedCell = this.ws.getCellContents(new Coord(col, row));
        this.setContents(contentsOfSelectedCell);
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int col = mainPanel.getTable().getSelectedColumn() + 1;
    int row = mainPanel.getTable().getSelectedRow() + 1;

    if (e.getKeyCode() == KeyEvent.VK_UP
        || e.getKeyCode() == KeyEvent.VK_DOWN
        || e.getKeyCode() == KeyEvent.VK_LEFT
        || e.getKeyCode() == KeyEvent.VK_RIGHT) {
      if (this.ws.getCellAt(new Coord(col, row)) == null) {
        this.editingPanel.clearTextField();
      } else {
        String contentsOfSelectedCell = this.ws.getCellContents(new Coord(col, row));
        this.setContents(contentsOfSelectedCell);
      }
    }
  }
}