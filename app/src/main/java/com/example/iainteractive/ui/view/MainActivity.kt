package com.example.iainteractive.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iainteractive.R
import com.example.iainteractive.data.model.VideogameModel
import com.example.iainteractive.db.GamesDBHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.rv_games_list)
        val searchEditText: EditText = findViewById(R.id.et_buscar)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val dbHelper = GamesDBHelper(this)
        val games: List<VideogameModel> = dbHelper.getAllGames()

        val adapter = GameAdapter(games){ gameId ->
            val intent = Intent(this, GameDetailActivity::class.java).apply {
                putExtra("GAME_ID", gameId.toString())
            }
            startActivity(intent)

        }
        recyclerView.adapter = adapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}