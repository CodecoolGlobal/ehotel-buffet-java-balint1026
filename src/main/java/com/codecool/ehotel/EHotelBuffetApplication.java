package com.codecool.ehotel;

import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.service.UI.Input;
import com.codecool.ehotel.service.guest.GuestProvider;
import com.codecool.ehotel.service.sheetsExporter.GoogleSheetsExporter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EHotelBuffetApplication {

    public static void main(String[] args) {
        // Initialize services
        Input input = new Input();
        LocalDate seasonStart = LocalDate.now();
        int seasonLength = input.getSeasonLength();
        LocalDate seasonEnd = LocalDate.now().plusDays(seasonLength);

        GuestProvider guestProvider = new GuestProvider();
        guestProvider.generateRandomGuest(seasonStart,seasonEnd);

        List<Guest> guestList = new ArrayList<>();

        for (int i = 0; i < 150; i++) {
             guestList.add(guestProvider.generateRandomGuest(seasonStart,seasonEnd));
        }

        List<List<Guest>> season = new ArrayList<>();
        for (int i = 0; i < seasonLength; i++) {
            season.add(new ArrayList<>());
        }
        fillSeasonDays(guestList, season, seasonStart, seasonEnd);
        GoogleSheetsExporter sheetExporter = new GoogleSheetsExporter();
        try {
            sheetExporter.exportDataToGoogleSheets(season);
        }catch(Exception err){
            err.printStackTrace();
        }
        System.out.println(season.get(2));

    }
    private static void fillSeasonDays(List<Guest> guestList, List<List<Guest>> season, LocalDate seasonStart, LocalDate seasonEnd) {
        for (Guest guest : guestList) {
            LocalDate checkInDate = guest.checkIn();
            LocalDate checkOutDate = guest.checkOut();
            int checkInDay = (int) ChronoUnit.DAYS.between(seasonStart, checkInDate);
            int checkOutDay = (int) ChronoUnit.DAYS.between(checkOutDate, seasonEnd);
            checkInDay = Math.max(checkInDay, 0);
            checkOutDay = Math.min(checkOutDay, season.size() - 1);
            for (int i = checkInDay; i < checkOutDay; i++) {
                season.get(i).add(guest);
            }
        }
    }
}
