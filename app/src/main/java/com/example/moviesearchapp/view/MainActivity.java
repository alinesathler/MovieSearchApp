package com.example.moviesearchapp.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesearchapp.adapter.MovieAdapter;
import com.example.moviesearchapp.databinding.ActivityMainBinding;
import com.example.moviesearchapp.viewmodel.MovieViewModel;

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