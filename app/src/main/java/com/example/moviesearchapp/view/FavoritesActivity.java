package com.example.moviesearchapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesearchapp.R;
import com.example.moviesearchapp.adapter.FavoriteAdapter;
import com.example.moviesearchapp.adapter.MovieAdapter;
import com.example.moviesearchapp.databinding.ActivityFavoritesBinding;
import com.example.moviesearchapp.model.MovieModel;
import com.example.moviesearchapp.utils.FirestoreHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityFavoritesBinding binding;
    private FavoriteAdapter favoriteAdapter;
    private final ArrayList<MovieModel> favoriteMovies = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteAdapter = new FavoriteAdapter(this, favoriteMovies);
        binding.recyclerView.setAdapter(favoriteAdapter);

        fetchFavorites();

        // Navbar
        binding.bottomNavigationView.setSelectedItemId(R.id.nav_favorites);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_search) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_favorites) {
                return true;
            }
            return false;
        });

        // Logout
        binding.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    // Fetch Favorites
    private void fetchFavorites() {
        FirestoreHelper.fetchFavorites(new FirestoreHelper.FavoritesCallback() {
            @Override
            public void onSuccess(List<MovieModel> movies) {
                favoriteMovies.clear();
                favoriteMovies.addAll(movies);
                favoriteAdapter.updateFavorites(movies);
                binding.noResultsTextView.setVisibility(View.GONE);  // Hide "No Movies" message
                binding.recyclerView.setVisibility(View.VISIBLE);   // Show RecyclerView

                if (movies.isEmpty()) {
                    Toast.makeText(FavoritesActivity.this, "No favorites yet", Toast.LENGTH_SHORT).show();
                    binding.noResultsTextView.setVisibility(View.VISIBLE);  // Show "No Movies" message
                    binding.recyclerView.setVisibility(View.GONE);   // Hide RecyclerView
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(FavoritesActivity.this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites(); // Fetch updated list from Firestore
    }

    // Ensure favorite list is updated after deleting
    private void loadFavorites() {
        FirestoreHelper.fetchFavorites(new FirestoreHelper.FavoritesCallback() {
            @Override
            public void onSuccess(List<MovieModel> movies) {
                favoriteAdapter.updateFavorites(movies);

                if (movies.isEmpty()) {
                    binding.noResultsTextView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                } else {
                    binding.noResultsTextView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(FavoritesActivity.this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }
}