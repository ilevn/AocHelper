package components;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class ADay {
    public final String data;
    private List<String> linesList = null;

    /**
     * Returns a {@code List<String>} of the puzzle data.
     */
    public List<String> getLinesList() {
        if (linesList == null) {
            linesList = data.lines().collect(Collectors.toList());
        }
        return linesList;
    }


    /**
     * Returns the {@code String} value of the puzzle data.
     */
    public String getData() {
        return data;
    }


    protected ADay(int year, int day) {
        data = Input.get(day, year);
    }

    /**
     * Attempts to evaluate the first part of the puzzle.
     *
     * @return The solution to part one.
     */
    public Object first() {
        return null;
    }

    /**
     * Attempts to evaluate the second part of the puzzle.
     *
     * @return The solution to part two.
     */
    public Object second() {
        return null;
    }

    /**
     * Runs the solutions for both parts and times them.
     * This is usually done inside a main with for example:
     * <pre>{@code
     *    public static void main(String[] args) {
     *         solve(Day1.class);
     *     }
     * }</pre>
     *
     * @param clazz The child class.
     */
    public static void solve(Class<? extends ADay> clazz) {
        ADay aDay;

        try {
            Constructor<? extends ADay> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            aDay = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("First: " + evalSolution(aDay::first));
        System.out.println("Second: " + evalSolution(aDay::second));
    }

    private static String evalSolution(Supplier<Object> func) {
        long startTime = System.currentTimeMillis();
        Object result = func.get();
        long endTime = System.currentTimeMillis();
        if (result == null) {
            result = "unsolved";
        }
        return "%s\nTime: %d ms".formatted(result, endTime - startTime);
    }
}
