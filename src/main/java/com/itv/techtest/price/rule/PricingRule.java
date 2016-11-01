package com.itv.techtest.price.rule;

import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.util.Set;

/**
 * Created by dman on 31/10/2016.
 */
public interface PricingRule {
  PriceCalculationResult applyRule(ShoppingCartItem shoppingCartItem);
  Set<String> getSkus();
  void setSkus(Set<String> skus);
}
