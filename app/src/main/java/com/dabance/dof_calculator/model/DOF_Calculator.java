package com.dabance.dof_calculator.model;

public class DOF_Calculator {

    private static final double CIRCLE_OF_CONFUSION = 0.029;

    public static double calculateDOF(Lens lens, double distance, double aperture){
        return ((calculateFarFocalPoint(lens, distance, aperture)) - calculateNearFocalPoint(lens, distance, aperture));

    }

    //returns distance in mm
    public static double calculateHyperfocalDistance(Lens lens, double aperture){

        return ((Math.pow(lens.getFocalLength(), 2)) / (aperture * CIRCLE_OF_CONFUSION));
    }

    public static double calculateNearFocalPoint(Lens lens, double distance, double aperture){
        return ((calculateHyperfocalDistance(lens, aperture) * distance) /
                (calculateHyperfocalDistance(lens, aperture) + ((distance * 1000) - lens.getFocalLength())));

    }

    public static double calculateFarFocalPoint(Lens lens, double distance, double aperture){

        double farFocalPoint = ((calculateHyperfocalDistance(lens, aperture) * distance) /
                (calculateHyperfocalDistance(lens, aperture) - ((distance * 1000) - lens.getFocalLength())));

        if(distance > farFocalPoint){
            return Double.POSITIVE_INFINITY;
        }
        return farFocalPoint;
    }
}
