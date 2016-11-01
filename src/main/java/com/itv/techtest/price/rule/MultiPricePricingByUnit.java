package com.itv.techtest.price.rule;

import com.itv.techtest.exception.PricingRuleException;
import com.itv.techtest.exception.ShoppingCartException;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.item.LineItem;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Represents a discount rule where user provides the number of items and
 * the unit price for those items. For example "items at 140"
 */
public class MultiPricePricingByUnit implements PricingRule {

  private int numberOfItemsInMultiUnit;
  private double multiPriceUnitPrice;
  private String description;
  private Set<String> skus;

  public MultiPricePricingByUnit(int numberOfItems, double multiPriceUnitPrice, Set<String> skus, String description) {
    this.multiPriceUnitPrice = multiPriceUnitPrice;
    this.numberOfItemsInMultiUnit = numberOfItems;
    this.description = description;
    this.skus = skus;

  }

  public PriceCalculationResult applyRule(ShoppingCartItem shoppingCartItem) {

    if (shoppingCartItem == null)
      throw new ShoppingCartException("shopping cart item cannot be null");

    if (shoppingCartItem.getLineItem() == null)
      throw new ShoppingCartException("shopping cart line item cannot be null");

    if (!this.getSkus().contains(shoppingCartItem.getLineItem().getSku()))
      throw new PricingRuleException(String.format("Pricing Rule `%s` cannot be applied to SKU `%s`", this.getDescription(), shoppingCartItem.getLineItem().getSku()));

    int numberOfItemsToBeDiscounted = (shoppingCartItem.getNumberOfItems() / this.getNumberOfItemsInMultiUnit()) * this.getNumberOfItemsInMultiUnit();
    int numberOfItemsChargedAtFullPrice = shoppingCartItem.getNumberOfItems() % this.getNumberOfItemsInMultiUnit();

    double unitPricePerItemAtFullPrice = shoppingCartItem.getLineItem().getPrice();

    //This the price of numberOfItemsInMultiUnit if it were not discounted
    double fullPriceMultiUnitWithoutDiscount = unitPricePerItemAtFullPrice * this.getNumberOfItemsInMultiUnit();

    // Get percentage discount to apply to the items to be discounted
    double discountPercentage =  1 - (this.getMultiPriceUnitPrice()/ fullPriceMultiUnitWithoutDiscount) ;

    double  pricePerItemAfterDiscount = unitPricePerItemAtFullPrice  -  (unitPricePerItemAtFullPrice * discountPercentage);

    BigDecimal totalDiscountedItems = new BigDecimal(pricePerItemAfterDiscount * numberOfItemsToBeDiscounted);
    BigDecimal totalItemsChargedAtFullPrice = new BigDecimal(numberOfItemsChargedAtFullPrice *  unitPricePerItemAtFullPrice);
    BigDecimal total = totalDiscountedItems.add(totalItemsChargedAtFullPrice);

    PriceCalculationResult priceCalculationResult = new PriceCalculationResult(numberOfItemsToBeDiscounted,
        numberOfItemsChargedAtFullPrice,
        total,
        totalDiscountedItems,
        totalItemsChargedAtFullPrice,
        shoppingCartItem.getLineItem().getSku());

    return priceCalculationResult;
  }

  /**
   *
   *  public PriceCalculationResult applyRule(ShoppingCartItem shoppingCartItem) {

   if (shoppingCartItem == null)
   throw new NullPointerException("shopping cart item cannot be null");

   int numberOfLineItemsAffectedByRule =  (int)lineItems.stream().filter(li -> li.getSkus().equalsIgnoreCase(this.getSkus())).count();
   int numberOfItemsToBeDiscounted = numberOfLineItemsAffectedByRule / this.getNumberOfItemsInMultiUnit();
   int numberOfItemsChargedAtFullPrice = numberOfLineItemsAffectedByRule % this.getNumberOfItemsInMultiUnit();

   double unitPricePerItemAtFullPrice = lineItems.get(0).getPrice();

   //This the price of numberOfItemsInMultiUnit if it were not discounted
   double fullPriceMultiUnitWithoutDiscount = unitPricePerItemAtFullPrice * this.getNumberOfItemsInMultiUnit();

   // Get percentage discount to apply to the items to be discounted
   double discountPercentage =  1 - (this.getMultiPriceUnitPrice()/ fullPriceMultiUnitWithoutDiscount) ;

   double  pricePerItemAfterDiscount = unitPricePerItemAtFullPrice  -  (unitPricePerItemAtFullPrice * discountPercentage);

   double totalDiscountedItems = pricePerItemAfterDiscount * numberOfItemsToBeDiscounted;
   double totalItemsChargedAtFullPrice = numberOfItemsChargedAtFullPrice *  unitPricePerItemAtFullPrice;

   BigDecimal totalAfterDiscount = new BigDecimal(totalDiscountedItems);
   totalAfterDiscount.add(new BigDecimal(totalItemsChargedAtFullPrice));

   PriceCalculationResult disountResult = new PriceCalculationResult(numberOfItemsToBeDiscounted, numberOfItemsChargedAtFullPrice, totalAfterDiscount, this.getSkus());

   return disountResult;
   }
   */

  public int getNumberOfItemsInMultiUnit() {
    return numberOfItemsInMultiUnit;
  }

  public void setNumberOfItemsInMultiUnit(int numberOfItemsInMultiUnit) {
    this.numberOfItemsInMultiUnit = numberOfItemsInMultiUnit;
  }

  public double getMultiPriceUnitPrice() {
    return multiPriceUnitPrice;
  }

  public void setMultiPriceUnitPrice(double multiPriceUnitPrice) {
    this.multiPriceUnitPrice = multiPriceUnitPrice;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
