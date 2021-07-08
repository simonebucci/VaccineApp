package it.mspc.vaccinedata.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import it.mspc.vaccinedata.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val nightMode: Preference = findPreference(getString(R.string.night_mode_pref))!!
        //nightMode.setOnPreferenceChangeListener(this)
    }
    /*
    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference.key == getString(R.string.first_use_pref)) { // Display custom dialog
            val dialogFragment: it.mspc.vaccinedata.ui.settings.SettingsFragment.FragmentResetDataDialog =
                it.mspc.vaccinedata.ui.settings.SettingsFragment.FragmentResetDataDialog
                    .newInstance(preference.key)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, null)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
    */
}