package com.example.moviesearchapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.moviesearchapp.R;
import com.example.moviesearchapp.databinding.ActivityDetailBinding;
import com.example.moviesearchapp.model.MovieModel;
import com.example.moviesearchapp.utils.FirestoreHelper;
import com.example.moviesearchapp.viewmodel.MovieViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private MovieViewModel movieViewModel;
    private MovieModel selectedMovie;

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
                    selectedMovie = movie;
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

        // Add to favorites button click listener
        binding.addToFavoritesButton.setOnClickListener(v -> {
            if (selectedMovie != null) {
                FirestoreHelper.addToFavorites(selectedMovie, alreadyExists -> {
                    if (alreadyExists) {
                        Toast.makeText(this, "Movie is already in favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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