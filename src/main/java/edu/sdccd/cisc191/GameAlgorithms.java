package edu.sdccd.cisc191;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Module 5 Lab: Recursion + Algorithms
 *
 * Reflection Questions:
 * 1. What is the base case for your recursive binary search?
 *    When low > high, the search range is empty and target isn't there. So, return -1.
 *
 * 2. Why is recursion natural for the bracket tree?
 *    A tree has a recursive structure as each node has subtrees that are also trees.
 *    Logic is to search this node, then its left and right children.
 *
 * 3. Why might the iterative tile-counting method be safer on a very large map?
 *    Too many recursions may cause a StackOverflowError because every call adds a frame to the call stack.
 *    The iterative version uses a heap-allocated Deque instead, which can grow much larger.
 *
 * 4. Which problems in this lab felt more natural with loops, and which felt more natural with recursion?
 *    I think the loop version of the binary search was a little more straightforward.
 *    The bracket search and tile flood-fill felt more natural with
 *    recursion because they work with structures that are built for trees and subtrees.
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
        // Base case: search range is empty
        if (low > high) {
            return -1;
        }

        int mid = low + (high - low) / 2;

        if (sortedMatchIds[mid] == target) {
            return mid;                                              // Means we found target
        } else if (sortedMatchIds[mid] < target) {
            return findMatchRecursiveHelper(sortedMatchIds, target, mid + 1, high); // Search right half
        } else {
            return findMatchRecursiveHelper(sortedMatchIds, target, low, mid - 1); // Search left half
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
        int low = 0;
        int high = sortedMatchIds.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (sortedMatchIds[mid] == target) {
                return mid;          // Means we found target
            } else if (sortedMatchIds[mid] < target) {
                low = mid + 1;       // Narrow to right half
            } else {
                high = mid - 1;      // Narrow to left half
            }
        }

        return -1; // Target not found
    }

    /**
     * Counts connected walkable tiles recursively.
     * Walkable tiles are represented by '.'.
     * Blocked tiles can be any other character.
     *
     * This method should count the size of the connected region starting at (startRow, startCol).
     * Count only vertical and horizontal neighbors, not diagonals.
     *
     * @param map mutable map of tiles
     * @param startRow starting row
     * @param startCol starting column
     * @return number of connected walkable tiles
     */
    public static int countConnectedTilesRecursive(char[][] map, int startRow, int startCol) {
        // Base case: out of bounds
        if (isOutOfBounds(map, startRow, startCol)) {
            return 0;
        }

        // Base case: not a walkable tile (already visited '#' or blocked)
        if (map[startRow][startCol] != '.') {
            return 0;
        }

        // Mark this tile as visited 'V' so we don't count it again
        map[startRow][startCol] = 'V';

        // Count this tile plus all connected neighbors (up, down, left, right)
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
        // Check start position
        if (isOutOfBounds(map, startRow, startCol) || map[startRow][startCol] != '.') {
            return 0;
        }

        Deque<CellPosition> stack = new ArrayDeque<>();
        pushNeighbor(stack, startRow, startCol);
        int count = 0;

        while (!stack.isEmpty()) {
            CellPosition current = stack.pop();
            int row = current.row();
            int col = current.col();

            // Skip if out of bounds or not walkable
            if (isOutOfBounds(map, row, col) || map[row][col] != '.') {
                continue;
            }

            // Mark visited and count
            map[row][col] = 'V';
            count++;

            // Push all four neighbors
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
        // Base case: reached a null node. Means target not found on this path
        if (node == null) {
            return false;
        }

        // Found the target at this node
        if (node.getMatchName().equals(target)) {
            return true;
        }

        // Recursively search left and right subtrees
        return containsMatchHelper(node.getLeft(), target)
                || containsMatchHelper(node.getRight(), target);
    }

    /**
     * Used to avoid repeating bounds checks.
     */
    public static boolean isOutOfBounds(char[][] map, int row, int col) {
        return row < 0 || row >= map.length || col < 0 || col >= map[row].length;
    }

    /**
     * Used in the iterative flood-fill.
     */
    public static void pushNeighbor(Deque<CellPosition> stack, int row, int col) {
        stack.push(new CellPosition(row, col));
    }
}