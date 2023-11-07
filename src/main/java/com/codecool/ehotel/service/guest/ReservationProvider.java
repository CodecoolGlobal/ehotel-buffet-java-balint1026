package com.codecool.ehotel.service.guest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationProvider {

    public static List<LocalDate> selectCheckInDate(int nights,int lengthOfTheSeason){
        List<LocalDate> days = new ArrayList<>();
        days.add(LocalDate.now());
        days.add(LocalDate.now().plusDays(nights));
        return days;
    }
}
