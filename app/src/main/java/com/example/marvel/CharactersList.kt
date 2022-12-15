package com.example.marvel

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharactersList(val code: Int,
                          val status: String,
                          val data: CharactersDataList?) : Parcelable
