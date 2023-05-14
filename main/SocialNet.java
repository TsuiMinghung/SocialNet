package main;

import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import exceptions.Myanf;
import exceptions.Myeei;
import exceptions.Myegi;
import exceptions.Myeinf;
import exceptions.Myemi;
import exceptions.Myepi;
import exceptions.Myer;
import exceptions.Myginf;
import exceptions.Myminf;
import exceptions.Mypinf;
import exceptions.Mypnf;
import exceptions.Myrnf;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final HashMap<Integer,Integer> emojiId2Heat;
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
        this.emojiId2Heat = new HashMap<>();
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
                    Vertex.fetch(person.getId()));
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
            this.vertices.get(id1).addAcquaintance(id2,value);
            this.vertices.get(id2).addAcquaintance(id1,value);
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
            EqualPersonIdException, EmojiIdNotFoundException {
        if (messages.containsKey(message.getId())) {
            throw new Myemi(message.getId());
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new Myeinf(message.getId());
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
                if (!message.getPerson1().isLinked(message.getPerson2())) {
                    throw new Myrnf(message.getPerson1().getId(),message.getPerson2().getId());
                } else {
                    Vertex v1 = vertices.get(message.getPerson1().getId());
                    v1.addSocialValue(message.getSocialValue());
                    Vertex v2 = vertices.get(message.getPerson2().getId());
                    v2.addSocialValue(message.getSocialValue());
                    if (message instanceof RedEnvelopeMessage) {
                        v1.addMoney(-((RedEnvelopeMessage) message).getMoney());
                        v2.addMoney(((RedEnvelopeMessage) message).getMoney());
                    } else if (message instanceof EmojiMessage) {
                        emojiId2Heat.put(((EmojiMessage) message).getEmojiId(), emojiId2Heat
                                .get(((EmojiMessage) message).getEmojiId()) + 1);
                    }
                    vertices.get(message.getPerson2().getId()).recvMessage(message);
                }
            } else if (message.getType() == 1) {
                if (!message.getGroup().hasPerson(message.getPerson1())) {
                    throw new Mypinf(message.getPerson1().getId());
                } else {
                    for (int pid : groups.get(message.getGroup().getId()).getMembers()) {
                        vertices.get(pid).addSocialValue(message.getSocialValue());
                    }
                    if (message instanceof RedEnvelopeMessage) {
                        int money = ((RedEnvelopeMessage) message).getMoney()
                                / message.getGroup().getSize();
                        message.getPerson1().addMoney(-(money * message.getGroup().getSize() - 1));
                        for (int pid : groups.get(message.getGroup().getId()).getMembers()) {
                            if (pid != message.getPerson1().getId()) {
                                vertices.get(pid).addMoney(money);
                            }
                        }
                    } else if (message instanceof EmojiMessage) {
                        emojiId2Heat.put(((EmojiMessage) message).getEmojiId(), emojiId2Heat
                                .get(((EmojiMessage) message).getEmojiId()) + 1);
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
            return vertices.get(id).getReceivedMessages();
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
                updateDisjoint(id1);
                updateDisjoint(id2);

            } else {
                vertices.get(id1).addAcquaintance(id2,value);
                vertices.get(id2).addAcquaintance(id1,value);
            }
        }
    }

    private void updateDisjoint(int id) {
        Vertex v = vertices.get(id);
        HashSet<Integer> visited = new HashSet<>(v.getAcquaintances());
        visited.add(id);
        fathers.put(id,id);
        ranks.put(id,1);
        Queue<Integer> queue = new LinkedList<>(v.getAcquaintances());
        while (!queue.isEmpty()) {
            int current = queue.poll();
            fathers.put(current,id);
            ranks.put(current,2);
            for (int neighbor : vertices.get(current).getAcquaintances()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
    }

    public boolean containsEmojiId(int id) {
        return emojiId2Heat.containsKey(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiId2Heat.containsKey(id)) {
            throw new Myeei(id);
        } else {
            emojiId2Heat.put(id,0);
        }
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new Mypinf(id);
        } else {
            return vertices.get(id).getMoney();
        }
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new Myeinf(id);
        } else {
            return emojiId2Heat.get(id);
        }
    }

    public int deleteColdEmoji(int limit) {
        emojiId2Heat.entrySet().stream().filter(k ->
                (k.getValue() < limit)).map(Map.Entry::getKey).collect(Collectors.toList()).
                forEach(emojiId2Heat.keySet()::remove);
        messages.entrySet().stream().filter(k -> (k.getValue() instanceof EmojiMessage &&
                !emojiId2Heat.containsKey(((EmojiMessage) k.getValue()).getEmojiId()))
        ).map(Map.Entry::getKey).collect(Collectors.toList()).forEach(messages.keySet()::remove);
        return emojiId2Heat.size();
    }

    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new Mypinf(personId);
        } else {
            vertices.get(personId).clearNotices();
        }
    }

    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        if (!contains(id)) {
            throw new Mypinf(id);
        } else {
            Vertex v = vertices.get(id);
            int result = Integer.MAX_VALUE;

            for (int neighborId : v.getAcquaintances()) {
                int ret = dijkstra(id,neighborId);
                if (ret == Integer.MAX_VALUE) {
                    continue;
                }
                result = Math.min(result,ret + v.queryValue(neighborId));
            }
            if (result == Integer.MAX_VALUE) {
                throw new Mypnf(id);
            } else {
                return result;
            }
        }
    }

    private int dijkstra(int src,int dst) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        HashMap<Integer,Integer> id2dist = new HashMap<>();
        for (int id : vertices.keySet()) {
            id2dist.put(id,Integer.MAX_VALUE);
        }
        id2dist.put(src,0);
        pq.add(new Node(src, 0));
        HashSet<Integer> settled = new HashSet<>();
        while (!settled.contains(dst)) {
            if (pq.isEmpty()) {
                return id2dist.getOrDefault(dst,Integer.MAX_VALUE);
            }
            int u = pq.remove().node;
            if (settled.contains(u)) {
                continue;
            }
            settled.add(u);
            for (int v : vertices.get(u).getAcquaintances()) {
                if ((u == src && v == dst) || (u == dst && v == src)) {
                    continue;
                }
                int newDist = id2dist.get(u) + vertices.get(u).queryValue(v);
                if (newDist < id2dist.get(v)) {
                    id2dist.put(v,newDist);
                }
                pq.add(new Node(v, id2dist.get(v)));
            }
        }
        return id2dist.get(dst);
    }

    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                                     ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        HashMap<Integer,Integer> oldEmoji = beforeData.get(0);
        HashMap<Integer,Integer> newEmoji = afterData.get(0);
        HashMap<Integer,Integer> newMessage = afterData.get(1);
        int length = 0;
        for (Map.Entry<Integer,Integer> id2heat : oldEmoji.entrySet()) {
            if (id2heat.getValue() >= limit) {
                ++length;
                if (!newEmoji.containsKey(id2heat.getKey())) {
                    return 1;
                }
            }
        }
        for (Map.Entry<Integer,Integer> id2heat : newEmoji.entrySet()) {
            if (!oldEmoji.containsKey(id2heat.getKey())) {
                return 2;
            } else {
                if (!Objects.equals(oldEmoji.get(id2heat.getKey()), id2heat.getValue())) {
                    return 2;
                }
            }
        }
        if (length != newEmoji.size()) {
            return 3;
        }
        if (newEmoji.keySet().size() != newEmoji.values().size()) {
            return 4;
        }
        length = 0;
        HashMap<Integer,Integer> oldMessage = beforeData.get(1);
        for (Map.Entry<Integer,Integer> m2e : oldMessage.entrySet()) {
            if (m2e.getValue() != null && newEmoji.containsKey(m2e.getValue())) {
                if (!newMessage.containsKey(m2e.getKey())
                        || !Objects.equals(newMessage.get(m2e.getKey()), m2e.getValue())) {
                    return 5;
                }
                ++length;
            } else if (m2e.getValue() == null) {
                if (!newMessage.containsKey(m2e.getKey())
                        || !Objects.equals(newMessage.get(m2e.getKey()),m2e.getValue())) {
                    return 6;
                }
                ++length;
            }
        }
        if (length != newMessage.size()) {
            return 7;
        }
        if (result != newEmoji.size()) {
            return 8;
        }
        return 0;
    }

    public static class Node implements Comparator<Node> {
        private final int node;
        private final int cost;

        public Node(int node,int cost) {
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compare(Node node1,Node node2) {
            if (node1.cost < node2.cost) {
                return -1;
            }
            if (node1.cost > node2.cost) {
                return 1;
            }
            return 0;
        }
    }
}

