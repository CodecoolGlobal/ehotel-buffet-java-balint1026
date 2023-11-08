package com.codecool.ehotel.service.UI;

import java.util.Scanner;

public class Input {

    public int getSeasonLength(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How long this season will be?");
        return scanner.nextInt()+1;
    }
}
