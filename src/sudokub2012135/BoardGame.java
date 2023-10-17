package sudokub2012135;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.FileChooserUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicFileChooserUI;

/**
 *
 * @author Phan Thanh Sieu - B2012135
 */
// Lớp chứa các thành phần Java Swing và xử lý UI game
public class BoardGame extends javax.swing.JFrame implements ActionListener, KeyListener {

    // Mảng các JButton để hiển thị bảng game 9x9
    private JButton[][] cells;
    // Biến I và J là vị trí của ô hiện tại khi nhấp vào các button trong bảng game
    private int I;
    private int J;
    // Các biến tạo màu
    private final Color line_color = new Color(20, 50, 100);
    private final Color focus_color = new Color(150, 200, 250);
    private final Color focus_color_light = new Color(220, 240, 255);
    // Biến đếm xử lý thua
    private int countTicket = 3;
    // Biến trạng thái bắt đầu game
    private boolean isStarted = false;
    // Biến trạng thái game lưu vào file (thắng = 1, thua = -1)
    private int statusGame;
    // Biến xử lý thời gian
    private Timer timer;
    // Đối tượng xử lý logic game
    private final SudokuGame game;

    /**
     * Creates new form BoardGame
     */
    public BoardGame() {
        // Kéo thả trong Netbeans
        initComponents();
        // Khởi tạo UI bảng game
        initBoard();
        // Khởi tạo đối tượng game
        game = new SudokuGame();
    }

    // Phương thức khởi tạo UI bảng game
    public final void initBoard() {
        // Canh giữa cửa sổ chính
        this.setLocationRelativeTo(null);
        // Đặt cho Jpanel layout grid 9x9
        this.getContentPane().setLayout(null);
        panelBoard.setLayout(new GridLayout(9, 9));
        // Đặt kích thước cho bảng game
        panelBoard.setSize(500, 500);
        // Khởi tạo bảng game
        cells = new JButton[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JButton();
                ButtonUI ui = new BasicButtonUI();
                cells[i][j].setUI(ui);
                cells[i][j].setBackground(Color.WHITE);
                cells[i][j].setForeground(Color.BLACK);
                cells[i][j].setFont(new Font("Times New Roman", Font.BOLD, 30));
                cells[i][j].addActionListener(this);
                cells[i][j].addKeyListener(this);
                cells[i][j].setActionCommand(i + " " + j);
                panelBoard.add(cells[i][j]);
            }
        }
        // Khởi tạo viền
        initBorderBoard();
        // Tạo bộ đếm thời gian
        initTimer();
    }

    // Phương thức tạo bộ đếm thời gian
    public void initTimer() {
        timer = new Timer(10, (ActionEvent e) -> {
            lblTime.setText(nextTime(lblTime));
        });
    }

    // Phương thức cập nhật bộ đếm
    public String nextTime(JLabel lb) {
        String str[] = lb.getText().split(":");
        int tt = Integer.parseInt(str[3]);
        int s = Integer.parseInt(str[2]);
        int m = Integer.parseInt(str[1]);
        int h = Integer.parseInt(str[0]);
        String kq = "";
        int sum = tt + s * 100 + m * 60 * 100 + h * 60 * 60 * 100 + 1;
        if (sum % 100 > 9) {
            kq = ":" + sum % 100 + kq;
        } else {
            kq = ":0" + sum % 100 + kq;
        }
        sum /= 100;
        if (sum % 60 > 9) {
            kq = ":" + sum % 60 + kq;
        } else {
            kq = ":0" + sum % 60 + kq;
        }
        sum /= 60;
        if (sum % 60 > 9) {
            kq = ":" + sum % 60 + kq;
        } else {
            kq = ":0" + sum % 60 + kq;
        }
        sum /= 60;
        if (sum > 9) {
            kq = sum + kq;
        } else {
            kq = "0" + sum + kq;
        }
        return kq;
    }

    // Phương thức tạo viền cho game
    public void initBorderBoard() {
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                cells[i][j].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, line_color));
                cells[i][j + 2].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, line_color));
                cells[i + 2][j + 2].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, line_color));
                cells[i + 2][j].setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, line_color));
                cells[i][j + 1].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, line_color));
                cells[i + 1][j + 2].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, line_color));
                cells[i + 2][j + 1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, line_color));
                cells[i + 1][j].setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, line_color));
                cells[i + 1][j + 1].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, line_color));
            }
        }
    }

    // Phương thức đặt lại màu nền cho nút
    public void resetFocus() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setBackground(Color.white);
            }
        }
    }

    // Phương thức tô sáng màu của những ô trùng nhau
    public void hightLightCell() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!"".equals(cells[I][J].getText())) {
                    if (cells[i][j].getText().equals(cells[I][J].getText())) {
                        // Tô sáng toàn bộ ô giống ô đang nhấp
                        cells[i][j].setBackground(focus_color);
                        // Tô theo hàng, cột và ô vuông 3x3
                        for (int k = 0; k < 9; k++) {
                            cells[I][k].setBackground(focus_color_light);
                            cells[k][J].setBackground(focus_color_light);
                            cells[I / 3 * 3 + k / 3][J / 3 * 3 + k % 3].setBackground(focus_color_light);
                        }
                    }
                } else {
                    for (int k = 0; k < 9; k++) {
                        cells[I][k].setBackground(focus_color_light);
                        cells[k][J].setBackground(focus_color_light);
                        cells[I / 3 * 3 + k / 3][J / 3 * 3 + k % 3].setBackground(focus_color_light);
                    }
                }
            }
        }
        cells[I][J].setBackground(focus_color);
    }

    // Phương thức điền số vào bảng game
    public void fillBoard(int[][] a) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (a[i][j] != 0) {
                    cells[i][j].setText(a[i][j] + "");
                }
            }
        }
    }

    // Phương thức đặt lại bảng game
    public void resetBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText("");
                cells[i][j].setForeground(Color.black);
            }
        }
    }

    // Phương thức tô xanh lá các ô khi dùng chức năng giải
    public void focusFillSolve() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (game.getIsFill()[i][j] == false) {
                    cells[i][j].setBackground(Color.green);
                }
            }
        }
    }

    // Phương thức chọn độ khó
    public void chooseDifficult() {
        int choose = comboDoKho.getSelectedIndex();
        int kho;
        kho = switch (choose) {
            case 0 ->
                30;
            case 1 ->
                40;
            case 2 ->
                50;
            default ->
                30;
        };
        SudokuGame.kho = kho;
    }

    // Phương thức định dạng trạng thái game để lưu vào file
    public String statusGame() {
        String txt = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!"".equals(cells[i][j].getText()) && game.getIsFill()[i][j]) {
                    txt += cells[i][j].getText() + " ";
                } else {
                    txt += "0" + " ";
                }
            }
            txt += "\n";
        }
        return txt;
    }

    // Phương thức dùng để lưu trạng thái game vào file
    public void saveGame() throws IOException {
        String path = "D:\\SudokuB2012135\\src\\SudokuSave\\";
        File pathDir = new File(path);
        if (!pathDir.exists()) {
            pathDir.mkdir();
        }
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
            String time = sdf.format(date);
            String tenfile = "SudokuPuzzle - " + time.trim();
            System.out.println(tenfile);
            File fileDir = new File(path + tenfile + ".txt");
            try (Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileDir), "UTF8"))) {
                out.append(statusGame());
                out.flush();
                out.close();
            }
            JOptionPane.showMessageDialog(null, "Trạng thái trò chơi Sudoku đã được lưu.\n Bạn có thể xem ở: " + path, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi đường dẫn!!!");
            System.out.println(e.getMessage());
        }
    }

    // Phương thức đặt trạng thái các nút
    public void setEnable(boolean status) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setEnabled(status);
            }
        }
    }
    
    //Phương thức chuyển text lưu game thành mảng số nguyên
    public void openGame(File file) throws FileNotFoundException, IOException {
        
        String txt = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line!=null) {
                if(!" ".equals(line) && !"\n".equals(line)) {
                    txt += line;
                }
                line = reader.readLine();
            }
        }
        game.resetIsFill();
        String[] saveGameString = txt.split(" ");
        for (int i = 0; i<9; i++) {
            for (int j = 0; j<9; j++) {
                game.getBoard()[i][j] = Integer.parseInt(saveGameString[i*9 + j]);
                if (game.getBoard()[i][j] != 0) {
                    game.getIsFill()[i][j] = true;
                }
            }
        }
        setEnable(true);
        resetBoard();
        countTicket = 3;
        lblCount.setText("Số lần nhập: 3");
        fillBoard(game.getBoard());
        isStarted = true;
        game.sudoku();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBoard = new javax.swing.JPanel();
        panelControl = new javax.swing.JPanel();
        btnChoiMoi = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        comboDoKho = new javax.swing.JComboBox<>();
        btnGiai = new javax.swing.JButton();
        panelTime = new javax.swing.JPanel();
        lblTime = new javax.swing.JLabel();
        panelCount = new javax.swing.JPanel();
        lblCount = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnContinue = new javax.swing.JButton();
        btnOpenGame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        panelBoard.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phan Thành Siêu - B2012135", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 14))); // NOI18N

        javax.swing.GroupLayout panelBoardLayout = new javax.swing.GroupLayout(panelBoard);
        panelBoard.setLayout(panelBoardLayout);
        panelBoardLayout.setHorizontalGroup(
            panelBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        panelBoardLayout.setVerticalGroup(
            panelBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        panelControl.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 14))); // NOI18N

        btnChoiMoi.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnChoiMoi.setText("Chơi mới");
        btnChoiMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoiMoiActionPerformed(evt);
            }
        });

        btnThoat.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        comboDoKho.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        comboDoKho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dễ", "Trung bình", "Khó" }));

        btnGiai.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnGiai.setText("Giải");
        btnGiai.setEnabled(false);
        btnGiai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGiaiActionPerformed(evt);
            }
        });

        panelTime.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTime.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTime.setText("00:00:00:00");

        javax.swing.GroupLayout panelTimeLayout = new javax.swing.GroupLayout(panelTime);
        panelTime.setLayout(panelTimeLayout);
        panelTimeLayout.setHorizontalGroup(
            panelTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTimeLayout.setVerticalGroup(
            panelTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelCount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCount.setText("Số lần nhập: 3");
        lblCount.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelCountLayout = new javax.swing.GroupLayout(panelCount);
        panelCount.setLayout(panelCountLayout);
        panelCountLayout.setHorizontalGroup(
            panelCountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCountLayout.setVerticalGroup(
            panelCountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCount, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnSave.setText("Lưu");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnStop.setText("Dừng");
        btnStop.setEnabled(false);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        btnContinue.setText("Tiếp tục");
        btnContinue.setEnabled(false);
        btnContinue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinueActionPerformed(evt);
            }
        });

        btnOpenGame.setText("Mở");
        btnOpenGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnOpenGame, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnContinue)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnContinue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnStop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenGame))
        );

        javax.swing.GroupLayout panelControlLayout = new javax.swing.GroupLayout(panelControl);
        panelControl.setLayout(panelControlLayout);
        panelControlLayout.setHorizontalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGiai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChoiMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelControlLayout.createSequentialGroup()
                        .addComponent(comboDoKho, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelControlLayout.setVerticalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChoiMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboDoKho, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnGiai, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Xử lý sự kiện nút Chơi Mới
    private void btnChoiMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoiMoiActionPerformed
        chooseDifficult();
        game.newGame();
        game.removeCell();
        game.sudoku();
        setEnable(true);
        countTicket = 3;
        lblCount.setText("Số lần nhập: 3");
        btnGiai.setEnabled(true);
        btnStop.setEnabled(true);
        btnSave.setEnabled(true);
        lblTime.setText("00:00:00:00");
        resetBoard();
        resetFocus();
        fillBoard(game.getBoard());
        isStarted = true;
        timer.start();
    }//GEN-LAST:event_btnChoiMoiActionPerformed

    // Xử lý sự kiện nút Giải
    private void btnGiaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGiaiActionPerformed
        fillBoard(game.getSolveBoard());
        focusFillSolve();
        timer.stop();
        btnSave.setEnabled(false);
        btnGiai.setEnabled(false);
        btnContinue.setEnabled(false);
        btnStop.setEnabled(false);
        isStarted = !isStarted;
    }//GEN-LAST:event_btnGiaiActionPerformed

    // Xử lý sự kiện nút Thoát
    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        Object[] options = {"Có, chắc rồi", "Không, tôi đã thay đổi ý định"};
        int result = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn thoát?", "Xác nhận", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnThoatActionPerformed

    // Xử lý sự kiện nút Lưu
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            saveGame();
        } catch (IOException ex) {
            System.out.println("Error");
        }
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed

    // Xử lý sự kiện nút dừng
    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        timer.stop();
        panelBoard.setVisible(false);
        btnContinue.setEnabled(true);
        btnSave.setEnabled(false);
        btnGiai.setEnabled(false);
        btnStop.setEnabled(false);
        btnChoiMoi.setEnabled(false);
    }//GEN-LAST:event_btnStopActionPerformed

    // Xử lý sự kiện nút Tiếp Tục
    private void btnContinueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinueActionPerformed
        btnStop.setEnabled(true);
        btnGiai.setEnabled(true);
        timer.start();
        panelBoard.setVisible(true);
        btnChoiMoi.setEnabled(true);
    }//GEN-LAST:event_btnContinueActionPerformed

    private void btnOpenGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenGameActionPerformed
        final JFileChooser fileOpenGame = new JFileChooser("D:\\SudokuB2012135\\src\\SudokuSave\\");
        int returnVal = fileOpenGame.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileOpenGame.getSelectedFile();
                openGame(file);
            } catch (IOException ex) {
                Logger.getLogger(BoardGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Cancle");
        }
        timer.start();
    }//GEN-LAST:event_btnOpenGameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BoardGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BoardGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BoardGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BoardGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BoardGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChoiMoi;
    private javax.swing.JButton btnContinue;
    private javax.swing.JButton btnGiai;
    private javax.swing.JButton btnOpenGame;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnThoat;
    private javax.swing.JComboBox<String> comboDoKho;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel panelBoard;
    private javax.swing.JPanel panelControl;
    private javax.swing.JPanel panelCount;
    private javax.swing.JPanel panelTime;
    // End of variables declaration//GEN-END:variables

    // Xử lý sự kiện nhấp vào ô
    @Override
    public void actionPerformed(ActionEvent e) {
        // Nếu game chưa bắt đầu không nhấp
        if (!isStarted) {
            return;
        }
        // Trích vị trí i, j từ chuỗi sự kiện gán vào I, J
        String s = e.getActionCommand();
        if (s == null) {
            return;
        }
        int k = s.indexOf(32);
        int i = Integer.parseInt(s.substring(0, k));
        int j = Integer.parseInt(s.substring(k + 1, s.length()));
        I = i;
        J = j;
        // Tô màu trắng (làm mới)
        resetFocus();
        // Tô màu hỗ trợ giải
        hightLightCell();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Xử lý sự kiện bàn phím
    @Override
    public void keyPressed(KeyEvent e) {
        // Lấy mã phím
        int kc = e.getKeyCode();
        // Nếu ô đang nhấp đã điền hoặc game chưa bắt đầu thì không làm gì
        if (game.getIsFill()[I][J] || !isStarted) {
            return;
        }
        // Chỉ nhận các phím từ 1 đến 9
        if ((kc >= 49 && kc <= 57) || (kc >= 97 && kc <= 105)) {
            cells[I][J].setText((char) kc + "");
        } else {
            return;
        }
//        System.out.println(cells[I][J].getText() + " / " + game.getSolveBoard()[I][J]);
        // Nếu tại ô đang nhấp trùng khớp với ô trong bảng kết quả thì điền vào:
        if (cells[I][J].getText().equals(game.getSolveBoard()[I][J] + "")) {
            btnSave.setEnabled(true);
            cells[I][J].setBackground(Color.green);
            cells[I][J].setForeground(Color.black);
            game.getIsFill()[I][J] = true;
        } else {
            // Ngược lại trừ bộ đếm thua
            cells[I][J].setForeground(Color.red);
            countTicket--;
            lblCount.setText("Số lần nhập: " + countTicket);
            // Nếu chọn sai 3 lần thì thua
            if (countTicket == 0) {
                statusGame = -1;
                timer.stop();
                btnStop.setEnabled(false);
                setEnable(false);
                JOptionPane.showMessageDialog(this, "Bạn đã thua ở thời gian: " + lblTime.getText());
            }
        }
        // Nếu đã điền hết thì thắng
        if (game.checkWin()) {
            statusGame = 1;
            timer.stop();
            btnStop.setEnabled(false);
            btnGiai.setEnabled(false);
            setEnable(false);
            JOptionPane.showMessageDialog(this, "Bạn đã thắng với thời gian: " + lblTime.getText());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
