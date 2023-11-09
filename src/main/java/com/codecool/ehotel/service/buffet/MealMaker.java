package com.codecool.ehotel.service.buffet;

import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.Meal;
import com.codecool.ehotel.model.MealType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MealMaker {

    public List<Meal> cookTheFirstPortion(List<Guest> guestList){
        List<Meal> meals = new ArrayList<>();
        Random random = new Random();
        for(Guest guest : guestList){
            int numberOfPreferences = guest.guestType().getMealPreferences().size();
            String mealPreferences = guest.guestType().getMealPreferences().get(random.nextInt(0,numberOfPreferences-1)).toString();
            Meal meal = new Meal(MealType.valueOf(mealPreferences));
            System.out.println(meal.getType().name());
            meals.add(meal);
        }
        System.out.println(meals.size());
        return meals;
    }

}
