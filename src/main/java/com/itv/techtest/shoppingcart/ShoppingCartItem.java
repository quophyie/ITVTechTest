package com.itv.techtest.shoppingcart;

import com.itv.techtest.item.LineItem;

/**
 * Created by dman on 31/10/2016.
 */
public class ShoppingCartItem {

  private LineItem lineItem;
  private int quantity;

  public ShoppingCartItem(LineItem lineItem, int quantity){
    this.lineItem = lineItem;
    this.quantity = quantity;
  }

  public LineItem getLineItem() {
    return lineItem;
  }

  public void setLineItem(LineItem lineItem) {
    this.lineItem = lineItem;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

}
