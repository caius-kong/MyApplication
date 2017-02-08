package com.zbiti.myapplication.sharedpreferences;

import de.devland.esperandro.SharedPreferenceActions;
import de.devland.esperandro.SharedPreferenceMode;
import de.devland.esperandro.annotations.SharedPreferences;

/**
 * Created by sunfusheng on 2015/6/25.
 */
@SharedPreferences(name = "user", mode = SharedPreferenceMode.PRIVATE)
public interface UserSharedPreferences extends SharedPreferenceActions {

    String name();
    void name(String name);

    String auth();
    void auth(String auth);
}
