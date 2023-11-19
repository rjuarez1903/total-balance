package app.rodrigojuarez.dev.totalbalance.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.models.Wallet

class WalletAdapter(private var wallets: List<Wallet>,
                    private val onEdit: (Wallet) -> Unit,
                    private val onDelete: (Wallet) -> Unit) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {
    // ...

    class WalletViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWalletName: TextView = view.findViewById(R.id.tvWalletName)
        val tvWalletCurrency: TextView = view.findViewById(R.id.tvWalletCurrency)
        val tvWalletAmount: TextView = view.findViewById(R.id.tvWalletAmount)
        val menuOptionsButton: ImageView = view.findViewById(R.id.menuOptionsButton)
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

        holder.menuOptionsButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.menu_edit_delete)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        onEdit(wallet)
                        true
                    }
                    R.id.action_delete -> {
                        onDelete(wallet)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

    }

    override fun getItemCount() = wallets.size

    fun updateData(newWallets: List<Wallet>) {
        wallets = newWallets
        notifyDataSetChanged()
    }
}
