package app.rodrigojuarez.dev.totalbalance.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.models.CurrencyQuote


class CurrencyQuoteAdapter(private val quotes: List<CurrencyQuote>) :
    RecyclerView.Adapter<CurrencyQuoteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCurrencyName: TextView = view.findViewById(R.id.tvCurrencyName)
        val tvValueInARS: TextView = view.findViewById(R.id.tvValueInARS)
        val tvValueInUSD: TextView = view.findViewById(R.id.tvValueInUSD)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency_quote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]
        holder.tvCurrencyName.text = quote.currencyName
        holder.tvValueInARS.text = "ARS ${quote.valueInARS}"
        holder.tvValueInUSD.text = "USD ${quote.valueInUSD}"
    }

    override fun getItemCount() = quotes.size
}
