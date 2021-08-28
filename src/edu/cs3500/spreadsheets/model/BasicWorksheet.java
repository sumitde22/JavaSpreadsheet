package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.Function;
import edu.cs3500.spreadsheets.model.formula.Reference;
import edu.cs3500.spreadsheets.model.valuetype.BooleanValue;
import edu.cs3500.spreadsheets.model.valuetype.DoubleValue;
import edu.cs3500.spreadsheets.model.valuetype.StringValue;
import edu.cs3500.spreadsheets.model.valuetype.Value;
import edu.cs3500.spreadsheets.sexp.FormulaBuilder;
import edu.cs3500.spreadsheets.sexp.Parser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of a model for a worksheet that stores data for cells and allows you to
 * evaluate the data.
 */
public class BasicWorksheet implements Worksheet {

  private final Map<Coord, Contents> cells;
  private final Map<Coord, String> errorCellsMap;
  private final Map<Coord, List<Coord>> referencesMap;
  private final Map<Coord, Set<Coord>> dependenciesMap;
  private final Map<Coord, String> stringRepMap;

  private final Map<Coord, Value> evaluatedValues;
  private final Map<Coord, String> errorsWhenEvaluating;

  /**
   * Constructs an instance of an empty worksheet.
   */
  public BasicWorksheet() {
    cells = new HashMap<>();
    errorCellsMap = new HashMap<>();
    referencesMap = new HashMap<>();
    dependenciesMap = new HashMap<>();
    stringRepMap = new HashMap<>();

    evaluatedValues = new HashMap<>();
    errorsWhenEvaluating = new HashMap<>();
  }

  @Override
  public void editCell(int col, int row, String contents) throws IllegalArgumentException {
    if (contents == null) {
      return;
    }

    clearCell(col, row);

    if (contents.equals("")) {
      return;
    }

    // throws illegal arg if invalid col/row
    Coord newPos = new Coord(col, row);

    String oldContents = contents;
    boolean isFormula = false;
    if (contents.charAt(0) == '=') {
      isFormula = true;
      contents = contents.substring(1);
    }

    Formula toAdd;
    try {
      // throws exception if syntactically incorrect string given
      // or if s-expression could not be converted to formula
      // or if formula is generated is malformed (i.e. less than with string arguments)
      toAdd = Parser.parse(contents).accept(new FormulaBuilder());
      if (isFormula && !toAdd.accept(new CanBeSavedAsFormula())) {
        throw new IllegalArgumentException("Contents could not be saved as formula");
      } else if (!isFormula && !toAdd.accept(new CanBeSavedAsValue())) {
        throw new IllegalArgumentException("Contents could not be saved as value");
      }
    } catch (Exception e) {
      stringRepMap.put(newPos, oldContents);
      errorCellsMap.put(newPos, e.getMessage());
      return;
    }

    cells.put(newPos, toAdd);
    List<Coord> referenceList = toAdd.getReferences();
    referencesMap.put(newPos, referenceList);
    for (Coord c : referenceList) {
      Set<Coord> newSet = dependenciesMap.getOrDefault(c, new HashSet<>());
      newSet.add(newPos);
      dependenciesMap.put(c, newSet);
    }
    stringRepMap.put(newPos, oldContents);
    evaluate(col, row);
    LinkedList<Coord> toChange = new LinkedList<>(getDependencies(col, row));
    HashSet<Coord> changedSoFar = new HashSet<>();
    changedSoFar.add(newPos);
    while (!toChange.isEmpty()) {
      Coord c = toChange.poll();
      if (!changedSoFar.contains(c)) {
        evaluatedValues.remove(c);
        errorsWhenEvaluating.remove(c);
        evaluate(c.col, c.row);
        toChange.addAll(getDependencies(c.col, c.row));
        changedSoFar.add(c);
      }
    }

  }

  @Override
  public void clearCell(int col, int row) throws IllegalArgumentException {
    // throws illegal arg if invalid col/row
    Coord newPos = new Coord(col, row);
    cells.remove(newPos);
    errorCellsMap.remove(newPos);
    if (referencesMap.containsKey(newPos)) {
      for (Coord c : referencesMap.get(newPos)) {
        dependenciesMap.get(c).remove(newPos);
      }
    }
    referencesMap.remove(newPos);
    stringRepMap.remove(newPos);
    evaluatedValues.remove(newPos);
    errorsWhenEvaluating.remove(newPos);
    LinkedList<Coord> toChange = new LinkedList<>(getDependencies(col, row));
    HashSet<Coord> changedSoFar = new HashSet<>();
    while (!toChange.isEmpty()) {
      Coord c = toChange.poll();
      if (!changedSoFar.contains(c)) {
        evaluatedValues.remove(c);
        errorsWhenEvaluating.remove(c);
        evaluate(c.col, c.row);
        toChange.addAll(getDependencies(c.col, c.row));
        changedSoFar.add(c);
      }
    }

  }

  @Override
  public String getExplicitContents(int col, int row) throws IllegalArgumentException {
    // will throw illegal arg if invalid coordinate
    Coord pos = new Coord(col, row);
    return stringRepMap.getOrDefault(pos, "");
  }

  private void evaluate(int col, int row) {
    // will throw illegal arg if invalid coordinate
    Coord pos = new Coord(col, row);
    if (errorCellsMap.containsKey(pos)) {
      return;
    }
    if (hasCycle(pos)) {
      errorsWhenEvaluating.put(pos, "Cycle!");
      return;
    }
    Contents toEval = cells.getOrDefault(pos, new Blank());
    // will throw illegal arg if cannot evaluate
    try {
      evaluatedValues.put(pos, toEval.evaluate(cells, new HashMap<>()));
    } catch (IllegalArgumentException e) {
      errorsWhenEvaluating.put(pos, e.getMessage());
    }
  }

  @Override
  public Value getEvaluatedContents(int col, int row) throws IllegalArgumentException {
    // will throw illegal arg if invalid coordinate
    Coord pos = new Coord(col, row);
    if (errorCellsMap.containsKey(pos)) {
      throw new IllegalArgumentException(errorCellsMap.get(pos));
    }

    if (errorsWhenEvaluating.containsKey(pos)) {
      throw new IllegalArgumentException(errorsWhenEvaluating.get(pos));
    }

    if (!cells.containsKey(pos)) {
      throw new IllegalArgumentException("Blank cell");
    }

    return evaluatedValues.get(pos);
  }

  private boolean hasCycle(Coord pos) {
    for (Coord c : referencesMap.getOrDefault(pos, new ArrayList<>())) {
      if (c.equals(pos)) {
        return true;
      }
    }
    HashSet<Coord> seenSoFar = new HashSet<>();
    seenSoFar.add(pos);
    HashSet<Coord> pathSoFar = new HashSet<>();
    pathSoFar.add(pos);
    for (Coord c : referencesMap.getOrDefault(pos, new ArrayList<>())) {
      if (hasCycleHelper(c, seenSoFar, pathSoFar)) {
        return true;
      }

    }
    return false;
  }

  private boolean hasCycleHelper(Coord pos, HashSet<Coord> seenSoFar, HashSet<Coord> pathSoFar) {
    if (pathSoFar.contains(pos)) {
      return true;
    }
    if (seenSoFar.contains(pos)) {
      return false;
    }

    seenSoFar.add(pos);
    pathSoFar.add(pos);
    for (Coord c : referencesMap.getOrDefault(pos, new ArrayList<>())) {
      if (hasCycleHelper(c, seenSoFar, pathSoFar)) {
        return true;
      }
    }
    pathSoFar.remove(pos);
    return false;
  }

  @Override
  public List<Coord> getFilledCells() {
    return new ArrayList<>(stringRepMap.keySet());
  }


  private List<Coord> getDependencies(int col, int row) throws IllegalArgumentException {
    return new ArrayList<>(dependenciesMap.getOrDefault(new Coord(col, row), new HashSet<>()));
  }


  /**
   * Helper function object to determine if given contents can be stored as value (without equal
   * sign).
   */
  private class CanBeSavedAsValue implements ContentsVisitor<Boolean> {

    @Override
    public Boolean visitBlank(Blank that) {
      return false;
    }

    @Override
    public Boolean visitBoolean(BooleanValue that) {
      return true;
    }

    @Override
    public Boolean visitDouble(DoubleValue that) {
      return true;
    }

    @Override
    public Boolean visitString(StringValue that) {
      return true;
    }

    @Override
    public Boolean visitReference(Reference that) {
      return false;
    }

    @Override
    public Boolean visitFunction(Function that) {
      return false;
    }
  }

  /**
   * Helper function object to determine if given contents can be stored as formula (with equal
   * sign).
   */
  private class CanBeSavedAsFormula implements ContentsVisitor<Boolean> {

    @Override
    public Boolean visitBlank(Blank that) {
      return false;
    }

    @Override
    public Boolean visitBoolean(BooleanValue that) {
      return true;
    }

    @Override
    public Boolean visitDouble(DoubleValue that) {
      return true;
    }

    @Override
    public Boolean visitString(StringValue that) {
      return true;
    }

    @Override
    public Boolean visitReference(Reference that) {
      return that.getReferences().size() == 1;
    }

    @Override
    public Boolean visitFunction(Function that) {
      return true;
    }
  }

}
