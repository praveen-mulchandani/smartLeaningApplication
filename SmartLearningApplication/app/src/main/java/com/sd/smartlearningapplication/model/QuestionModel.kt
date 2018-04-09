package com.sd.smartlearningapplication.model

import android.os.Parcel
import android.os.Parcelable

data class QuestionModel(var question: String, var choiceOne: String,
                         var choiceTwo: String, var choiceThree: String,
                         var choiceFour: String, var difficultyLevel: Int,
                         var correctAns: String, var isDone: Boolean = false,
                         var correctlyAnswered: Int= 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(choiceOne)
        parcel.writeString(choiceTwo)
        parcel.writeString(choiceThree)
        parcel.writeString(choiceFour)
        parcel.writeInt(difficultyLevel)
        parcel.writeString(correctAns)
        parcel.writeByte(if (isDone) 1 else 0)
        parcel.writeInt(correctlyAnswered)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionModel> {
        override fun createFromParcel(parcel: Parcel): QuestionModel {
            return QuestionModel(parcel)
        }

        override fun newArray(size: Int): Array<QuestionModel?> {
            return arrayOfNulls(size)
        }
    }
}