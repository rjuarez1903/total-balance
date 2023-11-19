package app.rodrigojuarez.dev.totalbalance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import app.rodrigojuarez.dev.totalbalance.models.Wallet
import app.rodrigojuarez.dev.totalbalance.storage.WalletStorage

class AddWalletActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wallet)

        val spinnerCurrency: Spinner = findViewById(R.id.spinnerCurrency)
        val editTextWalletName: EditText = findViewById(R.id.editTextWalletName)
        val editTextAmount: EditText = findViewById(R.id.editTextAmount)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        val walletStorage = WalletStorage(this)

        buttonSave.setOnClickListener {
            val name = editTextWalletName.text.toString()
            val amount = editTextAmount.text.toString()
            val currency = spinnerCurrency.selectedItem.toString()

            if (name.isNotBlank() && amount.isNotBlank() && currency.isNotBlank() && currency != getString(
                    R.string.currency_hint
                )
            ) {
                val newWallet = Wallet(name, currency, amount)
                val currentWallets = walletStorage.getWallets().toMutableList()
                currentWallets.add(newWallet)
                walletStorage.saveWallets(currentWallets)

                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("open_wallets_tab", true)
                startActivity(intent)
                finish()
            } else {
                // TODO Manejar campos vacíos o selección inválida
            }
        }

        val currencyList =
            listOf(getString(R.string.currency_hint), "USD", "EUR", "BTC") // Añade tus monedas aquí
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = adapter

        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    // TODO El hint está seleccionado, manejar como sea necesario
                } else {
                    // TODO Se seleccionó una moneda, manejar la selección
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // TODO Otra lógica si se necesita
            }
        }

        // TODO Configuración de otros componentes de la UI
        // ...
    }
}

