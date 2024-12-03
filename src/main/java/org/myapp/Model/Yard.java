package org.myapp.Model;

public class Yard {
    private int yardId;
    private String yardName;
    private String yardLocation;
    private int yardCapacity;
    private String surfaceType;
    private double pricePerDay;
    private String description;

    public Yard(int yardId, String yardName, String yardLocation, int yardCapacity, String surfaceType, double pricePerDay, String description) {
        this.yardId = yardId;
        this.yardName = yardName;
        this.yardLocation = yardLocation;
        this.yardCapacity = yardCapacity;
        this.surfaceType = surfaceType;
        this.pricePerDay = pricePerDay;
        this.description = description;
    }

    // Getters and Setters
    public int getYardId() {
        return yardId;
    }

    public void setYardId(int yardId) {
        this.yardId = yardId;
    }

    public String getYardName() {
        return yardName;
    }

    public void setYardName(String yardName) {
        this.yardName = yardName;
    }

    public String getYardLocation() {
        return yardLocation;
    }

    public void setYardLocation(String yardLocation) {
        this.yardLocation = yardLocation;
    }

    public int getYardCapacity() {
        return yardCapacity;
    }

    public void setYardCapacity(int yardCapacity) {
        this.yardCapacity = yardCapacity;
    }

    public String getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(String surfaceType) {
        this.surfaceType = surfaceType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String yardInfo() {
        return String.format(
                "+----------------------+--------------------------------------------+\n" +
                "  Yard ID:             | %-40s \n" +
                "  Name:                | %-40s \n" +
                "  Location:            | %-40s \n" +
                "  Capacity:            | %-40d \n" +
                "  Surface Type:        | %-40s \n" +
                "  Price per Day:       | $%-39.2f \n" +
                "  Description:         | %-40s \n" +
                "+----------------------+--------------------------------------------+",
                yardId, yardName, yardLocation, yardCapacity, surfaceType, pricePerDay, description
        );
    }

}


