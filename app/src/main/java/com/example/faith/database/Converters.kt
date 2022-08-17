package com.example.faith.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bmp: Bitmap?): ByteArray? {

        val outputStream = ByteArrayOutputStream()
        bmp?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(bytes: ByteArray?): Bitmap? {
        return if (bytes != null) {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else {
            null
        }
    }
}