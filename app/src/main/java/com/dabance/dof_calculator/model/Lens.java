package com.dabance.dof_calculator.model;

public class Lens {

    private String make;
    private double maxAperture;
    private int focalLength;

    public Lens() {
        this.make = "n/a";
        this.maxAperture = 0.0;
        this.focalLength = 0;
    }

    public Lens(String make, double maxAperture, int focalLength) {
        this.make = make;
        this.maxAperture = maxAperture;
        this.focalLength = focalLength;
    }

    public double getMaxAperture() {
        return maxAperture;
    }

    public double getFocalLength() {
        return focalLength;
    }

    @Override
    public String toString() {
        return make + ", " + focalLength + "mm, " + maxAperture;
    }
}