package com.codecool.ehotel.model;

import com.codecool.ehotel.service.buffet.BuffetService;
import com.codecool.ehotel.service.buffet.MealMaker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Buffet implements BuffetService {

    private final MealMaker mealMaker;
    private int[] totalWasteCost;

    public Buffet(MealMaker mealMaker) {
        this.mealMaker = mealMaker;
        this.totalWasteCost = new int[]{0};
    }

    @Override
    public List<Set<Guest>> getGuestsForCycle(Set<Guest> dailyGuests) {
        int cycleCount = 8;
        List<Set<Guest>> dividedGuests = new ArrayList<>();
        for (int i = 0; i < cycleCount; i++) {
            dividedGuests.add(new HashSet<>());
        }
        for (Guest guest : dailyGuests) {
            dividedGuests.get(ThreadLocalRandom.current().nextInt(0, cycleCount)).add(guest);
        }

        return dividedGuests;
    }


    // dailyServe "meals" contains longMeals
    public int dailyFoodPrep(Set<Guest> dailyGuests, List<Meal> meals) {
        meals = mealMaker.makeMediumMealsForADay(dailyGuests, meals);
        List<Set<Guest>> dividedGuests = getGuestsForCycle(dailyGuests);
        int[] totalWasteCost = {0};

        for (int i = 0; i < 8; i++) {
            if (i % 3 == 0) {
                removeShortMeals(meals);
                meals = mealMaker.makeShortMealsFor(dailyGuests, meals);
            }
            totalWasteCost[0] += serveCycle(dividedGuests.get(i), meals);
        }
        System.out.println(totalWasteCost[0]);
        return totalWasteCost[0];
    }

    private void removeShortMeals(List<Meal> meals) {
        meals.removeIf(meal -> meal.getType().getDurability() == MealDurability.SHORT);
    }

    @Override
    public int serveCycle(Set<Guest> guestsPerCycle, List<Meal> meals) {
        Set<Guest> guestsToRemove = new HashSet<>();
        List<Meal> mealsToRemove = new ArrayList<>();

        guestsPerCycle.forEach(guest -> {
            for (Meal meal : meals) {
                if (guest.guestType().getMealPreferences().contains(meal.getType())) {
                    guestsToRemove.add(guest);
                    mealsToRemove.add(meal);
                    totalWasteCost[0] += meal.getType().getCost();
                    break;
                }
            }
        });
        meals.removeAll(mealsToRemove);
        guestsPerCycle.removeAll(guestsToRemove);
        return totalWasteCost[0];
    }


}
