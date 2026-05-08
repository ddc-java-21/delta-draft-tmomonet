package edu.cnm.deepdive.diceroller.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiceExpression {

  private final List<DiceTerm> terms;
  private final int flatModifier;
  private final String original;

  public DiceExpression(List<DiceTerm> terms, int flatModifier, String original) {
    this.terms = new ArrayList<>(terms);
    this.flatModifier = flatModifier;
    this.original = original;
  }

  public List<DiceTerm> getTerms() {
    return Collections.unmodifiableList(terms);
  }

  public int getFlatModifier() {
    return flatModifier;
  }

  public String getOriginal() {
    return original;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (DiceTerm term : terms) {
      String s = term.toString();
      if (first && s.startsWith("+")) {
        s = s.substring(1); // drop leading + for first term
      }
      sb.append(s);
      first = false;
    }
    if (flatModifier != 0) {
      if (flatModifier > 0) {
        sb.append("+").append(flatModifier);
      } else {
        sb.append(flatModifier); // already has '-'
      }
    }
    return sb.toString();
  }
}
