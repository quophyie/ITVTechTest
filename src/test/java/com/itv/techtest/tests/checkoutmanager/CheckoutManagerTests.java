package com.itv.techtest.tests.checkoutmanager;

import com.itv.techtest.receipt.Receipt;
import com.itv.techtest.receipt.ReceiptManager;
import com.itv.techtest.receipt.ReceiptManagerImpl;
import com.itv.techtest.shoppingcart.ShoppingCart;
import com.itv.techtest.shoppingcart.ShoppingCartImpl;
import com.itv.techtest.checkoutmanager.CheckoutManager;
import com.itv.techtest.checkoutmanager.CheckoutManagerImpl;
import com.itv.techtest.item.LineItem;
import com.itv.techtest.price.manager.PriceManager;
import com.itv.techtest.price.manager.PriceManagerImpl;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.MultiPriceByBundlePriceUnitRule;
import com.itv.techtest.price.rule.PricingRule;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.*;

/**
 * Created by dman on 31/10/2016.
 */

public class CheckoutManagerTests {

  private CheckoutManager checkoutManager;
  private ShoppingCart shoppingCart;
  private PriceManager priceManager;
  private ReceiptManager receiptManager;
  private Set<String> skus;

  private LineItem lineItem;
  @Before
  public void setup(){
    receiptManager = new ReceiptManagerImpl();
    priceManager = new PriceManagerImpl();
    shoppingCart = new ShoppingCartImpl();
    checkoutManager = new CheckoutManagerImpl(priceManager, receiptManager);

    lineItem = new LineItem("A", 50, "Some A Item");
    shoppingCart.addItem(lineItem);
    shoppingCart.addItem(lineItem);
    shoppingCart.addItem(lineItem);

    skus = new HashSet<>();
    skus.add(lineItem.getSku());
  }

  @Test
  public void givenAShoppingCartWithMultiPriceRuleReturnListOfPriceCalculationResultWithSingleItem() {


    PricingRule multipriceRule = new MultiPriceByBundlePriceUnitRule(3, 130, skus, "3 for 130");
    List<PriceCalculationResult> results = checkoutManager.checkout(shoppingCart, Arrays.asList(multipriceRule));
    assertEquals(1, results.size());

  }

  @Test
  public void givenAShoppingCartContaining3ItemsWithMultiPriceRuleReturnListOfPriceCalculationResultWithSingleItemWherePricingDiscountRuleHasBeenAppliedTo3Items() {

    PricingRule multipriceRule = new MultiPriceByBundlePriceUnitRule(3, 130, skus, "3 for 130");

    List<PriceCalculationResult> results = checkoutManager.checkout(shoppingCart, Arrays.asList(multipriceRule));
    PriceCalculationResult priceCalculationResult = results.get(0);
    assertEquals(130.0, priceCalculationResult.getTotal().doubleValue());
    assertEquals(130.0, priceCalculationResult.getTotalOfDiscountedItems().doubleValue());
    assertEquals(3, priceCalculationResult.getNumberOfItemsDiscounted());
    assertEquals(0, priceCalculationResult.getNumberOfItemsAtFullPrice());

  }


  @Test
  public void givenAShoppingCartContaining3ItemsWithMultiPriceRuleReturnListOfPriceCalculationResultWithSingleItemWhereDiscountHasBeenAppliedTo3Items2ItemsWithoutDiscounts() {
    PricingRule multipriceRule = new MultiPriceByBundlePriceUnitRule(3, 130, skus, "3 for 130");
    shoppingCart.addItem(lineItem);
    shoppingCart.addItem(lineItem);

    List<PriceCalculationResult> results = checkoutManager.checkout(shoppingCart, Arrays.asList(multipriceRule));
    PriceCalculationResult priceCalculationResult = results.get(0);
    assertEquals(230.0, priceCalculationResult.getTotal().doubleValue());
    assertEquals(130.0, priceCalculationResult.getTotalOfDiscountedItems().doubleValue());
    assertEquals(100.0, priceCalculationResult.getTotalOfFullPriceItems().doubleValue());
    assertEquals(3, priceCalculationResult.getNumberOfItemsDiscounted());
    assertEquals(2, priceCalculationResult.getNumberOfItemsAtFullPrice());

  }

  @Test
  public void givenAShoppingCartWithoutPricingRulesDiscountsShouldNotBeAppliedToLineItems() {
    List<PriceCalculationResult> results = checkoutManager.checkout(shoppingCart, null);
    PriceCalculationResult priceCalculationResult = results.get(0);
    assertEquals(150.0, priceCalculationResult.getTotal().doubleValue());
    assertEquals(0.0, priceCalculationResult.getTotalOfDiscountedItems().doubleValue());
    assertEquals(150.0, priceCalculationResult.getTotalOfFullPriceItems().doubleValue());
    assertEquals(0, priceCalculationResult.getNumberOfItemsDiscounted());
    assertEquals(3, priceCalculationResult.getNumberOfItemsAtFullPrice());
  }

  @Test
  public void givenAShoppingCartWithDifferentSKUsAndDifferentPricingRulesDiscountsShouldBeAppliedToRespestiveSKUs() {
    LineItem  lineItemWithSKUB = new LineItem("B", 30, "Some B Item");
    shoppingCart.addItem(lineItemWithSKUB);
    shoppingCart.addItem(lineItemWithSKUB);
    shoppingCart.addItem(lineItemWithSKUB);


    shoppingCart.addItem(lineItem);
    shoppingCart.addItem(lineItem);

    PricingRule skuAMultipriceRule = new MultiPriceByBundlePriceUnitRule(3, 130, skus, "3 for 130");

    HashSet<String> skuBMultipriceRuleSKUs = new HashSet<>();
    skuBMultipriceRuleSKUs.add(lineItemWithSKUB.getSku());
    PricingRule skuBMultipriceRule = new MultiPriceByBundlePriceUnitRule(2, 45, skuBMultipriceRuleSKUs , "2 for 45");

    List<PriceCalculationResult> results = checkoutManager.checkout(shoppingCart, Arrays.asList(skuAMultipriceRule, skuBMultipriceRule));
    assertEquals(2, results.size());

    PriceCalculationResult priceCalculationResultSkuA = results.get(0);
    assertEquals(230.0, priceCalculationResultSkuA.getTotal().doubleValue());
    assertEquals(130.0, priceCalculationResultSkuA.getTotalOfDiscountedItems().doubleValue());
    assertEquals(100.0, priceCalculationResultSkuA.getTotalOfFullPriceItems().doubleValue());
    assertEquals(3, priceCalculationResultSkuA.getNumberOfItemsDiscounted());
    assertEquals(2, priceCalculationResultSkuA.getNumberOfItemsAtFullPrice());

    PriceCalculationResult priceCalculationResultSkuB = results.get(1);
    assertEquals(75.0, priceCalculationResultSkuB.getTotal().doubleValue());
    assertEquals(45.0, priceCalculationResultSkuB.getTotalOfDiscountedItems().doubleValue());
    assertEquals(30.0, priceCalculationResultSkuB.getTotalOfFullPriceItems().doubleValue());
    assertEquals(2, priceCalculationResultSkuB.getNumberOfItemsDiscounted());
    assertEquals(1, priceCalculationResultSkuB.getNumberOfItemsAtFullPrice());

  }

  @Test
  public void givenAShoppingCartShouldGenerateReceiptOfTransactions() {
    LineItem  lineItemWithSKUB = new LineItem("B", 30, "Some B Item");
    shoppingCart.addItem(lineItemWithSKUB);
    shoppingCart.addItem(lineItemWithSKUB);
    shoppingCart.addItem(lineItemWithSKUB);


    shoppingCart.addItem(lineItem);
    shoppingCart.addItem(lineItem);

    PricingRule skuAMultipriceRule = new MultiPriceByBundlePriceUnitRule(3, 130, skus, "3 for 130");

    HashSet<String> skuBMultipriceRuleSKUs = new HashSet<>();
    skuBMultipriceRuleSKUs.add(lineItemWithSKUB.getSku());
    PricingRule skuBMultipriceRule = new MultiPriceByBundlePriceUnitRule(2, 45, skuBMultipriceRuleSKUs , "2 for 45");

    List<PriceCalculationResult> results = checkoutManager.checkout(shoppingCart, Arrays.asList(skuAMultipriceRule, skuBMultipriceRule));

    Receipt receipt = checkoutManager.generateTransactionReciept(results);

    assertEquals(2, receipt.getItems().size());
    assertEquals(305.0, receipt.getTotal().doubleValue());
  }
}

