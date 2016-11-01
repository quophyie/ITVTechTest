package com.itv.techtest.price.rule;

import com.itv.techtest.exception.ShoppingCartException;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.Set;

/**
 * This is the default Rule applied
 */
public class DefaultNoDiscountPricingRule implements PricingRule {
  private  Set<String> skus;

  public DefaultNoDiscountPricingRule( Set<String> skus) {
    this.skus = skus;

  }
  @Override
  public PriceCalculationResult applyRule(ShoppingCartItem shoppingCartItem) {

    if (shoppingCartItem == null)
      throw new ShoppingCartException("shopping cart item cannot be null");

    if (shoppingCartItem.getLineItem() == null)
      throw new ShoppingCartException("shopping cart line item cannot be null");

    int numberOfItemsToBeDiscounted = 0;
    int numberOfItemsChargedAtFullPrice = shoppingCartItem.getNumberOfItems();

    BigDecimal total = new BigDecimal(numberOfItemsChargedAtFullPrice * shoppingCartItem.getLineItem().getPrice());
    BigDecimal totalDiscountedItems = new BigDecimal(0);

    PriceCalculationResult priceCalculationResult = new PriceCalculationResult(numberOfItemsToBeDiscounted,
        numberOfItemsChargedAtFullPrice,
        total,
        totalDiscountedItems,
        total,
        shoppingCartItem.getLineItem().getSku());

    return priceCalculationResult;
  }

  @Override
  public Set<String> getSkus() {
    return skus;
  }

  @Override
  public void setSkus(Set<String> skus) {
    this.skus = skus;
  }
}
