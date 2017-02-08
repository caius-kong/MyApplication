package com.zbiti.myapplication.sharedPreference;

import java.util.List;

import de.devland.esperandro.SharedPreferenceActions;
import de.devland.esperandro.SharedPreferenceMode;
import de.devland.esperandro.annotations.SharedPreferences;

/**
 * Created by admin on 2016/6/27.
 */
@SharedPreferences(name="item", mode= SharedPreferenceMode.PRIVATE)
public interface ItemSharedPreference extends SharedPreferenceActions {
    String name();
    void name(String name);

    float price();
    void price(float price);

    String content();
    void content(String content);

    List<String> imageList();
    void imageList(List<String> imageList);
}
