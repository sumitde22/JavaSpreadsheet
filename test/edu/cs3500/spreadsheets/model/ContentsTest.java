package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.Reference;
import edu.cs3500.spreadsheets.model.formula.functionobjects.Concat;
import edu.cs3500.spreadsheets.model.formula.functionobjects.LessThan;
import edu.cs3500.spreadsheets.model.formula.functionobjects.Product;
import edu.cs3500.spreadsheets.model.formula.functionobjects.Sum;
import edu.cs3500.spreadsheets.model.valuetype.BooleanValue;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.model.valuetype.StringValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing public methods in Contents interface.
 */
public class ContentsTest {

  private Contents a1Empty;
  private Formula a2String;
  private Formula a3Boolean;
  private Formula a4Number;
  private Formula a7ReferenceToNumber;
  private Formula b1SumNumbers;
  private Formula b2SumIndividualReference;
  private Formula b3SumMultiReference;
  private Formula b4SumNestedFunction;
  private Formula b5ProductNumbers;
  private Formula b6ProductInvidualReference;
  private Formula b7ProductMultiReference;
  private Formula b8ProductNestedFunction;
  private Formula b9LessThanNumbers;
  private Formula b10LessThanIndividualReference;
  private Formula b11LessThanNestedFunction;
  private Formula b12Concat;
  private Formula c1SumWithWeirdArgs;
  private Formula c2SumWithBlankArg;
  private Formula c5ProductWithWeirdArgs;
  private Formula c6ProductWithBlankArg;
  private Formula c9ProductNoNumbers;
  private Formula d1LessThanInvalidReference;
  private Formula d2LessThanNonExistentReference;
  private Map<Coord, Contents> data;

  @Before
  public void init() {
    a1Empty = new Blank();
    a2String = new StringValue("Hello world");
    a3Boolean = new BooleanValue(true);
    a4Number = new DoubleValue(3);
    Formula a5Number = new DoubleValue(4);
    Formula a6Number = new DoubleValue(5);
    a7ReferenceToNumber = new Reference(new Coord(1, 6));
    b1SumNumbers = new Sum(
        Arrays.asList(new DoubleValue(3), new DoubleValue(4), new DoubleValue(5)));
    b2SumIndividualReference = new Sum(Arrays
        .asList(new Reference(new Coord(1, 4)), new Reference(new Coord(1, 5)),
            new Reference(new Coord(1, 6))));
    b3SumMultiReference = new Sum(Arrays.asList(new Reference(new Coord(1, 4), new Coord(1, 6))));
    b4SumNestedFunction = new Sum(Arrays
        .asList(new Sum(Arrays.asList(new DoubleValue(3), new DoubleValue(4))),
            new DoubleValue(5)));
    b5ProductNumbers = new Product(
        Arrays.asList(new DoubleValue(3), new DoubleValue(4), new DoubleValue(5)));
    b6ProductInvidualReference = new Product(Arrays
        .asList(new Reference(new Coord(1, 4)), new Reference(new Coord(1, 5)),
            new Reference(new Coord(1, 6))));
    b7ProductMultiReference = new Product(
        Arrays.asList(new Reference(new Coord(1, 4), new Coord(1, 6))));
    b8ProductNestedFunction = new Product(
        Arrays.asList(b4SumNestedFunction, b7ProductMultiReference));
    b9LessThanNumbers = new LessThan(Arrays.asList(new DoubleValue(5), new DoubleValue(6)));
    b10LessThanIndividualReference = new LessThan(
        Arrays.asList(new Reference(new Coord(1, 6)), new Reference(new Coord(1, 5))));
    b11LessThanNestedFunction = new LessThan(Arrays
        .asList(new Sum(Arrays.asList(new Reference(new Coord(1, 4), new Coord(1, 6)))),
            new Product(Arrays.asList(new Reference(new Coord(1, 4), new Coord(1, 6))))));
    b12Concat = new Concat(
        Arrays.asList(new StringValue("Hi and "), new Reference(new Coord(1, 2))));
    c1SumWithWeirdArgs = new Sum(Arrays.asList(new Reference(new Coord(1, 2), new Coord(1, 4))));
    c2SumWithBlankArg = new Sum(Arrays.asList(new Reference(new Coord(1, 1), new Coord(1, 4))));
    Formula c3SumWithBoolean = new Sum(Arrays.asList(new BooleanValue(true), new DoubleValue(3)));
    Formula c4SumWithString = new Sum(
        Arrays.asList(new StringValue("Hello world"), new DoubleValue(3)));
    c5ProductWithWeirdArgs = new Product(
        Arrays.asList(new Reference(new Coord(1, 2), new Coord(1, 4))));
    c6ProductWithBlankArg = new Product(
        Arrays.asList(new Reference(new Coord(1, 1), new Coord(1, 4))));
    Formula c7ProductWithBoolean = new Product(
        Arrays.asList(new BooleanValue(true), new DoubleValue(3)));
    Formula c8ProductWithString = new Product(
        Arrays.asList(new StringValue("Hello world"), new DoubleValue(3)));
    c9ProductNoNumbers = new Product(
        Arrays.asList(new BooleanValue(true), new StringValue("Hello world")));
    d1LessThanInvalidReference = new LessThan(
        Arrays.asList(new Reference(new Coord(1, 6)), new Reference(new Coord(1, 3))));
    d2LessThanNonExistentReference = new LessThan(Arrays.asList(new DoubleValue(5),
        new Reference(new Coord(1, 10))));
    data = new HashMap<>();
    data.put(new Coord(1, 2), a2String);
    data.put(new Coord(1, 3), a3Boolean);
    data.put(new Coord(1, 4), a4Number);
    data.put(new Coord(1, 5), a5Number);
    data.put(new Coord(1, 6), a6Number);
    data.put(new Coord(1, 7), a7ReferenceToNumber);
    data.put(new Coord(2, 1), b1SumNumbers);
    data.put(new Coord(2, 2), b2SumIndividualReference);
    data.put(new Coord(2, 3), b3SumMultiReference);
    data.put(new Coord(2, 4), b4SumNestedFunction);
    data.put(new Coord(2, 5), b5ProductNumbers);
    data.put(new Coord(2, 6), b6ProductInvidualReference);
    data.put(new Coord(2, 7), b7ProductMultiReference);
    data.put(new Coord(2, 8), b8ProductNestedFunction);
    data.put(new Coord(2, 9), b9LessThanNumbers);
    data.put(new Coord(2, 10), b10LessThanIndividualReference);
    data.put(new Coord(2, 11), b11LessThanNestedFunction);
    data.put(new Coord(2, 12), b12Concat);
    data.put(new Coord(3, 1), c1SumWithWeirdArgs);
    data.put(new Coord(3, 2), c2SumWithBlankArg);
    data.put(new Coord(3, 3), c3SumWithBoolean);
    data.put(new Coord(3, 4), c4SumWithString);
    data.put(new Coord(3, 5), c5ProductWithWeirdArgs);
    data.put(new Coord(3, 6), c6ProductWithBlankArg);
    data.put(new Coord(3, 7), c7ProductWithBoolean);
    data.put(new Coord(3, 8), c8ProductWithString);
    data.put(new Coord(3, 9), c9ProductNoNumbers);
    data.put(new Coord(4, 1), d1LessThanInvalidReference);
    data.put(new Coord(4, 2), d2LessThanNonExistentReference);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidReference() {
    new Reference(new Coord(3, 3), new Coord(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSum() {
    new Sum(new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidProduct() {
    new Product(new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLessThanBadArgs() {
    new LessThan(Arrays.asList(new DoubleValue(5), new StringValue("oops")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLessThanMissingContents() {
    new LessThan(Arrays.asList(new DoubleValue(5), new Reference(new Coord(1, 1))))
        .evaluate(data, new HashMap<>());
  }

  @Test
  public void testGetReferencesBlank() {
    assertEquals(new ArrayList<>(), a1Empty.getReferences());
  }

  @Test
  public void testGetReferenceString() {
    assertEquals(new ArrayList<>(), a2String.getReferences());
  }

  @Test
  public void testGetReferenceBoolean() {
    assertEquals(new ArrayList<>(), a3Boolean.getReferences());
  }

  @Test
  public void testGetReferenceDouble() {
    assertEquals(new ArrayList<>(), a4Number.getReferences());
  }

  @Test
  public void testGetReferenceSingleReference() {
    assertEquals(new ArrayList<>(Arrays.asList(new Coord(1, 6))),
        a7ReferenceToNumber.getReferences());
  }

  @Test
  public void testGetReferencesSimpleFunction() {
    assertEquals(new ArrayList<>(), b1SumNumbers.getReferences());
  }

  @Test
  public void testGetReferencesMultipleSingleReferences() {
    assertEquals(new ArrayList<>(Arrays.asList(new Coord(1, 4), new Coord(1, 5), new Coord(1, 6))),
        b2SumIndividualReference.getReferences());
  }

  @Test
  public void testGetReferencesMultiReference() {
    assertEquals(new ArrayList<>(Arrays.asList(new Coord(1, 4), new Coord(1, 5), new Coord(1, 6))),
        b3SumMultiReference.getReferences());
  }

  @Test
  public void testGetReferencesComplexFunction() {
    Formula complexFormula = new Sum(
        Arrays.asList(c2SumWithBlankArg, b3SumMultiReference, new Reference(new Coord(1, 7))));
    assertEquals(new ArrayList<>(Arrays
        .asList(new Coord(1, 1), new Coord(1, 2), new Coord(1, 3), new Coord(1, 4), new Coord(1, 5),
            new Coord(1, 6), new Coord(1, 7))), complexFormula.getReferences());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateBlank() {
    a1Empty.evaluate(data, new HashMap<>());
  }

  @Test
  public void testEvaluateString() {
    assertEquals(new StringValue("Hello world"), a2String.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateBoolean() {
    assertEquals(new BooleanValue(true), a3Boolean.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateDouble() {
    assertEquals(new DoubleValue(3), a4Number.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSingleReference() {
    assertEquals(new DoubleValue(5), a7ReferenceToNumber.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSimpleSum() {
    assertEquals(new DoubleValue(12), b1SumNumbers.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSumWithSingleReferences() {
    assertEquals(new DoubleValue(12), b2SumIndividualReference.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSumWithMultiReference() {
    assertEquals(new DoubleValue(12), b3SumMultiReference.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSumWithNestedFunction() {
    assertEquals(new DoubleValue(12), b4SumNestedFunction.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSimpleProduct() {
    assertEquals(new DoubleValue(60), b5ProductNumbers.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateProductWithSingleReferences() {
    assertEquals(new DoubleValue(60), b6ProductInvidualReference.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateProductWithMultiReference() {
    assertEquals(new DoubleValue(60), b7ProductMultiReference.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateProductWithNestedFunction() {
    assertEquals(new DoubleValue(720), b8ProductNestedFunction.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSimpleLessThan() {
    assertEquals(new BooleanValue(true), b9LessThanNumbers.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateLessThanReference() {
    assertEquals(new BooleanValue(false),
        b10LessThanIndividualReference.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateLessThanComplexReference() {
    assertEquals(new BooleanValue(true), b11LessThanNestedFunction.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateConcat() {
    assertEquals(new StringValue("Hi and Hello world"), b12Concat.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSumWithIgnoredArgs() {
    assertEquals(new DoubleValue(3), c1SumWithWeirdArgs.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateSumWithBlankArgs() {
    assertEquals(new DoubleValue(3), c2SumWithBlankArg.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateProductWithIgnoredArgs() {
    assertEquals(new DoubleValue(3), c5ProductWithWeirdArgs.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateProductWithBlankArgs() {
    assertEquals(new DoubleValue(3), c6ProductWithBlankArg.evaluate(data, new HashMap<>()));
  }

  @Test
  public void testEvaluateProductWithNoNumbers() {
    assertEquals(new DoubleValue(0), c9ProductNoNumbers.evaluate(data, new HashMap<>()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateReferencesDontExist() {
    d1LessThanInvalidReference.evaluate(data, new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateNonexistentReference() {
    d2LessThanNonExistentReference.evaluate(data, new HashMap<>());
  }


}