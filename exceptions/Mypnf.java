package exceptions;

import com.oocourse.spec3.exceptions.PathNotFoundException;

import java.util.HashMap;

public class Mypnf extends PathNotFoundException {
    private static int total = 0;
    private static final HashMap<Integer,Integer> ID2COUNT = new HashMap<>();

    private final int id;
    private final int count;

    public Mypnf(int id) {
        ++total;
        this.id = id;
        if (ID2COUNT.containsKey(id)) {
            this.count = ID2COUNT.get(id) + 1;
            ID2COUNT.put(id,this.count);
        } else {
            ID2COUNT.put(id,1);
            this.count = 1;
        }
    }

    @Override
    public void print() {
        System.out.printf("pnf-%d, %d-%d%n",total,id,count);
    }
}
