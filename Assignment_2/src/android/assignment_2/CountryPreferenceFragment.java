package android.assignment_2;

import android.assignment_2.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class CountryPreferenceFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.country_prefs);
	}
}

