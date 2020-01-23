package com.dabance.dof_calculator.model;

public class NumberManager {

    public static boolean isIntegerInRange(String input, int min, int max){

        try {

            int n =  Integer.parseInt(input);
            if(n >= min && n <= max) {
                return true;
            }
            return false;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isDoubleInRange(String input, double min, double max){

        try {

            Double n =  Double.parseDouble(input);
            if(n >= min && n <= max) {
                return true;
            }
            return false;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }
}