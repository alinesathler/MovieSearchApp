package com.example.moviesearchapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesearchapp.databinding.ItemFavoriteMovieBinding;
import com.example.moviesearchapp.model.MovieModel;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    private final ItemFavoriteMovieBinding binding;

    public FavoriteViewHolder(@NonNull ItemFavoriteMovieBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindFavorite(MovieModel movie) {
        binding.favTitle.setText(movie.getTitle());
        binding.favStudio.setText(movie.getStudio());
        binding.favRating.setText(movie.getImdbRating());

        Glide.with(binding.getRoot().getContext())
                .load(movie.getPosterUrl())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(binding.favPoster);
    }
}