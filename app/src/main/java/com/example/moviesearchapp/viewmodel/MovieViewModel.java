package com.example.moviesearchapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearchapp.model.MovieModel;
import com.example.moviesearchapp.utils.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {
    private final MutableLiveData<List<MovieModel>> movieListData = new MutableLiveData<List<MovieModel>>();
    private final MutableLiveData<MovieModel> selectedMovieData = new MutableLiveData<>();

    // Movie List from Search
    public LiveData<List<MovieModel>> getMovieListData() {
        return movieListData;
    }

    //Selected Movie Data
    public LiveData<MovieModel> getSelectedMovieData() {
        return selectedMovieData;
    }

    // Refreshes the list of movies for the search
    public void RefreshMovieList(String query) {
        String queryParams = "s=" + query + "&type=movie";

        ApiClient.get(queryParams, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();  // Log error for debugging
                movieListData.postValue(null);  // Notify UI of failure
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) {
                    movieListData.postValue(null);
                    return;
                }

                String responseData = response.body().string();

                JSONObject json = null;

                try {
                    json = new JSONObject(responseData);

                    // Check if response contains movies
                    if (json.has("Search")) {
                        List<MovieModel> movieList = new ArrayList<>();
                        JSONArray movieArray = json.getJSONArray("Search");

                        for (int i = 0; i < movieArray.length(); i++) {
                            JSONObject movieJson = movieArray.getJSONObject(i);
                            MovieModel movieModel = new MovieModel();

                            // Basic information for search
                            movieModel.setTitle(movieJson.getString("Title"));
                            movieModel.setYear(movieJson.getString("Year"));
                            movieModel.setImdbId(movieJson.getString("imdbID"));
                            movieModel.setPosterUrl(movieJson.getString("Poster"));

                            movieList.add(movieModel);
                        }

                        // Post the movie list to LiveData for UI updates
                        movieListData.postValue(movieList);
                    } else {
                        movieListData.postValue(new ArrayList<>()); // Return empty list if no results
                    }
                } catch (JSONException e) {
                    e.printStackTrace();  // Log error
                    movieListData.postValue(null);  // Notify UI of parsing failure
                }
            }
        });
    }

    // Refreshes the details os a selected movie
    public void RefreshMovie(String imdbId) {
        String queryParams = "i=" + imdbId;

        ApiClient.get(queryParams, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();  // Log error for debugging
                selectedMovieData.postValue(null);  // Notify UI of failure
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) {
                    selectedMovieData.postValue(null);
                    return;
                }

                String responseData = response.body().string();

                try {
                    JSONObject movieJson = new JSONObject(responseData);
                    MovieModel movieModel = new MovieModel();

                    // Basic Information
                    movieModel.setTitle(movieJson.getString("Title"));
                    movieModel.setYear(movieJson.getString("Year"));
                    movieModel.setImdbId(movieJson.getString("imdbID"));
                    movieModel.setPosterUrl(movieJson.getString("Poster"));
                    movieModel.setRunTime(movieJson.getString("Runtime"));
                    movieModel.setRating(movieJson.optString("Rated", "N/A"));
                    movieModel.setStudio(movieJson.optString("Production", "Unknown Studio"));

                    // Additional details
                    movieModel.setDescription(movieJson.optString("Plot", "No description available"));
                    movieModel.setGenre(movieJson.optString("Genre", "Unknown Genre"));
                    movieModel.setDirector(movieJson.optString("Director", "Unknown Director"));
                    movieModel.setActors(movieJson.optString("Actors", "Unknown Actors"));
                    movieModel.setBoxOffice(movieJson.optString("BoxOffice", "N/A"));
                    movieModel.setImdbRating(movieJson.optString("imdbRating", "N/A"));

                    // Post the movie to LiveData for UI updates
                    selectedMovieData.postValue(movieModel);
                } catch (JSONException e) {
                    e.printStackTrace();  // Log error
                    selectedMovieData.postValue(null);  // Notify UI of parsing failure
                }
            }
        });
    }
}
