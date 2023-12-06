package org.kruskopf;

import java.io.PrintStream;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class labb3 {
    private static Scanner scanner;

    public static void main(String[] args) {
        boolean quit = false;
        printActions();

        while (!quit) {
            System.out.println("\nVälj (6 för att visa val):");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 0 -> quit = isQuit();
                case 1 -> selectMovie();
                case 2 -> insertMovie();
                case 3 -> updateMovie();
                case 4 -> deleteMovie();
                case 5 -> searchMovie(); // TODO: metod att söka efter film, switch, visa  favoriter, visa kategori, sök efter producent, sök efter filmtitel
                case 6 -> printActions();
            }

        }
    }


    private static void searchMovie() {
        //TODO metod att söka efter film: Switch Favoriter, Kategori, ange längd i minuter, vilken director, lägsta betyg -> alla skickar vidare till metod visa filmer med kategorier, längd och betyg
    }

    private static boolean isQuit() {
        System.out.println("\nStänger ner...");
        return true;
    }

    private static boolean isCancel() {
        System.out.println("\ngår tillbaka...");
        return true;
    }


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
        System.out.println("0  - Stäng av\n1  - Visa alla filmer\n2  - Lägga till en ny film\n3  - Uppdatera en film\n4  - Ta bort en film\n5  - sök efter film.\n6  - Visa en lista över alla val.");
    }


    private static void selectMovie() {
        selectAll(); // lägg in val, 1 välja alla 2, efter kategori, 3 efter Regissör, favoriter, söka efter titel,söka efter regissör
    }

    private static void selectAll() {
        String sql = "SELECT category.categoryName, movie.movieId, movie.movieTitle, movie.movieScore, movie.movieDirector\n" +
                "FROM category INNER JOIN movie ON category.categoryId = movie.movieCategoryId\n" +
                "ORDER BY movieScore DESC;";


        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                PrintStream var10000 = System.out;
                int var10001 = rs.getInt("movieId");
                int var10002 = rs.getInt("movieScore");
                var10000.println("" + rs.getString("categoryName") + "\t" + var10001 + "\t" +  rs.getString("movieTitle") + "\t"
                        + var10002 + "\t" + rs.getString("movieDirector") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


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

    private static String selectCategory() {
        //todo switch ska kunna lägga till fler kategorier
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

    private static void insertScore(String[] movie) {

        boolean validScore = false;

        while (!validScore) {
            try {
                System.out.println("Ange betyg 0-5 på filmen:");
                int score = Integer.parseInt(scanner.nextLine());
                if (score >= 0 && score <= 5) {
                    movie[3] = String.valueOf(score);
                    validScore = true;
                } else {
                    System.out.println("Betyg får vara minst 0 och högst 5. Försök igen.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Felaktigt format. Försök igen.");
            }
        }

    }

    private static void insertLength(String[] movie) {

        boolean validLength = false;

        while (!validLength) {
            try {
                System.out.println("Ange filmens längd i minuter:");
                int length = Integer.parseInt(scanner.nextLine());
                if (length >= 0) {
                    movie[2] = String.valueOf(length);
                    validLength = true;
                } else {
                    System.out.println("Endast positiva heltal, försök igen.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Endast positiva heltal, försök igen.");
            }
        }
    }

    private static void insertDirector(String[] movie) {
        System.out.println("ange regissör:");
        movie[1] = scanner.nextLine();
    }

    private static void insertTitle(String[] movie) {
        System.out.println("ange titel:");

        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) {
                    throw new Exception("Du måste skriva in något!");
                }
                movie[0] = input;
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void printActionsForInsertMovies() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - tillbaka\n1  - lägg till film");
    }
    private static void printAllCategories(){
        System.out.println("1 Action\n2 Adventure\n3 Comedy\n4 Documentary\n5 Horror\n6 Thriller\n7 Romance\n");
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
