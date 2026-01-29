package org.td.entity;

import java.time.Instant;
import java.util.Objects;

public class StockMovement {

    private Integer id;
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDatetime;

    public StockMovement() {
    }

    public StockMovement(Integer id, StockValue value,
                         MovementTypeEnum type, Instant creationDatetime) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDatetime = creationDatetime;
    }

    public StockMovement(StockValue value,
                         MovementTypeEnum type, Instant creationDatetime) {
        this(null, value, type, creationDatetime);
    }

    /* GETTERS / SETTERS */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    public MovementTypeEnum getType() {
        return type;
    }

    public void setType(MovementTypeEnum type) {
        this.type = type;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    /* EQUALS / HASHCODE */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockMovement)) return false;
        StockMovement that = (StockMovement) o;

        if (this.id == null || that.id == null) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* TO STRING */

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", value=" + value +
                ", type=" + type +
                ", creationDatetime=" + creationDatetime +
                '}';
    }
}
