package org.td.entity;

import java.time.Instant;
import java.util.Objects;

public class TableOrder {

    private Integer id;
    private Table table;
    private Instant arrivalDatetime;
    private Instant departureDatetime;

    public TableOrder() {
    }

    public TableOrder(Integer id, Table table, Instant arrivalDatetime, Instant departureDatetime) {
        this.id = id;
        this.table = table;
        this.arrivalDatetime = arrivalDatetime;
        this.departureDatetime = departureDatetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Instant getArrivalDatetime() {
        return arrivalDatetime;
    }

    public void setArrivalDatetime(Instant arrivalDatetime) {
        this.arrivalDatetime = arrivalDatetime;
    }

    public Instant getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(Instant departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableOrder)) return false;
        TableOrder that = (TableOrder) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TableOrder{" +
                "id=" + id +
                ", table=" + table +
                ", arrivalDatetime=" + arrivalDatetime +
                ", departureDatetime=" + departureDatetime +
                '}';
    }
}
