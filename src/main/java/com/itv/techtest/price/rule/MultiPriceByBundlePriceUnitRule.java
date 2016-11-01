package com.itv.techtest.price.rule;

import com.itv.techtest.exception.PricingRuleException;
import com.itv.techtest.exception.ShoppingCartException;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a discount rule where user provides the number of items and
 * the unit price for those items. For example "2 items at 140" where each item costs 100
 */
public class MultiPriceByBundlePriceUnitRule implements PricingRule {

  private int numberOfItemsInMultiUnit;
  private double multiPriceUnitPrice;
  private String description;
  private Set<String> skus;

  public MultiPriceByBundlePriceUnitRule(int numberOfItems, double multiPriceUnitPrice, Set<String> skus, String description) {
    this.multiPriceUnitPrice = multiPriceUnitPrice;
    this.numberOfItemsInMultiUnit = numberOfItems;
    this.description = description;
    this.skus = skus.stream().map(sku -> sku.toUpperCase()).collect(Collectors.toSet());
  }

  public PriceCalculationResult applyRule(ShoppingCartItem shoppingCartItem) {

    if (shoppingCartItem == null)
      throw new ShoppingCartException("shopping cart item cannot be null");

    if (shoppingCartItem.getLineItem() == null)
      throw new ShoppingCartException("shopping cart line item cannot be null");

    if (!this.getSkus().contains(shoppingCartItem.getLineItem().getSku()))
      throw new PricingRuleException(String.format("Pricing Rule `%s` cannot be applied to SKU `%s`.\n" +
          "Please check the supported SKUs for rule `%s`",
          this.getDescription(), shoppingCartItem.getLineItem().getSku(), this.getDescription()));

    int numberOfItemsToBeDiscounted = (shoppingCartItem.getQuantity() / this.getNumberOfItemsInMultiUnit()) * this.getNumberOfItemsInMultiUnit();
    int numberOfItemsChargedAtFullPrice = shoppingCartItem.getQuantity() % this.getNumberOfItemsInMultiUnit();

    double unitPricePerItemAtFullPrice = shoppingCartItem.getLineItem().getPrice();

    //This is the price of numberOfItemsInMultiUnit items if it were not discounted
    double fullPriceMultiUnitWithoutDiscount = unitPricePerItemAtFullPrice * this.getNumberOfItemsInMultiUnit();

    // Get percentage discount to apply on the items to be discounted
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
