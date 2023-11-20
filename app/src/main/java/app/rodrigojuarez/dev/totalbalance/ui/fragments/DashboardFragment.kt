package app.rodrigojuarez.dev.totalbalance.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.ui.activities.LoginActivity
import app.rodrigojuarez.dev.totalbalance.storage.Authenticator
import app.rodrigojuarez.dev.totalbalance.storage.WalletStorage
import app.rodrigojuarez.dev.totalbalance.utils.CurrencyApiUtil
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {

    private lateinit var authenticator: Authenticator
    private lateinit var walletStorage: WalletStorage
    private lateinit var totalTextView: TextView
    private lateinit var lastUpdateTextView: TextView
    private val currencyApiUtil = CurrencyApiUtil()

    private fun loadCurrencyData() {
        currencyApiUtil.fetchCurrencyRates("https://api.bluelytics.com.ar/v2/latest") { jsonResponse ->
            activity?.runOnUiThread {
                currencyApiUtil.fetchCurrencyRates("https://cex.io/api/tickers/BTC/USD") { cryptoResponse ->
                    activity?.runOnUiThread {
                        calculateTotal(jsonResponse, cryptoResponse)
                        updateLastUpdateTime()
                    }
                }
            }
        }
    }

    private fun calculateTotal(jsonResponse: JSONObject?, cryptoResponse: JSONObject?) {
        var totalInArs = 0.00
        val oficialRate = jsonResponse?.getJSONObject("oficial")?.getDouble("value_avg") ?: 0.00

        val btcRate = cryptoResponse?.getJSONArray("data")?.let { data ->
            (0 until data.length()).asSequence().map { data.getJSONObject(it) }
                .find { it.getString("pair") == "BTC:USD" }?.getDouble("last") ?: 0.00
        } ?: 0.0

        val wallets = walletStorage.getWallets()
        wallets.forEach { wallet ->
            val amountInOriginalCurrency = wallet.amount.toDoubleOrNull() ?: 0.00
            val amountInArs = when (wallet.currency) {
                "USD" -> amountInOriginalCurrency * oficialRate
                "BTC" -> amountInOriginalCurrency * btcRate * oficialRate
                else -> amountInOriginalCurrency
            }
            totalInArs += amountInArs
        }
        totalTextView.visibility = View.VISIBLE
        totalTextView.text = getString(R.string.total_format, totalInArs)
    }


    private fun updateLastUpdateTime() {
        val currentTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        lastUpdateTextView.text = getString(R.string.last_update_format, currentTime)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authenticator = Authenticator(requireActivity())
        walletStorage = WalletStorage(requireContext())
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        totalTextView = view.findViewById(R.id.tvTotalAmount)
        lastUpdateTextView = view.findViewById(R.id.tvLastUpdate)
        return view
    }

    override fun onResume() {
        super.onResume()
        loadCurrencyData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val context = requireContext()
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val spanString = SpannableString(menuItem.title.toString())
            spanString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.menuTextColor
                    )
                ), 0, spanString.length, 0
            )
            menuItem.title = spanString
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                authenticator.logout()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

