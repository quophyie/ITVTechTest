package com.itv.techtest.tests.price.rule;

import com.itv.techtest.exception.ShoppingCartException;
import com.itv.techtest.item.LineItem;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.DefaultNoDiscountPricingRule;
import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.shoppingcart.ShoppingCartItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sound.sampled.Line;
import java.util.HashSet;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by dman on 01/11/2016.
 */
public class DefaultNoDiscountPricingRuleTests {

  private PricingRule rule;
  private LineItem lineItem;
  private ShoppingCartItem shoppingCartItem;
  private final  int numberOfLineItems = 3;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp(){
    lineItem = new LineItem("A", 50, "Some A Item");
    rule = new DefaultNoDiscountPricingRule(new HashSet<>());
    shoppingCartItem = new ShoppingCartItem(lineItem, numberOfLineItems);
  }

  @Test
  public void givenAShoppingCartItemShouldReturnAPriceCalculationResultWhereRuleHasBeenApplied() {
    PriceCalculationResult priceCalculationResult = rule.applyRule(shoppingCartItem);
    assertEquals(150.0, priceCalculationResult.getTotal().doubleValue());
    assertEquals(0.0, priceCalculationResult.getTotalOfDiscountedItems().doubleValue());
    assertEquals(150.0, priceCalculationResult.getTotalOfFullPriceItems().doubleValue());
    assertEquals(0, priceCalculationResult.getNumberOfItemsDiscounted());
    assertEquals(numberOfLineItems, priceCalculationResult.getNumberOfItemsAtFullPrice());
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
}
