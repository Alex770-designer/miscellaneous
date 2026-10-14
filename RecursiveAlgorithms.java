public class RecursiveAlgorithms {
    public static int shortestPath(char[][] grid, int row, int col, boolean[][] visited, int steps) {
        int shortest = -1;

        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length) {
            return -1;
        }
        if (grid[row][col] == '#' || grid[row][col] == 'X' || visited[row][col] == true) {
            return -1;
        }
        if (grid[row][col] == 'E') {
            return steps;
        }
        
        visited[row][col] = true;

        int up = shortestPath(grid, row-1, col, visited, steps+1);
        int down = shortestPath(grid, row+1, col, visited, steps+1);
        int left = shortestPath(grid, row, col-1, visited, steps+1);
        int right = shortestPath(grid, row, col+1, visited, steps+1);

        visited[row][col] = false;
        
        if (up != -1) {
            shortest = up;
        }

        if (down != -1 && (shortest == -1 || down < shortest)) {
            shortest = down;
        }

        if (left != -1 && (shortest == -1 || left < shortest)) {
            shortest = left;
        }

        if (right != -1 && (shortest == -1 || right < shortest)) {
            shortest = right;
        }
        return shortest;
    }
}