package org.myapp.Model;

import java.time.LocalTime;

public class TimeSlot {
    private int timeSlotId;
    private int yardId;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructor
    public TimeSlot(int timeSlotId, int yardId, LocalTime startTime, LocalTime endTime) {
        this.timeSlotId = timeSlotId;
        this.yardId = yardId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public int getYardId() {
        return yardId;
    }

    public void setYardId(int yardId) {
        this.yardId = yardId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String displayTimeSlotInfo() {
        return "Time Slot ID: " + timeSlotId + "\n" +
                "Yard ID: " + yardId + "\n" +
                "Start Time: " + startTime + "\n" +
                "End Time: " + endTime;
    }
}

