package com.zbiti.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.zbiti.myapplication.sharedpreferences.FastJsonSerial;
import com.zbiti.myapplication.sharedpreferences.UserSharedPreferences;
import de.devland.esperandro.Esperandro;

public class MainActivity extends AppCompatActivity {
    private UserSharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Serialized object preferences
        Esperandro.setSerializer(new FastJsonSerial());

        preferences = Esperandro.getPreferences(UserSharedPreferences.class, this);
        preferences.name("kyh");
        String name = preferences.name();

        Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
    }
}
