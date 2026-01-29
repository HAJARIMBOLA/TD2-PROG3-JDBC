package org.td.entity;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Table {

    private Integer id;
    private Integer number;
    private Integer capacity;
    private List<TableOrder> tableOrders;

    public Table() {
    }

    public Table(Integer id, Integer number, Integer capacity) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<TableOrder> getTableOrders() {
        return tableOrders;
    }

    public void setTableOrders(List<TableOrder> tableOrders) {
        this.tableOrders = tableOrders;
    }

    public boolean isAvailableAt(Instant time) {
        if (tableOrders == null || tableOrders.isEmpty()) {
            return true;
        }

        for (TableOrder to : tableOrders) {
            if (!time.isBefore(to.getArrivalDatetime())
                    && !time.isAfter(to.getDepartureDatetime())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;
        Table table = (Table) o;
        return Objects.equals(id, table.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", number=" + number +
                ", capacity=" + capacity +
                '}';
    }
}
