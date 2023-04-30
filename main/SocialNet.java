package main;

import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;
import exceptions.Myanf;
import exceptions.Myegi;
import exceptions.Myemi;
import exceptions.Myepi;
import exceptions.Myer;
import exceptions.Myginf;
import exceptions.Myminf;
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
    private final HashMap<Integer,MyMessage> messages;
    private final HashMap<Integer,MyGroup> groups;
    private int qbs;
    private int qts;
    private final BitSet record;//false means need recalculate,true means last value could be used

    private SocialNet() {
        this.vertices = new HashMap<>();
        this.fathers = new HashMap<>();
        this.ranks = new HashMap<>();
        this.record = new BitSet(2);
        this.messages = new HashMap<>();
        this.groups = new HashMap<>();
        this.record.clear();
    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
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
            throw new Myepi(person.getId());
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
            merge(id1,id2);
        }
    }

    private void merge(int id1,int id2) {
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

    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new Myegi(group.getId());
        } else {
            groups.put(group.getId(),MyGroup.fetch(group.getId()));
        }
    }

    public Group getGroup(int id) {
        return groups.getOrDefault(id,null);
    }

    public void addToGroup(int id1,int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException,EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new Myginf(id2);
        } else if (!vertices.containsKey(id1)) {
            throw new Mypinf(id1);
        } else if (groups.get(id2).hasPerson(getPerson(id1))) {
            throw new Myepi(id1);
        } else {
            groups.get(id2).addPerson(getPerson(id1));
        }
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new Myginf(id);
        } else {
            return groups.get(id).getValueSum();
        }
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new Myginf(id);
        } else {
            return groups.get(id).getAgeVar();
        }
    }

    public void delFromGroup(int id1,int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException,EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new Myginf(id2);
        } else if (!vertices.containsKey(id1)) {
            throw new Mypinf(id1);
        } else if (!groups.get(id2).hasPerson(getPerson(id1))) {
            throw new Myepi(id1);
        } else {
            groups.get(id2).delPerson(getPerson(id1));
        }
    }

    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    public void addMessage(Message message) throws EqualMessageIdException,
            EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new Myemi(message.getId());
        } else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new Myepi(message.getPerson1().getId());
        } else {
            messages.put(message.getId(),MyMessage.fetch(message.getId()));
        }
    }

    public Message getMessage(int id) {
        return messages.getOrDefault(id,null);
    }

    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException,PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new Myminf(id);
        } else {
            Message message = messages.get(id);
            if (message.getType() == 0) {
                if (message.getPerson1().isLinked(message.getPerson2())) {
                    throw new Myrnf(message.getPerson1().getId(),message.getPerson2().getId());
                } else {
                    vertices.get(message.getPerson1().getId()).
                            addSocialValue(message.getSocialValue());
                    vertices.get(message.getPerson2().getId()).
                            addSocialValue(message.getSocialValue());
                    vertices.get(message.getPerson2().getId()).recvMessage(message);
                }
            } else if (message.getType() == 1) {
                if (!message.getGroup().hasPerson(message.getPerson1())) {
                    throw new Mypinf(message.getPerson1().getId());
                } else {
                    for (int pid : groups.get(message.getGroup().getId()).getMembers()) {
                        vertices.get(pid).addSocialValue(message.getSocialValue());
                    }
                }
            }
            messages.remove(message.getId());
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new Mypinf(id);
        } else {
            return vertices.get(id).getSocialValue();
        }
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new Mypinf(id);
        } else {
            return vertices.get(id).getMessages();
        }
    }

    public int queryBestAcquaintance(int id) throws PersonIdNotFoundException,
            AcquaintanceNotFoundException {
        if (!contains(id)) {
            throw new Mypinf(id);
        } else {
            if (vertices.get(id).getAcquaintances().size() == 0) {
                throw new Myanf(id);
            } else {
                return vertices.get(id).getBestAcquaintance();
            }
        }
    }

    public int queryCoupleSum() {
        int result = 0;
        for (Vertex v : vertices.values()) {
            if (v.hasBestAcquaintance() &&
                    vertices.get(v.getBestAcquaintance()).getBestAcquaintance() == v.getId()) {
                ++result;
            }
        }
        return result / 2;
    }

    public void modifyRelation(int id1,int id2,int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new Mypinf(id1);
        } else if (!contains(id2)) {
            throw new Mypinf(id2);
        } else if (id1 == id2) {
            throw new Myepi(id1);
        } else if (!vertices.get(id1).isLinked(id2)) {
            throw new Myrnf(id1,id2);
        } else {
            record.clear();
            if (vertices.get(id1).queryValue(id2) + value <= 0) {
                vertices.get(id1).delAcquaintance(id2);
                vertices.get(id2).delAcquaintance(id1);
                updateDisjoint();
            } else {
                vertices.get(id1).setAcquaintance(id2,value);
                vertices.get(id2).setAcquaintance(id1,value);
            }
        }
    }

    private void updateDisjoint() {
        fathers.replaceAll((i, v) -> i);
        ranks.replaceAll((i, v) -> 1);
        HashSet<Edge> edges = new HashSet<>();
        for (Vertex v : vertices.values()) {
            edges.addAll(v.getEdges());
        }
        for (Edge e : edges) {
            merge(e.getV1(),e.getV2());
        }
    }

    private int extraTest(int id1,int id2,int value,
                          HashMap<Integer,HashMap<Integer,Integer>> beforeData,
                          HashMap<Integer,HashMap<Integer,Integer>> afterData) {
        if (beforeData.get(id1).get(id2) + value > 0) {
            if (!(afterData.get(id1).containsKey(id2)
                    && afterData.get(id2).containsKey(id1))) { return 4; }
            if (afterData.get(id1).get(id2)
                    != beforeData.get(id1).get(id2) + value) { return 5; }
            if (afterData.get(id2).get(id1)
                    != beforeData.get(id2).get(id1) + value) { return 6; }
            if (afterData.get(id1).size() != beforeData.get(id1).size()) { return 7; }
            if (afterData.get(id2).size() != beforeData.get(id2).size()) { return 8; }
            if (!(afterData.get(id1).keySet().equals(beforeData.get(id1).keySet()))) { return 9; }
            if (!(afterData.get(id2).keySet().equals(beforeData.get(id2).keySet()))) { return 10; }
            for (int id : afterData.get(id1).keySet()) {
                if (id != id2) {
                    if (!Objects.equals(afterData.get(id1).get(id), beforeData.get(id1).get(id2))) {
                        return 11;
                    }
                }
            }
            for (int id : afterData.get(id2).keySet()) {
                if (id != id1) {
                    if (!Objects.equals(afterData.get(id1).get(id),beforeData.get(id1).get(id))) {
                        return 12;
                    }
                }
            }
            if (afterData.get(id1).values().size()
                    != beforeData.get(id1).values().size()) { return 13; }
            if (afterData.get(id2).values().size()
                    != beforeData.get(id2).values().size()) { return 14; }
        } else if (beforeData.get(id1).get(id2) + value <= 0) {
            if (afterData.get(id1).containsKey(id2)
                    || afterData.get(id2).containsKey(id1)) { return 15; }
            if (beforeData.get(id1).values().size()
                    != afterData.get(id1).keySet().size() + 1) { return 16; }
            if (beforeData.get(id2).values().size()
                    != afterData.get(id2).keySet().size()) { return 17; }
            if (afterData.get(id1).values().size()
                    != afterData.get(id1).keySet().size()) { return 18; }
            if (afterData.get(id2).values().size()
                    != afterData.get(id2).keySet().size()) { return 19; }
            for (int id : afterData.get(id1).keySet()) {
                if ((!beforeData.get(id1).containsKey(id)) || !Objects.equals(
                        afterData.get(id1).get(id), beforeData.get(id1).get(id))) {
                    return 20;
                }
            }
            for (int id : afterData.get(id2).keySet()) {
                if ((!beforeData.get(id2).containsKey(id)) || !Objects.equals(
                        beforeData.get(id2).get(id), afterData.get(id2).get(id))) {
                    return 21;
                }
            }
        }
        return 0;
    }

    public int modifyRelationOKTest(int id1,int id2,int value,
                                    HashMap<Integer, HashMap<Integer,Integer>> beforeData,
                                    HashMap<Integer,HashMap<Integer,Integer>> afterData) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2)
                || id1 == id2 || !beforeData.get(id1).containsKey(id2)) {
            return beforeData.equals(afterData) ? 0 : -1;
        } else {
            if (!(beforeData.size() == afterData.size())) {
                return 1;
            }
            if (!(beforeData.keySet().equals(afterData.keySet()))) {
                return 2;
            }
            for (int id : beforeData.keySet()) {
                if (id != id1 && id != id2) {
                    if (!(beforeData.get(id).equals(afterData.get(id)))) {
                        return 3;
                    }
                }
            }
            return extraTest(id1,id2,value,beforeData,afterData);
        }
    }
}

