package com.example.marvel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(val id: Long,
                     val name: String?,
                     val description: String?,
                     val thumbnail: Thumbnail?) : Parcelable

