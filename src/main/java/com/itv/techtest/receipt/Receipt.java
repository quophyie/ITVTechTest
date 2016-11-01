package com.itv.techtest.receipt;

import com.itv.techtest.price.result.PriceCalculationResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dman on 01/11/2016.
 */
public class Receipt {

  private List<PriceCalculationResult> items;
  private BigDecimal total;

  public Receipt(){

  }
  public Receipt(List<PriceCalculationResult> items) {
    this.items = items;
  }
  public List<PriceCalculationResult> getItems() {
    return items;
  }

  public void setItems(List<PriceCalculationResult> items) {
    this.items = items;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
