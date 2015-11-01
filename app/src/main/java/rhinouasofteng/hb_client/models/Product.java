package rhinouasofteng.hb_client.models;

import java.util.UUID;

/**
 * Created by benwa_000 on 10/31/2015.
 */
public class Product {
    private UUID ID;
    private String name;
    private String unit;
    private Integer count;
    private Double cost;
    private Integer reorder;

    public Product() {

    }

    public Product(UUID iD, String name, String unit, Integer count,
                   Double cost, Integer reorder) {
        ID = iD;
        this.name = name;
        this.unit = unit;
        this.count = count;
        this.cost = cost;
        this.reorder = reorder;
    }

    public UUID getID() {
        return ID;
    }
    public void setID(UUID iD) {
        ID = iD;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getReorder() {
        return reorder;
    }

    public void setReorder(Integer reorder) {
        this.reorder = reorder;
    }

}