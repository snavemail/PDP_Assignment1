package teller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that represents a limited teller machine. This machine only accepts these denominations:
 * {1, 5, 10, 20}.
 */
public class LimitedTellerMachine implements TellerMachine {

  private final Map<Integer, Integer> cash;
  private static final int[] DENOMINATIONS_DESC = {20, 10, 5, 1};

  /**
   * Constructor for limitedTellerMachine.
   * Sets all denominations to have quantity 0.
   */
  public LimitedTellerMachine() {
    cash = new HashMap<>();
    for (int denomination: DENOMINATIONS_DESC) {
      cash.put(denomination, 0);
    }
  }

  /**
   * Determines if the denomination is valid based on the AVAILABLE_DENOMINATIONS.
   * @param denomination the denomination to be checked.
   * @return a boolean stating if denomination is valid.
   */
  private boolean isInvalidDenomination(int denomination) {
    for (int availableDenomination: DENOMINATIONS_DESC) {
      if (availableDenomination == denomination) {
        return false;
      }
    }
    return true;
  }

  /**
   * Converts money in given cash (Map) to smaller denomination.
   * returns boolean to denote whether it was successful.
   * @param denomination the desired denomination to convert to.
   * @param amountNeeded the total cash you need (denomination * quantity).
   * @param tempCash the current cash you have.
   * @return true if you can convert it, false otherwise.
   */
  private boolean convertDenomination(int denomination, int amountNeeded,
      Map<Integer, Integer> tempCash) {
    if (amountNeeded == 0) {
      return false;
    }

    Map<Integer, Integer> neededDenominations = new HashMap<>();
    int numberNeeded = amountNeeded / denomination;
    neededDenominations.put(denomination, numberNeeded);
    int currentIndex = Arrays.stream(DENOMINATIONS_DESC).boxed().collect(Collectors.toList())
        .indexOf(denomination);

    for (int i = currentIndex; i >= 0; i--) {
      int largerIndex = i - 1;
      if (largerIndex < 0) {
        return false;
      }
      int largerDenomination = DENOMINATIONS_DESC[i - 1];
      int currentDenomination = DENOMINATIONS_DESC[i];
      int currentAmountNeeded = neededDenominations.getOrDefault(currentDenomination, 0)
          * currentDenomination;
      int largerDenominationQuantity = tempCash.getOrDefault(largerDenomination, 0);
      int totalAmount = largerDenomination * largerDenominationQuantity;

      if (totalAmount >= currentAmountNeeded) {
        break;
      } else {
        int neededQuantity =  (int) Math.ceil((double) (currentAmountNeeded - totalAmount)
            / largerDenomination);
        neededDenominations.put(largerDenomination, neededQuantity);
      }
    }
    for (int i = 0; i < DENOMINATIONS_DESC.length - 1; i++) {
      int currentDenomination = DENOMINATIONS_DESC[i];
      int nextDenomination = DENOMINATIONS_DESC[i + 1];

      int nextAmountNeeded =
          neededDenominations.getOrDefault(nextDenomination, 0) * nextDenomination;
      if (nextAmountNeeded >= 1) {
        int quantityNeeded = (int) Math.ceil((double) nextAmountNeeded / currentDenomination);
        tempCash.put(currentDenomination, tempCash.getOrDefault(currentDenomination, 0)
            - quantityNeeded);
        int totalNextMade = (currentDenomination / nextDenomination) * quantityNeeded;
        tempCash.put(nextDenomination, tempCash.getOrDefault(nextDenomination, 0)
            + totalNextMade);
      }
    }
    return true;
  }

  /**
   * Attempts to withdraw the denomination of a certain quantity.
   * If it can't, it will convert change.
   * @param denomination the denomination to be withdrawn.
   * @param quantity the number of bills to take out.
   * @param tempCash a map representing the current machine's inventory
   * @return a boolean representing whether cash can be taken out.
   */
  private boolean withdrawDenomination(int denomination, int quantity,
      Map<Integer, Integer> tempCash) {
    int availableAmount = tempCash.getOrDefault(denomination, 0);

    if (availableAmount >= quantity) {
      tempCash.put(denomination, availableAmount - quantity);
      return true;
    }

    int amountNeeded = denomination * (quantity - availableAmount);
    if (convertDenomination(denomination, amountNeeded, tempCash)) {
      tempCash.put(denomination, tempCash.get(denomination) - quantity);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean withdraw(int... request) {
    if (request.length == 0) {
      return true;
    }
    if (request.length % 2 != 0) {
      return false;
    }

    Map<Integer, Integer> tempCash = new HashMap<>(cash);
    Map<Integer, Integer> requestMap = new HashMap<>();

    for (int i = 0; i < request.length; i += 2) {
      int denomination = request[i];
      int quantity = request[i + 1];

      if (isInvalidDenomination(denomination) || quantity < 0) {
        return false;
      }
      requestMap.put(denomination, requestMap.getOrDefault(denomination, 0) + quantity);

    }
    for (int denomination: DENOMINATIONS_DESC) {
      int quantity = requestMap.getOrDefault(denomination, 0);
      if (quantity >= 1) {
        boolean result = withdrawDenomination(denomination, quantity, tempCash);
        if (!result) {
          return false;
        }
      }
    }
    cash.putAll(tempCash);
    return true;
  }

  @Override
  public void deposit(int... deposit) throws IllegalArgumentException {
    if (deposit.length % 2 != 0) {
      throw new IllegalArgumentException("Cannot have odd number of parameters");
    }

    for (int i = 0; i < deposit.length; i += 2) {
      int denomination = deposit[i];
      int quantity = deposit[i + 1];
      if (isInvalidDenomination(denomination)) {
        throw new IllegalArgumentException("Invalid denomination: " + denomination);
      }
      if (quantity < 0) {
        throw new IllegalArgumentException("Cannot be negative quantity: " + quantity);
      }
      cash.put(denomination, cash.get(denomination) + quantity);
    }
  }

  @Override
  public int getQuantity(int denomination) {
    return cash.getOrDefault(denomination, 0);
  }
}
