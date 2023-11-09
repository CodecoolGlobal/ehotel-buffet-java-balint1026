package com.codecool.ehotel.service.buffet;

import com.codecool.ehotel.model.Guest;

import java.util.List;
import java.util.Set;

public interface BuffetService {
    List<Set<Guest>> getGuestsForCycle(Set<Guest> dailyGuests);

}
