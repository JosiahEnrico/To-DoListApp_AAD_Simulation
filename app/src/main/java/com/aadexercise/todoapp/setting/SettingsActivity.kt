package com.aadexercise.todoapp.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.aadexercise.todoapp.R
import com.aadexercise.todoapp.notification.NotificationWorker
import com.aadexercise.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.aadexercise.todoapp.utils.NightMode
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            val preferenceTheme = findPreference<ListPreference>(getString(R.string.pref_key_dark))

            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                //TODO 13 : Schedule and cancel daily reminder using WorkManager with data channelName
                val workManager = context?.let { WorkManager.getInstance(it) }

                val data = Data.Builder()
                    .putString(NOTIFICATION_CHANNEL_ID, channelName)
                    .build()
                val periodicWorkRequest = PeriodicWorkRequest.Builder(
                    NotificationWorker::class.java,
                    1, TimeUnit.DAYS)
                    .setInputData(data)
                    .build()

                if (newValue == true){
                    workManager?.enqueue(periodicWorkRequest)
                } else{
                    workManager?.cancelWorkById(periodicWorkRequest.id)
                }

                return@setOnPreferenceChangeListener true

            }

            preferenceTheme?.setOnPreferenceChangeListener { _, newValue ->
                val mode = NightMode.valueOf(newValue.toString().toUpperCase(Locale.US))
                updateTheme(mode.value)
                Toast.makeText(requireContext(), "theme changed", Toast.LENGTH_SHORT).show()
                true
            }
        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }
}