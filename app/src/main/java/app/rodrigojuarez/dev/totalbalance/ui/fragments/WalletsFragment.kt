package app.rodrigojuarez.dev.totalbalance.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.ui.activities.AddWalletActivity
import app.rodrigojuarez.dev.totalbalance.ui.activities.HomeActivity
import app.rodrigojuarez.dev.totalbalance.models.Wallet
import app.rodrigojuarez.dev.totalbalance.ui.adapters.WalletAdapter
import app.rodrigojuarez.dev.totalbalance.storage.WalletStorage
import app.rodrigojuarez.dev.totalbalance.ui.activities.EditWalletActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class WalletsFragment : Fragment() {

    private lateinit var walletStorage: WalletStorage

    private fun onEdit(wallet: Wallet) {
        val intent = Intent(context, EditWalletActivity::class.java)
        intent.putExtra("EXTRA_WALLET", wallet)
        startActivity(intent)
    }

    private fun onDelete(wallet: Wallet) {
        Log.d("Delete", "Clicked")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.are_you_sure_delete_wallet))
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.delete)) { dialog, which ->
                deleteWallet(wallet)
            }
            .show()
    }

    private fun deleteWallet(wallet: Wallet) {
        // Aquí va tu lógica para eliminar la wallet de SharedPreferences
        val wallets = walletStorage.getWallets().filter { it != wallet }
        walletStorage.saveWallets(wallets)

        // Actualizar el RecyclerView
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvWallets) ?: return
        Snackbar.make(recyclerView, getString(R.string.wallet_deleted), Snackbar.LENGTH_SHORT).show()
        loadWallets()
    }

    private fun loadWallets() {
        val wallets = walletStorage.getWallets()
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvWallets) ?: return
        val tvEmptyMessage: TextView = view?.findViewById(R.id.tvEmptyMessage) ?: return

        if (wallets.isEmpty()) {
            tvEmptyMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            (recyclerView.adapter as? WalletAdapter)?.updateData(wallets)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletStorage = WalletStorage(requireContext())
    }

    override fun onResume() {
        super.onResume()
        loadWallets()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.wallets_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val context = requireContext() // Obtiene el contexto
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
            R.id.action_add_wallet -> {
                val intent = Intent(activity, AddWalletActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvWallets)
        val tvEmptyMessage: TextView = view.findViewById(R.id.tvEmptyMessage)
        val wallets = walletStorage.getWallets()

        if (wallets.isEmpty()) {
            tvEmptyMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = WalletAdapter(wallets, this::onEdit, this::onDelete)
        }
    }

}

