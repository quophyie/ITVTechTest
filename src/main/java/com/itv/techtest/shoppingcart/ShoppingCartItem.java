package com.itv.techtest.shoppingcart;


import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.item.LineItem;

/**
 * Created by dman on 31/10/2016.
 */
public class ShoppingCartItem {

  private LineItem lineItem;
  private int numberOfItems;

  public ShoppingCartItem(LineItem lineItem, int numberOfItems){
    this.lineItem = lineItem;
    this.numberOfItems = numberOfItems;
  };
  public LineItem getLineItem() {
    return lineItem;
  }

  public void setLineItem(LineItem lineItem) {
    this.lineItem = lineItem;
  }

  public int getNumberOfItems() {
    return numberOfItems;
  }

  public void setNumberOfItems(int numberOfItems) {
    this.numberOfItems = numberOfItems;
  }

}
