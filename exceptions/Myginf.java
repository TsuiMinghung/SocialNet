package exceptions;

import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class Myginf extends GroupIdNotFoundException {

    private static int total = 0;
    private static final HashMap<Integer,Integer> ID2COUNT = new HashMap<>();

    private final int id;
    private final int count;

    public Myginf(int id) {
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
        System.out.printf("ginf-%d, %d-%d%n",total,id,count);
    }
}
