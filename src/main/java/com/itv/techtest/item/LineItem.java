package com.itv.techtest.item;

/**
 * Created by dman on 31/10/2016.
 */
public class LineItem {

  private double price;
  private String sku;
  private String description;

  public LineItem() {}

  public LineItem(String sku, double price, String description) {
    this.sku = sku;
    this.price = price;
    this.description = description;
  }
  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
