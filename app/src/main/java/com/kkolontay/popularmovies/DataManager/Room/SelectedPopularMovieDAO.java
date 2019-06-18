package com.kkolontay.popularmovies.DataManager.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SelectedPopularMovieDAO {
    @Query("SELECT * FROM movieTable ORDER BY mVoteCount")
    List<SelectedPopularMovie> loadAllMovies();

    @Query("SELECT * FROm movieTable WHERE mId = :id LIMIT 1")
    SelectedPopularMovie fetchMovie(int id);

    @Insert
    void insertMovie(SelectedPopularMovie selectedMovie);

    @Delete
    void deleteMovie( SelectedPopularMovie selectedMovie);
}
