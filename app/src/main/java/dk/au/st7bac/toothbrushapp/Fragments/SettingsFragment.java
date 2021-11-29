package dk.au.st7bac.toothbrushapp.Fragments;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import dk.au.st7bac.toothbrushapp.R;

// inspiration for settings: https://developer.android.com/guide/topics/ui/settings
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}