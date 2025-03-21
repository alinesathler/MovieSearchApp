package com.example.moviesearchapp.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.moviesearchapp.R;
import com.example.moviesearchapp.databinding.ActivityDetailBinding;
import com.example.moviesearchapp.model.MovieModel;
import com.example.moviesearchapp.viewmodel.MovieViewModel;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the IMDb ID from the intent
        String imdbId = getIntent().getStringExtra("movieId");

        // Initialize the ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Observe selected movie data
        movieViewModel.getSelectedMovieData().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel movie) {
                if (movie != null) {
                    updateUI(movie);
                } else {
                    Log.e("DetailActivity", "Movie data is null");
                }
            }
        });

        // Refresh movie details using IMDb ID
        movieViewModel.RefreshMovie(imdbId);

        // Back button click listener
        binding.backButton.setOnClickListener(v -> finish());
    }

    // Update UI with movie details
    private void updateUI(MovieModel movie) {
        binding.detailTitle.setText(movie.getTitle());
        binding.detailGenre.setText(movie.getGenre());
        binding.detailRuntime.setText(movie.getRunTime());
        binding.detailYear.setText(movie.getYear());
        binding.rating.setText(movie.getImdbRating());
        binding.detailDescription.setText(movie.getDescription());

        // Load the movie poster using Glide
        Glide.with(this)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.rounded_image_placeholder)
                .into(binding.detailPoster);
    }
}