package com.itv.techtest.price.manager;

import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.item.LineItem;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.util.List;

/**
 * Created by dman on 31/10/2016.
 */
public interface PriceManager {

  List<PriceCalculationResult> applyRules(ShoppingCartItem shoppingCartItem, List<PricingRule> rules);
}
