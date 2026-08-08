package com.expensetracker.app.camera

import android.content.Context
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Photo capture is deliberately implemented via the system Camera app
 * (ACTION_IMAGE_CAPTURE) rather than embedding a CameraX preview. That
 * trade-off means: no camera preview UI to build ourselves, no CAMERA
 * runtime permission to request (the Camera app already has it), and one
 * fewer heavy dependency — at the cost of not being able to customize the
 * capture UI. For a receipt-photo feature that's a good trade.
 */
object PhotoUtils {

    /** Creates an empty, uniquely-named jpg file in the app's private storage. */
    fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = File(context.filesDir, "receipts").apply { mkdirs() }
        return File(storageDir, "receipt_$timestamp.jpg")
    }

    /**
     * Converts a private file path into a content:// URI the Camera app is
     * allowed to write into, via the FileProvider declared in the manifest.
     */
    fun getUriForFile(context: Context, file: File) =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
