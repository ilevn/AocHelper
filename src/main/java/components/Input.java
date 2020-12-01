package components;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Properties;


public final class Input {
    private final static HttpClient client = HttpClient.newHttpClient();
    private final static String secret = loadSecret();

    private Input() {
    }

    private static String loadSecret() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.properties"));
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
     * @param day  The day for the puzzle input
     * @param year The year for the puzzle input.
     * @return A String with the puzzle input.
     */
    public static String getHttp(int day, int year) {
        String url = String.format("https://adventofcode.com/%d/day/%d/input", year, day);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Cookie", "session=" + secret)
                .GET().build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new InputException("Could not retrieve puzzle content");

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
     * @param day  The day for the puzzle input
     * @param year The year for the puzzle input.
     * @return A String with the puzzle input.
     */
    public static String get(int day, int year) {
        File file = new File(String.format("src/main/resources/input/year%d/day%d", year, day));
        String content;

        if (file.exists()) {
            try {
                content = Files.readString(file.toPath());
            } catch (IOException e) {
                throw new InputException("Could not read content from file for some reason.");
            }
        } else {
            content = getHttp(day, year);
            try {
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
                Files.write(file.toPath(), content.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return content;
    }
}
