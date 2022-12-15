package com.example.marvel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharactersDataList(val offset: Int,
                              val limit: Int,
                              val total: Int,
                              val count: Int,
                              val results: ArrayList<Character>?) : Parcelable
