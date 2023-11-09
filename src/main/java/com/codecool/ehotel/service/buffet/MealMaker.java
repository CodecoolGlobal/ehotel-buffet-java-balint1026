package com.codecool.ehotel.service.buffet;

import com.codecool.ehotel.model.*;

import java.util.*;

public class MealMaker {

    public List<Meal> cookTheFirstPortion(Set<Guest> guestSet){
        List<Meal> meals = new ArrayList<>();
        Random random = new Random();
        int nonBusinessGuests=0;

        for(Guest guest : guestSet){
            if(guest.guestType() != GuestType.BUSINESS){
                nonBusinessGuests++;
            }
           /* int numberOfPreferences = guest.guestType().getMealPreferences().size();
            String mealPreferences = guest.guestType().getMealPreferences().get(random.nextInt(0,numberOfPreferences-1)).toString();
            Meal meal = new Meal(MealType.valueOf(mealPreferences));
            meals.add(meal); */
        }
        int BusinessGuest = guestSet.size()-nonBusinessGuests;
        List<MealType> allMeals = List.of(MealType.values());
        List<MealType> longMeals = new ArrayList<>();
        List<MealType> mediumMeals = new ArrayList<>();
        for(MealType meal : allMeals){
            if(meal.getDurability() == MealDurability.LONG){
                longMeals.add(meal);
            } else if (meal.getDurability() == MealDurability.MEDIUM){
                mediumMeals.add(meal);
            }
        }
        return meals;
    }

    public List<MealType> getLongMeals(){
        List<MealType> allMeals = List.of(MealType.values());
        List<MealType> longMeals = new ArrayList<>();
        for(MealType meal : allMeals){
            if(meal.getDurability() == MealDurability.LONG){
                longMeals.add(meal);
            }
        }
        return longMeals;
    }

    public List<Meal> makeShortMealsFor(Set<Guest> guestsForADay, List<Meal> madeMeals){
        Random random = new Random();
        int counter = 4;
        for(Guest guest : guestsForADay){
            if(guest.guestType() == GuestType.BUSINESS) {
                int numberOfPreferences = guest.guestType().getMealPreferences().size();
                String mealPreferences = guest.guestType().getMealPreferences().get(random.nextInt(0, numberOfPreferences - 1)).toString();
                if (counter % 4 == 0) {

                    madeMeals.add(new Meal(MealType.valueOf(mealPreferences)));
                }
            }
            counter++;
        }
        return madeMeals;
    }

    public List<Meal> makeMediumMealsForADay(Set<Guest> guestsForADay, List<Meal> madeMeals){
        for(Guest guest : guestsForADay){
            if (guest.guestType() == GuestType.TOURIST){
                madeMeals.add(new Meal(MealType.MASHED_POTATO));
            }
        }
        return madeMeals;
    }

    public List<Meal> makeLongMealsForTheSeason(List<List<Guest>> guestsForTheSeason){
        List<Meal> longMeals= new ArrayList<>();
        int kidNumber = 0;
        Random random = new Random();
        for(List<Guest> guestsForADay : guestsForTheSeason){
            for(Guest guest : guestsForADay){
                if(guest.guestType() == GuestType.KID){
                    longMeals.add(new Meal(getLongMeals().get(random.nextInt(0, getLongMeals().size()))));
                    kidNumber++;
                }
            }
        }
        System.out.println("Number of kids: " + kidNumber);
        System.out.println("Number of long-duration-meals created: " + longMeals.size());
        return longMeals;
    }

}
