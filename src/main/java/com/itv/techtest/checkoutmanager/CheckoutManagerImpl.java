package com.itv.techtest.checkoutmanager;

import com.itv.techtest.receipt.Receipt;
import com.itv.techtest.receipt.ReceiptManager;
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
  private ReceiptManager receiptManager;

  public CheckoutManagerImpl(PriceManager priceManager, ReceiptManager receiptManager) {
    this.pricingManager = priceManager;
    this.receiptManager = receiptManager;
  }


  @Override
  public List<PriceCalculationResult> checkout(ShoppingCart shoppingCart, List<PricingRule> pricingRules) {
    if(pricingRules == null)
      pricingRules = new ArrayList<>();

    List<PriceCalculationResult> priceCalculationResults = new ArrayList<>();
    priceCalculationResults.addAll(calculateResultsForItemsWithPricingRules(shoppingCart, pricingRules));
    priceCalculationResults.addAll(calculateResultsForItemsWithoutPricingRules(shoppingCart, pricingRules));

    return priceCalculationResults;
  }

  @Override
  public Receipt generateTransactionReciept(List<PriceCalculationResult> priceCalculationResultList) {
    return receiptManager.generateReciept(priceCalculationResultList);
  }

  private  List<PriceCalculationResult> calculateResultsForItemsWithPricingRules(ShoppingCart shoppingCart, List<PricingRule> pricingRules) {

    List<PriceCalculationResult> priceCalculationResults = new ArrayList<>();
    List<String> skusOfItemsWithDiscountRules = shoppingCart.getAllShoppingCartItems()
        .keySet()
        .stream()
        .filter(sku -> pricingRules.stream().filter(pricingRule -> pricingRule.getSkus().contains(sku)).count() > 0)
        .collect(Collectors.toList());

    // Get all shoppingCart items that have a rule associated with them
    shoppingCart.getAllShoppingCartItems()
        .entrySet()
        .stream()
        .filter(entry -> skusOfItemsWithDiscountRules.contains(entry.getKey()))
        .forEach(entry -> {
          // Apply the pricing rule
          List<PricingRule> pricingRulesToApply =  pricingRules.stream().filter(pricingRule -> pricingRule.getSkus().contains(entry.getKey())).collect(Collectors.toList());
          List<PriceCalculationResult> priceCalculationResultList = pricingManager.applyRules(entry.getValue(), pricingRulesToApply);
          priceCalculationResults.addAll(priceCalculationResultList);
        });

    return priceCalculationResults;

  }


  private  List<PriceCalculationResult> calculateResultsForItemsWithoutPricingRules(ShoppingCart shoppingCart, List<PricingRule> pricingRules) {

    List<PriceCalculationResult> priceCalculationResults = new ArrayList<>();
    Set<String> skusOfItemsWithOutDiscountRules = shoppingCart.getAllShoppingCartItems()
        .keySet()
        .stream()
        // Filter out SKUs that do not have a pricing rule in the pricing rules list
        .filter(sku -> pricingRules.stream().filter(pricingRule -> pricingRule.getSkus().contains(sku)).count() == 0)
        .collect(Collectors.toSet());


    // Get all shopping cart items that do not have a pricing rule
    // These will use the pricing rule that DO NOT apply a discount rule i.e. the Default
    List<ShoppingCartItem> shoppingCartItems = shoppingCart.getAllShoppingCartItems()
        .entrySet()
        .stream()
        .filter(entry -> skusOfItemsWithOutDiscountRules.contains(entry.getKey()))
        .map(entry -> entry.getValue())
        .collect(Collectors.toList());

    // Apply the default pricing rules (i.e. no no discounts pricing rule)
    List<PricingRule> pricingRulesToApply = Arrays.asList(new DefaultNoDiscountPricingRule(skusOfItemsWithOutDiscountRules));
    shoppingCartItems
        .stream()
        .forEach(shoppingCartItem -> {
          List<PriceCalculationResult> priceCalculationResultList = pricingManager.applyRules(shoppingCartItem, pricingRulesToApply);
          priceCalculationResults.addAll(priceCalculationResultList);
        });

    return priceCalculationResults;

  }





}
