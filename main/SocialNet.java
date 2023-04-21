package main;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Person;
import exceptions.Myepid;
import exceptions.Myer;
import exceptions.Mypinf;
import exceptions.Myrnf;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SocialNet {

    private static SocialNet instance = null;

    public static SocialNet getInstance() {
        if (instance == null) {
            instance = new SocialNet();
        }
        return instance;
    }

    private final HashMap<Integer,Vertex> vertices;
    private final HashMap<Integer,Integer> fathers;
    private final HashMap<Integer,Integer> ranks;
    private int qbs;
    private int qts;
    private final BitSet record;//false means need recalculate,true means last value could be used

    private SocialNet() {
        this.vertices = new HashMap<>();
        this.fathers = new HashMap<>();
        this.ranks = new HashMap<>();
        this.record = new BitSet(2);
        this.record.clear();
    }

    public boolean contains(int id) {
        return vertices.containsKey(id);
    }

    public Person getPerson(int id) {
        if (vertices.containsKey(id)) {
            return vertices.get(id).getPerson();
        } else {
            return null;
        }
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (vertices.containsKey(person.getId())) {
            throw new Myepid(person.getId());
        } else {
            this.record.clear();
            vertices.put(person.getId(),
                    new Vertex(person.getId(), person.getName(), person.getAge(),person));
            fathers.put(person.getId(), person.getId());
            ranks.put(person.getId(),1);
        }
    }

    public void addRelation(int id1,int id2,int value)
        throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new Mypinf(id1);
        } else if (!contains(id2)) {
            throw new Mypinf(id2);
        } else if (this.vertices.get(id1).isLinked(id2)) {
            throw new Myer(id1,id2);
        } else {
            this.record.clear();
            this.vertices.get(id1).setAcquaintance(id2,value);
            this.vertices.get(id2).setAcquaintance(id1,value);
            int f1 = fathers.get(id1);
            int f2 = fathers.get(id2);
            if (ranks.get(f1) <= ranks.get(f2)) {
                fathers.put(f1,f2);
            } else {
                fathers.put(f2,f1);
            }
            if (Objects.equals(ranks.get(f1), ranks.get(f2)) && f1 != f2) {
                ranks.put(f2,ranks.get(f2) + 1);
            }
            fathers.replaceAll((i,v) -> find(i));
        }
    }

    private int find(int id) {
        if (id == fathers.get(id)) {
            return id;
        } else {
            int result = find(fathers.get(id));
            fathers.put(id,result);
            return result;
        }
    }

    public int queryValue(int id1,int id2)
        throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new Mypinf(id1);
        } else if (!contains(id2)) {
            throw new Mypinf(id2);
        } else if (!vertices.get(id1).isLinked(id2)) {
            throw new Myrnf(id1,id2);
        } else {
            return vertices.get(id1).queryValue(id2);
        }
    }

    public boolean isCircle(int id1,int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new Mypinf(id1);
        } else if (!contains(id2)) {
            throw new Mypinf(id2);
        } else { //bfs
            return find(id1) == find(id2);
        }
    }

    public int queryBlockSum() {
        if (!record.get(0)) {
            HashSet<Integer> result = new HashSet<>();
            for (int id : fathers.keySet()) {
                result.add(find(id));
            }
            record.set(0);
            qbs = result.size();
            return result.size();
        } else {
            return qbs;
        }
    }
    
    public int queryTripleSum() {
        if (!record.get(1)) {
            HashMap<Integer, Set<Integer>> data = new HashMap<>();
            for (Map.Entry<Integer, Vertex> id2v : vertices.entrySet()) {
                data.put(id2v.getKey(), id2v.getValue().getAcquaintances());
            }
            int result = triangleCount(data);
            record.set(1);
            qts = result;
            return result;
        } else {
            return qts;
        }
    }

    private int triangleCount(HashMap<Integer, Set<Integer>> data) {
        int result = 0;
        for (Map.Entry<Integer,Set<Integer>> i : data.entrySet()) {
            List<Integer> doubleAccess = new ArrayList<>();
            for (int j : i.getValue()) {
                doubleAccess.addAll(data.get(j));
            }
            for (int k : doubleAccess) {
                if (data.get(k).contains(i.getKey())) {
                    ++result;
                }
            }
        }
        return result / 6;
    }

    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData
            , HashMap<Integer, HashMap<Integer, Integer>> afterData, int result) {
        if (!beforeData.equals(afterData)) { //pure ok test
            return false;
        }
        HashMap<Integer,Set<Integer>> data = new HashMap<>();
        for (Map.Entry<Integer,HashMap<Integer,Integer>> i : beforeData.entrySet()) {
            data.put(i.getKey(),i.getValue().keySet());
        }
        return triangleCount(data) == result;
    }
}

