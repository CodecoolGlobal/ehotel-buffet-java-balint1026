package com.codecool.ehotel;
import com.codecool.ehotel.service.UI.Input;
import com.codecool.ehotel.service.simulation.Simulation;

public class EHotelBuffetApplication {
    public static void main(String[] args) {
        Input input = new Input();
        int seasonLength = input.getSeasonLength();
        int guestAmount = input.getGuestAmount();

        Simulation simulation = new Simulation();
        simulation.run(seasonLength, guestAmount);
    }
}
