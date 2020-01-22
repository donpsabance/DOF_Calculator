package com.dabance.dof_calculator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LensManager implements Iterable<Lens> {

    private List<Lens> lensList = new ArrayList<>();

    private static LensManager instance;
    private LensManager(){
//        Private to prevent anyone else in creating an instance
    }
    public static LensManager getInstance(){
        if(instance == null){
            instance = new LensManager();
        }
        return instance;
    }

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
