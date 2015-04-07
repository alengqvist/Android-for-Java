package android.assignment_2;

import java.util.List;

import android.assignment_2.R;
import android.preference.PreferenceActivity;

public class CountryPreference extends PreferenceActivity {
	
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
	
	@Override
	protected boolean isValidFragment (String fragmentName) {
	  return (CountryPreferenceFragment.class.getName().equals(fragmentName));
	}
}
