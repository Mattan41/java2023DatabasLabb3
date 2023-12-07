package org.kruskopf;

import java.io.PrintStream;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class labb3 {
    private static Scanner scanner;

    public static void main(String[] args) {
        boolean quit = false;


        while (!quit) {
            printActions();
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 0 -> quit = isQuit();
                case 1 -> insertMovie();
                case 2 -> selectMovie();
                case 3 -> searchMovie(); // TODO: metod att söka efter specifik film, switch, sök efter regissör, sök efter filmtitel
                case 4 -> statistics();
                case 5 -> updateMovie();
                case 6 -> deleteMovie();
            }

        }
    }
    //TODO när man skriver in en filmtitel - kolla i databasen om den redan finns, felmeddelande att den inte finns om man söker/uppdaterar, felmeddelande att den redan finns när man ska lägga till
    private static Connection connect() {
        String url = "jdbc:sqlite:/Users/knish/Documents/Programmering/sqlite-tools-win-x64-3440200/labb3.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    private static void printActions() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - Stäng av\n1  - Lägga till ny film\n2  - Visa filmer\n3  - sök efter film\n4  - Statistik\n5  - Uppdatera film\n6  - Ta bort film");
    }


    private static boolean isQuit() {
        System.out.println("\nStänger ner...");
        return true;
    }
    private static boolean isCancel() {
        System.out.println("\ngår tillbaka...");
        return true;
    }
    private static void printAllCategories(){
        System.out.println("1 Action\n2 Adventure\n3 Comedy\n4 Documentary\n5 Horror\n6 Thriller\n7 Romance\n");
    }
    private static String selectCategory() {
        System.out.println("välj filmkategori:");
        printAllCategories();

        int action = Integer.parseInt(scanner.nextLine());
        while (action < 1 || action > 7) {
            try {
                System.out.println("Välj en siffra mellan 1 och 7:");
                action = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ogiltigt val, försök igen.");
                scanner.nextLine();
            }
        }

        return switch (action) {
            case 1 -> "1";
            case 2 -> "2";
            case 3 -> "3";
            case 4 -> "4";
            case 5 -> "5";
            case 6 -> "6";
            case 7 -> "7";
            default -> "Ogiltigt val";
        };

    }
    private static String chooseScore0To5() {
        boolean validScore = false;
        int score = 0;
        while (!validScore) {
            try {
                score = Integer.parseInt(scanner.nextLine());
                if (score >= 0 && score <= 5) {
                    validScore = true;
                } else {
                    System.out.println("Betyg får vara minst 0 och högst 5. Försök igen.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Felaktigt format. Försök igen.");
            }
        }
        return String.valueOf(score);
    }
    private static int chooseLengthOfMovie() {
        boolean validLength = false;
        int movieLength = 0;
        while (!validLength) {
            try {

                int length = Integer.parseInt(scanner.nextLine());
                if (length >= 0) {
                    movieLength = length;
                    validLength = true;
                } else {
                    System.out.println("Endast positiva heltal, försök igen.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Endast positiva heltal, försök igen.");
            }
        }
        return movieLength;
    }
    private static String chooseMovieTitle() {
        String movieTitle = "";
        boolean validTitle = false;
        while (!validTitle) {
            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) {
                    throw new Exception("Du måste skriva in något!");
                }
                movieTitle = input;
                validTitle = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return movieTitle;
    }


    //INSERT MOVIES
    private static void insertMovie() {

        boolean quit = false;
        while (!quit) {
            printActionsForInsertMovies();
            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 0 -> quit = isCancel();
                case 1 -> insertTheMovie();
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }
    private static void printActionsForInsertMovies() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - tillbaka\n1  - lägg till film");
    }

    private static void insertTitle(String[] movie) {
        System.out.println("ange titel:");
        movie[0] =chooseMovieTitle();
    }

    private static void insertDirector(String[] movie) {
        System.out.println("ange regissör:");
        movie[1] = scanner.nextLine();
    }
    private static void insertLength(String[] movie) {
        System.out.println("Ange filmens längd i minuter:");
        movie[2] =  String.valueOf(chooseLengthOfMovie());

    }
    private static void insertTheMovie() {
        String[] movie = new String[5];

        insertTitle(movie);
        insertDirector(movie);
        insertLength(movie);
        insertScore(movie);
        insertCategory(movie);
        String title = movie[0];
        String director = movie[1];
        int movieLength = Integer.parseInt(movie[2]);
        int movieScore = Integer.parseInt(movie[3]);
        int CategoryId = Integer.parseInt(movie[4]);
        insertIntoMovie(title, director, movieLength, movieScore, CategoryId);
    }
    private static void insertCategory(String[] movie) {
        movie[4] = selectCategory();
    }
    private static void insertScore(String[] movie) {
        System.out.println("Ange betyg 0-5 på filmen:");
        movie[3] = chooseScore0To5();
    }

    private static void insertIntoMovie(String inTitle, String inDirector, int inLength, int inScore, int inMovieCategory) {
        String sql = "INSERT INTO movie(movieTitle,movieDirector,movieLength,movieScore,movieCategoryId) VALUES(?,?,?,?,?)";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, inTitle);
            pstmt.setString(2, inDirector);
            pstmt.setInt(3, inLength);
            pstmt.setInt(4, inScore);
            pstmt.setInt(5, inMovieCategory);

            pstmt.executeUpdate();
            System.out.println("Du har lagt till en ny film");
        } catch (SQLException var6) {
            System.out.println(var6.getMessage());
        }

    }




    // SELECT MOVIES

    private static void selectMovie() {

        boolean quit = false;

        while (!quit) {
            printActionsForSelectMovie();
            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 0 -> quit = isCancel();
                case 1 -> selectAll();
                case 2 -> selectAllFavourites();
                case 3 -> inputAllMoviesFromOneCategory();
                case 4 -> inputAllMoviesWithScoreEqualOrGreater();
                case 5 -> inputAllMoviesWithScoreEqualOrLess();
                case 6 -> inputAllMoviesLongerThan();
                case 7 -> inputAllMoviesShorterThan();
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }
    private static void printActionsForSelectMovie() {
        System.out.println("\nVälj:\n");
        System.out.println("""
                0  - tillbaka
                1  - visa alla filmer
                2  - visa favoriter
                3  - visa alla filmer från en kategori
                4  - visa alla filmer med högre betyg än:
                5  - visa alla filmer med lägre betyg än:
                6  - visa alla filmer som är längre än:
                7  - visa alla filmer som är kortare än
                \s""");
    }
    private static void selectAll() {
        String sql = """
                SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength
                FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId
                ORDER BY movieScore DESC;""";

        selectMoviesToViewFromSql(sql);

    }
    private static void selectAllFavourites() {
        String sql = """
                SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength
                FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId
                WHERE movie.movieFavourite = 1
                ORDER BY movieScore DESC;""";

        selectMoviesToViewFromSql(sql);
    }
    private static void inputAllMoviesFromOneCategory() {
        printAllCategories();
        selectAllMoviesFromOneCategoryToView(selectCategory());
    }
    private static void selectAllMoviesFromOneCategoryToView(String categoryId) {

        String sql = "SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "WHERE category.categoryId = '"+categoryId+"'\n" +
                "ORDER BY movieScore DESC;";

        selectMoviesToViewFromSql(sql);

    }
    private static void inputAllMoviesWithScoreEqualOrLess() {
        System.out.println("ange högsta betyg du vill sortera efter (0-5)");
        selectAllMoviesWithScoreEqualOrLessToView(chooseScore0To5());
    }
    private static void selectAllMoviesWithScoreEqualOrLessToView(String score) {
        String sql = "SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "WHERE movie.MovieScore <= '"+ score +"'\n" +
                "ORDER BY movieScore DESC;";
        selectMoviesToViewFromSql(sql);
    }
    private static void inputAllMoviesWithScoreEqualOrGreater() {
        System.out.println("ange lägsta betyg du vill sortera efter (0-5)");
        selectAllMoviesWithScoreEqualOrGreaterToView(chooseScore0To5());
    }
    private static void selectAllMoviesWithScoreEqualOrGreaterToView(String score) {
        String sql = "SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "WHERE movie.MovieScore >= '"+ score +"'\n" +
                "ORDER BY movieScore DESC;";
        selectMoviesToViewFromSql(sql);
    }
    private static void inputAllMoviesLongerThan() {
        System.out.println("ange kortaste speltid i minuter");
        selectAllMoviesLongerThan(chooseLengthOfMovie());
    }
    private static void selectAllMoviesLongerThan(int length) {
        String sql = "SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "WHERE movie.movieLength >= '"+ length +"'\n" +
                "ORDER BY movieLength DESC;";
        selectMoviesToViewFromSql(sql);
    }
    private static void inputAllMoviesShorterThan() {
        System.out.println("ange längsta speltid i minuter");
        selectAllMoviesShorterThan(chooseLengthOfMovie());
    }
    private static void selectAllMoviesShorterThan(int length) {
        String sql = "SELECT category.categoryName,movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "WHERE movie.movieLength <= '"+ length +"'\n" +
                "ORDER BY movieLength DESC;";
        selectMoviesToViewFromSql(sql);
    }


    private static void selectMoviesToViewFromSql(String sql) {
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                PrintStream var10000 = System.out;
                int var10001 = rs.getInt("movieLength");
                int var10002 = rs.getInt("movieScore");
                var10000.println("Genre: " + rs.getString("categoryName") + "\t" + "\t" + " Titel: " + rs.getString("movieTitle") + "\t"
                        +" Betyg (0-5): " + var10002 + "\t" +" Regissör: " + rs.getString("movieDirector") + " Längd(minuter): " + var10001 + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //SEARCH MOVIES
    private static void searchMovie() {

        boolean quit = false;
        while (!quit) {
            printActionsForSearchMovie();
            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 0 -> quit = isCancel();
                case 1 -> inputMovieTitle();
                case 2 -> inputMovieDirector();
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }

    }

    private static void inputMovieTitle() {
        System.out.println("ange filmtitel");
        selectMovieToView(chooseMovieTitle());
    }
    private static void selectMovieToView(String inTitle) {
        String sql = "SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "WHERE movie.MovieTitle = '"+ inTitle +"'\n" +
                "ORDER BY movieScore DESC;";
        selectMoviesToViewFromSql(sql);
    }
    private static void inputMovieDirector() {
        System.out.println("ange Regissör");
        selectDirectorToView(scanner.nextLine());
    }
    private static void selectDirectorToView(String inDirector) {
        String sql = "SELECT category.categoryName, movie.movieTitle, movie.movieScore, movie.movieDirector, movie.movieLength\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "WHERE movie.MovieDirector = '"+ inDirector +"'\n" +
                "ORDER BY movieScore DESC;";
        selectMoviesToViewFromSql(sql);
    }

    private static void printActionsForSearchMovie() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - tillbaka\n1  - sök efter titel\n2  - sök efter Regissör");
    }



    //STATISTICS
    private static void statistics() {

        boolean quit = false;
        while (!quit) {
            printActionsForStatistics();
            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 0 -> quit = isCancel();
                case 1 -> inputScoreToCount();
                case 2 -> inputCategoryToMax();
                case 3 -> inputDirectorToAvg();
                case 4 -> inputDirectorForOneCategoryToCount();
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }

    private static void inputDirectorForOneCategoryToCount() {

        System.out.println("ange regissör: ");
        String directorToCount = scanner.nextLine();
        selectMovieDirectorForOneCategoryToCount(selectCategory(),directorToCount);

    }

    private static void selectMovieDirectorForOneCategoryToCount(String category, String director) {
        String sql = "SELECT COUNT(movieTitle) FROM movie WHERE movieCategoryId = "+ category + " AND movieDirector = '" + director +"'";
        String function = "COUNT(movieTitle)";
        selectMoviesFunctionFromSql(sql, function);
    }

    private static void inputDirectorToAvg() {
        System.out.println("ange regissör: ");
        selectMovieDirectorToCount(scanner.nextLine());
    }

    private static void selectMovieDirectorToCount(String director) {

        String sql = "SELECT AVG(movieScore) FROM movie WHERE movieDirector = '" + director +"'";
        String function = "AVG(movieScore)";
        selectMoviesFunctionFromSql(sql, function);
    }

    private static void inputCategoryToMax() {
        selectMovieCategoryToMax(selectCategory());
    }

    private static void selectMovieCategoryToMax(String category) {
        String sql = "SELECT MAX(movieScore) FROM movie WHERE movieCategoryId =" + category;
        String function = "MAX(movieScore)";
        selectMoviesFunctionFromSql(sql, function);
    }

    private static void inputScoreToCount() {
        System.out.println("ange betyg att räkna efter (0-5):");
        selectMovieScoreToCount(chooseScore0To5());
    }

    private static void selectMovieScoreToCount(String score) {
    String sql = "SELECT COUNT(movieTitle) FROM movie WHERE movieScore = " + score;
    String function = "COUNT(movieTitle)";
    selectMoviesFunctionFromSql(sql, function);
    }

    private static void selectMoviesFunctionFromSql(String sql, String function) {
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                PrintStream var10000 = System.out;
                int var10002 = rs.getInt(function);
                var10000.println(function + " : " + var10002 + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printActionsForStatistics() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - tillbaka\n1  - visa antal filmer med betyg: \n2  - visa högsta betyg inom en kategori \n3  - visa medelbetyg för filmer av en regissör \n4  - visa antal filmer av en regissör inom en viss kategori");
    }


    // UPDATE MOVIES


    private static void updateMovie(){
        System.out.println("vilken film vill du uppdatera?");
        String movie = scanner.nextLine();

        boolean quit = false;

        while (!quit) {
            printActionsInUpdate();
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 0 -> quit = isCancel();
                case 1 -> updateDirector(movie);
                case 2 -> updateScore(movie);
                case 3 -> updateFavourite(movie);
            }

        }


    }
    private static void printActionsInUpdate() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - tillbaka\n1  - uppdatera Regissör\n2  - uppdatera betyg\n3  - sätt som favorit");
    }
    private static void updateDirector(String inTitle){
        System.out.println("Skriv in det nya namnet på den nya regissören");
        setDirector(scanner.nextLine(),inTitle);
    }
    private static void setDirector(String inDirector, String inTitle) {
        String sql = "UPDATE movie SET  movieDirector = ? WHERE movieTitle = ?";

        try {
            Connection conn = connect();
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                try {
                    pstmt.setString(1, inDirector);
                    pstmt.setString(2, inTitle);

                    pstmt.executeUpdate();
                    System.out.println("Du har uppdaterat" + inTitle + " med regissör: " + inDirector);
                } catch (Throwable var11) {
                    if (pstmt != null) {
                        try {
                            pstmt.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Throwable var12) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }
                throw var12;
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var13) {
            System.out.println(var13.getMessage());
        }
    }
    private static void updateScore(String inTitle) {
        System.out.println("Skriv in det nya betyget");
        boolean validScore = false;

        while (!validScore) {
            try {
                System.out.println("Ange betyg 0-5 på filmen:");
                int score = Integer.parseInt(scanner.nextLine());
                if (score >= 0 && score <= 5) {
                    setNewScore(score,inTitle);
                    validScore = true;
                } else {
                    System.out.println("Betyg får vara minst 0 och högst 5. Försök igen.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Felaktigt format. Försök igen.");
            }
        }

    }
    private static void setNewScore(int inScore, String inTitle) {
        String sql = "UPDATE movie SET  movieScore = ? WHERE movieTitle = ?";

        try {
            Connection conn = connect();
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                try {
                    pstmt.setInt(1, inScore);
                    pstmt.setString(2, inTitle);

                    pstmt.executeUpdate();
                    System.out.println("Du har uppdaterat" + inTitle + " med betyg: " + inScore);
                } catch (Throwable var11) {
                    if (pstmt != null) {
                        try {
                            pstmt.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Throwable var12) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }
                throw var12;
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var13) {
            System.out.println(var13.getMessage());
        }
    }

    private static void updateFavourite(String inTitle) {
        System.out.println("Favorit? Ja/nej");
        boolean validScore = false;

        while (!validScore) {
            int favourite = 2;
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("ja"))
                favourite = 1;
            else if (response.equalsIgnoreCase("nej"))
                favourite = 0;

            if (favourite == 0 || favourite == 1) {
                if (favourite == 1)
                    System.out.print(inTitle + "är tillagd till");
                if (favourite == 0)
                    System.out.print(inTitle + "är borttagen från");


                setFavourite(favourite,inTitle);
                validScore = true;
            } else {
                System.out.println("Felaktigt format. ange Ja/Nej.");
            }
        }
    }
    private static void setFavourite(int inFavourite, String inTitle) {
        String sql = "UPDATE movie SET  movieFavourite = ? WHERE movieTitle = ?";

        try {
            Connection conn = connect();
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                try {
                    pstmt.setInt(1, inFavourite);
                    pstmt.setString(2, inTitle);

                    pstmt.executeUpdate();
                    System.out.println("favoriter");
                } catch (Throwable var11) {
                    if (pstmt != null) {
                        try {
                            pstmt.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Throwable var12) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }
                throw var12;
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var13) {
            System.out.println(var13.getMessage());
        }
    }




    //DELETE
    private static void deleteMovie() {
        System.out.println("Skriv in filmen som ska tas bort: ");
        String inputMovie = scanner.nextLine();
        delete(inputMovie);
    }
    private static void delete(String inTitle) {
        String sql = "DELETE FROM movie WHERE movieTitle = ?";

        try {
            Connection conn = connect();

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);

                try {
                    pstmt.setString(1, inTitle);
                    pstmt.executeUpdate();
                    System.out.println("Du har tagit bort filmen: " + inTitle );
                } catch (Throwable var8) {
                    if (pstmt != null) {
                        try {
                            pstmt.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }

                    throw var8;
                }

                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Throwable var9) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }

                throw var9;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var10) {
            System.out.println(var10.getMessage());
        }

    }
    static {
        scanner = new Scanner(System.in);
    }
}
