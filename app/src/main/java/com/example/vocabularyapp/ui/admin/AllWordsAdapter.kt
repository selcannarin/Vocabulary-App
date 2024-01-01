package com.example.vocabularyapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyapp.data.model.Word
import com.example.vocabularyapp.databinding.CardViewItemWordBinding

class AllWordsAdapter(
    private val wordList: List<Word>, private val onClickListener: OnClickListener
) : RecyclerView.Adapter<AllWordsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardViewItemWordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = wordList[position]
        holder.bind(word)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(wordList[position])
        }
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    inner class ViewHolder(private val binding: CardViewItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.apply {
                tvEnglish.text = word.english
                tvTurkish.text = word.turkish
            }
        }
    }

    class OnClickListener(val clickListener: (word: Word) -> Unit) {
        fun onClick(word: Word) = clickListener(word)
    }

}
