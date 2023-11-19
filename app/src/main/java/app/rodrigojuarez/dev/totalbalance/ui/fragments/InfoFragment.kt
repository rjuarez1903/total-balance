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

class InfoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCurrencyQuotes)

        // Datos dummy para cotizaciones
        val dummyQuotes = listOf(
            CurrencyQuote("Dólar", 100.0, 1.0),
            CurrencyQuote("Bitcoin", 3000000.0, 30000.0)
            // Agrega más si lo necesitas
        )

        // Configuración del RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CurrencyQuoteAdapter(dummyQuotes)

        return view
    }
}

