package app.rodrigojuarez.dev.totalbalance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.models.CurrencyQuote
import app.rodrigojuarez.dev.totalbalance.ui.adapters.CurrencyQuoteAdapter
import app.rodrigojuarez.dev.totalbalance.utils.CurrencyApiUtil
import com.facebook.shimmer.ShimmerFrameLayout
import org.json.JSONObject

class InfoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val currencyApiUtil = CurrencyApiUtil()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCurrencyQuotes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CurrencyQuoteAdapter(emptyList())

        loadCurrencyData()

        return view
    }

    private fun loadCurrencyData() {
        currencyApiUtil.fetchCurrencyRates("https://api.bluelytics.com.ar/v2/latest") { jsonResponse ->
            activity?.runOnUiThread {
                processCurrencyData(jsonResponse)
            }
        }
    }

    private fun processCurrencyData(jsonResponse: JSONObject?) {
        val quotes = mutableListOf<CurrencyQuote>()

        // Parsear las cotizaciones del dólar
        jsonResponse?.let { json ->
            val oficialRate = json.getJSONObject("oficial")
            val blueRate = json.getJSONObject("blue")

            quotes.add(
                CurrencyQuote(
                    "Dólar Oficial",
                    oficialRate.getDouble("value_avg"),
                    1.00
                )
            )
            quotes.add(
                CurrencyQuote(
                    "Dólar Blue",
                    blueRate.getDouble("value_avg"),
                    1.00
                )
            )

            // Llamada a otra API para obtener cotización de criptomonedas (Ejemplo: Bitcoin)
            currencyApiUtil.fetchCurrencyRates("https://cex.io/api/tickers/BTC/USD") { cryptoResponse ->
                activity?.runOnUiThread {
                    cryptoResponse?.let { cryptoJson ->
                        val data = cryptoJson.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val crypto = data.getJSONObject(i)
                            if (crypto.getString("pair") == "BTC:USD") {
                                val lastPrice = crypto.getDouble("last")
                                // Supongamos que 1 Bitcoin = lastPrice USD y lo convertimos a ARS usando el dólar oficial (simplificación)
                                val lastPriceInArs = lastPrice * oficialRate.getDouble("value_avg")
                                quotes.add(CurrencyQuote("Bitcoin", lastPriceInArs, lastPrice))
                                break
                            }
                        }
                    }

                    // Actualizar el RecyclerView
                    recyclerView.adapter = CurrencyQuoteAdapter(quotes)
                }
            }
        }
    }

}