package com.itv.techtest.price.result;

import java.math.BigDecimal;

/**
 * This class represents the details after applying discounts to a list of line items
 */
public class PriceCalculationResult {

  private int numberOfItemsDiscounted;
  private int numberOfItemsAtFullPrice;
  private BigDecimal total;
  private BigDecimal totalOfDiscountedItems;
  private BigDecimal totalOfFullPriceItems;
  private String sku;

  public PriceCalculationResult(int numberOfItemsDiscounted,
                                int numberOfItemsAtFullPrice,
                                BigDecimal total,
                                BigDecimal totalOfDiscountedItems,
                                BigDecimal totalOfFullPriceItems,
                                String sku){
    this.numberOfItemsAtFullPrice = numberOfItemsAtFullPrice;
    this.numberOfItemsDiscounted = numberOfItemsDiscounted;
    this.totalOfDiscountedItems = totalOfDiscountedItems;
    this.totalOfFullPriceItems = totalOfFullPriceItems;
    this.total = total;
    this.sku = sku;
  }

  public int getNumberOfItemsDiscounted() {
    return numberOfItemsDiscounted;
  }

  public void setNumberOfItemsDiscounted(int numberOfItemsDiscounted) {
    this.numberOfItemsDiscounted = numberOfItemsDiscounted;
  }

  public int getNumberOfItemsAtFullPrice() {
    return numberOfItemsAtFullPrice;
  }

  public void setNumberOfItemsAtFullPrice(int numberOfItemsAtFullPrice) {
    this.numberOfItemsAtFullPrice = numberOfItemsAtFullPrice;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getTotalOfDiscountedItems() {
    return totalOfDiscountedItems;
  }

  public void setTotalOfDiscountedItems(BigDecimal totalOfDiscountedItems) {
    this.totalOfDiscountedItems = totalOfDiscountedItems;
  }

  public BigDecimal getTotalOfFullPriceItems() {
    return totalOfFullPriceItems;
  }

  public void setTotalOfFullPriceItems(BigDecimal totalOfFullPriceItems) {
    this.totalOfFullPriceItems = totalOfFullPriceItems;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }
}
