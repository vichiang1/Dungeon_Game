package byow.Core;


import byow.Util.RandomUtils;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class ArraySet<Item> implements Iterable {
    private ArrayList<Item> arrList;
    private Set<Item> set;

    public ArraySet() {
        arrList = new ArrayList();
        set = new HashSet<>();
    }

    public ArraySet(List<Item> input) {
        this();
        for (Item i : input) {
            add(i);
        }
    }

    public void add(Item e) {
        if (!set.contains(e)) {
            arrList.add(e);
            set.add(e);
        }
    }

    public boolean contains(Item e) {
        return set.contains(e);
    }

    public boolean remove(Item e) {
        if (set.contains(e)) {
            arrList.remove(e);
            set.remove(e);
            return true;
        }
        return false;
    }

    public ArraySet<Item> copy() {
        return new ArraySet(arrList);
    }

    public ArraySet<Item> combine(ArraySet<Item> two) {
        ArraySet c = this.copy();
        for (Object i : two) {
            Item item = (Item) i;
            c.add(item);
        }
        return c;
    }

    public Iterator<Item> iterator() {
        return arrList.iterator();
    }

    public Item getRandom(Random rand) {
        return arrList.get(RandomUtils.uniform(rand, arrList.size()));
    }
    public int size(){
        return arrList.size();
    }

}
