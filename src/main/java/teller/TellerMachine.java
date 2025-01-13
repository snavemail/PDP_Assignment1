package teller;

/**
 * This interface represents the operations of a src.main.teller machine.
 * A src.main.teller machine contains notes/coins of specific denominations.
 * It supports adding and withdrawing notes and coins of the denominations it supports.
 */
public interface TellerMachine {
  /**
   * Add notes/coins of the specified denomination to this src.main.teller.
   * @param deposit several pairs of (denomination,quantity) to be deposited.
   *                Denominators are in no specific order.
   *                For example, if 10 1s and 20 2s are to be deposited,
   *                then this method is called as obj.deposit(1,10,2,20)
   *                or obj.deposit(new int[]{1,10,2,20})
   *                A call to this method with no parameters does nothing.
   * @throws IllegalArgumentException if there are an odd number of numbers specified,
   * any denomination given is not supported by this src.main.teller, or
   * any quantity is negative
   */
  void deposit(int...deposit) throws IllegalArgumentException;

  /**
   * Withdraw the requested change from this src.main.teller.
   * The request can be fulfilled so long as the total amount
   * requested is at least the total amount in this src.main.teller.
   * The request cannot be fulfilled if an unsupported denomination
   * is part of the request or a negative quantity is requested.
   * An empty request has no effect.
   * If the requested quantity of a particular denomination
   * is not available, then the src.main.teller automatically makes change
   * by using bigger denominations available in the src.main.teller.
   * @param request several pairs of numbers (denomination, quantity) that
   *                represent the requested amount of change.

   *                Denominators are in no specific order.
   * @return true if the request can be fulfilled, false otherwise.
   */
  boolean withdraw(int...request);

  /**
   * Return the number of notes/coins in this src.main.teller of the specified denomination.
   * @param denomination the denomination whose quantity is requested
   * @return the quantity of the specified denomination in this src.main.teller. If the
   * denomination is not supported by this src.main.teller, this method returns 0
   */
  int getQuantity(int denomination);
}
