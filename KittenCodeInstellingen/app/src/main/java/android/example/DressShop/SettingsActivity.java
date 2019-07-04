package android.example.DressShop;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
;
import android.os.Bundle;


public class SettingsActivity extends PreferenceActivity {
    public final static String BG_COLOR = "@android:color/blue";  //Gebruik background color

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);     //Krijg Preference Lay-out

    }

    public static boolean CHANGE_BC(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BG_COLOR, false); //chane background color to non default
    }

}