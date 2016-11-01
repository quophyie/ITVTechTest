package com.itv.techtest.shoppingcart;

import com.itv.techtest.item.LineItem;

import java.util.*;

/**
 * Created by dman on 31/10/2016.
 */
public class ShoppingCartImpl implements ShoppingCart {

  private Map<String, ShoppingCartItem>  items = new HashMap<>();

  @Override
  public void addItem(LineItem lineItem) {
    String sku = lineItem.getSku().toUpperCase();
    items.putIfAbsent(sku, new ShoppingCartItem(lineItem, 0));
    items.computeIfPresent(sku.toUpperCase(), (key, shoppingCartItem) -> {
      int numOfItems = shoppingCartItem.getQuantity();
      shoppingCartItem.setQuantity(++numOfItems);
      return shoppingCartItem;
    } );

  }

  @Override
  public void removeItem(String sku) {
    ShoppingCartItem shoppingCartItem = items.get(sku.toUpperCase());
    if (shoppingCartItem !=null ){
       int quantity = shoppingCartItem.getQuantity();
       quantity--;

       if (quantity <= 0 ){
         items.remove(sku.toUpperCase());
       } else {
         shoppingCartItem.setQuantity(quantity);
       }
    }
  }

  @Override
  public Map<String, ShoppingCartItem> getAllShoppingCartItems() {
    return items;
  }

}
