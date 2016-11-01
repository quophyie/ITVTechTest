package com.itv.techtest.shoppingcart;

import com.itv.techtest.item.LineItem;

import java.util.Map;

/**
 * Created by dman on 31/10/2016.
 */
public interface ShoppingCart {

  /**
   * Adds an item to the basket
   * @param item -  The item to be added
   */
  void addItem(LineItem item);

  /**
   * returns all the items in the basket
   * @return
   */
  Map<String, ShoppingCartItem> getAllShoppingCartItems();

}
