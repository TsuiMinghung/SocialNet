package exceptions;

import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;

public class Myer extends EqualRelationException  {
    private static int total;
    private static final HashMap<Integer,Integer> ID2COUNT = new HashMap<>();

    private final int id1;
    private final int id2;
    private final int count1;
    private final int count2;

    public Myer(int id1,int id2) {
        this.id1 = id1;
        this.id2 = id2;
        if (ID2COUNT.containsKey(id1)) {
            this.count1 = ID2COUNT.get(id1) + 1;
            ID2COUNT.put(id1,this.count1);
        } else {
            this.count1 = 1;
            ID2COUNT.put(id1,this.count1);
        }

        if (ID2COUNT.containsKey(id2)) {
            this.count2 = ID2COUNT.get(id2) + 1;
            ID2COUNT.put(id2,this.count2);
        } else {
            this.count2 = 1;
            ID2COUNT.put(id2,this.count2);
        }
    }

    @Override
    public void print() {
        System.out.printf("rnf-%d,%d-%d,%d-%d%n",total,id1,count1,id2,count2);
    }
}
