package com.example.marvel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterDetailsResponse(val code: Int,
                                    val status: String,
                                    val data: CharacterDetailsList?) :Parcelable
