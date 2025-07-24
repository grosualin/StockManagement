package ro.alingrosu.stockmanagement.presentation.ui.auth

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ro.alingrosu.stockmanagement.R

class AuthActivity : AppCompatActivity(R.layout.activity_auth) {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState, persistentState)
    }
}