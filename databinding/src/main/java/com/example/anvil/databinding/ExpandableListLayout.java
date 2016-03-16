package com.example.anvil.databinding;

import android.content.Context;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;

import java.util.*;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

public class ExpandableListLayout extends RenderableView {
    private static final String NAME = "NAME";
    private final static String[] GROUP = {"Veggies" , "Fruits"};
    private final static String[][] CHILD = {{"Carrot", "Cucumber", "Beet"}, {"Orange", "Apple", "Cherry"}};

    private ExpandableListAdapter mAdapter;

    boolean[] expanded;

    public ExpandableListLayout(Context c) {
        super(c);

        expanded = new boolean[GROUP.length];
        List<Map<String, String>> groupData = new ArrayList<>();
        List<List<Map<String, String>>> childData = new ArrayList<>();
        for (int i = 0; i < GROUP.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            groupData.add(map);
            map.put(NAME, GROUP[i]);

            List<Map<String, String>> children = new ArrayList<>();
            for (int j = 0; j < CHILD[i].length; j++) {
                Map<String, String> childMap = new HashMap<String, String>();
                children.add(childMap);
                childMap.put(NAME, CHILD[i][j]);
            }
            childData.add(children);
            expanded[i] = false;
        }

        mAdapter = new SimpleExpandableListAdapter(c, groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { NAME }, new int[] { android.R.id.text1 },
                childData, android.R.layout.simple_expandable_list_item_2,
                new String[] { NAME }, new int[] { android.R.id.text1 });
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);

            list();
            list();
        });
    }

    private void list() {
        expandableListView(() -> {
            size(0, FILL);
            weight(1);
            adapter(mAdapter);
            for (int i = 0; i < GROUP.length; i++) {
                if (expanded[i]) {
                    ((ExpandableListView) Anvil.currentView()).expandGroup(i);
                } else {
                    ((ExpandableListView) Anvil.currentView()).collapseGroup(i);
                }
            }
            onGroupCollapse(this::onColapsed);
            onGroupExpand(this::onExpanded);
        });
    }

    public void onColapsed(int groupPos) {
        expanded[groupPos] = false;
    }

    public void onExpanded(int groupPos) {
        expanded[groupPos] = true;
    }
}




