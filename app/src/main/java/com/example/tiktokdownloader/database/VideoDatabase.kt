package com.example.tiktokdownloader.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tiktokdownloader.Model.VideoModel

@Database(entities = [VideoModel::class], version = 1)
abstract class VideoDatabase: RoomDatabase() {
    abstract fun videoDAO():VideoDAO
}