package com.example.inventory.util;

import com.example.inventory.entity.Product;
import com.example.inventory.enums.StockLevel;

public final class StockStatusUtil {

    private StockStatusUtil() {}

    public static StockLevel calculate(Product p) {
        int q = p.getStockQuantity() != null ? p.getStockQuantity() : 0;
        int low = p.getLowStockThreshold() != null ? p.getLowStockThreshold() : 50;
        int med = p.getMediumStockThreshold() != null ? p.getMediumStockThreshold() : 100;

        if (q <= low) {
			return StockLevel.LOW;
		}
        if (q <= med) {
			return StockLevel.MEDIUM;
		}
        return StockLevel.HIGH;
    }
}
