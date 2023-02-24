package com.example.musicplayer

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.musicplayer.databinding.ActivityMainBinding

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playButton.setOnClickListener { checkPermission(binding.playButton) }
        binding.pauseButton.setOnClickListener { checkPermission(binding.pauseButton) }
        binding.stopButton.setOnClickListener { checkPermission(binding.stopButton) }
    }

    private fun mediaPlayerPlay() {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PLAY
        }
        startService(intent)
    }

    private fun mediaPlayerPause() {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PAUSE
        }
        startService(intent)
    }

    private fun mediaPlayerStop() {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_STOP
        }
        startService(intent)
    }

    private fun checkPermission(button: ImageView) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                NOTIFICATION_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                when (button) {
                    binding.playButton -> mediaPlayerPlay()
                    binding.pauseButton -> mediaPlayerPause()
                    binding.stopButton -> mediaPlayerStop()
                }
            }
            shouldShowRequestPermissionRationale(NOTIFICATION_PERMISSION) -> {
                showPermissionDialog()
            }
            else -> {
                requestPermission()
            }
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("앱의 알림 기능을 사용하기 위한 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("허용") { _, _ ->
                requestPermission()
            }
        }.show()
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(NOTIFICATION_PERMISSION), NOTIFICATION_PERMISSION_CODE)
    }

    override fun onDestroy() {
        stopService(Intent(this, MediaPlayerService::class.java))
        super.onDestroy()
    }

    companion object {
        val NOTIFICATION_PERMISSION = android.Manifest.permission.POST_NOTIFICATIONS
        val NOTIFICATION_PERMISSION_CODE = 100
    }
}