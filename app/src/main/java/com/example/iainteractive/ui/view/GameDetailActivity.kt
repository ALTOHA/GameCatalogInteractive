package com.example.iainteractive.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.iainteractive.R
import com.example.iainteractive.data.model.VideogameModel
import com.example.iainteractive.databinding.ActivityGameDetailBinding
import com.example.iainteractive.db.GamesDBHelper
import kotlin.math.min

class GameDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameDetailBinding
    private lateinit var adapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = GamesDBHelper(this)
        val bundle = intent.extras
        val id: String? = bundle?.getString("GAME_ID")

        val gameList: List<VideogameModel> = dbHelper.getAllGames()

        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (id != null) {

            val game: List<VideogameModel> = id.let { dbHelper.getOneGame(it) }

            binding.ivMainGame.post {
                val maxWidth = 500
                val maxHeight = 300
                val layoutParams = binding.ivMainGame.layoutParams

                layoutParams.width = min(binding.ivMainGame.width, maxWidth)
                layoutParams.height = min(binding.ivMainGame.height, maxHeight)
                binding.ivMainGame.layoutParams = layoutParams
            }
            Glide.with(this)
                .load(game[0].thumbnail)
                .placeholder(R.drawable.samplegameimage)
                .error(R.drawable.samplegameimage)
                .into(binding.ivMainGame)

            binding.etGameTitle.setText(game[0].title)
            binding.etGameUrl.setText(game[0].gameUrl)
            binding.etGameGenre.setText(game[0].genre)
            binding.etGamePlatform.setText(game[0].platform)
            binding.etGamePublisher.setText(game[0].publisher)
            binding.etGameDeveloper.setText(game[0].developer)
            binding.etGameReleaseDate.setText(game[0].releaseDate)
            binding.etGameGameProfile.setText(game[0].freeToGameProfileUrl)

            binding.btnUpdate.setOnClickListener {
                dbHelper.updateGame(
                    id,
                    binding.etGameTitle.text.toString(),
                    binding.etGameUrl.text.toString(),
                    binding.etGameGenre.text.toString(),
                    binding.etGamePlatform.text.toString(),
                    binding.etGamePublisher.text.toString(),
                    binding.etGameDeveloper.text.toString(),
                    binding.etGameReleaseDate.text.toString(),
                    binding.etGameGameProfile.text.toString()
                )

                adapter.updateList(gameList)
                finish()
            }

            binding.btnEliminate.setOnClickListener {
                dbHelper.deleteGame(id)
                adapter.updateList(gameList)
                finish()
            }

        } else {
            Toast.makeText(this, "Hubo un error, intentelo más tarde", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

    }
}