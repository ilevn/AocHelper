package components;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Properties;


public final class Input {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String SECRET = loadSecret();

    private Input() {
        throw new AssertionError("This is a utility class with no instances.");
    }

    private static String loadSecret() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            throw new InputException("Missing config.properties file at project root");
        }
        return prop.getProperty("secret");
    }

    /**
     * Retrieves puzzle input for Advent of Code problems over the network layer.
     * <br>
     * Note: This completely ignores cached input files.
     *
     * @param day  the day of the puzzle input
     * @param year the year of the puzzle input
     * @return the puzzle input
     */
    public static String getHttp(int day, int year) {
        String url = "https://adventofcode.com/%d/day/%d/input".formatted(year, day);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Cookie", "session=" + SECRET)
                .GET().build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new InputException("Could not retrieve puzzle content", e);

        }
        if (response.statusCode() != 200) {
            // Something went wrong.
            throw new InputException("Download exited with bad status code: " + response.statusCode());
        }
        return response.body();
    }

    /**
     * Retrieves puzzle input for Advent of Code puzzles.
     * <br>
     * This first looks for cached input files before attempting a fetch over
     * the network layer.
     *
     * @param day  the day of the puzzle input
     * @param year the year of the puzzle input
     * @return the puzzle input
     */
    public static String get(int day, int year) {
        File file = new File("src/main/resources/input/year%d/day%d".formatted(year, day));
        String content;

        if (file.exists()) {
            try {
                content = Files.readString(file.toPath());
            } catch (IOException e) {
                throw new InputException("Could not read content from file for some reason.", e);
            }
        } else {
            content = getHttp(day, year);
            try {
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
                Files.write(file.toPath(), content.getBytes());
            } catch (IOException e) {
                throw new InputException("Writing to file failed.", e);
            }
        }

        return content;
    }
}
