package com.codecool.ehotel.service.buffet;

import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.Meal;

import java.util.List;
import java.util.Set;

public interface BuffetService {
    List<Set<Guest>> getGuestsForCycle(Set<Guest> dailyGuests);

    int dailyFoodPrep(Set<Guest> dailyGuests, List<Meal> longMeals);

    int serveCycle(Set<Guest> guestsPerCycle, List<Meal> meals);

}
