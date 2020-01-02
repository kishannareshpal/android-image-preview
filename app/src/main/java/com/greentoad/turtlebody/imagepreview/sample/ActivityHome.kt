package com.greentoad.turtlebody.imagepreview.sample


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.greentoad.turtlebody.imagepreview.ImagePreview
import com.greentoad.turtlebody.imagepreview.core.ImagePreviewConfig
import com.greentoad.turtlebody.mediapicker.MediaPicker
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class ActivityHome : AppCompatActivity(), AnkoLogger {

    private var mImagePreview = ImagePreview.with(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initStatusBar()

        initButton()

        Glide.with(this)
            .load(R.drawable.pic_image)
            .into(activity_home_background_image)

        Glide.with(this)
            .load(R.drawable.pic_blur_back)
            .into(activity_home_background_blur)
    }

    /*
      .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
     */

    private fun initButton() {
        activity_home_single_image.setOnClickListener {
            startMediaPickerSingleImage()
        }

        activity_home_with_add_btn.setOnClickListener {
            startMediaPickerMultiImages(true)
        }

        activity_home_without_add_btn.setOnClickListener {
            startMediaPickerMultiImages(false)
        }

    }

    @SuppressLint("CheckResult")
    private fun startMediaPickerSingleImage() {
        MediaPicker.with(this, MediaPicker.MediaTypes.IMAGE)
            .setConfig(
                MediaPickerConfig()
                    .setUriPermanentAccess(true)
                    .setAllowMultiSelection(false)
                    .setShowConfirmationDialog(true)
            )
            .setFileMissingListener(object : MediaPicker.MediaPickerImpl.OnMediaListener {
                override fun onMissingFileWarning() {
                    Toast.makeText(this@ActivityHome, "some file is missing", Toast.LENGTH_LONG).show()
                }
            })
            .onResult()
            .subscribe({
                info { "success: $it" }
                info { "list size: ${it.size}" }
                startSingleImagePreview(it)
            }, {
                info { "error: $it" }
            })
    }

    @SuppressLint("CheckResult")
    private fun startMediaPickerMultiImages(allowAddBtn: Boolean) {
        MediaPicker.with(this, MediaPicker.MediaTypes.IMAGE)
            .setConfig(
                MediaPickerConfig()
                    .setUriPermanentAccess(true)
                    .setAllowMultiSelection(true)
                    .setShowConfirmationDialog(true)
                    .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            )
            .setFileMissingListener(object : MediaPicker.MediaPickerImpl.OnMediaListener {
                override fun onMissingFileWarning() {
                    Toast.makeText(this@ActivityHome, "some file is missing", Toast.LENGTH_LONG).show()
                }
            })
            .onResult()
            .subscribe({
                info { "success: $it" }
                info { "list size: ${it.size}" }
                if (allowAddBtn) {
                    startMultiImagePreviewWithAddBtn(it)
                } else {
                    startMultiImagePreviewWithoutAddBtn(it)
                }

            }, {
                info { "error: $it" }
            })
    }


    private fun startSingleImagePreview(list: List<Uri>) {
        mImagePreview
            //if you send single uri(image)..then regardless of what you set allowBtn..addButton and bottom recyclerView will be invisible
            .setConfig(ImagePreviewConfig().setAllowAddButton(true).setUris(list))
            .setListener(object : ImagePreview.ImagePreviewImpl.OnImagePreviewListener {
                override fun onDone(data: List<Uri>) {
                    info { "data: $data" }
                }

                override fun onAddBtnClicked() {
                    info { "addBtn clicked" }
                }
            })
            .start()
    }

    private fun startMultiImagePreviewWithAddBtn(list: List<Uri>) {
        mImagePreview
            .setConfig(ImagePreviewConfig().setAllowAddButton(true).setUris(list))
            .setListener(object : ImagePreview.ImagePreviewImpl.OnImagePreviewListener {
                override fun onDone(data: List<Uri>) {
                    info { "data: $data" }
                }
                override fun onAddBtnClicked() {
                    info { "addBtn clicked" }
                }
            })
            .start()
    }


    private fun startMultiImagePreviewWithoutAddBtn(list: List<Uri>) {
        mImagePreview
            .setConfig(ImagePreviewConfig().setAllowAddButton(false).setUris(list))
            .setListener(object : ImagePreview.ImagePreviewImpl.OnImagePreviewListener {
                override fun onDone(data: List<Uri>) {
                    info { "data: $data" }
                    //info { "data size: ${DocumentFile.fromSingleUri(this@ActivityHome,data[0])!!.length()}" }
                }
                override fun onAddBtnClicked() {
                    info { "addBtn clicked" }
                }
            })
            .start()
    }


    private fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.md_white_1000)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
            window.statusBarColor = Color.WHITE
        }
    }
}
