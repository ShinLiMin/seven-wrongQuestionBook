package com.seven.wqb.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.seven.wqb.dao.SensitiveWordDao;
import com.seven.wqb.domain.SensitiveWord;
import com.seven.wqb.domain.TireTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
@Component
public class SensitiveWordUtils {

    @Autowired
    private SensitiveWordDao sensitiveWordDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String key = "sensitive words";

    public void addWord(String value) {
        SensitiveWord sensitiveWord = new SensitiveWord();
        SensitiveWord sensitiveWordDB = sensitiveWordDao.getWordByVal(value);
        if (sensitiveWordDB == null) {
            sensitiveWord.setCreateTime(new Date());
            sensitiveWord.setValue(value);
            sensitiveWordDao.insertWord(sensitiveWord);
            List<SensitiveWord> sensitiveWords = sensitiveWordDao.getAllWord();
            List<String> words = new LinkedList<>();
            for (SensitiveWord word : sensitiveWords) {
                words.add(word.getValue());
            }
            TireTree root = buildTree(words);
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(root));
        }
    }

    //todo
    public void addWords(List<String> words) {

    }

    public void deleteWord(String value) {
        sensitiveWordDao.deleteWord(value);
    }

    //todo
    public void deleteWords(List<String> words) {

    }

    public Boolean checkContext(String context) {
        String treeStr = redisTemplate.opsForValue().get(key);
        TireTree cur = JSONObject.parseObject(treeStr, TireTree.class);
        for (int i = 0; i < context.length(); i++) {
            TireTree fail = cur.fail;
            char c = context.charAt(i);
            cur = cur.child.get(c);
            if (cur == null) {
                cur = fail;
            }
            if (cur.isEnd) return false;
        }
        return true;
    }

    public Boolean check(String context) {
        List<SensitiveWord> sensitiveWords = sensitiveWordDao.getAllWord();
        List<String> words = new LinkedList<>();
        for (SensitiveWord sensitiveWord : sensitiveWords) {
            words.add(sensitiveWord.getValue());
        }
        TireTree cur = buildTree(words);
        for (int i = 0; i < context.length(); i++) {
            TireTree fail = cur.fail;
            char c = context.charAt(i);
            cur = cur.child.get(c);
            if (cur == null) {
                cur = fail;
            }
            if (cur.isEnd) return false;
        }
        return true;
    }



    public TireTree buildTree(List<String> words) {
        TireTree root = new TireTree(new HashMap<>());
        initTreeNode(words, root);
        buildFail(root);
        return root;
    }


    public void initTreeNode(List<String> words, TireTree root) {
        root.fail = root;
        for (String word : words) {
            TireTree cur = root;
            char[] array = word.toCharArray();
            for (char c : array) {
                if (!cur.child.containsKey(c)) {
                    cur.child.put(c, new TireTree(new HashMap<>()));
                }
                cur = cur.child.get(c);
            }
            cur.isEnd = true;
        }
    }

    public static void buildFail(TireTree root) {
        Queue<TireTree> q = new LinkedList<>();
        for (char c : root.child.keySet()) {
            TireTree child = root.child.get(c);
            child.fail = root;
            q.offer(child);
        }
        while (!q.isEmpty()) {
            TireTree cur = q.poll();
            for (char c : cur.child.keySet()) {
                TireTree cursChild = cur.child.get(c);
                TireTree cursFail = cur.fail;
                if (cursFail.child.containsKey(c)) {
                    TireTree childFail = cursFail.child.get(c);
                    cursChild.fail = childFail;
                } else {
                    cursChild.fail = root;
                }
                q.offer(cursChild);
            }
        }
    }


}
