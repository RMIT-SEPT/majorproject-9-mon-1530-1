package sept.major.availability.service;

import java.util.Comparator;

public class TimeSlotComparitor implements Comparator<TimeSlot> {
    @Override
    public int compare(TimeSlot o1, TimeSlot o2) {
        if (o1.getStartDateTime().isBefore(o2.getStartDateTime())) {
            return -1;
        } else if (o1.getStartDateTime().isAfter(o2.getStartDateTime())) {
            return 1;
        } else {
            return 0;
        }
    }
}
