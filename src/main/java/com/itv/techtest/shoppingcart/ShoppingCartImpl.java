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
    /*items.putIfAbsent(sku, Arrays.asList(lineItem));
    items.computeIfPresent(sku.toUpperCase(), (key, list)-> {
      if (!list.contains(lineItem))
        list.add(lineItem);
      return list;
    });*/
    items.putIfAbsent(sku, new ShoppingCartItem(lineItem, 0));
    items.computeIfPresent(sku.toUpperCase(), (key, shoppingCartItem) -> {
      int numOfItems = shoppingCartItem.getNumberOfItems();
      shoppingCartItem.setNumberOfItems(++numOfItems);
      return shoppingCartItem;
    } );

  }

  @Override
  public Map<String, ShoppingCartItem> getAllShoppingCartItems() {
    return items;
  }

}
