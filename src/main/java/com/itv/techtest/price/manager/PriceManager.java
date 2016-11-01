package com.itv.techtest.price.manager;

import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.util.List;

/**
 * This interface is responsible for applying pricing rules to a shopping cart item
 */
public interface PriceManager {
  List<PriceCalculationResult> applyRules(ShoppingCartItem shoppingCartItem, List<PricingRule> pricingRules);
}
