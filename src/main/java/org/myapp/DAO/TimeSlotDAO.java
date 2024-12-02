package org.myapp.DAO;

import org.myapp.Model.TimeSlot;

import java.util.List;

public interface TimeSlotDAO {
    boolean createTimeSlot(TimeSlot timeSlot);
    TimeSlot getTimeSlotById(int timeSlotId);
    boolean updateTimeSlot(TimeSlot timeSlot);
    boolean deleteTimeSlot(int timeSlotId);
    List<TimeSlot> getAllTimeSlots();
    List<TimeSlot> getTimeSlotsByYardId(int yardId);
}

