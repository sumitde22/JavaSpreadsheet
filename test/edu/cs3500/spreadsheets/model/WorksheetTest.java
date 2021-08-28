package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import edu.cs3500.spreadsheets.model.valuetype.BooleanValue;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.model.valuetype.StringValue;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for worksheet class.
 */
public class WorksheetTest {

  private Worksheet worksheet;

  @Before
  public void setup() {
    worksheet = new BasicWorksheet();
  }

  private class Cell {

    private final int col;
    private final int row;
    private final String contents;

    private Cell(int col, int row, String contents) {
      this.col = col;
      this.row = row;
      this.contents = contents;
    }
  }

  private void testEditCellDoesntThrowExceptionButEvaluateDoes(Cell... cells) {
    for (Cell c : cells) {
      boolean exceptionThrown = false;
      try {
        worksheet.editCell(c.col, c.row, c.contents);
        assertTrue(worksheet.getFilledCells().contains(new Coord(c.col, c.row)));
      } catch (Exception e) {
        exceptionThrown = true;
      }
      assertFalse(exceptionThrown);
    }

    for (Cell c : cells) {
      boolean exceptionThrown = false;
      try {
        worksheet.getEvaluatedContents(c.col, c.row);
      } catch (Exception e) {
        exceptionThrown = true;
      }
      assertTrue(exceptionThrown);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void editCellInvalidCoordCol() {
    worksheet.editCell(0, 1, "hi");
  }

  @Test(expected = IllegalArgumentException.class)
  public void editCellInvalidCoordRow() {
    worksheet.editCell(1, 0, "hi");
  }

  @Test
  public void editCellMalformedString1() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=("));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(");
  }

  @Test
  public void editCellMalformedString2() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=)");
  }

  @Test
  public void editCellMalformedString3() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(SUM A1 A2) cfde"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(SUM A1 A2) cfde");
  }

  @Test
  public void editCellInvalidFunctionName() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(QUADRUPLE A2)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(QUADRUPLE A2)");
  }

  @Test
  public void editCellLessThan1Arg() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(< A2)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(< A2)");
  }

  // can't save a cell's contents as just a multi-reference
  @Test
  public void editCellCantSaveFormula() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=A1:A2"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=A1:A2");
  }

  @Test
  public void editCellCantSaveValueReference() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "A1"));
    assertEquals(worksheet.getExplicitContents(1, 1), "A1");
  }

  // test same as above but for Blank

  @Test
  public void editCellCantSaveValueFunction() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "SUM (A1 A2)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "SUM (A1 A2)");
  }

  // self-referential formula
  @Test
  public void editCellDirectReferenceCycle() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(SUM A1:B2)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(SUM A1:B2)");
  }

  // indirect self-referential formula
  @Test
  public void editCellInDirectReferenceCycle() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(SUM B1:B2)"),
        new Cell(2, 1, "=(SUM A1)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(SUM B1:B2)");
    assertEquals(worksheet.getExplicitContents(2, 1), "=(SUM A1)");
  }

  @Test
  public void editCellBlankValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "\"hello\"");
    worksheet.clearCell(1, 1);
    assertEquals("", worksheet.getExplicitContents(1, 1));
  }

  @Test
  public void editCellStringValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "\"hi\"");
    assertEquals("\"hi\"", worksheet.getExplicitContents(1, 1));
    assertEquals(new StringValue("hi"), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellDoubleValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "123");
    assertEquals("123", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(123), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellBooleanValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "true");
    assertEquals("true", worksheet.getExplicitContents(1, 1));
    assertEquals(new BooleanValue(true), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellStringFormula() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=\"hi\"");
    assertEquals("=\"hi\"", worksheet.getExplicitContents(1, 1));
    assertEquals(new StringValue("hi"), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellDoubleFormula() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=123.5");
    assertEquals("=123.5", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(123.5), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellBooleanFormula() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=false");
    assertEquals("=false", worksheet.getExplicitContents(1, 1));
    assertEquals(new BooleanValue(false), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellReferenceSingleCell() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(2, 2));
    worksheet.editCell(1, 1, "123");
    assertEquals("123", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(123), worksheet.getEvaluatedContents(1, 1));
    worksheet.editCell(2, 2, "=A1");
    assertEquals("=A1", worksheet.getExplicitContents(2, 2));
    assertEquals("123", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(123), worksheet.getEvaluatedContents(2, 2));
  }

  @Test
  public void editCellSum1StringValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(SUM \"hello\")");
    assertEquals("=(SUM \"hello\")", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(0), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellSum1DoubleValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(SUM 1)");
    assertEquals("=(SUM 1)", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(1), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellSum1BooleanValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(SUM true)");
    assertEquals("=(SUM true)", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(0), worksheet.getEvaluatedContents(1, 1));
  }

  // not correct type
  @Test
  public void editCellSumMultipleMixedValues() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(SUM 1 2 4.2 true \"hi\" 3)");
    assertEquals("=(SUM 1 2 4.2 true \"hi\" 3)", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(10.2), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellSumSingleReferenceOneCell() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(3, 3));
    worksheet.editCell(1, 1, "-5.2");
    assertEquals("-5.2", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(-5.2), worksheet.getEvaluatedContents(1, 1));
    worksheet.editCell(3, 3, "=(SUM A1)");
    assertEquals("=(SUM A1)", worksheet.getExplicitContents(3, 3));
    assertEquals(new DoubleValue(-5.2), worksheet.getEvaluatedContents(3, 3));
  }


  // formula with region references
  @Test
  public void editCellSumSingleReferencesMultipleCells() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 3));
    assertEquals("", worksheet.getExplicitContents(1, 4));
    assertEquals("", worksheet.getExplicitContents(1, 5));
    worksheet.editCell(1, 1, "1");
    assertEquals("1", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(1.0), worksheet.getEvaluatedContents(1, 1));
    worksheet.editCell(1, 2, "4.2");
    assertEquals("4.2", worksheet.getExplicitContents(1, 2));
    assertEquals(new DoubleValue(4.2), worksheet.getEvaluatedContents(1, 2));
    worksheet.editCell(1, 3, "false");
    assertEquals("false", worksheet.getExplicitContents(1, 3));
    assertEquals(new BooleanValue(false), worksheet.getEvaluatedContents(1, 3));
    worksheet.editCell(1, 4, "\"hello\""); // doesnt like strings
    assertEquals("\"hello\"", worksheet.getExplicitContents(1, 4));
    assertEquals(new StringValue("hello"), worksheet.getEvaluatedContents(1, 4));
    worksheet.editCell(1, 5, "=(SUM A1:A4)");
    assertEquals("=(SUM A1:A4)", worksheet.getExplicitContents(1, 5));
    assertEquals(new DoubleValue(5.2), worksheet.getEvaluatedContents(1, 5));
  }


  @Test
  public void editCellSumMultipleReferences() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 5));
    worksheet.editCell(1, 1, "1");
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 5, "=(SUM A1:A2 A1:A2)");
    assertEquals("=(SUM A1:A2 A1:A2)", worksheet.getExplicitContents(1, 5));
    assertEquals(new DoubleValue(10.4), worksheet.getEvaluatedContents(1, 5));
  }

  // same cell multiple times
  @Test
  public void editCellSumSameCell() {
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 5));
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 5, "=(SUM A2 A2)");
    assertEquals("=(SUM A2 A2)", worksheet.getExplicitContents(1, 5));
    assertEquals(new DoubleValue(8.4), worksheet.getEvaluatedContents(1, 5));
  }

  @Test
  public void editCellSumNoFormulas() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(SUM)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(SUM)");
  }

  @Test
  public void editCellSumSingleFunction() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 3));
    worksheet.editCell(1, 1, "-1");
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 3, "=(SUM (SUM A1:A2))");
    assertEquals("=(SUM (SUM A1:A2))", worksheet.getExplicitContents(1, 3));
    assertEquals(new DoubleValue(3.2), worksheet.getEvaluatedContents(1, 3));
  }

  @Test
  public void editCellSumMultipleFunctionsAndTypes() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 3));
    assertEquals("", worksheet.getExplicitContents(2, 1));
    worksheet.editCell(1, 1, "-1");
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 3, "=(SUM A1:A2)");
    worksheet.editCell(2, 1, "=(SUM (SUM A1 A2) (SUM 3 1 A3) 0 true A1:A2)");
    assertEquals("=(SUM (SUM A1 A2) (SUM 3 1 A3) 0 true A1:A2)",
        worksheet.getExplicitContents(2, 1));
    assertEquals(new DoubleValue(13.6), worksheet.getEvaluatedContents(2, 1));
  }

  @Test
  public void editCellProduct1StringValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(PRODUCT \"hello\")");
    assertEquals("=(PRODUCT \"hello\")", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(0), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellProduct1DoubleValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(PRODUCT 1)");
    assertEquals("=(PRODUCT 1)", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(1), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellProduct1BooleanValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(PRODUCT true)");
    assertEquals("=(PRODUCT true)", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(0), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellProductMultipleMixedValues() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(PRODUCT 1 2 4.2 true \"hi\" 3)");
    assertEquals("=(PRODUCT 1 2 4.2 true \"hi\" 3)", worksheet.getExplicitContents(1, 1));
    assertEquals(new DoubleValue(25.2), worksheet.getEvaluatedContents(1, 1));
  }

  @Test
  public void editCellProductSingleReferenceOneCell() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(3, 3));
    worksheet.editCell(1, 1, "-5.2");
    assertEquals("-5.2", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(3, 3, "=(PRODUCT A1)");
    assertEquals("=(PRODUCT A1)", worksheet.getExplicitContents(3, 3));
    assertEquals(new DoubleValue(-5.2), worksheet.getEvaluatedContents(3, 3));
  }

  @Test
  public void editCellProductSingleReferencesMultipleCells() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 3));
    assertEquals("", worksheet.getExplicitContents(1, 4));
    assertEquals("", worksheet.getExplicitContents(1, 5));
    worksheet.editCell(1, 1, "1");
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 3, "false");
    worksheet.editCell(1, 4, "\"hello\"");
    worksheet.editCell(1, 5, "=(PRODUCT A1:A4)");
    assertEquals("=(PRODUCT A1:A4)", worksheet.getExplicitContents(1, 5));
    assertEquals(new DoubleValue(4.2), worksheet.getEvaluatedContents(1, 5));
  }

  @Test
  public void editCellProductMultipleReferences() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 4));
    assertEquals("", worksheet.getExplicitContents(1, 5));
    worksheet.editCell(1, 1, "1");
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 3, "\"hello\"");
    worksheet.editCell(1, 4, "=A3");
    worksheet.editCell(1, 5, "=(PRODUCT A1:A4)");
    assertEquals("=(PRODUCT A1:A4)", worksheet.getExplicitContents(1, 5));
    assertEquals(new DoubleValue(4.2), worksheet.getEvaluatedContents(1, 5));
  }

  @Test
  public void editCellProductNoFormulas() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(PRODUCT)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(PRODUCT)");
  }

  @Test
  public void editCellProductSingleFunction() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 3));
    worksheet.editCell(1, 1, "-1");
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 3, "=(PRODUCT (PRODUCT A1:A2))");
    assertEquals("=(PRODUCT (PRODUCT A1:A2))", worksheet.getExplicitContents(1, 3));
    assertEquals(new DoubleValue(-4.2), worksheet.getEvaluatedContents(1, 3));
  }

  @Test
  public void editCellProductMultipleFunctionsAndTypes() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    assertEquals("", worksheet.getExplicitContents(1, 3));
    assertEquals("", worksheet.getExplicitContents(2, 1));
    worksheet.editCell(1, 1, "-1");
    worksheet.editCell(1, 2, "4.2");
    worksheet.editCell(1, 3, "=(PRODUCT A1:A2)");
    worksheet.editCell(2, 1, "=(PRODUCT (PRODUCT A1 A2) "
        + "(SUM 3 1 A3) 0 true A1:A2)");
    assertEquals("=(PRODUCT (PRODUCT A1 A2) (SUM 3 1 A3) 0 true A1:A2)",
        worksheet.getExplicitContents(2, 1));
    assertEquals(new DoubleValue(0), worksheet.getEvaluatedContents(2, 1));
  }

  @Test
  public void editCellLessThanTwoBooleans() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(< true false)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(< true false)");
  }

  @Test
  public void editCellLessThanTwoStrings() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(< \"hi\" \"bye\")"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(< \"hi\" \"bye\")");
  }

  @Test
  public void editCellLessThanOneBoolean() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(< 1 true)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(< 1 true)");
  }

  @Test
  public void editCellLessThanOneString() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(< \"hi\" 2)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(< \"hi\" 2)");
  }

  @Test
  public void editCellLessThanReturnFalse2DoubleValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(< 2 1)");
    assertEquals(new BooleanValue(false), worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(< 2 1)", worksheet.getExplicitContents(1, 1));
  }

  @Test
  public void editCellLessThanReturnTrue2DoubleValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(< 1 2)");
    assertEquals(new BooleanValue(true), worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(< 1 2)", worksheet.getExplicitContents(1, 1));
  }

  @Test
  public void editCellLessThanReturnFalse2DoubleValueSame() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "=(< 1 1)");
    assertEquals(new BooleanValue(false), worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(< 1 1)", worksheet.getExplicitContents(1, 1));
  }

  @Test
  public void editCellLessThanSingleReference() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 1, "1");
    worksheet.editCell(2, 1, "=A1");
    worksheet.editCell(3, 1, "=(< A1 B1)");
    assertEquals(new BooleanValue(false), worksheet.getEvaluatedContents(3, 1));
    assertEquals("=(< A1 B1)", worksheet.getExplicitContents(3, 1));
  }

  @Test
  public void editCellLessThanMultipleReferences() {
    worksheet.editCell(1, 1, "3");
    worksheet.editCell(1, 2, "false");
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 3, "(< A1:A2 2)"));
    assertEquals("(< A1:A2 2)", worksheet.getExplicitContents(1, 3));
  }

  @Test
  public void editCellLessThanFunctionAndValue() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(1, 2));
    worksheet.editCell(1, 1, "2");
    assertEquals(new DoubleValue(2), worksheet.getEvaluatedContents(1, 1));
    assertEquals("2", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(1, 2, "=(< A1 (SUM A1 3))");
    assertEquals(new BooleanValue(true), worksheet.getEvaluatedContents(1, 2));
    assertEquals("=(< A1 (SUM A1 3))", worksheet.getExplicitContents(1, 2));
  }

  @Test
  public void editCellLessThanComplicatedWorks() {
    assertEquals("", worksheet.getExplicitContents(1, 1));
    assertEquals("", worksheet.getExplicitContents(2, 1));
    assertEquals("", worksheet.getExplicitContents(3, 1));
    assertEquals("", worksheet.getExplicitContents(4, 1));
    worksheet.editCell(1, 1, "3");
    assertEquals(new DoubleValue(3), worksheet.getEvaluatedContents(1, 1));
    assertEquals("3", worksheet.getExplicitContents(1, 1));
    worksheet.editCell(2, 1, "1");
    assertEquals(new DoubleValue(1), worksheet.getEvaluatedContents(2, 1));
    assertEquals("1", worksheet.getExplicitContents(2, 1));
    worksheet.editCell(3, 1, "\"hello\"");
    assertEquals(new StringValue("hello"), worksheet.getEvaluatedContents(3, 1));
    assertEquals("\"hello\"", worksheet.getExplicitContents(3, 1));
    worksheet.editCell(4, 1, "=(< -1.5 (SUM (PRODUCT A1:C1) 2 (< 1 2)))");
    assertEquals(new BooleanValue(true), worksheet.getEvaluatedContents(4, 1));
    assertEquals("=(< -1.5 (SUM (PRODUCT A1:C1) 2 (< 1 2)))",
        worksheet.getExplicitContents(4, 1));
  }

  @Test
  public void editCellLessThanNoFormulas() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(<)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(<)");
  }

  @Test
  public void editCellConcatNoFormulas() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(CONCAT)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(CONCAT)");
  }

  @Test
  public void editCellConcatBools() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(CONCAT true false)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(CONCAT true false)");
  }

  @Test
  public void editCellConcatDoubles() {
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 1, "=(CONCAT 1 -2.5)"));
    assertEquals(worksheet.getExplicitContents(1, 1), "=(CONCAT 1 -2.5)");
  }

  @Test
  public void editCellConcat1Strings() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.editCell(1, 1, "=(CONCAT \"hi\")");
    assertEquals(new StringValue("hi"), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(CONCAT \"hi\")", this.worksheet.getExplicitContents(1, 1));
  }

  @Test
  public void editCellConcat2Strings() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.editCell(1, 1, "=(CONCAT \"hi\" \"bye\")");
    assertEquals(new StringValue("hibye"), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(CONCAT \"hi\" \"bye\")", this.worksheet.getExplicitContents(1, 1));
  }

  @Test
  public void editCellConcatSingleReference() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    assertEquals("", this.worksheet.getExplicitContents(1, 2));
    this.worksheet.editCell(1, 1, "\"hi\"");
    this.worksheet.editCell(1, 2, "=(CONCAT A1)");
    assertEquals(new StringValue("hi"), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("\"hi\"", this.worksheet.getExplicitContents(1, 1));
    assertEquals(new StringValue("hi"), this.worksheet.getEvaluatedContents(1, 2));
    assertEquals("=(CONCAT A1)", this.worksheet.getExplicitContents(1, 2));
  }

  @Test
  public void editCellConcatMultipleReferences() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    assertEquals("", this.worksheet.getExplicitContents(1, 2));
    assertEquals("", this.worksheet.getExplicitContents(1, 3));
    assertEquals("", this.worksheet.getExplicitContents(1, 4));
    this.worksheet.editCell(1, 1, "\"hi\"");
    assertEquals(new StringValue("hi"), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("\"hi\"", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.editCell(1, 2, "\" \"");
    assertEquals(new StringValue(" "), this.worksheet.getEvaluatedContents(1, 2));
    assertEquals("\" \"", this.worksheet.getExplicitContents(1, 2));
    this.worksheet.editCell(1, 3, "\"bye\"");
    assertEquals(new StringValue("bye"), this.worksheet.getEvaluatedContents(1, 3));
    assertEquals("\"bye\"", this.worksheet.getExplicitContents(1, 3));
    this.worksheet.editCell(1, 4, "=(CONCAT A1:A3)");
    assertEquals(new StringValue("hi bye"), this.worksheet.getEvaluatedContents(1, 4));
    assertEquals("=(CONCAT A1:A3)", this.worksheet.getExplicitContents(1, 4));
  }

  @Test
  public void editCellConcatReferencesDontHaveStrings() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    assertEquals("", this.worksheet.getExplicitContents(1, 2));
    assertEquals("", this.worksheet.getExplicitContents(1, 3));
    assertEquals("", this.worksheet.getExplicitContents(1, 4));
    this.worksheet.editCell(1, 1, "1");
    assertEquals(new DoubleValue(1.0), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("1", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.editCell(1, 2, "true");
    assertEquals(new BooleanValue(true), this.worksheet.getEvaluatedContents(1, 2));
    assertEquals("true", this.worksheet.getExplicitContents(1, 2));
    this.worksheet.editCell(1, 3, "=(< 4 3)");
    assertEquals(new BooleanValue(false), this.worksheet.getEvaluatedContents(1, 3));
    assertEquals("=(< 4 3)", this.worksheet.getExplicitContents(1, 3));
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(1, 4, "=(CONCAT A1:A3 \"hello\")"));
  }

  @Test
  public void editCellConcatConcatFunctions() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.editCell(1, 1, "=(CONCAT \"hello\" (CONCAT \" \" \"bye\"))");
    assertEquals(new StringValue("hello bye"), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(CONCAT \"hello\" (CONCAT \" \" \"bye\"))",
        this.worksheet.getExplicitContents(1, 1));
  }

  @Test
  public void editCellComplicatedValuesChangeProgressively() {
    worksheet.editCell(1, 1, "5");
    worksheet.editCell(2, 1, "=A1");
    worksheet.editCell(3, 1, "=B1");
    worksheet.editCell(4, 1, "=C1");
    worksheet.editCell(5, 1, "=D1");
    for (Coord c : worksheet.getFilledCells()) {
      assertEquals(worksheet.getEvaluatedContents(c.col, c.row), new DoubleValue(5));
    }
    worksheet.editCell(1, 1, "=B1");
    for (Coord c : worksheet.getFilledCells()) {
      try {
        System.out.println(worksheet.getEvaluatedContents(c.col, c.row));
        fail();
      } catch (Exception e) {
        // yay exception thrown
      }
    }
    worksheet.editCell(2,1, "=C1");
    for (Coord c : worksheet.getFilledCells()) {
      try {
        worksheet.getEvaluatedContents(c.col, c.row);
        fail();
      } catch (Exception e) {
        // yay exception thrown
      }
    }
    worksheet.editCell(3, 1, "=D1");
    for (Coord c : worksheet.getFilledCells()) {
      try {
        worksheet.getEvaluatedContents(c.col, c.row);
        fail();
      } catch (Exception e) {
        // yay exception thrown
      }
    }
    worksheet.editCell(4, 1, "=E1");
    for (Coord c : worksheet.getFilledCells()) {
      try {
        worksheet.getEvaluatedContents(c.col, c.row);
        fail();
      } catch (Exception e) {
        // yay exception thrown
      }
    }
    worksheet.editCell(5, 1, "5");
    for (Coord c : worksheet.getFilledCells()) {
      assertEquals(worksheet.getEvaluatedContents(c.col, c.row), new DoubleValue(5));
    }

  }

  @Test
  public void testEditCellWeirdOrder() {
    worksheet.editCell(1, 1, "=B2");
    worksheet.editCell(1, 3, "=(SUM B2 B4)");
    worksheet.editCell(1, 5, "=(PRODUCT B2 A3)");
    boolean exceptionThrown = false;
    try {
      worksheet.getEvaluatedContents(1, 1);
    } catch (IllegalArgumentException e) {
      exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
    assertEquals(worksheet.getEvaluatedContents(1, 3), new DoubleValue(0));
    assertEquals(worksheet.getEvaluatedContents(1, 5), new DoubleValue(0));
    worksheet.editCell(2, 2, "5");
    assertEquals(worksheet.getEvaluatedContents(1, 1), new DoubleValue(5));
    assertEquals(worksheet.getEvaluatedContents(1, 3), new DoubleValue(5));
    assertEquals(worksheet.getEvaluatedContents(1, 5), new DoubleValue(25));
    worksheet.editCell(2, 4, "2");
    assertEquals(worksheet.getEvaluatedContents(1, 1), new DoubleValue(5));
    assertEquals(worksheet.getEvaluatedContents(1, 3), new DoubleValue(7));
    assertEquals(worksheet.getEvaluatedContents(1, 5), new DoubleValue(35));
    worksheet.editCell(1, 3, "5");
    assertEquals(worksheet.getEvaluatedContents(1, 5), new DoubleValue(25));
    worksheet.clearCell(2,2);
    assertEquals(worksheet.getEvaluatedContents(1, 5), new DoubleValue(5));

  }

  @Test(expected = IllegalArgumentException.class)
  public void clearCellInvalidCoord() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.editCell(1, 1, "=(SUM 4 5)");
    assertEquals(new DoubleValue(9), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(SUM 4 5)", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.clearCell(0, 0);
  }

  @Test
  public void clearCellValid() {
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.editCell(1, 1, "=(SUM 4 5)");
    assertEquals(new DoubleValue(9), this.worksheet.getEvaluatedContents(1, 1));
    assertEquals("=(SUM 4 5)", this.worksheet.getExplicitContents(1, 1));
    this.worksheet.clearCell(1, 1);
    assertEquals("", this.worksheet.getExplicitContents(1, 1));
  }

  // test for an empty spreadsheet
  @Test
  public void getFilledCellsNoneFilled() {
    Worksheet blankWorksheet = new BasicWorksheet();
    assertEquals(new ArrayList<Coord>(), blankWorksheet.getFilledCells());
  }

  // test confirming all cells adding are there
  @Test
  public void getFilledCellsSomeFilled() {
    ArrayList<Coord> filledCells = new ArrayList<Coord>();
    assertEquals(new ArrayList<Coord>(), this.worksheet.getFilledCells());
    this.worksheet.editCell(1, 1, "123");
    this.worksheet.editCell(3, 3, "\"hello\"");
    this.worksheet.editCell(2, 2, "false");
    this.worksheet.editCell(1, 2, "=A1");
    this.worksheet.editCell(4, 4, "=(PRODUCT A1:C3)");
    filledCells.add(new Coord(1, 2));
    filledCells.add(new Coord(1, 1));
    filledCells.add(new Coord(3, 3));
    filledCells.add(new Coord(2, 2));
    filledCells.add(new Coord(4, 4));
    assertEquals("123", worksheet.getExplicitContents(1, 1));
    assertEquals("\"hello\"", worksheet.getExplicitContents(3, 3));
    assertEquals("false", worksheet.getExplicitContents(2, 2));
    assertEquals("=A1", worksheet.getExplicitContents(1, 2));
    assertEquals("=(PRODUCT A1:C3)", worksheet.getExplicitContents(4, 4));
    assertEquals(filledCells, this.worksheet.getFilledCells());
  }

  @Test
  public void getFilledCellsSomeFilledInvalidCellsShowUp() {
    ArrayList<Coord> filledCells = new ArrayList<Coord>();
    assertEquals(new ArrayList<Coord>(), this.worksheet.getFilledCells());
    this.worksheet.editCell(1, 1, "123");
    this.worksheet.editCell(3, 3, "\"hello\"");
    this.worksheet.editCell(2, 2, "false");
    this.worksheet.editCell(1, 2, "=A1");
    this.worksheet.editCell(4, 4, "=(PRODUCT A1:C3)");
    testEditCellDoesntThrowExceptionButEvaluateDoes(new Cell(5, 5, "=(< 5 true)"));
    filledCells.add(new Coord(1, 2));
    filledCells.add(new Coord(1, 1));
    filledCells.add(new Coord(3, 3));
    filledCells.add(new Coord(2, 2));
    filledCells.add(new Coord(4, 4));
    filledCells.add(new Coord(5, 5));
    assertEquals("123", worksheet.getExplicitContents(1, 1));
    assertEquals("\"hello\"", worksheet.getExplicitContents(3, 3));
    assertEquals("false", worksheet.getExplicitContents(2, 2));
    assertEquals("=A1", worksheet.getExplicitContents(1, 2));
    assertEquals("=(PRODUCT A1:C3)", worksheet.getExplicitContents(4, 4));
    assertEquals(filledCells, this.worksheet.getFilledCells());
  }

  @Test
  public void blankValueToString() {
    assertEquals("", new Blank().toString());
  }

  @Test
  public void stringValueToString() {
    assertEquals("hello", new StringValue("hello").toString());
  }

  @Test
  public void booleanValueToString() {
    assertEquals("false", new BooleanValue(false).toString());
  }

  @Test
  public void doubleValueToString() {
    assertEquals("123.0", new DoubleValue(123).toString());
  }

}



