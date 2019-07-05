package android.example.DressShop;
import android.content.Context;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {
    public final static String PREF_HELLO = "check_box_preference";     //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);     //Settings.xml word ingeladen.
    }

    public static boolean changeBackgroundColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_HELLO, false);
        //Stuur boolean terug naar preferenceManager (iets van Android zelf).
    }
}
