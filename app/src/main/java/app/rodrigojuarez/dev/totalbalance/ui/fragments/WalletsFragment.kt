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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.AddWalletActivity
import app.rodrigojuarez.dev.totalbalance.ui.adapters.WalletAdapter
import app.rodrigojuarez.dev.totalbalance.storage.WalletStorage

class WalletsFragment : Fragment() {

    private lateinit var walletStorage: WalletStorage

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

        val wallets = walletStorage.getWallets()

        val recyclerView: RecyclerView = view.findViewById(R.id.rvWallets)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = WalletAdapter(wallets)
    }
}

