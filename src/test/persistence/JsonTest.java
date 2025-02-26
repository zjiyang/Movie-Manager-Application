package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import model.*;

public class JsonTest {
    protected void checkMovie(Movie movie,
            String name,
            int yearReleased,
            ArrayList<Genre> genres,
            ArrayList<Rate> rates,
            ArrayList<StreamService> streamServices) {
        assertEquals(name, movie.getName());

        assertEquals(yearReleased, movie.getYearReleased());

        assertEquals(genres.size(), movie.getGenres().size());
        for (int i = 0; i < genres.size(); i++) {
            assertEquals(genres.get(i).getGenreName(),
                    movie.getGenres().get(i).getGenreName());
        }

        assertEquals(rates.size(), movie.getRates().size());
        for (int i = 0; i < rates.size(); i++) {
            assertEquals(rates.get(i).getUserID(),
                    movie.getRates().get(i).getUserID());
            assertEquals(rates.get(i).getScore(),
                    movie.getRates().get(i).getScore());
        }

        assertEquals(streamServices.size(), movie.getStreamServices().size());
        for (int i = 0; i < streamServices.size(); i++) {
            assertEquals(streamServices.get(i).getStreamPlatformName(),
                    movie.getStreamServices().get(i).getStreamPlatformName());
        }
    }
}