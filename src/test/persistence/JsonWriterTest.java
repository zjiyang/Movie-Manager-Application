// package persistence;

// import model.*;
// import persistence.*;
// import org.junit.jupiter.api.Test;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// class JsonWriterTest extends JsonTest {
//     //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
//     //write data to a file and then use the reader to read it back in and check that we
//     //read in a copy of what was written out.

//     @Test
//     void testWriterInvalidFile() {
//         try {
//             MovieDataBase database = new MovieDataBase();
//             JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
//             writer.open();
//             fail("IOException was expected");
//         } catch (IOException e) {
//             // pass
//         }
//     }

//     @Test
//     void testWriterEmptyWorkroom() {
//         try {
//             MovieDataBase database = new MovieDataBase();
//             JsonWriter writer = new JsonWriter("./data/testWriterEmptyMovieDataBase.json");
//             writer.open();
//             writer.write(database);
//             writer.close();

//             JsonReader reader = new JsonReader("./data/testWriterEmptyMovieDataBase.json");
//             database = reader.read();
//         } catch (IOException e) {
//             fail("Exception should not have been thrown");
//         }
//     }

//     @Test
//     void testWriterGeneralWorkroom() {
//         try {
//             MovieDataBase database = new MovieDataBase();
//             database.addMovie(new Movie("A1"));
//             database.addMovie(new Movie("A2"));
//             JsonWriter writer = new JsonWriter("./data/testWriterGeneralMovieDataBase.json");
//             writer.open();
//             writer.write(database);
//             writer.close();

//             JsonReader reader = new JsonReader("./data/testWriterGeneralMovieDataBase.json");
//             database = reader.read();
//             List<Movie> movies = database.getDataBase();
//             assertEquals(2, movies.size());
//             //checkMovie("A1", movies.get(0));
//             //checkMovie(movies.get(1));

//         } catch (IOException e) {
//             fail("Exception should not have been thrown");
//         }
//     }
// }
