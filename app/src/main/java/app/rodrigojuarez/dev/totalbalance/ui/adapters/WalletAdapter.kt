package app.rodrigojuarez.dev.totalbalance.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.models.Wallet

class WalletAdapter(private val wallets: List<Wallet>) :
    RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    class WalletViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWalletName: TextView = view.findViewById(R.id.tvWalletName)
        val tvWalletCurrency: TextView = view.findViewById(R.id.tvWalletCurrency)
        val tvWalletAmount: TextView = view.findViewById(R.id.tvWalletAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallet, parent, false)
        return WalletViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.tvWalletName.text = wallet.name
        holder.tvWalletCurrency.text = wallet.currency
        holder.tvWalletAmount.text = wallet.amount
    }

    override fun getItemCount() = wallets.size
}
