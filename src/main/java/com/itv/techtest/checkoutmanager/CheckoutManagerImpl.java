package com.itv.techtest.checkoutmanager;

import com.itv.techtest.shoppingcart.ShoppingCart;
import com.itv.techtest.price.manager.PriceManager;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.DefaultNoDiscountPricingRule;
import com.itv.techtest.price.rule.PricingRule;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dman on 31/10/2016.
 */
public class CheckoutManagerImpl implements CheckoutManager {


  private PriceManager pricingManager;

  public CheckoutManagerImpl(PriceManager priceManager) {
    this.pricingManager = priceManager;
  }


  @Override
  public List<PriceCalculationResult> checkout(ShoppingCart shoppingCart, List<PricingRule> pricingRules) {
    if(pricingRules == null)
      pricingRules = new ArrayList<>();

    List<PriceCalculationResult> priceCalculationResults = new ArrayList<>();
    priceCalculationResults.addAll(calculateItemsWithPricingRules(shoppingCart, pricingRules));
    priceCalculationResults.addAll(calculateItemsWithoutPricingRules(shoppingCart, pricingRules));

    return priceCalculationResults;
  }

  private  List<PriceCalculationResult> calculateItemsWithPricingRules(ShoppingCart shoppingCart, List<PricingRule> pricingRules) {

    List<PriceCalculationResult> priceCalculationResults = new ArrayList<>();
    List<String> skusOfItemsWithDiscountRules = shoppingCart.getAllShoppingCartItems()
        .keySet()
        .stream()
        .filter(sku -> pricingRules.stream().filter(pricingRule -> pricingRule.getSkus().contains(sku)).count() > 0)
        .collect(Collectors.toList());

    // Get all shoppingCart items that have a ruke associated with thwn
    shoppingCart.getAllShoppingCartItems()
        .entrySet()
        .stream()
        .filter(entry -> skusOfItemsWithDiscountRules.contains(entry.getKey()))
        .forEach(entry -> {
          List<PricingRule> pricingRulesToApply =  pricingRules.stream().filter(pricingRule -> pricingRule.getSkus().contains(entry.getKey())).collect(Collectors.toList());
          List<PriceCalculationResult> priceCalculationResultList = pricingManager.applyRules(entry.getValue(), pricingRulesToApply);
          priceCalculationResults.addAll(priceCalculationResultList);
        });

    return priceCalculationResults;

  }


  private  List<PriceCalculationResult> calculateItemsWithoutPricingRules(ShoppingCart shoppingCart, List<PricingRule> pricingRules) {

    List<PriceCalculationResult> priceCalculationResults = new ArrayList<>();
    Set<String> skusOfItemsWithOutDiscountRules = shoppingCart.getAllShoppingCartItems()
        .keySet()
        .stream()
        // Filter out SKUs that do not have a pricing rule in the pricing rules list
        .filter(sku -> pricingRules.stream().filter(pricingRule -> pricingRule.getSkus().contains(sku)).count() == 0)
        .collect(Collectors.toSet());


    // Get all shoppingCart items that do not have a pricing rule
    // These will use the pricing rule that DO NOT apply a discount i.e. the Default
    List<ShoppingCartItem> shoppingCartItems = shoppingCart.getAllShoppingCartItems()
        .entrySet()
        .stream()
        .filter(entry -> skusOfItemsWithOutDiscountRules.contains(entry.getKey()))
        .map(entry -> entry.getValue())
        .collect(Collectors.toList());

    // Apply the default pricing rules
    List<PricingRule> pricingRulesToApply = Arrays.asList(new DefaultNoDiscountPricingRule(skusOfItemsWithOutDiscountRules));
    shoppingCartItems.stream().forEach(shoppingCartItem ->  {
      List<PriceCalculationResult> priceCalculationResultList =  pricingManager.applyRules(shoppingCartItem, pricingRulesToApply);
      priceCalculationResults.addAll(priceCalculationResultList);
    });


    return priceCalculationResults;

  }
}
