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
import app.rodrigojuarez.dev.totalbalance.R
import app.rodrigojuarez.dev.totalbalance.ui.activities.LoginActivity
import app.rodrigojuarez.dev.totalbalance.storage.Authenticator

class DashboardFragment : Fragment() {

    private lateinit var authenticator: Authenticator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authenticator = Authenticator(requireActivity())
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
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
