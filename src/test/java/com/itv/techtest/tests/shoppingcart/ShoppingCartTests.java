package com.itv.techtest.tests.shoppingcart;

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

public class ShoppingCartTests {


  private ShoppingCart shoppingCart;
  private LineItem lineItem;

  @Before
  public void setup(){
    shoppingCart = new ShoppingCartImpl();
    lineItem = new LineItem("A", 50, "Some A Item");
  }

  @Test
  public void givenAShoppingCartAddNewItemToShoppingCart() {
    assertEquals(0, shoppingCart.getAllShoppingCartItems().size());
    shoppingCart.addItem(lineItem);
    assertEquals(1, shoppingCart.getAllShoppingCartItems().size());
  }

  @Test
  public void givenAShoppingCartThatContainsAnItemRemoveItemFromCartAndTestSuccess() {
    shoppingCart.addItem(lineItem);
    assertEquals(1, shoppingCart.getAllShoppingCartItems().size());
    shoppingCart.removeItem(lineItem.getSku());
    assertEquals(0, shoppingCart.getAllShoppingCartItems().size());
  }



}

