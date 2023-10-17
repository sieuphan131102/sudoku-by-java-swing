package sudokub2012135;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Phan Thanh Sieu - B2012135
 */

// Lớp chứa các phương thức xử lý logic game
public class SudokuGame {
    // Mảng board lưu các số trong game
    private int[][] board;
    // Biến isFill lưu trạng thái đã điền hoặc chưa điền
    private final boolean[][] isFill;
    // Mảng solveBoard chứa kết quả của giải thuật game
    private final int[][] solveBoard;
    // Biến BOARD_SIZE là kích cở bảng game (9x9)
    private final int BOARD_SIZE;
    // Biến chỉ định độ khó
    public static int kho = 0;
    
    //Phương thức khởi tạo
    public SudokuGame() {
        // Chỉ định kích thước bảng game 9x9
        this.BOARD_SIZE = 9;
        // Khởi tạo mảng board
        this.board = new int[BOARD_SIZE][BOARD_SIZE];
        // Khởi tạo trạng thái
        this.isFill = new boolean[BOARD_SIZE][BOARD_SIZE];
        // Khởi tạo bảng kết quả
        this.solveBoard = new int[BOARD_SIZE][BOARD_SIZE];
    }
    
    // Phương thức tạo trò chơi mới
    public void newGame() {
        // Thiết lập bảng với các giá trị ban đầu là 0
        board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
                isFill[i][j] = false;
            }
        }
        // Điền các giá trị ban đầu ngẫu nhiên vào bảng
        Random random = new Random();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Tạo một danh sách chứa các giá trị có thể được điền vào ô hiện tại
                List<Integer> possibleValues = new ArrayList<>();
                for (int value = 1; value <= BOARD_SIZE; value++) {
                    if (check(i, j, value)) {
                        possibleValues.add(value);
                    }
                }
                // Nếu không có giá trị nào phù hợp, bắt đầu lại từ đầu
                if (possibleValues.isEmpty()) {
                    newGame();
                    return;
                }
                // Chọn một giá trị ngẫu nhiên từ danh sách các giá trị có thể được điền vào ô hiện tại
                int index = random.nextInt(possibleValues.size());
                int value = possibleValues.get(index);
                board[i][j] = value;
            }
        }
    }
    
    public void resetIsFill() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                isFill[i][j] = false;
            }
        }
    }
    
    // Phương thức xóa ô ngẫu nhiên theo độ khó
    public void removeCell() {
        Random rd = new Random();
        while (kho > 0) {
            // Tạo chỉ số hàng ngẫu nhiên
            int rowRan = rd.nextInt(BOARD_SIZE);
            // Tạo chỉ số cột ngẫu nhiên
            int colRan = rd.nextInt(BOARD_SIZE);
            // Nếu ô trống thì xóa
            if (board[rowRan][colRan] != 0) {
                board[rowRan][colRan] = 0;
                kho--;
            }
        }
        // Khởi tạo trạng thái từng ô
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                isFill[i][j] = board[i][j] != 0;
            }
        }
    }
    
    // Phương thức kiểm tra chiến thắng
    public boolean checkWin() {
        // Nếu không còn ô nào chưa điền thì thắng
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isFill[i][j] == false){
                    return false;
                }
            }
        }
        return true;
    }
    
    // Phương thức kiểm tra ô ở vị trí [row][col] có điền k chưa
    public boolean check(int row, int col, int k) {
        for(int i = 0; i < 9; i++) {
            // Kiểm tra hàng
            if(board[row][i]==k)return false;
            // Kiểm tra cột
            if(board[i][col]==k)return false;
            // Kiểm tra ô vuông 3x3
            if(board[row / 3 * 3 + i/3][col / 3 * 3 + i % 3]==k)return false;
        }
        return true;
    }
    
    // Phương thức giải game Sudoku bằng giải thuật quai lui - vét cạn
    public void sudoku() {
        // Lặp qua bảng game
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                // Nếu ô trống thì kiểm tra luật chơi
                if(board[i][j]==0) {
                    for(int k = 1; k <= 9; k++) {
                        // Nếu k hợp lệ thì gán bằng k tạm thời
                        if(check(i, j, k)) {
                            board[i][j]=k;
                            // Đệ qui
                            sudoku();
                            // Quai lui nếu có trường hợp không hợp lệ
                            board[i][j]=0;
                        }
                    }
                    return;
                }
            }
        }
        // Lưu kết quả giải được vào mảng solveBoard
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, solveBoard[i], 0, BOARD_SIZE);
        }
    }
    
    // Phương thức in mảng board ra console
    public void print() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    // Phương thức getter của thuộc tính isFill
    public boolean[][] getIsFill() {
        return isFill;
    }
    
    // Phương thức getter của thuộc tính board
    public int[][] getBoard() {
        return board;
    }
    
    // Phương thức getter của thuộc tính solveBoard
    public int[][] getSolveBoard() {
        return solveBoard;
    }
}
