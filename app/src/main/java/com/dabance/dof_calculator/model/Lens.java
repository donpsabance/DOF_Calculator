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

    public String getMake() {
        return make;
    }


    public double getMaxAperture() {
        return maxAperture;
    }

    public int getFocalLength() {
        return focalLength;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setMaxAperture(double maxAperture) {
        this.maxAperture = maxAperture;
    }

    public void setFocalLength(int focalLength) {
        this.focalLength = focalLength;
    }

    @Override
    public String toString() {
        return make + " " + focalLength + "mm F" + maxAperture;
    }
}