package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.ViewModel;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetBuilderImpl;
import edu.cs3500.spreadsheets.model.WorksheetReader;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the TextualView class that implements the WorksheetView interface.
 */
public class TextualViewTest {

  private Worksheet worksheet;
  private Appendable appendable;
  private TextualView view;

  @Before
  public void setup() {
    worksheet = new BasicWorksheet();
    appendable = new StringBuilder();
    view = new TextualView(appendable, new ViewModel(worksheet));
  }

  @Test
  public void editCellMalformedString1() {
    worksheet.editCell(1, 1, "=(");
    view.render();
    assertEquals("A1 =(", appendable.toString());
  }

  @Test
  public void editCellMalformedString2() {
    worksheet.editCell(1, 1, "=(");
    view.render();
    assertEquals("A1 =(", appendable.toString());
  }

  @Test
  public void editCellMalformedString3() {
    worksheet.editCell(1, 1, "=(SUM A1 A2) cfde");
    view.render();
    assertEquals("A1 =(SUM A1 A2) cfde", appendable.toString());
  }

  @Test
  public void editCellInvalidFunctionName() {
    worksheet.editCell(1, 1, "=(QUADRUPLE A2)");
    view.render();
    assertEquals("A1 =(QUADRUPLE A2)", appendable.toString());
  }

  @Test
  public void editCellLessThan1Arg() {
    worksheet.editCell(1, 1, "=(< A2)");
    view.render();
    assertEquals("A1 =(< A2)", appendable.toString());
  }

  @Test
  public void editCellCantSaveFormula() {
    worksheet.editCell(1, 1, "=A1:A2");
    view.render();
    assertEquals("A1 =A1:A2", appendable.toString());
  }

  @Test
  public void editCellCantSaveValueReference() {
    worksheet.editCell(1, 1, "A1");
    view.render();
    assertEquals("A1 A1", appendable.toString());
  }

  @Test
  public void editCellCantSaveValueFunction() {
    worksheet.editCell(1, 1, "SUM (A1 A2)");
    view.render();
    assertEquals("A1 SUM (A1 A2)", appendable.toString());
  }

  @Test
  public void editCellDirectReferenceCycle() {
    worksheet.editCell(1, 1, "=(SUM A1:B2)");
    view.render();
    assertEquals("A1 =(SUM A1:B2)", appendable.toString());
  }

  @Test
  public void editCellSumNoFormulas() {
    worksheet.editCell(1, 1, "=(SUM)");
    view.render();
    assertEquals("A1 =(SUM)", appendable.toString());
  }

  @Test
  public void editCellProductNoFormulas() {
    worksheet.editCell(1, 1, "=(PRODUCT)");
    view.render();
    assertEquals("A1 =(PRODUCT)", appendable.toString());
  }

  @Test
  public void editCellLessThanTwoBooleans() {
    worksheet.editCell(1, 1, "=(< true false)");
    view.render();
    assertEquals("A1 =(< true false)", appendable.toString());
  }

  @Test
  public void editCellLessThanTwoStrings() {
    worksheet.editCell(1, 1, "=(< \"hi\" \"bye\")");
    view.render();
    assertEquals("A1 =(< \"hi\" \"bye\")", appendable.toString());
  }

  @Test
  public void editCellLessThanOneBoolean() {
    worksheet.editCell(1, 1, "=(< 1 true)");
    view.render();
    assertEquals("A1 =(< 1 true)", appendable.toString());
  }

  @Test
  public void editCellLessThanOneString() {
    worksheet.editCell(1, 1, "=(< \"hi\" 2)");
    view.render();
    assertEquals("A1 =(< \"hi\" 2)", appendable.toString());
  }

  @Test
  public void editCellLessThanNoFormulas() {
    worksheet.editCell(1, 1, "=(<)");
    view.render();
    assertEquals("A1 =(<)", appendable.toString());
  }

  @Test
  public void editCellConcatNoFormulas() {
    worksheet.editCell(1, 1, "=(CONCAT)");
    view.render();
    assertEquals("A1 =(CONCAT)", appendable.toString());
  }

  @Test
  public void editCellConcatBools() {
    worksheet.editCell(1, 1, "=(CONCAT true false)");
    view.render();
    assertEquals("A1 =(CONCAT true false)", appendable.toString());
  }

  @Test
  public void editCellConcatDoubles() {
    worksheet.editCell(1, 1, "=(CONCAT 1 -2.5)");
    view.render();
    assertEquals("A1 =(CONCAT 1 -2.5)", appendable.toString());
  }

  @Test
  public void testEmptySpreadsheet() {
    view.render();
    assertEquals("", appendable.toString());
  }

  @Test
  public void testStringValue() {
    worksheet.editCell(1, 1, "\"hi\"");
    view.render();
    assertEquals("A1 \"hi\"", appendable.toString());
  }

  @Test
  public void testDoubleValue() {
    worksheet.editCell(2, 3, "12.5");
    view.render();
    assertEquals("B3 12.5", appendable.toString());
  }

  @Test
  public void testBooleanValue() {
    worksheet.editCell(3, 1, "true");
    view.render();
    assertEquals("C1 true", appendable.toString());
  }

  @Test
  public void testStringFormula() {
    worksheet.editCell(1, 1, "=\"hi\"");
    view.render();
    assertEquals("A1 =\"hi\"", appendable.toString());
  }

  @Test
  public void testDoubleFormula() {
    worksheet.editCell(1, 1, "=-10");
    view.render();
    assertEquals("A1 =-10", appendable.toString());
  }

  @Test
  public void testBooleanFormula() {
    worksheet.editCell(1, 1, "=false");
    view.render();
    assertEquals("A1 =false", appendable.toString());
  }

  @Test
  public void testReferenceSingleCell() {
    worksheet.editCell(1, 1, "true");
    worksheet.editCell(1, 2, "=A1");
    view.render();
    assertEquals("A2 =A1\n" +
        "A1 true", appendable.toString());
  }

  @Test
  public void testSum1StringValue() {
    worksheet.editCell(1, 1, "=(SUM \"hi\")");
    view.render();
    assertEquals("A1 =(SUM \"hi\")", appendable.toString());
  }

  @Test
  public void testSum1DoubleValue() {
    worksheet.editCell(1, 1, "=(SUM 123.5)");
    view.render();
    assertEquals("A1 =(SUM 123.5)", appendable.toString());
  }

  @Test
  public void testSum1BooleanValue() {
    worksheet.editCell(1, 1, "=(SUM false)");
    view.render();
    assertEquals("A1 =(SUM false)", appendable.toString());
  }

  @Test
  public void testSumMultipleMixedValues() {
    worksheet.editCell(1, 1, "=(SUM 1 2 true -4 false \"hi\")");
    view.render();
    assertEquals("A1 =(SUM 1 2 true -4 false \"hi\")", appendable.toString());
  }

  @Test
  public void testSumSingleReference() {
    worksheet.editCell(1, 1, "12");
    worksheet.editCell(2, 1, "=(SUM A1 2)");
    view.render();
    assertEquals("A1 12\n" +
        "B1 =(SUM A1 2)", appendable.toString());
  }

  @Test
  public void testSumMultipleReferences() {
    worksheet.editCell(1, 1, "12");
    worksheet.editCell(2, 1, "=(SUM A1 2)");
    worksheet.editCell(3, 1, "true");
    worksheet.editCell(4, 1, "=(SUM A1:C1 \"hi\")");
    view.render();
    assertEquals("A1 12\n" +
            "B1 =(SUM A1 2)\n" +
            "C1 true\n" +
            "D1 =(SUM A1:C1 \"hi\")",
        appendable.toString());
  }

  @Test
  public void testSumSameCell() {
    worksheet.editCell(1, 1, "12");
    worksheet.editCell(7, 2, "=(SUM A1 A1)");
    view.render();
    assertEquals("A1 12\n" +
        "G2 =(SUM A1 A1)", appendable.toString());
  }

  @Test
  public void testSumWithFunctions() {
    worksheet.editCell(1, 1, "=(SUM (SUM 1 2) true (SUM 3))");
    view.render();
    assertEquals("A1 =(SUM (SUM 1 2) true (SUM 3))", appendable.toString());
  }


  @Test
  public void testProduct1StringValue() {
    worksheet.editCell(1, 1, "=(PRODUCT \"hi\")");
    view.render();
    assertEquals("A1 =(PRODUCT \"hi\")", appendable.toString());
  }

  @Test
  public void testProduct1DoubleValue() {
    worksheet.editCell(1, 1, "=(PRODUCT 123.5)");
    view.render();
    assertEquals("A1 =(PRODUCT 123.5)", appendable.toString());
  }

  @Test
  public void testProduct1BooleanValue() {
    worksheet.editCell(1, 1, "=(PRODUCT false)");
    view.render();
    assertEquals("A1 =(PRODUCT false)", appendable.toString());
  }

  @Test
  public void testProductMultipleMixedValues() {
    worksheet.editCell(1, 1, "=(PRODUCT 1 2 true -4 false \"hi\")");
    view.render();
    assertEquals("A1 =(PRODUCT 1 2 true -4 false \"hi\")", appendable.toString());
  }

  @Test
  public void testProductSingleReference() {
    worksheet.editCell(1, 1, "12");
    worksheet.editCell(2, 1, "=(PRODUCT A1 2)");
    view.render();
    assertEquals("A1 12\n" +
        "B1 =(PRODUCT A1 2)", appendable.toString());
  }

  @Test
  public void testProductMultipleReferences() {
    worksheet.editCell(1, 1, "12");
    worksheet.editCell(2, 1, "=(PRODUCT A1 2)");
    worksheet.editCell(3, 1, "true");
    worksheet.editCell(4, 1, "=(PRODUCT A1:C1 \"hi\")");
    view.render();
    assertEquals("A1 12\n" +
            "B1 =(PRODUCT A1 2)\n" +
            "C1 true\n" +
            "D1 =(PRODUCT A1:C1 \"hi\")",
        appendable.toString());
  }

  @Test
  public void testProductSameCell() {
    worksheet.editCell(1, 1, "12");
    worksheet.editCell(7, 2, "=(PRODUCT A1 A1)");
    view.render();
    assertEquals("A1 12\n" +
        "G2 =(PRODUCT A1 A1)", appendable.toString());
  }

  @Test
  public void testProductWithFunctions() {
    worksheet.editCell(1, 1, "=(PRODUCT (SUM 1 2) true (PRODUCT 3))");
    view.render();
    assertEquals("A1 =(PRODUCT (SUM 1 2) true (PRODUCT 3))", appendable.toString());
  }

  @Test
  public void testLessThanEvalTrue() {
    worksheet.editCell(1, 1, "=(< 1 2)");
    view.render();
    assertEquals("A1 =(< 1 2)", appendable.toString());
  }

  @Test
  public void testLessThanEvalFalse() {
    worksheet.editCell(1, 1, "=(< 2 1)");
    view.render();
    assertEquals("A1 =(< 2 1)", appendable.toString());
  }

  @Test
  public void testLessThanReferences() {
    worksheet.editCell(1, 1, "1");
    worksheet.editCell(2, 1, "-1");
    worksheet.editCell(3, 1, "=(< A1 B1)");
    view.render();
    assertEquals("A1 1\n" +
        "B1 -1\n" +
        "C1 =(< A1 B1)", appendable.toString());
  }

  @Test
  public void testLessThanFunctions() {
    worksheet.editCell(1, 1, "=(< (SUM 1 2) (PRODUCT 1 0))");
    view.render();
    assertEquals("A1 =(< (SUM 1 2) (PRODUCT 1 0))", appendable.toString());
  }

  @Test
  public void testConcatOneString() {
    worksheet.editCell(1, 1, "=(CONCAT \"hi\")");
    view.render();
    assertEquals("A1 =(CONCAT \"hi\")", appendable.toString());
  }

  @Test
  public void testConcatMultipleStrings() {
    worksheet.editCell(1, 1, "=(CONCAT \"hi\" \"bye\")");
    view.render();
    assertEquals("A1 =(CONCAT \"hi\" \"bye\")", appendable.toString());
  }

  @Test
  public void testConcatSingleReference() {
    worksheet.editCell(1, 1, "\"hi\"");
    worksheet.editCell(1, 2, "=(CONCAT A1)");
    view.render();
    assertEquals("A2 =(CONCAT A1)\n" +
        "A1 \"hi\"", appendable.toString());
  }

  @Test
  public void testConcatMultipleReferences() {
    worksheet.editCell(1, 1, "123");
    worksheet.editCell(2, 1, "\"hi\"");
    worksheet.editCell(3, 1, "\"bye\"");
    worksheet.editCell(5, 1, "=(CONCAT A1:C1)");
    view.render();
    assertEquals("A1 123\n" +
        "B1 \"hi\"\n" +
        "C1 \"bye\"\n" +
        "E1 =(CONCAT A1:C1)", appendable.toString());
  }

  @Test
  public void testClearCell() {
    worksheet.editCell(1, 1, "true");
    Appendable appendableAfter = new StringBuilder();
    TextualView viewBefore = new TextualView(appendable, new ViewModel(worksheet));
    viewBefore.render();
    assertEquals("A1 true", appendable.toString());
    worksheet.clearCell(1, 1);
    TextualView viewAfter = new TextualView(appendableAfter, new ViewModel(worksheet));
    viewAfter.render();
    assertEquals("", appendableAfter.toString());
  }

  @Test
  public void testRoundtrip() {
    File file = new File("resources/WorkingExampleNotConcatInput.txt");
    Readable reader;
    try {
      reader = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      System.out.print("File not found");
      return;
    }
    WorksheetBuilderImpl worksheetBuilder = new WorksheetBuilderImpl();
    Worksheet w = WorksheetReader.read(worksheetBuilder, reader);

    Appendable renderedViewIn = new StringBuilder();

    TextualView viewReadIn = new TextualView(renderedViewIn, new ViewModel(w));
    viewReadIn.render();

    PrintWriter writer;
    try {
      writer = new PrintWriter("resources/WorkingExampleNotConcatSaved.txt");
    }
    catch (FileNotFoundException e) {
      System.out.print("File not found!");
      return;
    }

    TextualView renderedViewSaved;
    renderedViewSaved = new TextualView(writer, new ViewModel(w));
    renderedViewSaved.render();
    writer.close();

    File file2 = new File("resources/WorkingExampleNotConcatSaved.txt");
    Readable reader2;
    try {
      reader2 = new BufferedReader(new FileReader(file2));
    } catch (FileNotFoundException e) {
      System.out.print("File not found");
      return;
    }
    WorksheetBuilderImpl worksheetBuilder2 = new WorksheetBuilderImpl();
    Worksheet w2 = WorksheetReader.read(worksheetBuilder2, reader2);

    Appendable renderedViewIn2 = new StringBuilder();

    TextualView viewReadIn2 = new TextualView(renderedViewIn2, new ViewModel(w2));
    viewReadIn2.render();

    assertEquals(renderedViewIn2.toString(), renderedViewIn.toString());
  }



  @Test
  public void testRefreshDoesntDoAnything() {
    worksheet.editCell(1, 1, "123");
    worksheet.editCell(2, 1, "\"hi\"");
    worksheet.editCell(3, 1, "\"bye\"");
    worksheet.editCell(5, 1, "=(CONCAT A1:C1)");
    view.refresh();
    assertEquals("", appendable.toString());
    view.render();
    assertEquals("A1 123\n" +
        "B1 \"hi\"\n" +
        "C1 \"bye\"\n" +
        "E1 =(CONCAT A1:C1)", appendable.toString());
    view.refresh();
    assertEquals("A1 123\n" +
        "B1 \"hi\"\n" +
        "C1 \"bye\"\n" +
        "E1 =(CONCAT A1:C1)", appendable.toString());
  }



}
