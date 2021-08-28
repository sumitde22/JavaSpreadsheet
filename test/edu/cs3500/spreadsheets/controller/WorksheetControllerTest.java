package edu.cs3500.spreadsheets.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyWorksheet;
import edu.cs3500.spreadsheets.model.ViewModel;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.view.TextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.junit.Test;

/**
 * Tests to ensure that a correct sequence of events is triggered when a controller method is
 * called.
 */
public class WorksheetControllerTest {

  /**
   * Represents function object that represents an input/output action and applies corresponding
   * change to give input/output objects.
   */
  interface Interaction {

    /**
     * Adds this interaction to built-up input/output as deemed fit.
     *
     * @param inputs An Interaction may add new functions to this list to simulate an input action
     * @param output An interaction may add new text here to simulate expected output
     */
    void apply(List<TriggerFeatureFunction> inputs, StringBuilder output);
  }

  /**
   * Represents a function object that triggers (i.e. calls) a method on a controller.
   */
  interface TriggerFeatureFunction {

    /**
     * Calls appropriate method on controller.
     *
     * @param controller given WorksheetController to call function on
     */
    void applyAction(WorksheetController controller);
  }

  private void testRun(Interaction... interactions) {
    List<TriggerFeatureFunction> fakeUserInput = new LinkedList<>();
    StringBuilder expectedOutput = new StringBuilder();

    for (Interaction interaction : interactions) {
      interaction.apply(fakeUserInput, expectedOutput);
    }

    Appendable log = new StringBuilder();
    Worksheet mockModel = new MockWorksheetModel(log);
    WorksheetBuilder<Worksheet> mockModelBuilder = new MockWorksheetBuilder(log);
    WorksheetView mockView = new MockWorksheetView(log);
    WorksheetController controller = new SpreadsheetController(mockModel, mockModelBuilder,
        mockView);

    for (TriggerFeatureFunction f : fakeUserInput) {
      f.applyAction(controller);
    }

    assertEquals(expectedOutput.toString(), log.toString());
  }

  private static Interaction inputAction(TriggerFeatureFunction f) {
    Objects.requireNonNull(f);

    return ((inputs, output) -> inputs.add(f));
  }

  private static Interaction log(String... outputs) {
    Objects.requireNonNull(outputs);

    return (inputs, output) -> {
      for (String s : outputs) {
        output.append(s);
      }
    };
  }

  private static Interaction createModelSelecter(String fileName) {
    return inputAction(controller -> controller.setNewModel(fileName));
  }

  private static TriggerFeatureFunction cellSelecter(int col, int row) {
    return controller -> controller.selectCell(col, row);
  }

  private static TriggerFeatureFunction cellClearer(int col, int row) {
    return controller -> controller.clearCell(col, row);
  }

  private static TriggerFeatureFunction cellEditer(int col, int row, String contents) {
    return controller -> controller.editCell(col, row, contents);
  }

  private static TriggerFeatureFunction fileSave(String fileName) {
    return controller -> controller.saveToFile(fileName);
  }

  @Test
  public void testNormalSelectCell() {
    boolean doItForStyle = true;
    testRun(inputAction(cellSelecter(2, 1)),
        log("Unselect any cell if selected\n"
        + "Selected cell at row 1 column 2\n"
        + "Refreshed view to account for changes\n"));
    assertTrue(doItForStyle);
  }

  @Test
  public void testInvalidSelectCell() {
    boolean doItForStyle = true;
    testRun(inputAction(cellSelecter(-1, -1)));
    assertTrue(doItForStyle);
  }

  @Test
  public void testClearCell() {
    boolean doItForStyle = true;

    testRun(inputAction(cellClearer(2, 1)),
        log("Cleared cell at row 1 column 2\n"
        + "Unselect any cell if selected\n"
        + "Refreshed view to account for changes\n"));
    assertTrue(doItForStyle);
  }

  @Test
  public void testInvalidCell() {
    boolean doItForStyle = true;
    testRun(inputAction(cellClearer(-1, -1)));
    assertTrue(doItForStyle);
  }

  @Test
  public void testEditCell() {
    boolean doItForStyle = true;
    testRun(inputAction(cellEditer(2, 1, "5")),
        log("Edited cell at row 1 column 2 with contents 5\n"
            + "Unselect any cell if selected\n"
            + "Refreshed view to account for changes\n"));
    assertTrue(doItForStyle);
  }

  @Test
  public void testInvalidEditCell() {
    boolean doItForStyle = true;
    testRun(inputAction(cellEditer(-1, -1, "5")));
    assertTrue(doItForStyle);
  }

  @Test
  public void testMultipleControllerActions() {
    boolean doItForStyle = true;
    testRun(inputAction(cellClearer(2, 1)),
        log("Cleared cell at row 1 column 2\n"
            + "Unselect any cell if selected\n"
            + "Refreshed view to account for changes\n"),
        inputAction(cellSelecter(2, 1)),
        log("Unselect any cell if selected\n"
            + "Selected cell at row 1 column 2\n"
            + "Refreshed view to account for changes\n"),
        inputAction(cellEditer(2, 1, "5")),
        log("Edited cell at row 1 column 2 with contents 5\n"
            + "Unselect any cell if selected\n"
            + "Refreshed view to account for changes\n"));
    assertTrue(doItForStyle);

  }


  @Test
  public void testSaveFile() {
    Readable readable;
    try {
      readable
          = new FileReader(new File("resources/NonWorkingExampleDirectCycleInput.txt"));
    } catch (IOException e) {
      fail();
      return;
    }
    WorksheetBuilder<Worksheet> modelBuilder = new WorksheetBuilderImpl();
    Worksheet model = WorksheetReader.read(modelBuilder, readable);
    SpreadsheetController controller
        = new SpreadsheetController(model, modelBuilder,
        new TextualView(new StringBuilder(), new ViewModel(model)));
    controller.saveToFile("resources/saveTest.txt");
    Readable readingInSavedFile;
    try {
      readingInSavedFile = new FileReader(new File("resources/saveTest.txt"));
    } catch (IOException e) {
      fail();
      return;
    }
    Worksheet testModelToSeeSame
        = WorksheetReader.read(new WorksheetBuilderImpl(), readingInSavedFile);
    Appendable readInModel = new StringBuilder();
    Appendable savedModel = new StringBuilder();
    WorksheetView viewForReadInModel = new TextualView(readInModel, new ViewModel(model));
    viewForReadInModel.render();
    WorksheetView viewForSavedModel = new TextualView(savedModel,
        new ViewModel(testModelToSeeSame));
    viewForSavedModel.render();
    assertEquals(readInModel.toString(), savedModel.toString());
  }

  @Test
  public void testSetModel() {
    WorksheetBuilder<Worksheet> modelBuilder = new WorksheetBuilderImpl();
    Worksheet model = new BasicWorksheet();
    SpreadsheetController controller
        = new SpreadsheetController(model, modelBuilder,
        new TextualView(new StringBuilder(), new ViewModel(model)));
    ReadOnlyWorksheet newModel
        = controller.setNewModel("resources/NonWorkingExampleDirectCycleInput.txt");
    assertEquals(new ArrayList<>(Arrays.asList(new Coord(1, 1),
        new Coord(2, 1))), newModel.getFilledCells());
    assertEquals("=5", newModel.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(5), newModel.getEvaluatedContents(1, 1));
    assertEquals("=(SUM A1:B2)", newModel.getExplicitContents(2, 1));
    try {
      newModel.getEvaluatedContents(2, 1);
      fail();
    } catch (IllegalArgumentException e) {
      // exception thrown as expected
    }
  }
}