package com.seven.wqb.domain;

import java.util.List;
import java.util.Map;

public class TireTree {
    public Map<Character, TireTree> child;
    public boolean isEnd;
    public TireTree fail;

    public TireTree(Map<Character, TireTree> map) {
        this.child = map;
    }

    public TireTree() {}

}
