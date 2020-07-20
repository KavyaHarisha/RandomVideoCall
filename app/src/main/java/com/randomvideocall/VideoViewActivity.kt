package com.randomvideocall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.android.synthetic.main.activity_video_view.*

class VideoViewActivity : AppCompatActivity() {
    private var mRtcEngine: RtcEngine? = null
    private var deviceId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
    }

    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()
        joinChannel()
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, getString(R.string.agora_app_id),
                mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(LOG_TAG, Log.getStackTraceString(e))
        }
    }

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {

        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            runOnUiThread {
                setupRemoteVideo(uid)
                deviceId = uid
            }
        }


        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread {
                onRemoteUserLeft()
                endVideoCall(btn_end_call)
                }
        }


        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            runOnUiThread { onRemoteUserVideoMuted(uid, muted)}
        }
    }

    private fun setupRemoteVideo(uid: Int) {
        val container = findViewById<FrameLayout>(R.id.video_frame_container)

        if (container.childCount >= 1) {
            return
        }

        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        container.addView(surfaceView)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
        surfaceView.tag = uid
    }

    private fun onRemoteUserLeft() {
        val container = findViewById<FrameLayout>(R.id.video_frame_container)
        container.removeAllViews()
    }

    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container = findViewById<FrameLayout>(R.id.video_frame_container)

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }

    private fun setupVideoProfile() {
        mRtcEngine!!.enableVideo()
        mRtcEngine!!.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT)
        )
    }

    private fun setupLocalVideo() {
        val container = findViewById<FrameLayout>(R.id.local_video_view_container)
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun joinChannel() {
        var token: String? = getString(R.string.agora_access_token)
        if (token!!.isEmpty()) {
            token = null
        }
        mRtcEngine!!.joinChannel(token, "demoChannel1",
            "Extra Optional Data", 0)
    }

    fun endVideoCall(view: View) {
        finish()
    }

    companion object {
        private val LOG_TAG = VideoViewActivity::class.java.simpleName
        private const val CHECK_VIDEO_TIME_OUT =15000
    }

    override fun onResume() {
        super.onResume()
        randomButtonClick()
    }

    private fun randomButtonClick() {
        btn_random_call.setOnClickListener {
            videoConnectionTimeOut()
            btn_random_call.visibility = View.GONE
            video_frame_container.visibility = View.VISIBLE
            local_video_view_container.visibility = View.VISIBLE
            btn_end_call.visibility = View.VISIBLE
            initAgoraEngineAndJoinChannel()
        }
    }

    private fun videoConnectionTimeOut() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (deviceId == 0) {
                endVideoCall(btn_end_call)
                Toast.makeText(this, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show()
            }
        }, CHECK_VIDEO_TIME_OUT.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        RtcEngine.destroy()
        mRtcEngine = null
    }
}