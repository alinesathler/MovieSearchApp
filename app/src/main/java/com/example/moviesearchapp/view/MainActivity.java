package com.example.moviesearchapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesearchapp.R;
import com.example.moviesearchapp.adapter.MovieAdapter;
import com.example.moviesearchapp.databinding.ActivityMainBinding;
import com.example.moviesearchapp.viewmodel.MovieViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(this, new ArrayList<>()); // Pass context
        binding.recyclerView.setAdapter(movieAdapter);

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Observe movie list LiveData
        movieViewModel.getMovieListData().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                movieAdapter.updateMovies(movies);  // Update RecyclerView
                binding.noResultsTextView.setVisibility(View.GONE);  // Hide "No Movies" message
                binding.recyclerView.setVisibility(View.VISIBLE);   // Show RecyclerView
            } else {
                movieAdapter.updateMovies(new ArrayList<>());  // Clear the list
                binding.noResultsTextView.setVisibility(View.VISIBLE);  // Show "No Movies" message
                binding.recyclerView.setVisibility(View.GONE);   // Hide RecyclerView
                Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
            }
        });

        // Search Button Click Listener
        binding.searchButton.setOnClickListener(v -> {
            String query = binding.searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                hideKeyboard();
                movieViewModel.RefreshMovieList(query);  // Fetch movies
            } else {
                Toast.makeText(this, "Enter a movie name", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Search EditText Focus (show keyboard when clicked)
        binding.searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showKeyboard(v);
            }
        });

        // Navbar
        binding.bottomNavigationView.setSelectedItemId(R.id.nav_search);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_search) {
                // Already on Search screen – do nothing or refresh
                return true;
            } else if (id == R.id.nav_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
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

    // Hide the keyboard
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Show the keyboard
    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}