package com.example.moviesearchapp.adapter;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviesearchapp.databinding.ItemMovieBinding;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    private final ItemMovieBinding binding;

    public MovieViewHolder(@NonNull ItemMovieBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindMovie(com.example.moviesearchapp.model.MovieModel movie) {
        binding.movieTitle.setText(movie.getTitle());
        binding.movieYear.setText(movie.getYear());

        // Load movie poster using Glide
        Glide.with(binding.getRoot().getContext())
                .load(movie.getPosterUrl())
                .placeholder(com.example.moviesearchapp.R.drawable.rounded_image_placeholder)
                .into(binding.moviePoster);
    }
}