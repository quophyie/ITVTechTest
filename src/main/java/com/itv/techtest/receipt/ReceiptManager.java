package com.itv.techtest.receipt;

import com.itv.techtest.price.result.PriceCalculationResult;

import java.util.List;

/**
 * Created by dman on 01/11/2016.
 */
public interface ReceiptManager {
  Receipt generateReciept(List<PriceCalculationResult> priceCalculationResultList);
}
