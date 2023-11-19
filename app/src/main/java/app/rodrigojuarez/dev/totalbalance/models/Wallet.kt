package app.rodrigojuarez.dev.totalbalance.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Wallet(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val currency: String,
    val amount: String
) : Parcelable
