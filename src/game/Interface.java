package game;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

class Interface {

    private static final int borderWidthHalf = 2;
    private static final Color borderColor = Color.lightGray;
    private static final String highScoreRecord = "highscore.dat";
    private final int style;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel savePanel;
    private JLabel[][] labels;
    private JLabel scoreLabel;
    private JLabel highScoreLabel;
    private JLabel southLabel;
    private JButton saveButton;
    private Game game;
    private long highScore;
    private boolean saved;

    Interface(int gameStyle) {
        style = gameStyle;
        highScore = loadHighScore();
        initialize(false);
    }

    Interface(File savedGame) throws IOException {
        game = new Game(savedGame);
        style = game.getStyle();
        highScore = loadHighScore();
        initialize(true);
    }

    private void initialize(boolean gameInitialized) {
        mainFrame = new JFrame("2048");
        mainPanel = new JPanel();
        northPanel = new JPanel();
        savePanel = new JPanel();

        if (!gameInitialized) {
            game = new Game(style);
            saved = false;
        } else {
            saved = true;
        }

        labels = new JLabel[game.rows][game.colomns];
        int i, j;
        for (i = 0; i < game.rows; i++) {
            for (j = 0; j < game.colomns; j++) {
                labels[i][j] = new JLabel();
                labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                labels[i][j].setFont(game.font);
                labels[i][j].setOpaque(true);
            }
        }

        labels[0][0].setBorder(BorderFactory.createMatteBorder(
                borderWidthHalf * 2, borderWidthHalf * 2, borderWidthHalf, borderWidthHalf, borderColor));
        for (j = 1; j < game.colomns - 1; j++) {
            labels[0][j].setBorder(BorderFactory.createMatteBorder(
                    borderWidthHalf * 2, borderWidthHalf, borderWidthHalf, borderWidthHalf, borderColor));
        }
        labels[0][game.colomns - 1].setBorder(BorderFactory.createMatteBorder(
                borderWidthHalf * 2, borderWidthHalf, borderWidthHalf, borderWidthHalf * 2, borderColor));
        for (i = 1; i < game.rows - 1; i++) {
            labels[i][0].setBorder(BorderFactory.createMatteBorder(
                    borderWidthHalf, borderWidthHalf * 2, borderWidthHalf, borderWidthHalf, borderColor));
            for (j = 1; j < game.colomns - 1; j++) {
                labels[i][j].setBorder(BorderFactory.createMatteBorder(
                        borderWidthHalf, borderWidthHalf, borderWidthHalf, borderWidthHalf, borderColor));
            }
            labels[i][game.colomns - 1].setBorder(BorderFactory.createMatteBorder(borderWidthHalf,
                    borderWidthHalf, borderWidthHalf, borderWidthHalf * 2, borderColor));
        }
        labels[game.rows - 1][0].setBorder(BorderFactory.createMatteBorder(
                borderWidthHalf, borderWidthHalf * 2, borderWidthHalf * 2, borderWidthHalf, borderColor));
        for (j = 1; j < game.colomns - 1; j++) {
            labels[game.rows - 1][j].setBorder(BorderFactory.createMatteBorder(
                    borderWidthHalf, borderWidthHalf, borderWidthHalf * 2, borderWidthHalf, borderColor));
        }
        labels[game.rows - 1][game.colomns - 1].setBorder(BorderFactory.createMatteBorder(borderWidthHalf,
                borderWidthHalf, borderWidthHalf * 2, borderWidthHalf * 2, borderColor));

        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        highScoreLabel = new JLabel();
        highScoreLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        highScoreLabel.setForeground(Color.black);

        saveButton = new JButton("Save");
        saveButton.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        saveButton.setFocusable(false);
        saveButton.setMnemonic('S');
        saveButton.addActionListener(e -> {
            saved = saveGame();
        });

        southLabel = new JLabel();
        southLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
        southLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (game.getHaveWon()) {
            southLabel.setText("YOU HAVE WON!");
        } else {
            southLabel.setText(game.hint);
        }

        mainFrame.setLayout(new BorderLayout(0, 5));
        mainFrame.add(northPanel, BorderLayout.NORTH);
        mainFrame.add(southLabel, BorderLayout.SOUTH);
        mainFrame.add(mainPanel, BorderLayout.CENTER);

        mainPanel.setLayout(new GridLayout(game.rows, game.colomns));
        for (i = 0; i < game.rows; i++) {
            for (j = 0; j < game.colomns; j++) {
                mainPanel.add(labels[i][j]);
            }
        }

        northPanel.setLayout(new BorderLayout(0, 10));
        northPanel.add(scoreLabel, BorderLayout.NORTH);
        northPanel.add(highScoreLabel, BorderLayout.CENTER);
        northPanel.add(savePanel, BorderLayout.EAST);

        savePanel.setLayout(new BorderLayout(10, 0));
        savePanel.add(saveButton, BorderLayout.CENTER);
        savePanel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
                new Dimension(0, 0)), BorderLayout.EAST);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("2048.png"));
        mainFrame.setIconImage(imageIcon.getImage());

        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (saved) {
                    exit();
                    return;
                }
                int option = JOptionPane.showConfirmDialog(mainFrame, "Do you want to save your game?",
                        "Exiting game", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch (option) {
                    case JOptionPane.YES_OPTION:
                        if (saveGame()) {
                            exit();
                        }
                        break;
                    case JOptionPane.NO_OPTION:
                        exit();
                        break;
                    default:
                        break;
                }
            }
        });

        mainFrame.setBounds(400, 100, 400, 530);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        mainFrame.addKeyListener(new MyKeyListener());
        display();
    }

    private void display() {
        int i, j;
        for (i = 0; i < game.rows; i++) {
            for (j = 0; j < game.colomns; j++) {
                labels[i][j].setBackground(game.style[game.getTile(i, j)].bgColor);
                labels[i][j].setForeground(game.style[game.getTile(i, j)].txColor);
                labels[i][j].setText(game.style[game.getTile(i, j)].label);
            }
        }
        scoreLabel.setText("SCORE: " + game.getScore());
        highScoreLabel.setText("High Score: " + highScore);
    }

    private boolean saveGame() {
        JFileChooser fChooser = new JFileChooser();
        fChooser.setCurrentDirectory(Main.defaultSavePath);
        fChooser.setFileFilter(new FileNameExtensionFilter(Main.savedGameDescription, Main.savedGameExtension));
        int ret = fChooser.showSaveDialog(mainFrame);
        if (ret != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        File file = fChooser.getSelectedFile();
        String filePath = file.getPath();
        if (!filePath.endsWith('.' + Main.savedGameExtension)) {
            filePath += '.' + Main.savedGameExtension;
        }
        try {
            game.save(filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Saving failed!\n" + e.getMessage(),
                    "Saving Failed", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void recordHighScore(long score) {
        long[] tmp = new long[Style.styleNum];
        try {
            DataInputStream dataInput = new DataInputStream(new FileInputStream(highScoreRecord));
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = dataInput.readLong();
                if (tmp[i] < 0L) {
                    throw new IOException("Input logical error");
                }
            }
        } catch (IOException e) {
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = 0L;
            }
            e.printStackTrace();
        }
        tmp[style] = score;
        try {
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(highScoreRecord));
            for (int i = 0; i < tmp.length; i++) {
                dataOutput.writeLong(tmp[i]);
            }
            dataOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long loadHighScore() {
        long score;
        try {
            DataInputStream dataInput = new DataInputStream(new FileInputStream(highScoreRecord));
            long tmp[] = new long[Style.styleNum];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = dataInput.readLong();
                if (tmp[i] < 0L) {
                    throw new IOException("Input logical error");
                }
            }
            score = tmp[style];
        } catch (IOException e) {
            score = 0L;
            e.printStackTrace();
        }
        return score;
    }

    private void exit() {
        recordHighScore(highScore);
        mainFrame.dispose();
        Main.main(null);
        System.gc();
    }

    private class MyKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            int result;
            switch (key) {
                case KeyEvent.VK_UP:
                    result = game.move(Game.MOVE_UP);
                    break;
                case KeyEvent.VK_DOWN:
                    result = game.move(Game.MOVE_DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    result = game.move(Game.MOVE_LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    result = game.move(Game.MOVE_RIGHT);
                    break;
                default:
                    result = Game.NO_MOVEMENT;
            }
            if (result != Game.NO_MOVEMENT) {
                saved = false;
                if (game.getScore() > highScore) {
                    highScore = game.getScore();
                    highScoreLabel.setForeground(Color.red);
                }
            }
            switch (result) {
                case Game.NORMAL_STEP:
                    display();
                    break;
                case Game.GAME_OVER:
                    southLabel.setText("GAME OVER!");
                    display();
                    int option1 = JOptionPane.showConfirmDialog(mainFrame,
                            "You have lost! Your score is " + game.getScore()
                                    + ".\nDo you want to start new game?",
                            "Game over!", JOptionPane.YES_NO_OPTION);
                    if (option1 == JOptionPane.YES_OPTION) {
                        mainFrame.dispose();
                        initialize(false);
                    } else {
                        exit();
                    }
                    break;
                case Game.PLAYER_WON:
                    southLabel.setText("YOU HAVE WON!");
                    display();
                    int option2 = JOptionPane.showConfirmDialog(mainFrame,
                            "You have won! Do you want to continue?",
                            "Congratulations!", JOptionPane.YES_NO_OPTION);
                    if (option2 == JOptionPane.NO_OPTION) {
                        exit();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
