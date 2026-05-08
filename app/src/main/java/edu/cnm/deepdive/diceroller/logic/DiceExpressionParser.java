package edu.cnm.deepdive.diceroller.logic;

import java.util.ArrayList;
import java.util.List;

public class DiceExpressionParser {

  public DiceExpression parse(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Expression cannot be null");
    }
    String expr = input.replaceAll("\\s+", ""); // strip whitespace
    if (expr.isEmpty()) {
      throw new IllegalArgumentException("Expression cannot be empty");
    }

    List<DiceTerm> terms = new ArrayList<>();
    int flatModifier = 0;

    int i = 0;
    int length = expr.length();

    while (i < length) {
      // 1. Sign
      char c = expr.charAt(i);
      int sign = 1;
      if (c == '+') {
        sign = 1;
        i++;
      } else if (c == '-') {
        sign = -1;
        i++;
      }

      if (i >= length) {
        throw new IllegalArgumentException("Dangling sign at end of expression: " + input);
      }

      // 2. Read number part (could be count for dice, or a flat modifier)
      int start = i;
      while (i < length && Character.isDigit(expr.charAt(i))) {
        i++;
      }
      String numberPart = expr.substring(start, i);

      if (i < length && (expr.charAt(i) == 'd' || expr.charAt(i) == 'D')) {
        // It's a dice term: [sign][numberPart]d[sides]
        i++; // skip 'd'
        if (i >= length || !Character.isDigit(expr.charAt(i))) {
          throw new IllegalArgumentException("Missing dice sides in: " + input);
        }
        int sidesStart = i;
        while (i < length && Character.isDigit(expr.charAt(i))) {
          i++;
        }
        String sidesPart = expr.substring(sidesStart, i);

        int count;
        if (numberPart.isEmpty()) {
          // "d20" means 1d20
          count = 1;
        } else {
          count = Integer.parseInt(numberPart);
        }
        int sides = Integer.parseInt(sidesPart);

        DiceTerm term = new DiceTerm(count, sides, sign);
        terms.add(term);

      } else {
        // It's a flat modifier: [sign][numberPart]
        if (numberPart.isEmpty()) {
          throw new IllegalArgumentException("Expected number or dice term at position " + start
              + " in expression: " + input);
        }
        int value = Integer.parseInt(numberPart) * sign;
        flatModifier += value;
      }
    }

    if (terms.isEmpty() && flatModifier == 0) {
      throw new IllegalArgumentException("No dice or modifiers found in expression: " + input);
    }

    return new DiceExpression(terms, flatModifier, input);
  }
}

