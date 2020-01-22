package com.dabance.dof_calculator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LensManager implements Iterable<Lens> {

    private List<Lens> lensList = new ArrayList<>();

    public void addLens(Lens lens){
        lensList.add(lens);
    }

    public List<Lens> getLensList() {
        return lensList;
    }

    @Override
    public Iterator<Lens> iterator() {
        return lensList.iterator();
    }

}
