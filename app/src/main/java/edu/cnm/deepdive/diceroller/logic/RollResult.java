package edu.cnm.deepdive.diceroller.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RollResult {

  private final int total;
  private final List<Integer> individualRolls;
  private final String breakdown;
  private final String expression;

  public RollResult(int total, List<Integer> individualRolls,
      String breakdown, String expression) {
    this.total = total;
    this.individualRolls = new ArrayList<>(individualRolls);
    this.breakdown = breakdown;
    this.expression = expression;
  }

  public int getTotal() {
    return total;
  }

  public List<Integer> getIndividualRolls() {
    return Collections.unmodifiableList(individualRolls);
  }

  public String getBreakdown() {
    return breakdown;
  }

  public String getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    return expression + " => " + breakdown + " = " + total;
  }
}

