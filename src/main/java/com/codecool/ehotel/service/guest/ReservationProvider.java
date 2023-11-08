package com.codecool.ehotel.service.guest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservationProvider {

    public static List<LocalDate> selectCheckInDate(){
        List<LocalDate> days = new ArrayList<>();
        Random random = new Random();
        int start = random.nextInt(5);
        days.add(LocalDate.now().plusDays(start));
        days.add(LocalDate.now().plusDays(start+random.nextInt(7)));
        return days;
    }
}
