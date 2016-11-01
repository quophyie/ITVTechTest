package com.itv.techtest.tests.receipt;

import com.itv.techtest.checkoutmanager.CheckoutManager;
import com.itv.techtest.checkoutmanager.CheckoutManagerImpl;
import com.itv.techtest.item.LineItem;
import com.itv.techtest.price.manager.PriceManager;
import com.itv.techtest.price.manager.PriceManagerImpl;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.DefaultNoDiscountPricingRule;
import com.itv.techtest.price.rule.MultiPriceByBundlePriceUnitRule;
import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.receipt.Receipt;
import com.itv.techtest.receipt.ReceiptManager;
import com.itv.techtest.receipt.ReceiptManagerImpl;
import com.itv.techtest.shoppingcart.ShoppingCart;
import com.itv.techtest.shoppingcart.ShoppingCartImpl;
import com.itv.techtest.shoppingcart.ShoppingCartItem;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by dman on 31/10/2016.
 */

public class ReceiptManagerTests {


  private PriceManager priceManager;
  private Set<String> skus;
  private PricingRule defaultRule;
  private PricingRule multipriceRule;
  private ReceiptManager receiptManager;


  private LineItem lineItem;

  @Before
  public void setup(){

    lineItem = new LineItem("A", 50, "Some A Item");
    skus = new HashSet<>();
    skus.add(lineItem.getSku());

    priceManager = new PriceManagerImpl();
    receiptManager = new ReceiptManagerImpl();
    defaultRule = new DefaultNoDiscountPricingRule(new HashSet<>());
    multipriceRule = new MultiPriceByBundlePriceUnitRule(3, 130, skus, "3 for 130");

  }

  @Test
  public void givenAListOfPriceCalculationResultWithDefaultNoDiscountRuleReturnRecieptThatListsAllItemsAndTheTotal() {
    ShoppingCartItem shoppingCartItem = new ShoppingCartItem(lineItem, 5);
    List<PriceCalculationResult> priceCalculationResults = priceManager.applyRules(shoppingCartItem, Arrays.asList(defaultRule));
    Receipt result = receiptManager.generateReciept(priceCalculationResults);
    assertEquals(250.00, result.getTotal().doubleValue());
    assertEquals(1, result.getItems().size());

  }

  @Test
  public void givenPriceCalculationResultListWithMultipleRuleShouldReturnRecieptThatListsAllItemsAndTheTotalWithRuleApplied() {


    ShoppingCartItem shoppingCartItem = new ShoppingCartItem(lineItem, 4);
    List<PriceCalculationResult> priceCalculationResults = priceManager.applyRules(shoppingCartItem, Arrays.asList(multipriceRule));
    Receipt result = receiptManager.generateReciept(priceCalculationResults);
    assertEquals(180.00, result.getTotal().doubleValue());
    assertEquals(1, result.getItems().size());
  }

  @Test
  public void givenMultiplePriceCalculationResultShouldReturnRecieptThatContainsAllPriceCalculationResult() {

    ShoppingCartItem shoppingCartItem = new ShoppingCartItem(lineItem, 4);
    LineItem lineItemB =  new LineItem("B", 30, "Some B Item");

    ShoppingCartItem shoppingCartItemB = new ShoppingCartItem(lineItemB, 4);
    List<PriceCalculationResult> priceCalculationResults = priceManager.applyRules(shoppingCartItem, Arrays.asList(multipriceRule));
    List<PriceCalculationResult> priceCalculationResultsB = priceManager.applyRules(shoppingCartItemB, Arrays.asList(defaultRule));

    priceCalculationResults.addAll(priceCalculationResultsB);

    Receipt result = receiptManager.generateReciept(priceCalculationResults);
    assertEquals(300.00, result.getTotal().doubleValue());
    assertEquals(2, result.getItems().size());
  }



}

