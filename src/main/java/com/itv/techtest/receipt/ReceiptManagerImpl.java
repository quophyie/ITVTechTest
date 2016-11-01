package com.itv.techtest.receipt;

import com.itv.techtest.price.result.PriceCalculationResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * Created by dman on 01/11/2016.
 */
public class ReceiptManagerImpl implements ReceiptManager {

  @Override
  public Receipt generateReciept(List<PriceCalculationResult> priceCalculationResultList) {
    Receipt receipt = new Receipt();

    if (priceCalculationResultList != null && !priceCalculationResultList.isEmpty()) {
      BigDecimal total = new BigDecimal(0);
      for (PriceCalculationResult priceCalculationResult : priceCalculationResultList) {
        total = priceCalculationResult.getTotal().add(total, MathContext.DECIMAL64);
      }
      receipt.setTotal(total);
      receipt.setItems(priceCalculationResultList);

    }

    return receipt;
  }
}
