package com.example.marvel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CharacterDetail(val id: Long,
                           val title: String,
                           val resourceURI: String?) : Parcelable