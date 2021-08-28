package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.ReadOnlyWorksheet;
import edu.cs3500.spreadsheets.model.ViewModel;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.view.TextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Provides a set of functionalities for interacting with the given Worksheet through the given
 * WorksheetView.
 */
public class SpreadsheetController implements WorksheetController {

  private Worksheet model;
  private WorksheetBuilder<Worksheet> modelCreator;
  private WorksheetView view;

  /**
   * Constructs a controller that allows you to interact with Worksheet through given
   * WorksheetView.
   *
   * @param model the model that this controller will modify
   * @param view the view that this controller will adjust and redraw
   */
  public SpreadsheetController(Worksheet model,
      WorksheetBuilder<Worksheet> modelCreator, WorksheetView view) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(view);
    this.model = model;
    this.modelCreator = modelCreator;
    this.view = view;
    view.addFeatures(this);
  }

  public void launchView() {
    view.render();
  }

  @Override
  public ReadOnlyWorksheet setNewModel(String fileName) {
    File file = new File(fileName);
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid file name");
    }
    try {
      model = WorksheetReader.read(modelCreator, reader);
    } catch (IllegalStateException e) {
      throw new IllegalArgumentException("Model could not be instantiated");
    }
    return new ViewModel(model);
  }

  @Override
  public void selectCell(int col, int row) {
    if (col >= 1 && row >= 1) {
      view.unSelectCell();
      view.selectCell(col, row);
      view.refresh();
    }
  }

  @Override
  public void clearCell(int col, int row) {
    if (col >= 1 && row >= 1) {
      model.clearCell(col, row);
      view.unSelectCell();
      view.refresh();
    }
  }

  @Override
  public void editCell(int col, int row, String contents) {
    if (col >= 1 && row >= 1) {
      model.editCell(col, row, contents);
      view.unSelectCell();
      view.refresh();
    }
  }

  @Override
  public void saveToFile(String fileName) {
    PrintWriter writer;
    try {
      writer = new PrintWriter(fileName);
    } catch (FileNotFoundException e) {
      return;
    }
    TextualView savingFile = new TextualView(writer, new ViewModel(model));
    savingFile.render();
    writer.close();
  }
}
