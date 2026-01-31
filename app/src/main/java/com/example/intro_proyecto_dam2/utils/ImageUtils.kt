package com.example.intro_proyecto_dam2.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        // Base64 string a bytes
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        // Bytes a Bitmap image
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun encodeUriToBase64(context: Context, uri: Uri): String? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val bytes = inputStream.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
