package com.dabance.dof_calculator.model;

import java.util.Comparator;

public class LensComparator implements Comparator<Lens> {

    @Override
    public int compare(Lens lens1, Lens lens2){
        return lens1.getMake().compareTo(lens2.getMake());
    }
}
