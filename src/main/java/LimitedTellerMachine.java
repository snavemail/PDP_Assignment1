import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import teller.TellerMachine;

public class LimitedTellerMachine implements TellerMachine {

  private final Map<Integer, Integer> cash;
  private static final int[] AVAILABLE_DENOMINATIONS = {1, 5, 10, 20};
  private static final int[] DENOMINATIONS_DESC = {20, 10, 5, 1};

  public LimitedTellerMachine() {
    cash = new HashMap<>();
    for (int denomination: AVAILABLE_DENOMINATIONS) {
      cash.put(denomination, 0);
    }
  }

  /**
   * Determines if the denomination is valid based on the AVAILABLE_DENOMINATIONS.
   * @param denomination the denomination to be checked.
   * @return a boolean stating if denomination is valid.
   */
  private boolean isInvalidDenomination(int denomination) {
    for (int availableDenomination: AVAILABLE_DENOMINATIONS) {
      if (availableDenomination == denomination) {
        return false;
      }
    }
    return true;
  }

  /**
   * Converts money in given cash to smaller denomination.
   * returns boolean to denote whether or not it was successful.
   * @param denomination the desired denomination to convert to.
   * @param amountNeeded the total cash you need (denomination * quantity).
   * @param tempCash the current cash you have.
   * @return true if you can convert it, false otherwise.
   */
  private boolean convertDenomination(int denomination, int amountNeeded,
      HashMap<Integer, Integer> tempCash) {
    System.out.println("Converting denomination " + denomination + " to amount needed "
        + amountNeeded);

    Map<Integer, Integer> neededDenominations = new HashMap<>();
    int numberNeeded = amountNeeded / denomination;
    neededDenominations.put(denomination, numberNeeded);
    int currentIndex = Arrays.stream(DENOMINATIONS_DESC).boxed().collect(Collectors.toList())
        .indexOf(denomination);

    for (int i = currentIndex; i >= 0; i--) {
      System.out.println(neededDenominations);
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
      System.out.println("in for loop " + i);
      int currentDenomination = DENOMINATIONS_DESC[i];
      int nextDenomination = DENOMINATIONS_DESC[i + 1];

      int nextAmountNeeded = neededDenominations.getOrDefault(nextDenomination, 0) * nextDenomination;
      if (nextAmountNeeded > 0) {
        System.out.println("NA " + nextDenomination + " " + currentDenomination + " " + nextAmountNeeded);
        System.out.println(nextDenomination);
        int quantityNeeded = (int) Math.ceil((double) nextAmountNeeded / currentDenomination);
        System.out.println(quantityNeeded);
        tempCash.put(currentDenomination, tempCash.getOrDefault(currentDenomination, 0) - quantityNeeded);
        int totalNextMade = (currentDenomination / nextDenomination) * quantityNeeded;
        System.out.println(totalNextMade);
        tempCash.put(nextDenomination, tempCash.getOrDefault(nextDenomination, 0) + totalNextMade);
        System.out.println(tempCash);
      }

    }
    tempCash.put(denomination, tempCash.getOrDefault(denomination, 0) - numberNeeded);
    System.out.println(tempCash + " Temp cash");
    return true;
  }

  private boolean withdrawDenomination(int denomination, int quantity,
      HashMap<Integer, Integer> tempCash) {

    System.out.println("Withdrawing denomination " + denomination + " to quantity " + quantity);
    int availableAmount = tempCash.getOrDefault(denomination, 0);

    if (availableAmount >= quantity) {
      System.out.println("Enough available denomination " + denomination);
      tempCash.put(denomination, availableAmount - quantity);
      return true;
    }

    int amountNeeded = denomination * (quantity - availableAmount);
    return convertDenomination(denomination, amountNeeded, tempCash);
  }

  @Override
  public boolean withdraw(int... request) {
    if (request.length == 0) return true;
    if (request.length % 2 != 0) return false;

    HashMap<Integer, Integer> tempCash = new HashMap<>(cash);
    HashMap<Integer, Integer> requestMap = new HashMap<>();

    for (int i = 0; i < request.length; i += 2) {
      int denomination = request[i];
      int quantity = request[i + 1];

      if (isInvalidDenomination(denomination) || quantity < 0) return false;
      requestMap.put(denomination, requestMap.getOrDefault(denomination, 0) + quantity);

    }
    for (int denomination: DENOMINATIONS_DESC) {
      int quantity = requestMap.getOrDefault(denomination, 0);
      if (quantity > 0) {
        boolean result = withdrawDenomination(denomination, quantity, tempCash);
        if (!result) return false;
      }
    }
    cash.clear();
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
