package app.rodrigojuarez.dev.totalbalance.storage

import android.content.Context
import app.rodrigojuarez.dev.totalbalance.models.Wallet
import org.json.JSONArray
import org.json.JSONObject

class WalletStorage(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

    fun saveWallets(wallets: List<Wallet>) {
        val jsonArray = JSONArray()
        wallets.forEach { wallet ->
            jsonArray.put(walletToJson(wallet))
        }

        val editor = sharedPreferences.edit()
        editor.putString("wallets", jsonArray.toString())
        editor.apply()
    }

    fun updateWallet(updatedWallet: Wallet) {
        val wallets = getWallets().map { if (it.id == updatedWallet.id) updatedWallet else it }
        saveWallets(wallets)
    }

    fun getWallets(): List<Wallet> {
        val json = sharedPreferences.getString("wallets", null)
        if (json != null) {
            val jsonArray = JSONArray(json)
            val wallets = mutableListOf<Wallet>()
            for (i in 0 until jsonArray.length()) {
                wallets.add(jsonToWallet(jsonArray.getJSONObject(i)))
            }
            return wallets
        } else {
            return emptyList()
        }
    }

    fun walletToJson(wallet: Wallet): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("id", wallet.id)
        jsonObject.put("name", wallet.name)
        jsonObject.put("currency", wallet.currency)
        jsonObject.put("amount", wallet.amount)
        return jsonObject
    }

    fun jsonToWallet(jsonObject: JSONObject): Wallet {
        val id = jsonObject.getString("id")
        val name = jsonObject.getString("name")
        val currency = jsonObject.getString("currency")
        val amount = jsonObject.getString("amount")
        return Wallet(id, name, currency, amount)
    }

}
