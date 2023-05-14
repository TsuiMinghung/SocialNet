package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OkTest {
    public static int deleteColdEmojiOKTest(int limit,
                                            ArrayList<HashMap<Integer, Integer>> beforeData,
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
}
