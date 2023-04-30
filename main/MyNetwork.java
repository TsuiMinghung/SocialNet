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
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {

    public MyNetwork() {

    }

    @Override
    public boolean contains(int id) {
        return SocialNet.getInstance().contains(id);
    }

    @Override
    public Person getPerson(int id) {
        return SocialNet.getInstance().getPerson(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        SocialNet.getInstance().addPerson(person);
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        SocialNet.getInstance().addRelation(id1,id2,value);
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        return SocialNet.getInstance().queryValue(id1,id2);
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        return SocialNet.getInstance().isCircle(id1,id2);
    }

    @Override
    public int queryBlockSum() {
        return SocialNet.getInstance().queryBlockSum();
    }

    @Override
    public int queryTripleSum() {
        return SocialNet.getInstance().queryTripleSum();
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        SocialNet.getInstance().modifyRelation(id1,id2,value);
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        SocialNet.getInstance().addGroup(group);
    }

    @Override
    public Group getGroup(int id) {
        return SocialNet.getInstance().getGroup(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        SocialNet.getInstance().addToGroup(id1,id2);
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        return SocialNet.getInstance().queryGroupValueSum(id);
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        return SocialNet.getInstance().queryGroupAgeVar(id);
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        SocialNet.getInstance().delFromGroup(id1,id2);
    }

    @Override
    public boolean containsMessage(int id) {
        return SocialNet.getInstance().containsMessage(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        SocialNet.getInstance().addMessage(message);
    }

    @Override
    public Message getMessage(int id) {
        return SocialNet.getInstance().getMessage(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        SocialNet.getInstance().sendMessage(id);
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        return SocialNet.getInstance().querySocialValue(id);
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        return SocialNet.getInstance().queryReceivedMessages(id);
    }

    @Override
    public int queryBestAcquaintance(int id)
            throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        return SocialNet.getInstance().queryBestAcquaintance(id);
    }

    @Override
    public int queryCoupleSum() {
        return SocialNet.getInstance().queryCoupleSum();
    }

    @Override
    public int modifyRelationOKTest(int id1, int id2, int value, HashMap<Integer,
            HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return SocialNet.getInstance().modifyRelationOKTest(id1,id2,value,beforeData,afterData);
    }
}
