package edu.cnm.deepdive.diceroller.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.cnm.deepdive.diceroller.logic.DiceRoller;
import edu.cnm.deepdive.diceroller.logic.RollResult;

@Singleton
public class DicerollerService {

  private final DiceRoller roller;

  @Inject
  public DicerollerService() {
    this.roller = new DiceRoller();
  }

  public RollResult roll(String expression) {
    return roller.roll(expression);
  }
}

