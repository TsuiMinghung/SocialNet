package exceptions;

import com.oocourse.spec2.exceptions.EqualRelationException;

import java.util.HashMap;

public class Myer extends EqualRelationException  {
    private static int total = 0;
    private static final HashMap<Integer,Integer> ID2COUNT = new HashMap<>();

    private final int id1;
    private final int id2;
    private final int count1;
    private final int count2;

    public Myer(int id1,int id2) {
        ++total;
        if (id1 <= id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
        if (ID2COUNT.containsKey(this.id1)) {
            this.count1 = ID2COUNT.get(this.id1) + 1;
            ID2COUNT.put(this.id1,this.count1);
        } else {
            this.count1 = 1;
            ID2COUNT.put(this.id1,this.count1);
        }
        if (this.id1 == this.id2) {
            this.count2 = this.count1;
            return;
        }
        if (ID2COUNT.containsKey(this.id2)) {
            this.count2 = ID2COUNT.get(this.id2) + 1;
            ID2COUNT.put(this.id2,this.count2);
        } else {
            this.count2 = 1;
            ID2COUNT.put(this.id2,this.count2);
        }
    }

    @Override
    public void print() {
        System.out.printf("er-%d, %d-%d, %d-%d%n",total,id1,count1,id2,count2);
    }
}
