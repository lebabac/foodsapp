package com.example.food_ordering_app
import android.os.Parcel
import android.os.Parcelable

data class Order(
    val id: Int,
    val fullName: String,
    val phoneNumber: String,
    val address: String,
    val quantity: Int,
    val paymentMethod: String,
    val totalPrice: Double,
    val userId: Int,
    val foodId: Int,
    var status: String,
    var foodName: String? = null,
    var foodPrice: Double? = null,
    var foodImage: ByteArray? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readDouble(),
        parcel.createByteArray()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(fullName)
        parcel.writeString(phoneNumber)
        parcel.writeString(address)
        parcel.writeInt(quantity)
        parcel.writeString(paymentMethod)
        parcel.writeDouble(totalPrice)
        parcel.writeInt(userId)
        parcel.writeInt(foodId)
        parcel.writeString(status)
        parcel.writeString(foodName)
        parcel.writeDouble(foodPrice ?: 0.0)
        parcel.writeByteArray(foodImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}
