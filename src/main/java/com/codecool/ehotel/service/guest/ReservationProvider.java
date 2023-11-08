package com.codecool.ehotel.service.guest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservationProvider {

    public static List<LocalDate> selectCheckInDate(LocalDate seasonStart, LocalDate seasonEnd){
        List<LocalDate> days = new ArrayList<>();
        Random random = new Random();
        int daysBetween = (int) ChronoUnit.DAYS.between(seasonStart, seasonEnd);
        int maxLengthOfReservation = 7;
        int nights;
        if(daysBetween>=maxLengthOfReservation){
            nights = random.nextInt(1,7);
        } else {
            nights = random.nextInt(1,daysBetween);
        }
        int latestPossibleDay = daysBetween-nights;

        int start = random.nextInt(latestPossibleDay);
        days.add(seasonStart.plusDays(start));
        days.add(LocalDate.now().plusDays(start+nights));

        return days;
    }
}
