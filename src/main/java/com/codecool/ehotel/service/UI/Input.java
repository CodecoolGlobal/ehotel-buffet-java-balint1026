package com.codecool.ehotel.service.UI;

import java.util.Scanner;

public class Input {

    public int getSeasonLength(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How long will this season be?");
        return scanner.nextInt()+1;
    }
    public int getGuestAmount(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many guests will there be?");
        return scanner.nextInt()+1;
    }
}
