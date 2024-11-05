package com.example.iainteractive.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.iainteractive.R
import com.example.iainteractive.data.model.VideogameModel
import kotlin.math.min

class GameAdapter(
    private var games: List<VideogameModel>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private var fullItemList = games.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_videogame, parent, false)
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_title_game)
        private val imgCover: ImageView = itemView.findViewById(R.id.img_cover_game);

        fun bind(game: VideogameModel) {

            imgCover.post {
                val maxWidth = 500
                val maxHeight = 300
                val layoutParams = imgCover.layoutParams

                layoutParams.width = min(imgCover.width, maxWidth)
                layoutParams.height = min(imgCover.height, maxHeight)
                imgCover.layoutParams = layoutParams
            }

            titleTextView.text = game.title
            Glide.with(itemView.context)
                .load(game.thumbnail)
                .placeholder(R.drawable.samplegameimage)
                .error(R.drawable.samplegameimage)
                .into(imgCover)

            itemView.apply {
                setOnClickListener {
                    onItemClicked(game.id)
                }
            }
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<VideogameModel>() {
        override fun areItemsTheSame(oldItem: VideogameModel, newItem: VideogameModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VideogameModel, newItem: VideogameModel): Boolean =
            oldItem == newItem
    }

    // Método para filtrar
    fun filter(query: String) {
        games = if (query.isEmpty()) {
            fullItemList
        } else {
            fullItemList.filter { it.title.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<VideogameModel>) {
        games = newList // Asignar la nueva lista a la variable de clase
        notifyDataSetChanged() // Notificar al adapter que los datos han cambiado
    }
}