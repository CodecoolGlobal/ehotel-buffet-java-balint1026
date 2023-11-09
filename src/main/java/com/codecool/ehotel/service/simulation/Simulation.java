package com.codecool.ehotel.service.simulation;

import com.codecool.ehotel.model.Buffet;
import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.Meal;
import com.codecool.ehotel.service.buffet.MealMaker;
import com.codecool.ehotel.service.guest.GuestProvider;
import com.codecool.ehotel.service.sheetsExporter.GoogleSheetsExporter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Simulation {


    private GuestProvider guestProvider;
    private Buffet buffet;
    private MealMaker mealMaker;


    public void run(int seasonLength, int guestAmount) {
        MealMaker mealMaker = new MealMaker();

        Buffet buffet = new Buffet(mealMaker);

        List<Guest> guestList = generateGuests(guestAmount, LocalDate.now(), LocalDate.now().plusDays(seasonLength));
        List<List<Guest>> season = fillSeasonDays(guestList, LocalDate.now(), seasonLength + 1);
        exportToSheets(season);
        List<Meal> meals = mealMaker.makeLongMealsForTheSeason(season);

        for (int i = 0; i < seasonLength; i++) {
            prepareAndServeDailyMeals(season, i, meals, buffet);
        }

    }
    private void prepareAndServeDailyMeals(List<List<Guest>> season, int dayIndex, List<Meal> meals,Buffet buffet) {
        Set<Guest> dailyGuests = guestProvider.getGuestsForDay(season, dayIndex);
        buffet.dailyFoodPrep(dailyGuests, meals);
    }


    private void exportToSheets(List<List<Guest>> season) {
        GoogleSheetsExporter sheetExporter = new GoogleSheetsExporter();
        try {
            sheetExporter.exportDataToGoogleSheets(season);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public List<Guest> generateGuests(int guestAmount, LocalDate seasonStart, LocalDate seasonEnd) {
         guestProvider = new GuestProvider();
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

    public List<List<Guest>> fillSeasonDays(List<Guest> guestList, LocalDate seasonStart, int seasonLength) {
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
