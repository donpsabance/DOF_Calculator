package com.dabance.dof_calculator.model;

public class Lens {

    private String make;
    private double maxAperture;
    private int focalLength;
    private int imageId;

    public Lens(String make, double maxAperture, int focalLength, int imageId) {
        this.make = make;
        this.maxAperture = maxAperture;
        this.focalLength = focalLength;
        this.imageId = imageId;
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

    public int getImageId() {
        return imageId;
    }

    public String getInfo(){
        return make + "," + Double.toString(maxAperture) + "," + Integer.toString(focalLength) + "," + imageId;
    }

    @Override
    public String toString() {
        return make + " " + focalLength + "mm F" + maxAperture;
    }
}