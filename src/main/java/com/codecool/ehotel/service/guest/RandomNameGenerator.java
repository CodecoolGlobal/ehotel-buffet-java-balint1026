package com.codecool.ehotel.service.guest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNameGenerator {

   // public RandomNameGenerator() {}

    public static String generate() {
        String[] firstNames = {"John", "Emma", "Michael", "Sophia", "Liam", "Olivia", "James", "Ava", "William", "Mia", "Bruce", "Adam", "George", "Catherine", "Alex", "Jeff", "Abed", "Troy", "Rich", "Pam", "Meredith", "Creed", "Leslie"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Willis", "Wick", "Sparrow", "Depp", "Pratt", "Swanson", "Scott", "Beasley", "Halpert", "Bratton", "Haverford", "Bing"};

        Random rand = new Random();
        String firstName = firstNames[rand.nextInt(firstNames.length)];
        String lastName = lastNames[rand.nextInt(lastNames.length)];

        return firstName + " " + lastName;
    }
}
