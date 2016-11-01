package com.itv.techtest.checkoutmanager;

import com.itv.techtest.receipt.Receipt;
import com.itv.techtest.shoppingcart.ShoppingCart;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.price.rule.PricingRule;

import java.util.List;

/**
 * The interface for the checkout / checkout manager
 */
public interface CheckoutManager {

  /**
   *
   * @param shoppingCart - The shopping cart
   * @param pricingRules - A list of pricing rules (usually discount rules) to apply to the items in the
   *                     shopping cart
   * @return A list of PriceCalculationResult where each PriceCalculationResult corresponds to the
   * result of applying pricing rules to each ShoppingCartItem
   */
  List<PriceCalculationResult> checkout(ShoppingCart shoppingCart, List<PricingRule> pricingRules);

  /**
   * Used to generate the transaction receipt after checkout
   * @param priceCalculationResultList
   * @return
   */
  Receipt generateTransactionReciept(List<PriceCalculationResult>  priceCalculationResultList);
}
