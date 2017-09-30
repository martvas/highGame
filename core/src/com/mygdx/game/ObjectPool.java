package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T extends PoolableMy> {
    protected List<T> freeList;
    protected List<T> activeList;

    public List<T> getFreeList() {
        return freeList;
    }

    public List<T> getActiveList() {
        return activeList;
    }

    protected abstract T newObject();

    public ObjectPool(int size){
        freeList = new ArrayList<T>();
        activeList = new ArrayList<T>();
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
    }

    public T getActiveElement(){
        if (freeList.size() == 0){
            freeList.add(newObject());
        }
        T temp = freeList.remove(freeList.size() - 1);
        activeList.add(temp);
        return temp;
    }

    public void checkNoActiveElement(){
        for (int i = activeList.size()-1; i >= 0; i--) {
            if (!activeList.get(i).isActive()){
                freeList.add(activeList.remove(i));
            }
        }
    }

}
