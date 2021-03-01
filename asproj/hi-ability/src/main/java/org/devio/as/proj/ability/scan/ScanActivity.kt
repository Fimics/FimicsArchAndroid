package org.devio.`as`.proj.ability.scan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.ability_activity_scan.*
import org.devio.`as`.proj.ability.HiAbility
import org.devio.`as`.proj.ability.R
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.util.HiDisplayUtil


internal class ScanActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_SELECT_PHOTO = 100
    }

    private lateinit var remoteView: RemoteView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ability_activity_scan)

        val rect = Rect()
        val screenWidth = HiDisplayUtil.getDisplayWidthInPx(this)
        val screenHeight = HiDisplayUtil.getDisplayHeightInPx(this)
        val scanFrameSize = HiDisplayUtil.dp2px(240f)

        rect.left = screenWidth / 2 - scanFrameSize / 2
        rect.right = screenWidth / 2 + scanFrameSize / 2
        rect.top = screenHeight / 2 - scanFrameSize / 2
        rect.bottom = screenHeight / 2 + scanFrameSize / 2

        remoteView = RemoteView.Builder().setContext(this).setBoundingBox(rect)
            .setFormat(HmsScan.ALL_SCAN_TYPE).build()

        remoteView.setOnLightVisibleCallback { visible ->
            flush_btn.visibility = if (visible) View.VISIBLE else View.GONE
        }

        remoteView.setOnResultCallback { results ->
            if (results != null && results.isNotEmpty() && results[0] != null && !TextUtils.isEmpty(
                    results[0].originalValue
                )
            ) {
                showResult(results[0])
            }
        }

        remoteView.onCreate(savedInstanceState)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        container.addView(remoteView, params)

        initOperations();
    }

    private fun initOperations() {
        back_img.setOnClickListener { finish() }
        flush_btn.setOnClickListener {
            if (remoteView.lightStatus) {
                remoteView.switchLight()
                flush_btn.setImageResource(R.drawable.scan_flashlight_off)
            } else {
                remoteView.switchLight()
                flush_btn.setImageResource(R.drawable.scan_flashlight_on)
            }
        }

        select_photo.setOnClickListener {
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SELECT_PHOTO && data != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            val results = ScanUtil.decodeWithBitmap(
                this,
                bitmap,
                HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create()
            )
            if (results != null && results.isNotEmpty() && results[0] != null && !TextUtils.isEmpty(
                    results[0].originalValue
                )
            ) {
                showResult(results[0])
            }
        }
    }

    private fun showResult(hmsScan: HmsScan) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(300)

        ScanResult.onScanResult(hmsScan)

        HiLog.e("scan_type:" + hmsScan.scanType + ",scan_reuslt：" + hmsScan.originalValue)
        finish()
    }


    override fun onStart() {
        super.onStart()
        remoteView.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView.onPause()
    }

    override fun onStop() {
        super.onStop()
        remoteView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView.onDestroy()
    }

}