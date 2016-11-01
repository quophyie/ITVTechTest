package com.itv.techtest.tests.price.rule;

import com.itv.techtest.exception.PricingRuleException;
import com.itv.techtest.exception.ShoppingCartException;
import com.itv.techtest.item.LineItem;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.MultiPriceByBundlePriceUnitRule;
import com.itv.techtest.price.rule.MultiPriceByPercentageDiscountRule;
import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.shoppingcart.ShoppingCartItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by dman on 01/11/2016.
 */
public class MultiPriceByPercentageDiscountTests {

  private PricingRule rule;
  private LineItem lineItem;
  private ShoppingCartItem shoppingCartItem;
  private final  int quantity = 3;
  private final String ruleDesc = "10% off 3 items";

  private Set<String> skus;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp(){
    lineItem = new LineItem("A", 50, "Some A Item");
    skus= new HashSet<>();
    skus.add(lineItem.getSku());
    rule =  new MultiPriceByPercentageDiscountRule(3, 10, skus, ruleDesc);;
    shoppingCartItem = new ShoppingCartItem(lineItem, quantity);
  }

  @Test
  public void givenAShoppingCartItemWith3ItemsShouldReturnAPriceCalculationResultWhereDiscountHasBeenAppliedToAll3Items() {
    PriceCalculationResult priceCalculationResult = rule.applyRule(shoppingCartItem);
    assertEquals(135.0, priceCalculationResult.getTotal().doubleValue());
    assertEquals(135.0, priceCalculationResult.getTotalOfDiscountedItems().doubleValue());
    assertEquals(0.0, priceCalculationResult.getTotalOfFullPriceItems().doubleValue());
    assertEquals(quantity, priceCalculationResult.getNumberOfItemsDiscounted());
    assertEquals(0, priceCalculationResult.getNumberOfItemsAtFullPrice());
  }

  @Test
  public void givenAShoppingCartItemWith5ItemsShouldReturnAPriceCalculationResultWhereDiscountHasBeenAppliedTo3ItemsOnly() {
    int newQuantity = 5;
    shoppingCartItem.setQuantity(newQuantity);
    PriceCalculationResult priceCalculationResult = rule.applyRule(shoppingCartItem);

    assertEquals(235.0, priceCalculationResult.getTotal().doubleValue());
    assertEquals(135.0, priceCalculationResult.getTotalOfDiscountedItems().doubleValue());
    assertEquals(100.0, priceCalculationResult.getTotalOfFullPriceItems().doubleValue());
    assertEquals(3, priceCalculationResult.getNumberOfItemsDiscounted());
    assertEquals(2, priceCalculationResult.getNumberOfItemsAtFullPrice());
  }

  @Test
  public void givenANullShoppingCartItemShouldThrowShoppingCartException() {
    thrown.expect(ShoppingCartException.class);
    thrown.expectMessage("shopping cart item cannot be null");

    rule.applyRule(null);
  }


  @Test
  public void givenAShoppingCartItemWithNullLineItemShouldThrowShoppingCartException() {
    thrown.expect(ShoppingCartException.class);
    thrown.expectMessage("shopping cart line item cannot be null");

    shoppingCartItem.setLineItem(null);

    rule.applyRule(shoppingCartItem);
  }

  @Test
  public void givenAShoppingCartItemWithNullLineItemShouldThrowPricingRuleException() {
    String newSku = "C";
    String message = String.format("Pricing Rule `%s` cannot be applied to SKU `%s`.\n" +
        "Please check the supported SKUs for rule `%s`",
        ruleDesc, shoppingCartItem.getLineItem().getSku(), ruleDesc);

    thrown.expect(PricingRuleException.class);
    thrown.expectMessage(message);

    Set<String> newSkus = new HashSet<>();
    newSkus.add(newSku);

    rule.setSkus(newSkus);
    rule.applyRule(shoppingCartItem);
  }
}
