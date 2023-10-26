package com.fa.beatify.data.models

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "like")
@SuppressLint("KotlinNullnessAnnotation")
data class Like(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") @NotNull var id: Int,
    @ColumnInfo("artist_name") @NotNull var artistName: String,
    @ColumnInfo("album_name") @NotNull var albumName: String,
    @ColumnInfo("music_id") @NotNull var musicId: Int,
    @ColumnInfo("music_image") @NotNull var musicImage: String,
    @ColumnInfo("music_name") @NotNull var musicName: String,
    @ColumnInfo("music_duration") @NotNull var musicDuration: String,
    @ColumnInfo("music_preview") @NotNull var musicPreview: String
)