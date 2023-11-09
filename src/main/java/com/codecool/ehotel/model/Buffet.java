package com.codecool.ehotel.model;

import com.codecool.ehotel.service.buffet.BuffetService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Buffet implements BuffetService {

    @Override
    public List<Set<Guest>> getGuestsForCycle(Set<Guest> dailyGuests) {
        int cycleCount = 8;
        int guestCount = dailyGuests.size();
        List<Set<Guest>> dividedGuests = new ArrayList<>();
        for (int i = 0; i < cycleCount; i++) {
            dividedGuests.add(new HashSet<>());
        }
        for (Guest guest : dailyGuests) {
                dividedGuests.get(ThreadLocalRandom.current().nextInt(0, cycleCount)).add(guest);
        }

        return dividedGuests;
    }
}
