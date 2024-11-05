package com.example.iainteractive.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.iainteractive.data.repository.DataRepository
import com.example.iainteractive.ui.viewmodel.DataViewModelFactory
import com.example.iainteractive.R
import com.example.iainteractive.db.GamesDBHelper
import com.example.iainteractive.ui.viewmodel.DataViewModel


class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000L
    private val dataViewModel: DataViewModel by viewModels {
        DataViewModelFactory(DataRepository())
    }
    val dbHelper = GamesDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        dataViewModel.games.observe(this, Observer { gamesList ->

            if (!dbHelper.isDataInserted(this)) {
                // Insertar datos iniciales
                if (!gamesList.isNullOrEmpty()) {
                    gamesList.forEach { it
                        dbHelper.insertGame(
                            it.id,
                            it.title,
                            it.thumbnail,
                            it.shortDescription,
                            it.gameUrl,
                            it.genre,
                            it.platform,
                            it.publisher,
                            it.developer,
                            it.releaseDate,
                            it.freeToGameProfileUrl
                        )
                    }
                    // Marcar que los datos iniciales se han insertado
                    dbHelper.setDataInserted(this, true)
                } else {
                    Toast.makeText(this, "No se encontraron juegos", Toast.LENGTH_LONG).show()
                }

            }

        })

        dataViewModel.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })

        dataViewModel.fetchGames()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTimeOut)
    }


}
