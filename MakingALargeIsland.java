import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * LeetCode 827. Making A Large Island
 * https://leetcode.com/problems/making-a-large-island/
 */
public class MakingALargeIsland {


    // **** global variable(s) ****
    static int islandID;
    static HashMap<Integer, Integer> sizes;
    static int[][] dirs = new int[][]{ {0,1}, {0,-1}, {-1,0}, {1,0} };


    /**
     * Visit an entire island.
     * Recursive DFS call.
     * Execution O(n * m)
     */
    static void visitIsland(int[][] grid, int r, int c, int[] size) {

        // **** base condition (water or visited island) ****
        if (grid[r][c] != 1)
            return;

        // **** color this cell ****
        grid[r][c] = islandID;

        // **** count this cell ****
        size[0]++;

        // **** recurse right ****
        if (c < grid[r].length - 1)
            visitIsland(grid, r, c + 1, size);

        // **** recurse down ****
        if (r < grid.length - 1)
            visitIsland(grid, r + 1, c, size);

        // **** recurse left ****
        if (c > 0)
            visitIsland(grid, r, c - 1, size);

        // **** recurse up ****
        if (r > 0)
            visitIsland(grid, r - 1, c, size);
    }


   /**
     * Given a 2D grid map of 1s (land) and 0s (water),
     * assign each island a unique color (ID).
     */
    static void colorIslands(int[][] grid, int rows, int cols) {

        // **** initialization ****
        int[] size  = new int[] {0};

        // **** loop once per cell in the grid ****
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                // **** visit cell (if on land and not visited yet) ****
                if (grid[r][c] == 1) {

                    // **** initialize this island size ****
                    size[0] = 0;

                    // **** visit the island starting at specified cell ****
                    visitIsland(grid, r, c, size);

                    // **** save the island ID (color) and size in the hash map ****
                    sizes.put(islandID, size[0]);

                    // **** increment the island ID (different color) ****
                    islandID++;
                }
            }
        }
    }    


    /**
     * Join islands and return the largest size.
     */
    static int joinIslands(int[][] grid, int rows, int cols) {

        // **** sanity check(s) ****
        if (grid.length == 1)
            return 1;

        // **** initialization ****
        int largestSize         = 0;
        int id                  = 0;
        int size                = 0;
        Set<Integer> ids        = new HashSet<>();

        // **** additional sanity check ****
        if (sizes.size() == 1 && sizes.get(2) == rows * cols)
            return rows * cols;

        // **** loop checking water neighbors ****
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                // **** skip land ****
                if (grid[r][c] != 0)
                    continue;

                // **** clear the list of ids ****
                ids.clear();

                // **** clear the size of the island ****
                size = 0;

                // **** check all four directions ****
                for (int[] dir : dirs) {

                    // **** set the row and column ****
                    int rr = r + dir[0];
                    int cc = c +  dir[1];

                    // **** check bounds and not water ****
                    if (rr < rows && rr >= 0 && cc >= 0 && cc < cols && grid[rr][cc] != 0) {

                        // **** get island ID ****
                        id = grid[rr][cc];

                        // **** join this island (if needed) ****
                        if (!ids.contains(id)) {
                            ids.add(id);
                            size += sizes.get(id);
                        }
                    }
                }

                // **** include in the count the cell we are visiting ****
                size++;

                // **** update the largest joint island size (as needed) ****
                largestSize = (size > largestSize) ? size : largestSize;
            }
        }

        // **** return the size of the largest joint island ****
        return largestSize;
    }


    /**
     * You are allowed to change at most one 0 to be 1.
     * Return the size of the largest island in grid 
     * AFTER applying this operation.
     * 
     * NOTE: in the current version of the LeetCode problem,
     * the number of rows == number of columns. We will allow 
     * for different dimensions in this implementation.
     * 
     * Runtime: 66 ms, faster than 35.39% of Java online submissions.
     * Memory Usage: 78.5 MB, less than 11.28% of Java online submissions.
     */
    static int largestIsland(int[][] grid) {
        
        // **** sanity check(s) ****
        if (grid.length == 1 && grid[0].length == 1)
            return 1;

        // **** initialization ****
        int rows    = grid.length;
        int cols    = grid[0].length;
        islandID    = 2;
        sizes       = new HashMap<Integer,Integer>();

        // **** color islands and associate the color with their size ****
        colorIslands(grid, rows, cols);

        // **** join islands and determine which is the largest joint island ****
        return joinIslands(grid, rows, cols);
    }


    /**
     * Test scaffolding
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // **** open a buffered reader ****
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // **** read the number of rows and columns ****
        int[] rc = Arrays.stream(br.readLine().trim().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();

        // **** ****
        int rows = rc[0];
        int cols = rc[1];

        // ???? ????
        System.out.println("main <<< rows: " + rows + " cols: " + cols);

        // **** declare and populate grid ****
        int[][] grid = new int[rows][];
        for (int i = 0; i < rows; i++) {
            grid[i] = Arrays.stream(br.readLine().trim().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
        }

        // **** close the buffered reader ****
        br.close();

        // ???? ????
        System.out.println("main <<< grid: ");
        for (int r = 0; r < rows; r++)
            System.out.println(Arrays.toString(grid[r]));

        // **** generate and display size of the largest island ****
        System.out.println("main <<< largestIsland: " + largestIsland(grid));
    }
}