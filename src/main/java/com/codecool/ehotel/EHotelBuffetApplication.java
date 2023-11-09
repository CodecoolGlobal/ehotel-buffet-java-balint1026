package com.codecool.ehotel;

import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.Meal;
import com.codecool.ehotel.service.UI.Input;
import com.codecool.ehotel.service.buffet.MealMaker;
import com.codecool.ehotel.service.guest.GuestProvider;
import com.codecool.ehotel.service.sheetsExporter.GoogleSheetsExporter;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EHotelBuffetApplication {

    public static void main(String[] args) {
        Input input = new Input();
        LocalDate seasonStart = LocalDate.now();
        int seasonLength = input.getSeasonLength();
        int guestAmount = input.getGuestAmount();
        LocalDate seasonEnd = LocalDate.now().plusDays(seasonLength + 1);
        List<Guest> guestList = generateGuests(guestAmount, seasonStart, seasonEnd);
        List<List<Guest>> season = fillSeasonDays(guestList, seasonStart, seasonLength);

        GoogleSheetsExporter sheetExporter = new GoogleSheetsExporter();
        try {
            sheetExporter.exportDataToGoogleSheets(season);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static List<Guest> generateGuests(int guestAmount, LocalDate seasonStart, LocalDate seasonEnd) {
        GuestProvider guestProvider = new GuestProvider();
        for (int i = 0; i < guestAmount; i++) {
            guestProvider.generateRandomGuest(seasonStart, seasonEnd);

        }
        List<Guest> guestList = new ArrayList<>();


        for (int i = 0; i < guestAmount; i++) {
            guestList.add(guestProvider.generateRandomGuest(seasonStart, seasonEnd));
        }
        guestList.sort(Comparator.comparing(Guest::checkIn));
        return guestList;
    }

    private static List<List<Guest>> fillSeasonDays(List<Guest> guestList, LocalDate seasonStart, int seasonLength) {
        List<List<Guest>> season = new ArrayList<>();
        for (int i = 0; i < seasonLength; i++) {
            season.add(new ArrayList<>());
        }
        for (Guest guest : guestList) {
            LocalDate checkInDate = guest.checkIn();
            LocalDate checkOutDate = guest.checkOut();
            int checkInDay = (int) ChronoUnit.DAYS.between(seasonStart, checkInDate);
            int checkOutDay = (int) ChronoUnit.DAYS.between(seasonStart, checkOutDate);
            for (int i = checkInDay; i < checkOutDay; i++) {
                season.get(i).add(guest);
            }
        }
        return season;
    }
}
