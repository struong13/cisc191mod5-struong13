package edu.sdccd.cisc191;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Module 5 Lab: Recursion + Algorithms
 * Reflection Questions:
 * 1. What is the base case for your recursive binary search?
 * 2. Why is recursion natural for the bracket tree?
 * 3. Why might the iterative tile-counting method be safer on a very large map?
 * 4. Which problems in this lab felt more natural with loops, and which felt more natural with recursion?
 */
public class GameAlgorithms {

    /**
     * Searches a sorted array of match IDs recursively.
     *
     * @param sortedMatchIds sorted in ascending order
     * @param target the match ID to find
     * @return index of target, or -1 if not found
     */
    public static int findMatchRecursive(int[] sortedMatchIds, int target) {
        // TODO: Replace this stub by calling a recursive helper method.

        // Start recursion with full range of array
        return findMatchRecursiveHelper(sortedMatchIds, target, 0, sortedMatchIds.length - 1);
    }

    /**
     * Helper method for recursive binary search.
     *
     * @param sortedMatchIds sorted in ascending order
     * @param target the match ID to find
     * @param low starting index of the current search range
     * @param high ending index of the current search range
     * @return index of target, or -1 if not found
     */
    private static int findMatchRecursiveHelper(int[] sortedMatchIds, int target, int low, int high) {
        // TODO: Implement recursive binary search.

        // Base case: range is invalid -> not found
        if (low > high) {
            return -1;
        }

        int mid = (low + high) / 2; //find middle index

        // Found case
        if (sortedMatchIds[mid] == target) {
            return mid;
        }

        // Recursive case: search left half
        else if (target < sortedMatchIds[mid]) {
            return findMatchRecursiveHelper(sortedMatchIds, target, low, mid - 1);
        }
        // Recursive case: search right half
        else {
            return findMatchRecursiveHelper(sortedMatchIds, target, mid + 1, high);
        }
    }
    /**
     * Searches a sorted array of match IDs iteratively.
     *
     * @param sortedMatchIds sorted in ascending order
     * @param target the match ID to find
     * @return index of target, or -1 if not found
     */
    public static int findMatchIterative(int[] sortedMatchIds, int target) {
        // TODO: Implement iterative binary search with a loop.
        int low = 0;
        int high = sortedMatchIds.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2; // find middle

            if (sortedMatchIds[mid] == target) {
                return mid; // found
            } else if (target < sortedMatchIds[mid]) {
                high = mid - 1; // search left
            } else {
                low = mid + 1; // search right
            }
        }
        return -1; // not found
    }

    /**
     * Counts connected walkable tiles recursively.
     * Walkable tiles are represented by '.'.
     * Blocked tiles can be any other character.
     * This method should count the size of the connected region starting at (startRow, startCol).
     * Count only vertical and horizontal neighbors, not diagonals.
     *
     * @param map mutable map of tiles
     * @param startRow starting row
     * @param startCol starting column
     * @return number of connected walkable tiles
     */
    public static int countConnectedTilesRecursive(char[][] map, int startRow, int startCol) {
        // TODO: Implement recursive flood-fill / connected tile counting.

        // Base case: out of bounds
        if (isOutOfBounds(map, startRow, startCol)) {
            return 0;
        }

        // Base case: not a walkable tile
        if (map[startRow][startCol] != '.') {
            return 0;
        }

        // mark visited so we don't count it again
        map[startRow][startCol] = '#';

        // count this tile + all 4 directions
        return 1
                + countConnectedTilesRecursive(map, startRow - 1, startCol) // up
                + countConnectedTilesRecursive(map, startRow + 1, startCol) // down
                + countConnectedTilesRecursive(map, startRow, startCol - 1) // left
                + countConnectedTilesRecursive(map, startRow, startCol + 1); // right
    }
    /**
     * Counts connected walkable tiles iteratively using an explicit stack.
     *
     * @param map mutable map of tiles
     * @param startRow starting row
     * @param startCol starting column
     * @return number of connected walkable tiles
     */
    public static int countConnectedTilesIterative(char[][] map, int startRow, int startCol) {
        // TODO: Implement iterative flood-fill / connected tile counting.

        // if starting tile is invalid or blocked
        if (isOutOfBounds(map, startRow, startCol) || map[startRow][startCol] != '.') {
            return 0;
        }

        int count = 0;
        Deque<CellPosition> stack = new ArrayDeque<>();

        // push starting position
        stack.push(new CellPosition(startRow, startCol));

        while (!stack.isEmpty()) {
            CellPosition current = stack.pop();
            int row = current.row();
            int col = current.col();

            // skip invalid positions
            if (isOutOfBounds(map, row, col)) {
                continue;
            }
            if (map[row][col] != '.') {
                continue;
            }
            // mark visited
            map[row][col] = '#';
            count++;

            // push neighbors
            pushNeighbor(stack, row - 1, col); // up
            pushNeighbor(stack, row + 1, col); // down
            pushNeighbor(stack, row, col - 1); // left
            pushNeighbor(stack, row, col + 1); // right
        }

        return count;
    }

    /**
     * Returns true if the tournament bracket contains a match with the given target name.
     * This public method should call a recursive helper.
     *
     * @param root root of the bracket tree
     * @param target match name to search for
     * @return true if found, false otherwise
     */
    public static boolean containsMatch(BracketNode root, String target) {
        // TODO: Replace this stub by calling a helper method.
        return containsMatchHelper(root, target);
    }

    /**
     * Helper method for recursive bracket tree search.
     *
     * @param node current node
     * @param target match name to search for
     * @return true if found, false otherwise
     */
    private static boolean containsMatchHelper(BracketNode node, String target) {
        // TODO: Implement recursive tree search.

        // Base case: reached end of branch
        if (node == null) {
            return false;
        }

        // Found case
        if (node.getMatchName().equals(target)) {
            return true;
        }

        // Recursive case: search left or right subtree
        return containsMatchHelper(node.getLeft(), target)
                || containsMatchHelper(node.getRight(), target);

    }
    /**
     * Optional utility students may use if they want to avoid repeating bounds checks.
     */
    public static boolean isOutOfBounds(char[][] map, int row, int col) {
        return row < 0 || row >= map.length || col < 0 || col >= map[row].length;
    }

    /**
     * Optional utility students may use in the iterative flood-fill.
     */
    public static void pushNeighbor(Deque<CellPosition> stack, int row, int col) {
        stack.push(new CellPosition(row, col));
    }
}
