package com.example.moviesearchapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearchapp.databinding.ItemFavoriteMovieBinding;
import com.example.moviesearchapp.model.MovieModel;
import com.example.moviesearchapp.view.DetailActivity;
import com.example.moviesearchapp.view.FavoriteDetailsActivity;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private final List<MovieModel> favorites;
    private final Context context;

    public FavoriteAdapter(Context context, List<MovieModel> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteMovieBinding binding = ItemFavoriteMovieBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        MovieModel movie = favorites.get(position);
        holder.bindFavorite(movie);

        // On click listener for each favorite
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FavoriteDetailsActivity.class);
            intent.putExtra("movieId", movie.getImdbId()); // Pass movieId
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void updateFavorites(List<MovieModel> newFavorites) {
        favorites.clear();
        favorites.addAll(newFavorites);
        notifyDataSetChanged();
    }
}