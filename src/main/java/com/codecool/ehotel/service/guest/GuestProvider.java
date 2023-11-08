package com.codecool.ehotel.service.guest;

import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.GuestType;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GuestProvider implements GuestService{

    Random random = new Random();
    @Override
    public Guest generateRandomGuest(LocalDate seasonStart, LocalDate seasonEnd) {
        List<LocalDate> days = ReservationProvider.selectCheckInDate(seasonStart, seasonEnd);
        Guest guest = new Guest(RandomNameGenerator.generate(),GuestType.values()[random.nextInt(GuestType.values().length)], days.get(0),days.get(1));
        System.out.println(guest.name());
        System.out.println(guest.toString());

        return guest;
    }

    @Override
    public Set<Guest> getGuestsForDay(List<Guest> guests, LocalDate date) {
        return null;
    }
}
