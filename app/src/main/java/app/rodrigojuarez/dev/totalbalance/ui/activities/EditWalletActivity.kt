package app.rodrigojuarez.dev.totalbalance.ui.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import app.rodrigojuarez.dev.totalbalance.models.Wallet
import app.rodrigojuarez.dev.totalbalance.storage.WalletStorage
import app.rodrigojuarez.dev.totalbalance.R
import com.google.android.material.snackbar.Snackbar

class EditWalletActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var spinnerCurrency: Spinner
    private lateinit var walletStorage: WalletStorage
    private var currentWallet: Wallet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wallet)

        walletStorage = WalletStorage(this)

        editTextName = findViewById(R.id.editTextEditWalletName)
        editTextAmount = findViewById(R.id.editTextEditAmount)
        spinnerCurrency = findViewById(R.id.spinnerEditCurrency)

        // Configurar el adaptador del Spinner con la lista de monedas
        val currencyList = listOf("ARS", "USD", "BTC") // Añade tus monedas aquí
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = adapter

        currentWallet = intent.getParcelableExtra("EXTRA_WALLET") as? Wallet
        currentWallet?.let {
            editTextName.setText(it.name)
            editTextAmount.setText(it.amount)
            spinnerCurrency.setSelection(currencyList.indexOf(it.currency))
        }

        val buttonSaveChanges: Button = findViewById(R.id.buttonSaveChanges)
        buttonSaveChanges.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val name = editTextName.text.toString()
        val amount = editTextAmount.text.toString()
        val currency = spinnerCurrency.selectedItem.toString()

        if (name.isNotBlank() && amount.isNotBlank() && currency.isNotBlank()) {
            currentWallet?.let {
                val updatedWallet = Wallet(it.id, name, currency, amount)
                walletStorage.updateWallet(updatedWallet)
                // TODO snackbar
                finish()
            }
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.complete_all_fields),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}
