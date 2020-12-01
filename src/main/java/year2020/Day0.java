package year2020;

import java.util.List;

/**
 * Sample solution for non existent day.
 * Note: This won't actually run as there is no puzzle input for Day 0.
 */
public class Day0 extends DayOf2020 {
    public Day0() {
        super(0);
    }

    @Override
    public Object first() {
        return "Hello, I'm a sample solution!";
    }

    @Override
    public Object second() {
        // Access to puzzle data.
        String[] input = getData().split("[aoc]");
        List<String> lines = getLinesList();

        return "Merry Christmas";
    }

    public static void main(String[] args) {
        // Let both solutions run and time them.
        solve(Day0.class);
    }
}
