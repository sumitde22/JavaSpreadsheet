package edu.cs3500.spreadsheets;

import edu.cs3500.spreadsheets.controller.SpreadsheetController;
import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ViewModel;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.valuetype.PrintValue;
import edu.cs3500.spreadsheets.model.valuetype.Value;
import edu.cs3500.spreadsheets.provider.controller.Controller;
import edu.cs3500.spreadsheets.provider.view.IView;
import edu.cs3500.spreadsheets.provideradapters.ControllerAdapter;
import edu.cs3500.spreadsheets.provideradapters.ReadOnlyWorksheetAdapter;
import edu.cs3500.spreadsheets.provideradapters.WorksheetAdapter;
import edu.cs3500.spreadsheets.sexp.FormulaBuilder;
import edu.cs3500.spreadsheets.view.DisplayOnlyGUI;
import edu.cs3500.spreadsheets.view.EditableGUI;
import edu.cs3500.spreadsheets.view.TextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Main class to run Spreadsheet program through.
 */
public class BeyondGood {

  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    Worksheet model;
    WorksheetView view;
    WorksheetController controller;
    WorksheetBuilder<Worksheet> modelBuilder = new WorksheetBuilderImpl();
    if (args.length == 1 && args[0].equals("-gui")) {
      model = new BasicWorksheet();
      view = new DisplayOnlyGUI(new ViewModel(model));
      view.render();
      return;
    } else if (args.length == 1 && args[0].equals("-edit")) {
      model = new BasicWorksheet();
      view = new EditableGUI(new ViewModel(model));
      controller = new SpreadsheetController(model, modelBuilder, view);
      controller.launchView();
      return;
    } else if (args.length == 1 && args[0].equals("-provider")) {
      edu.cs3500.spreadsheets.provider.model.Worksheet<String> adaptedModel = new WorksheetAdapter(
          new BasicWorksheet());
      IView providerView = new edu.cs3500.spreadsheets.provider.view.EditableGUI(
          new ReadOnlyWorksheetAdapter(adaptedModel));
      Controller adaptedController = new ControllerAdapter(providerView, adaptedModel);
      adaptedController.useTable();
      return;
    } else if (args.length >= 2 && args[0].equals("-in")) {
      File file = new File(args[1]);
      BufferedReader reader;
      try {
        reader = new BufferedReader(new FileReader(file));
      } catch (FileNotFoundException e) {
        System.out.print("File not found");
        return;
      }
      try {
        model = WorksheetReader.read(new WorksheetBuilderImpl(), reader);
      } catch (IllegalStateException e) {
        System.out.print("Parser error");
        return;
      }

      if (args.length == 3 && args[2].equals("-gui")) {
        view = new DisplayOnlyGUI(new ViewModel(model));
        view.render();
        return;
      } else if (args.length == 3 && args[2].equals("-edit")) {
        view = new EditableGUI(new ViewModel(model));
        controller = new SpreadsheetController(model, modelBuilder, view);
        controller.launchView();
        return;
      } else if (args.length == 4 && args[2].equals("-eval")) {
        boolean allEvaluated = true;
        StringBuilder errorMessages = new StringBuilder();
        for (Coord c : model.getFilledCells()) {
          try {
            model.getEvaluatedContents(c.col, c.row);
          } catch (IllegalArgumentException e) {
            if (!allEvaluated) {
              errorMessages.append('\n');
            } else {
              allEvaluated = false;
            }
            errorMessages.append(String.format("Error in cell %s: %s", c, e.getMessage()));
          }
        }
        if (!allEvaluated) {
          System.out.print(errorMessages.toString());
          return;
        }

        if (!FormulaBuilder.isValidCellName(args[3])) {
          System.out.print("Invalid cell name");
        } else {
          Coord c = FormulaBuilder.stringToCoord(args[3]);
          try {
            Value value = model.getEvaluatedContents(c.col, c.row);
            System.out.print(value.accept(new PrintValue()));
          } catch (Exception e) {
            System.out.print("Error: " + e.getMessage());
          }
        }
        return;
      } else if (args.length == 4 && args[2].equals("-save")) {
        PrintWriter writer;
        try {
          writer = new PrintWriter(args[3]);
        } catch (FileNotFoundException e) {
          System.out.print("File not found!");
          return;
        }
        view = new TextualView(writer, new ViewModel(model));
        view.render();
        writer.close();
        return;
      } else if (args.length == 3 && args[2].equals("-provider")) {
        edu.cs3500.spreadsheets.provider.model.Worksheet<String> adaptedModel =
            new WorksheetAdapter(model);
        IView providerView = new edu.cs3500.spreadsheets.provider.view.EditableGUI(
            new ReadOnlyWorksheetAdapter(adaptedModel));
        Controller adaptedController = new ControllerAdapter(providerView, adaptedModel);
        adaptedController.useTable();
        return;
      }
    }

    System.out.print("Invalid command line!");
  }
}
