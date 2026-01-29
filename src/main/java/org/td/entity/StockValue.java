package org.td.entity;

import java.util.Objects;

public class StockValue {

    private double quantity;
    private UnitType unit;

    public StockValue() {
    }

    public StockValue(double quantity, UnitType unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockValue)) return false;
        StockValue that = (StockValue) o;
        return Double.compare(that.quantity, quantity) == 0
                && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, unit);
    }

    @Override
    public String toString() {
        return "StockValue{" +
                "quantity=" + quantity +
                ", unit=" + unit +
                '}';
    }
}
