import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test to test all public methods in LimitedTellerMachine which implements its methods from
 * TellerMachine Interface.
 */
public class LimitedTellerMachineTest {

  private LimitedTellerMachine tellerMachine;

  @Before
  public void setUp() {
    tellerMachine = new LimitedTellerMachine();
  }

  /**
   * Tests constructor. Ensures that it initializes the atm to have zeroed out quantities of all
   * valid denominations.
   */
  @Test
  public void constructor() {
    LimitedTellerMachine atm = new LimitedTellerMachine();
    assertEquals(0, atm.getQuantity(1));
    assertEquals(0, atm.getQuantity(5));
    assertEquals(0, atm.getQuantity(10));
    assertEquals(0, atm.getQuantity(20));


  }

  /**
   * Verifies deposit method throws an IllegalArgumentException
   * if there is a negative quantity.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeQuantity() {
    tellerMachine.deposit(10, 1, -1, 2);
  }

  /**
   * Verifies deposit method throws an IllegalArgumentException
   * if there is an invalid denomination.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDenomination() {
    tellerMachine.deposit(2, 0);
  }

  /**
   * Verifies deposit method throws an IllegalArgumentException
   * if there is an invalid denomination of 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testZeroDenomination() {
    tellerMachine.deposit(0, 1);
  }

  /**
   * Verifies deposit method throws an IllegalArgumentException
   * if there is an odd number of parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOddNumberOfParameters() {
    tellerMachine.deposit(10, 1, 10);
  }

  /**
   * Verifies that deposit will throw an IllegalArgumentException
   * if there are multiple invalid denominations.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMultipleInvalidDenominations() {
    tellerMachine.deposit(2, 1, 3, 1, 7, 3, 21, -4);
  }

  /**
   * Verifies that deposit will throw an IllegalArgumentException
   * if there are multiple invalid quantities.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMultipleInvalidQuantities() {
    tellerMachine.deposit(1, -5, 11, 8, 19, -4, 0, 0, 100, 100);
  }

  /**
   * Assures that an empty function call with no parameters with not throw an error
   * and update nothing.
   */
  @Test
  public void testNoParameters() {
    tellerMachine.deposit();
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  /**
   * Assures that depositing a valid denomination with no quantity adds nothing.
   */
  @Test
  public void testZeroQuantity() {
    tellerMachine.deposit(10, 0);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  /**
   * Tests a single deposit of denomination 1 with arbitrary quantity.
   */
  @Test
  public void testSingleDepositOne() {
    tellerMachine.deposit(1, 1);
    assertEquals(1, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  /**
   * Tests a single deposit of denomination 10 with arbitrary quantity.
   */
  @Test
  public void testSingleDepositTen() {
    tellerMachine.deposit(10, 3);
    assertEquals(3, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that multiple deposits in the same request works as expected. It should update all
   * of the quantities accordingly.
   */
  @Test
  public void testMultipleDeposit() {
    tellerMachine.deposit(10, 1, 20, 2);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(2, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that calling multiple deposits in a row works as expected.
   */
  @Test
  public void testDepositChain() {
    tellerMachine.deposit(10, 1, 20, 2);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(2, tellerMachine.getQuantity(20));

    tellerMachine.deposit(1, 3, 5, 2, 20, 3);
    assertEquals(3, tellerMachine.getQuantity(1));
    assertEquals(2, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(5, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that the order does not affect depositing.
   */
  @Test
  public void testMultipleDepositWeirdOrder() {
    tellerMachine.deposit(20, 1, 5, 2, 1, 7, 10, 2);
    assertEquals(7, tellerMachine.getQuantity(1));
    assertEquals(2, tellerMachine.getQuantity(5));
    assertEquals(2, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that depositing the same denomination in the same function call works as expected.
   * It should stack the deposits and add up all the quantities.
   */
  @Test
  public void testDepositWithSameDenomination() {
    tellerMachine.deposit(10, 2, 10, 5, 10, 3);
    assertEquals(10, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  /**
   * Tests depositing multiple valid denominations of varying valid quantities.
   */
  @Test
  public void testAllDenominationsDeposit() {
    tellerMachine.deposit(1, 1, 5, 2, 10, 5, 20, 1);
    assertEquals(1, tellerMachine.getQuantity(1));
    assertEquals(2, tellerMachine.getQuantity(5));
    assertEquals(5, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that a withdrawal of no parameter returns true, and the teller's cash remains the same.
   */
  @Test
  public void testEmptyWithdraw() {
    tellerMachine.deposit(10, 1);
    assertEquals(1, tellerMachine.getQuantity(10));
    assertTrue(tellerMachine.withdraw());
    assertEquals(1, tellerMachine.getQuantity(10));
    assertTrue(tellerMachine.withdraw(10, 1));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertTrue(tellerMachine.withdraw());
    assertEquals(0, tellerMachine.getQuantity(10));
  }

  /**
   * Tests a single withdrawal with sufficient funds and no conversion.
   */
  @Test
  public void testSingleWithdraw() {
    tellerMachine.deposit(10, 1);
    assertEquals(1, tellerMachine.getQuantity(10));
    assertTrue(tellerMachine.withdraw(10, 1));
    assertEquals(0, tellerMachine.getQuantity(10));
  }

  /**
   * Tests that withdraw works when there is no need for conversion, and there is sufficient funds.
   */
  @Test
  public void testNoConvertedDenominations() {
    tellerMachine.deposit(1, 5, 5, 5, 10, 5, 20, 5);
    assertTrue(tellerMachine.withdraw(1, 3, 5, 5, 10, 2, 20, 4));
    assertEquals(2, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(3, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that withdraw works when there is perfect change.
   */
  @Test
  public void testWithdrawPerfectChange() {
    tellerMachine.deposit(5, 10);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(10, tellerMachine.getQuantity(5));

    tellerMachine.withdraw(1, 50);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
  }

  /**
   * Tests a single withdraw that requires conversion. Makes sure that it returns true,
   * and that the remaining bills changes correctly.
   */
  @Test
  public void testComplexSingleWithdraw() {
    tellerMachine.deposit(10, 1, 20, 2);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(2, tellerMachine.getQuantity(20));
    assertTrue(tellerMachine.withdraw(1, 11));
    assertEquals(4, tellerMachine.getQuantity(1));
    assertEquals(1, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that multiple valid withdraws works as expected when there is no change needed.
   */
  @Test
  public void testMultipleWithdraw() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertEquals(100, tellerMachine.getQuantity(1));
    assertEquals(100, tellerMachine.getQuantity(5));
    assertEquals(100, tellerMachine.getQuantity(10));
    assertEquals(100, tellerMachine.getQuantity(20));

    assertTrue(tellerMachine.withdraw(1, 50, 5, 20, 10, 10, 20, 10));
    assertEquals(50, tellerMachine.getQuantity(1));
    assertEquals(80, tellerMachine.getQuantity(5));
    assertEquals(90, tellerMachine.getQuantity(10));
    assertEquals(90, tellerMachine.getQuantity(20));

    assertTrue(tellerMachine.withdraw(1, 20, 5, 17));
    assertEquals(30, tellerMachine.getQuantity(1));
    assertEquals(63, tellerMachine.getQuantity(5));
    assertEquals(90, tellerMachine.getQuantity(10));
    assertEquals(90, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that withdraw returns false if there is an invalid denomination,
   * and the teller remains the same.
   */
  @Test
  public void testWithdrawWithInvalidDenomination() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertFalse(tellerMachine.withdraw(11, 2));
    assertEquals(100, tellerMachine.getQuantity(1));
    assertEquals(100, tellerMachine.getQuantity(5));
    assertEquals(100, tellerMachine.getQuantity(10));
    assertEquals(100, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that withdraw returns false if there is an odd number of parameters,
   * and the teller remains the same.
   */
  @Test
  public void testWithdrawWithOddNumParameters() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertFalse(tellerMachine.withdraw(1));
    assertFalse(tellerMachine.withdraw(1, 100, 5));
    assertEquals(100, tellerMachine.getQuantity(1));
    assertEquals(100, tellerMachine.getQuantity(5));
    assertEquals(100, tellerMachine.getQuantity(10));
    assertEquals(100, tellerMachine.getQuantity(20));
  }

  /**
   * Verifies that withdraw returns false when any request has negative quantity,
   * and that the machine remains the same.
   */
  @Test
  public void testWithdrawWithNegativeQuantity() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertFalse(tellerMachine.withdraw(1, -10));
    assertFalse(tellerMachine.withdraw(1, 100, 5, -10));
    assertEquals(100, tellerMachine.getQuantity(1));
    assertEquals(100, tellerMachine.getQuantity(5));
    assertEquals(100, tellerMachine.getQuantity(10));
    assertEquals(100, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that a withdrawal of a valid denomination with a 0 quantity returns true
   * and that the number of bills remains the same.
   */
  @Test
  public void testWithdrawWithZeroDenomination() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertEquals(100, tellerMachine.getQuantity(20));
    assertTrue(tellerMachine.withdraw(20, 0));
    assertEquals(100, tellerMachine.getQuantity(20));
  }

  /**
   * Tests that withdraw returns false if there is insufficient funds.
   */
  @Test
  public void testWithdrawWithInsufficientFunds() {
    tellerMachine.deposit(10, 3);
    assertFalse(tellerMachine.withdraw(1, 31));
    assertEquals(3, tellerMachine.getQuantity(10));
  }

  /**
   * Tests that withdraw returns false if any of the requests cannot be fulfilled.
   */
  @Test
  public void testWithdrawWithInsufficientFunds2() {
    tellerMachine.deposit(1, 8, 5, 2, 10, 1, 20, 3);
    assertFalse(tellerMachine.withdraw(1, 1, 20, 4, 5, 1));
    assertEquals(3, tellerMachine.getQuantity(20));
    assertEquals(8, tellerMachine.getQuantity(1));
    assertEquals(2, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
  }

  /**
   * Tests that even if there is enough money, if you cannot make the change from a larger bill,
   * it returns false, and the money remains the same.
   */
  @Test
  public void testWithdrawWithInsufficientFunds3() {
    tellerMachine.deposit(1, 8, 5, 2, 10, 5, 20, 3);
    assertFalse(tellerMachine.withdraw(20, 4));
    assertEquals(3, tellerMachine.getQuantity(20));
  }


  /**
   * Tests the example given in the assignment. Tests that the order is correct, and that it
   * converts properly.
   */
  @Test
  public void testAssignmentExample() {
    tellerMachine.deposit(1, 3, 10, 1, 20, 2);
    assertEquals(3, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(2, tellerMachine.getQuantity(20));

    assertTrue(tellerMachine.withdraw(1, 5, 10, 1));
    assertEquals(3, tellerMachine.getQuantity(1));
    assertEquals(1, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

  /**
   * Tests the example given in the assignment but written in a different order.
   */
  @Test
  public void testAssignmentExample2() {
    tellerMachine.deposit(1, 3, 10, 1, 20, 2);
    assertEquals(3, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(2, tellerMachine.getQuantity(20));

    assertTrue(tellerMachine.withdraw(10, 1, 1, 5));
    assertEquals(3, tellerMachine.getQuantity(1));
    assertEquals(1, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

  /**
   * Tests the getQuantity method.
   *
   * Ensures it returns 0 for invalid denominations.
   * Ensures it returns 0 when constructor is called.
   * Ensures it returns the correct output after a deposit is made.
   */
  @Test
  public void getQuantity() {
    // Returns 0 if denomination is not valid.
    assertEquals(0, tellerMachine.getQuantity(2));
    assertEquals(0, tellerMachine.getQuantity(3));
    assertEquals(0, tellerMachine.getQuantity(4));
    assertEquals(0, tellerMachine.getQuantity(-1));
    assertEquals(0, tellerMachine.getQuantity(0));
    assertEquals(0, tellerMachine.getQuantity(100));

    // Initially the denominations are all set to 0.
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(20));

    // Adds 1 10 and 2 20s.
    tellerMachine.deposit(10, 1, 20, 2);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(2, tellerMachine.getQuantity(20));

    tellerMachine.deposit(5, 2, 20, 10);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(2, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(12, tellerMachine.getQuantity(20));
  }
}