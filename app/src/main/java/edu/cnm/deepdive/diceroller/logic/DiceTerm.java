package edu.cnm.deepdive.diceroller.logic;

public class DiceTerm {

  private final int count; // e.g. 2 in 2d6 (always positive)
  private final int sides; // e.g. 6 in 2d6
  private final int sign;  // +1 or -1 (for +2d6 vs -2d6)

  public DiceTerm(int count, int sides, int sign) {
    if (count <= 0) {
      throw new IllegalArgumentException("Dice count must be > 0");
    }
    if (sides <= 1) {
      throw new IllegalArgumentException("Dice sides must be > 1");
    }
    if (sign != 1 && sign != -1) {
      throw new IllegalArgumentException("Sign must be +1 or -1");
    }
    this.count = count;
    this.sides = sides;
    this.sign = sign;
  }

  public int getCount() {
    return count;
  }

  public int getSides() {
    return sides;
  }

  public int getSign() {
    return sign;
  }

  @Override
  public String toString() {
    String prefix = (sign < 0) ? "-" : "+";
    return prefix + count + "d" + sides;
  }
}

