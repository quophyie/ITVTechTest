package com.itv.techtest.shoppingcart;

import com.itv.techtest.item.LineItem;

import java.util.Map;

/**
 * Created by dman on 31/10/2016.
 */
public interface ShoppingCart {

  /**
   * Adds an item to the shoppping cart
   * @param item -  The item to be added
   */
  void addItem(LineItem item);

  /**
   * Removed an item from the cart
   * @param sku -  The SKU code of the item to be removed
   */
  void removeItem(String sku);

  /**
   * returns all the items in the shopping cart where each Key is the SKU Code
   * @return
   */
  Map<String, ShoppingCartItem> getAllShoppingCartItems();



}
