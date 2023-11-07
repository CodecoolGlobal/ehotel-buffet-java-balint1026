package com.codecool.ehotel;

import com.codecool.ehotel.service.UI.Input;
import com.codecool.ehotel.service.guest.GuestProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EHotelBuffetApplication {

    public static void main(String[] args) {
        // Initialize services
        Input input = new Input();
        LocalDate seasonStart = LocalDate.now();
        LocalDate seasonEnd = LocalDate.now().plusDays(input.getSeasonLength());

        GuestProvider guestProvider = new GuestProvider();
        guestProvider.generateRandomGuest(seasonStart,seasonEnd);
        // Generate guests for the season


        // Run breakfast buffet


    }
}
