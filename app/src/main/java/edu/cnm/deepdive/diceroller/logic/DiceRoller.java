package edu.cnm.deepdive.diceroller.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DiceRoller {

  private final DiceExpressionParser parser = new DiceExpressionParser();

  public RollResult roll(String expression) {
    DiceExpression diceExpression = parser.parse(expression);
    return roll(diceExpression);
  }

  public RollResult roll(DiceExpression expression) {
    int total = 0;
    List<Integer> allRolls = new ArrayList<>();
    StringBuilder breakdownBuilder = new StringBuilder();

    boolean firstTerm = true;

    for (DiceTerm term : expression.getTerms()) {
      int sign = term.getSign();
      int count = term.getCount();
      int sides = term.getSides();

      List<Integer> termRolls = new ArrayList<>();
      int termSum = 0;

      for (int i = 0; i < count; i++) {
        int roll = ThreadLocalRandom.current().nextInt(1, sides + 1);
        termRolls.add(roll);
        termSum += roll;
      }

      int signedSum = termSum * sign;
      total += signedSum;
      allRolls.addAll(termRolls);

      // Build breakdown
      if (!firstTerm) {
        breakdownBuilder.append(sign > 0 ? " + " : " - ");
      } else if (sign < 0) {
        breakdownBuilder.append("-");
      }

      breakdownBuilder.append(term.getCount())
          .append("d")
          .append(term.getSides())
          .append(" [")
          .append(joinInts(termRolls))
          .append("]");

      firstTerm = false;
    }

    // Flat modifier
    int modifier = expression.getFlatModifier();
    if (modifier != 0) {
      if (modifier > 0) {
        breakdownBuilder.append(" + ").append(modifier);
      } else {
        breakdownBuilder.append(" - ").append(Math.abs(modifier));
      }
      total += modifier;
    }

    String breakdown = breakdownBuilder.toString();
    return new RollResult(total, allRolls, breakdown, expression.toString());
  }

  private String joinInts(List<Integer> ints) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ints.size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append(ints.get(i));
    }
    return sb.toString();
  }
}
