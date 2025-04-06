package com.example.moviesearchapp.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.moviesearchapp.R;
import com.example.moviesearchapp.databinding.ActivityFavoriteDetailsBinding;
import com.example.moviesearchapp.model.MovieModel;
import com.example.moviesearchapp.utils.FirestoreHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class FavoriteDetailsActivity extends AppCompatActivity {

    private ActivityFavoriteDetailsBinding binding;
    private String imdbId;
    private MovieModel movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get IMDb ID from intent
        imdbId = getIntent().getStringExtra("movieId");

        // Fetch movie details from Firestore
        FirestoreHelper.getFavoriteById(imdbId, new FirestoreHelper.MovieCallback() {
            @Override
            public void onSuccess(MovieModel fetchedMovie) {
                movie = fetchedMovie;
                populateUI(fetchedMovie);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(FavoriteDetailsActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });

        // Update Button
        binding.updateButton.setOnClickListener(v -> {
            String newDescription = binding.detailDescription.getText().toString().trim();

            if (newDescription.isEmpty()) {
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            FirestoreHelper.updateFavoriteDescription(imdbId, newDescription, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Description updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update description", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Delete Button
        binding.deleteButton.setOnClickListener(v -> {
            FirestoreHelper.deleteFavorite(imdbId, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Movie deleted from favorites", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to delete movie", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Back Button
        binding.backButton.setOnClickListener(v -> finish());
    }

    // Display Movie Information
    private void populateUI(MovieModel movie) {
        binding.detailTitle.setText(movie.getTitle());
        binding.detailGenre.setText(movie.getGenre());
        binding.detailYear.setText(movie.getYear());
        binding.detailRuntime.setText(movie.getRunTime());
        binding.rating.setText(movie.getImdbRating());
        binding.detailDescription.setText(movie.getDescription());

        Glide.with(this)
                .load(movie.getPosterUrl())
                .into(binding.detailPoster);
    }
}