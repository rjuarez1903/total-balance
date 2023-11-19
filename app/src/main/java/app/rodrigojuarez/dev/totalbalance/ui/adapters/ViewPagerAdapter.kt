package app.rodrigojuarez.dev.totalbalance.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.rodrigojuarez.dev.totalbalance.ui.fragments.DashboardFragment
import app.rodrigojuarez.dev.totalbalance.ui.fragments.InfoFragment
import app.rodrigojuarez.dev.totalbalance.ui.fragments.WalletsFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardFragment()
            1 -> WalletsFragment()
            2 -> InfoFragment()
            else -> Fragment()
        }
    }
}
