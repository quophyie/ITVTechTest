package com.itv.techtest.price.manager;

import com.itv.techtest.exception.ShoppingCartException;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dman on 31/10/2016.
 */
public class PriceManagerImpl implements PriceManager {

  @Override
  public List<PriceCalculationResult> applyRules(ShoppingCartItem shoppingCartItem, List<PricingRule> rules) {
    List<PriceCalculationResult> priceCalculationResults = new ArrayList<>();

    if (shoppingCartItem == null)
      throw new ShoppingCartException("shopping cart item cannot be null");

    if (shoppingCartItem.getLineItem() == null)
      throw new ShoppingCartException("shopping cart line item cannot be null");


    if (rules != null && !rules.isEmpty()) {
      rules.stream().forEach(rule -> {
        PriceCalculationResult priceCalculationResult = rule.applyRule(shoppingCartItem);
        priceCalculationResults.add(priceCalculationResult);
      });
    }

    return priceCalculationResults;
  }
}
