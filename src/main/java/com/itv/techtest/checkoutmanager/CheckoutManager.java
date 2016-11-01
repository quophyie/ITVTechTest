package com.itv.techtest.checkoutmanager;

import com.itv.techtest.shoppingcart.ShoppingCart;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.PricingRule;

import java.util.List;

/**
 * Created by dman on 31/10/2016.
 */
public interface CheckoutManager {

  List<PriceCalculationResult> checkout(ShoppingCart shoppingCart, List<PricingRule> pricingRules);
}
