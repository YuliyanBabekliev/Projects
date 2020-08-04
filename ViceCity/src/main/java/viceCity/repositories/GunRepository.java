package viceCity.repositories;

import viceCity.models.guns.Gun;
import viceCity.repositories.interfaces.Repository;

import java.util.*;

public class GunRepository implements Repository<Gun> {
    private ArrayDeque<Gun> models;

    public GunRepository() {
        this.models = new ArrayDeque<>();
    }

    @Override
    public Collection<Gun> getModels() {
        return Collections.unmodifiableCollection(this.models);
    }

    @Override
    public void add(Gun model) {
        this.models.offer(model);
    }

    @Override
    public boolean remove(Gun model) {
        if(this.models.contains(model)){
            this.models.poll();
            return true;
        }
        return false;
    }

    @Override
    public Gun find(String name) {
        for (Gun model : this.models) {
            if(model.getName().equals(name)){
                return model;
            }
        }
        return null;
    }
}
