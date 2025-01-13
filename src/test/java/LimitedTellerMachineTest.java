import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LimitedTellerMachineTest {

  private LimitedTellerMachine tellerMachine;

  @Before
  public void setUp() {
    tellerMachine = new LimitedTellerMachine();
  }

  @Test
  public void constructor() {
    LimitedTellerMachine atm = new LimitedTellerMachine();
    assertEquals(0, atm.getQuantity(1));
    assertEquals(0, atm.getQuantity(5));
    assertEquals(0, atm.getQuantity(10));
    assertEquals(0, atm.getQuantity(20));
    assertEquals(0, atm.getQuantity(-1));
    assertEquals(0, atm.getQuantity(0));
    assertEquals(0, atm.getQuantity(100));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeQuantity() {
    tellerMachine.deposit(10, 1, -1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDenomination() {
    tellerMachine.deposit(2, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroDenomination() {
    tellerMachine.deposit(0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOddNumberOfParameters() {
    tellerMachine.deposit(10, 1, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultipleInvalidDenominations() {
    tellerMachine.deposit(2, 1, 3, 1, 7, 3, 21, -4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultipleInvalidDeposits() {
    tellerMachine.deposit(1, -5, 11, 8, 19, -4, 0, 0, 100, 100);
  }


  @Test
  public void testNoParameters() {
    tellerMachine.deposit();
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  @Test
  public void testZeroQuantity() {
    tellerMachine.deposit(10, 0);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  @Test
  public void testSingleDepositOne() {
    tellerMachine.deposit(1, 1);
    assertEquals(1, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  @Test
  public void testSingleDepositTen() {
    tellerMachine.deposit(10, 3);
    assertEquals(3, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  @Test
  public void testMultipleDeposit() {
    tellerMachine.deposit(10, 1, 20, 2);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(2, tellerMachine.getQuantity(20));
  }

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

  @Test
  public void testMultipleDepositWeirdOrder() {
    tellerMachine.deposit(20, 1, 5, 2, 1, 7, 10, 2);
    assertEquals(7, tellerMachine.getQuantity(1));
    assertEquals(2, tellerMachine.getQuantity(5));
    assertEquals(2, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }


  @Test
  public void testDepositWithSameDenomination() {
    tellerMachine.deposit(10, 2, 10, 5, 10, 3);
    assertEquals(10, tellerMachine.getQuantity(10));
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(0, tellerMachine.getQuantity(20));
  }

  @Test
  public void testAllDenominationsDeposit() {
    tellerMachine.deposit(1, 1, 5, 1, 10, 1, 20, 1);
    assertEquals(1, tellerMachine.getQuantity(1));
    assertEquals(1, tellerMachine.getQuantity(5));
    assertEquals(1, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

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

  @Test
  public void testSingleWithdraw() {
    tellerMachine.deposit(10, 1);
    assertEquals(1, tellerMachine.getQuantity(10));
    assertTrue(tellerMachine.withdraw(10, 1));
    assertEquals(0, tellerMachine.getQuantity(10));
  }

  @Test
  public void testNoConvertedDenominations() {
    tellerMachine.deposit(1, 5, 5, 5, 10, 5, 20, 5);
    assertTrue(tellerMachine.withdraw(1, 3, 5, 5, 10, 2, 20, 4));
    assertEquals(2, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
    assertEquals(3, tellerMachine.getQuantity(10));
    assertEquals(1, tellerMachine.getQuantity(20));
  }

  @Test
  public void testWithdrawPerfectChange() {
    tellerMachine.deposit(5, 10);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(10, tellerMachine.getQuantity(5));

    tellerMachine.withdraw(1, 50);
    assertEquals(0, tellerMachine.getQuantity(1));
    assertEquals(0, tellerMachine.getQuantity(5));
  }

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
    assertEquals(80, tellerMachine.getQuantity(10));
    assertEquals(90, tellerMachine.getQuantity(20));

    assertTrue(tellerMachine.withdraw(1, 20, 5, 17));
    assertEquals(30, tellerMachine.getQuantity(1));
    assertEquals(63, tellerMachine.getQuantity(5));
    assertEquals(80, tellerMachine.getQuantity(10));
    assertEquals(90, tellerMachine.getQuantity(20));
  }

  @Test
  public void testWithdrawWithInvalidDenomination() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertFalse(tellerMachine.withdraw(11, 2));
  }

  @Test
  public void testWithdrawWithOddNumParameters() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertFalse(tellerMachine.withdraw(1));
    assertFalse(tellerMachine.withdraw(1, 100, 5));
  }

  @Test
  public void testWithdrawWithNegativeQuantity() {
    tellerMachine.deposit(1, 100, 5, 100, 10, 100, 20, 100);
    assertFalse(tellerMachine.withdraw(1, -10));
    assertFalse(tellerMachine.withdraw(1, 100, 5, -10));
  }

  @Test
  public void testWithdrawWithZeroDenomination() {
    assertTrue(tellerMachine.withdraw(20, 0));
  }

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