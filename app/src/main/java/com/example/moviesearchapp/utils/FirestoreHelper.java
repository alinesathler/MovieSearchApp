package com.example.moviesearchapp.utils;

import android.util.Log;

import com.example.moviesearchapp.model.MovieModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreHelper {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    // Add movie to favorites (database)
    public static void addToFavorites(MovieModel movie, AddFavoriteCallback callback) {
        String uid = auth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("users")
                .document(uid)
                .collection("favorites")
                .document(movie.getImdbId());

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            // First check if movie already exists
            if (documentSnapshot.exists()) {
                Log.d("Firestore", "Movie already in favorites");
                callback.onComplete(true); // Already exists
            } else {
                // If not, add it
                docRef.set(movie)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Movie added to favorites");
                            callback.onComplete(false); // Successfully added
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error adding movie", e));
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error checking document", e));
    }

    public interface AddFavoriteCallback {
        void onComplete(boolean alreadyExists);
    }

    // Get Favorite Movies
    public static void fetchFavorites(FavoritesCallback callback) {
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .collection("favorites").limit(20).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<MovieModel> favorites = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        MovieModel movie = doc.toObject(MovieModel.class);
                        favorites.add(movie);
                    }
                    callback.onSuccess(favorites);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public interface FavoritesCallback {
        void onSuccess(List<MovieModel> movies);
        void onFailure(Exception e);
    }

    // Get Favorite by ID
    public static void getFavoriteById(String id, MovieCallback callback) {
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .collection("favorites").document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    MovieModel movie = documentSnapshot.toObject(MovieModel.class);
                    callback.onSuccess(movie);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public interface MovieCallback {
        void onSuccess(MovieModel movie);
        void onFailure(Exception e);
    }

    // Delete Favorite
    public static void deleteFavorite(String id, OnCompleteListener<Void> onCompleteListener) {
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .collection("favorites").document(id)
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }

    // Update Favorite description
    public static void updateFavoriteDescription(String id, String newDescription, OnCompleteListener<Void> listener) {
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .collection("favorites").document(id)
                .update("description", newDescription)
                .addOnCompleteListener(listener);
    }
}
