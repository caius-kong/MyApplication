package com.zbiti.myapplication.sharedPreference;

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

    String userAvatarPath();
    void userAvatarPath(String userAvatarPath);

    String nickName();
    void nickName(String nickName);

    String userSign();
    void userSign(String userSign);
}
