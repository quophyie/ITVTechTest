package com.itv.techtest.price.rule;

import com.itv.techtest.exception.PricingRuleException;
import com.itv.techtest.exception.ShoppingCartException;
import com.itv.techtest.price.result.PriceCalculationResult;
import com.itv.techtest.shoppingcart.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a discount rule where user provides the percentage to reduce a bundle of of items by.
 * For example "each bundle of 2 items discounted at 20" where each item costs 100
 */
public class MultiPriceByPercentageDiscountRule implements PricingRule {

  private int numberOfItemsInMultiUnit;
  private double percentageDiscount;
  private String description;
  private Set<String> skus;

  public MultiPriceByPercentageDiscountRule(int numberOfItems, double percentageDiscount, Set<String> skus, String description) {
    this.percentageDiscount = percentageDiscount;
    this.numberOfItemsInMultiUnit = numberOfItems;
    this.description = description;
    this.skus = skus.stream().map(sku -> sku.toUpperCase()).collect(Collectors.toSet());

  }

  public PriceCalculationResult applyRule(ShoppingCartItem shoppingCartItem) {

    if (shoppingCartItem == null)
      throw new ShoppingCartException("shopping cart item cannot be null");

    if (shoppingCartItem.getLineItem() == null)
      throw new ShoppingCartException("shopping cart line item cannot be null");

    if (!this.getSkus().contains(shoppingCartItem.getLineItem().getSku().toUpperCase()))
      throw new PricingRuleException(String.format("Pricing Rule `%s` cannot be applied to SKU `%s`.\n" +
          "Please check the supported SKUs for rule `%s`",
          this.getDescription(), shoppingCartItem.getLineItem().getSku(), this.getDescription()));

    int numberOfItemsToBeDiscounted = (shoppingCartItem.getQuantity() / this.getNumberOfItemsInMultiUnit()) * this.getNumberOfItemsInMultiUnit();
    int numberOfItemsChargedAtFullPrice = shoppingCartItem.getQuantity() % this.getNumberOfItemsInMultiUnit();

    double unitPricePerItemAtFullPrice = shoppingCartItem.getLineItem().getPrice();

    double  pricePerItemAfterDiscount = unitPricePerItemAtFullPrice  -  (unitPricePerItemAtFullPrice * (this.getPercentageDiscount() / 100));

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

  public double getPercentageDiscount() {
    return percentageDiscount;
  }

  public void setPercentageDiscount(double percentageDiscount) {
    this.percentageDiscount = percentageDiscount;
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
