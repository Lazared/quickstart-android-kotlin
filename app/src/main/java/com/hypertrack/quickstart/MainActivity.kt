package com.hypertrack.quickstart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hypertrack.sdk.HyperTrack
import com.hypertrack.sdk.TrackingError
import com.hypertrack.sdk.TrackingStateObserver
import java.util.Collections.emptyMap


class MainActivity : AppCompatActivity(), TrackingStateObserver.OnTrackingStateChangeListener, View.OnClickListener {

    private lateinit var trackingSwitcher: Button
    private lateinit var deviceId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trackingSwitcher = findViewById(R.id.trackingButton)
        deviceId = findViewById(R.id.deviceIdTextView)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        HyperTrack.enableDebugLogging()
        updateUi()

    }

    // OnClickListener

    override fun onClick(view: View) {

        Log.d(TAG, "Tracking button pressed")
        if (HyperTrack.isTracking()) {
            HyperTrack.stopTracking()
            trackingSwitcher.text = getString(R.string.resume_tracking)
        } else {
            HyperTrack.initialize(this, PUBLISHABLE_KEY)
            HyperTrack.addTrackingStateListener(this)
            HyperTrack.setNameAndMetadataForDevice("Elvis", emptyMap())
        }
    }

    private fun updateUi() {
        Log.d(TAG, "Updating UI")
        deviceId.text = HyperTrack.getDeviceId()

        trackingSwitcher.isEnabled = true
        if (HyperTrack.isTracking()) {
            trackingSwitcher.text = getString(R.string.pause_tracking)
        } else {
            trackingSwitcher.text = getString(R.string.resume_tracking)
        }
    }

    // TrackingStateObserver.OnTrackingStateChangeListener interface methods

    override fun onTrackingStart() {
        deviceId.text = HyperTrack.getDeviceId()
        trackingSwitcher.text = getString(R.string.pause_tracking)

    }

    override fun onError(p0: TrackingError?) {
        Log.d(TAG, "Initialization failed with error " + p0?.message)
        trackingSwitcher.text = getString(R.string.resume_tracking)
    }

    override fun onTrackingStop() {
        trackingSwitcher.text = getString(R.string.resume_tracking)
    }

    companion object {

        private const val TAG = "MainActivity"
        private const val PUBLISHABLE_KEY = "paste_your_key_here"
    }
}
