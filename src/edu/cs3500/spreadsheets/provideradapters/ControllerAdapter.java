package edu.cs3500.spreadsheets.provideradapters;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.provider.controller.Controller;
import edu.cs3500.spreadsheets.provider.model.Worksheet;
import edu.cs3500.spreadsheets.provider.view.IView;
import java.io.File;

/**
 * Provides a set of functionalities for interacting with the given Worksheet through the given
 * IView.
 */
public class ControllerAdapter implements Controller {

  private final IView view;
  private final Worksheet<String> model;

  /**
   * Constructs a controller that allows you to interact with Worksheet through given
   * IView.
   *
   * @param model the model that this controller will modify
   * @param view the view that this controller will adjust and redraw
   */
  public ControllerAdapter(IView view, Worksheet<String> model) {
    this.view = view;
    this.model = model;
    view.addListener(this);
  }

  /**
   * Renders the controller's spreadsheet, so that the table can be used (and edited, if it is an
   * editable version of the view).
   */
  @Override
  public void useTable() {
    view.render();
  }

  /**
   * Update the model based on the given coordinates and the new contents.
   *
   * @param col the zero-indexed column of the cell to be updated.
   * @param row the zero-indexed row of the cell to be updated.
   * @param s the new contents of the cell.
   */
  @Override
  public void update(int col, int row, String s) {
    model.editCell(new Coord(col, row), s);
  }

  /**
   * Loads the given file chosen in the file chooser as an Editable GUI.
   *
   * @param selectedFile the selected file in the file chooser
   */
  @Override
  public void openFile(File selectedFile) {
    // extra credit, dont have to implement
  }

  /**
   * Saves the current worksheet as a new file with the given file name using a Textual View.
   *
   * @param selectedFile the user inputted file name
   */
  @Override
  public void saveFile(File selectedFile) {
    // extra credit, dont have to implement
  }
}
