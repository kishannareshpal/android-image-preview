package com.greentoad.turtlebody.imagepreview.core

import android.net.Uri
import java.io.Serializable

/**
 * Created by WANGSUN on 08-May-19.
 */
class ImagePreviewConfig: Serializable {
    var mAllowAddButton: Boolean = false
    private set // the setter is private and has the default implementation

    companion object{
        val ARG_BUNDLE = javaClass.canonicalName + ".bundle_arg"
    }

    var mUriList: List<Uri> = arrayListOf()
    private set

    var isDoneButtonVisible: Boolean = false
    private set

    var startingPosition: Int = 0
    private set

    /**
     * @param value: false to hide add button, true to show add button
     */
    fun setAllowAddButton(value: Boolean): ImagePreviewConfig{
        mAllowAddButton = value
        return this
    }

    /**
     * @param value: array of uri to be sent for preview
     */
    fun setUris(value: List<Uri>): ImagePreviewConfig{
        mUriList = value
        return this
    }

    /**
     * @param position: must be less than mUriList size.
     */
    fun setStartingPosition(position: Int): ImagePreviewConfig {
        startingPosition = position
        return this;
    }

    /**
     * @param isVisible: false to hide the done button, true to show done button
     */
    fun setDoneButtonVisible(isVisible: Boolean): ImagePreviewConfig{
        isDoneButtonVisible = isVisible;
        return this
    }
}