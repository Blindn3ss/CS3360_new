package org.myapp.Model;

import org.myapp.DAO.YardDAOImpl;

import java.util.List;
import static org.myapp.Menu.Utility.wrapText;

public class Yard {
    private int yardId;
    private String yardName;
    private String yardLocation;
    private int yardCapacity;
    private String surfaceType;
    private double pricePerDay;
    private String description;

    public Yard() {
        this.yardId = 0;
        this.yardName = "";
        this.yardLocation = "";
        this.yardCapacity = 0;
        this.surfaceType = "";
        this.pricePerDay = 0;
        this.description = "";
    }

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
        String wrappedDescription = wrapText(description, 60);

        StringBuilder formattedInfo = new StringBuilder(String.format(
                        "+----------------------+--------------------------------------------------------------+\n" +
                        "| Yard ID:             | %-60s |\n" +
                        "| Name:                | %-60s |\n" +
                        "| Location:            | %-60s |\n" +
                        "| Capacity:            | %-60d |\n" +
                        "| Surface Type:        | %-60s |\n" +
                        "| Price per Day:       | $%-59.2f |\n",
                yardId, yardName, yardLocation, yardCapacity, surfaceType, pricePerDay
        ));

        String[] descriptionLines = wrappedDescription.split("\n");
        for (int i = 0; i < descriptionLines.length; i++) {
            if (i == 0){
                formattedInfo.append(String.format("| Description:         | %-60s |\n", descriptionLines[i]));
            }
            else
                formattedInfo.append(String.format("|                      | %-60s |\n", descriptionLines[i]));
        }
        formattedInfo.append("+----------------------+--------------------------------------------------------------+");

        return formattedInfo.toString();
    }

    public List<Yard> getYardsWithFilter(Integer minCapacity,
                                         Integer maxCapacity,
                                         String location,
                                         String surfaceType,
                                         Double minPrice,
                                         Double maxPrice){
        return YardDAOImpl.getInstance().getYardsWithFilter(minCapacity, maxCapacity, location, surfaceType, minPrice, maxPrice );
    }
}


