package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 游戏界面类，负责游戏内容的显示和操作的输入
 *
 * @author 王力博
 */
public class Interface {

	private static final int borderWidthHalf = 2;
	private static final Color borderColor = Color.lightGray;

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
	private final int style;
	private boolean saved;

	/**
	 * 存档文件路径
	 */
	protected static final String highScoreRecord = "highscore.dat";

	/**
	 * 构造指定游戏模式的游戏界面
	 *
	 * @param gameStyle
	 *            游戏模式类型
	 * @see Game
	 */
	protected Interface(int gameStyle) {
		style = gameStyle;
		highScore = loadHighScore();
		initialize(false);
	}

	/**
	 * 通过存档构造游戏界面
	 *
	 * @param savedGame
	 *            游戏存档
	 * @throws IOException
	 *             读档发生异常
	 * @see Game
	 */
	protected Interface(File savedGame) throws IOException {
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

		// 初始化game
		if (!gameInitialized) {
			game = new Game(style);
			saved = false;
		} else {
			saved = true;
		}

		// 初始化labels数组
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

		// 设置边框
		labels[0][0].setBorder(BorderFactory.createMatteBorder(
				borderWidthHalf * 2, borderWidthHalf * 2, borderWidthHalf,
				borderWidthHalf, borderColor));
		for (j = 1; j < game.colomns - 1; j++) {
			labels[0][j].setBorder(BorderFactory.createMatteBorder(
					borderWidthHalf * 2, borderWidthHalf, borderWidthHalf,
					borderWidthHalf, borderColor));
		}
		labels[0][game.colomns - 1].setBorder(BorderFactory.createMatteBorder(
				borderWidthHalf * 2, borderWidthHalf, borderWidthHalf,
				borderWidthHalf * 2, borderColor));
		for (i = 1; i < game.rows - 1; i++) {
			labels[i][0].setBorder(BorderFactory.createMatteBorder(
					borderWidthHalf, borderWidthHalf * 2, borderWidthHalf,
					borderWidthHalf, borderColor));
			for (j = 1; j < game.colomns - 1; j++) {
				labels[i][j].setBorder(BorderFactory.createMatteBorder(
						borderWidthHalf, borderWidthHalf, borderWidthHalf,
						borderWidthHalf, borderColor));
			}
			labels[i][game.colomns - 1].setBorder(BorderFactory
					.createMatteBorder(borderWidthHalf, borderWidthHalf,
							borderWidthHalf, borderWidthHalf * 2, borderColor));
		}
		labels[game.rows - 1][0].setBorder(BorderFactory.createMatteBorder(
				borderWidthHalf, borderWidthHalf * 2, borderWidthHalf * 2,
				borderWidthHalf, borderColor));
		for (j = 1; j < game.colomns - 1; j++) {
			labels[game.rows - 1][j].setBorder(BorderFactory.createMatteBorder(
					borderWidthHalf, borderWidthHalf, borderWidthHalf * 2,
					borderWidthHalf, borderColor));
		}
		labels[game.rows - 1][game.colomns - 1].setBorder(BorderFactory
				.createMatteBorder(borderWidthHalf, borderWidthHalf,
						borderWidthHalf * 2, borderWidthHalf * 2, borderColor));

		// 初始化scoreLabel
		scoreLabel = new JLabel();
		scoreLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// 初始化highScoreLabel
		highScoreLabel = new JLabel();
		highScoreLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
		highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		highScoreLabel.setForeground(Color.black);

		// 初始化saveButton
		saveButton = new JButton("Save");
		saveButton.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		saveButton.setFocusable(false);
		saveButton.setMnemonic('S');
		saveButton.addActionListener((ActionEvent e) -> {
			saved = saveGame();
		});

		// 初始化southLabel
		southLabel = new JLabel();
		southLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		southLabel.setHorizontalAlignment(SwingConstants.CENTER);
		if (game.getHaveWon()) {
			southLabel.setText("YOU HAVE WON!");
		} else {
			southLabel.setText(game.hint);
		}

		// 设置mainFrame布局
		mainFrame.setLayout(new BorderLayout(0, 5));
		mainFrame.add(northPanel, BorderLayout.NORTH);
		mainFrame.add(southLabel, BorderLayout.SOUTH);
		mainFrame.add(mainPanel, BorderLayout.CENTER);

		// 设置mainPanel布局
		mainPanel.setLayout(new GridLayout(game.rows, game.colomns));
		for (i = 0; i < game.rows; i++) {
			for (j = 0; j < game.colomns; j++) {
				mainPanel.add(labels[i][j]);
			}
		}

		// 设置northPanel布局
		northPanel.setLayout(new BorderLayout(0, 10));
		northPanel.add(scoreLabel, BorderLayout.NORTH);
		northPanel.add(highScoreLabel, BorderLayout.CENTER);
		northPanel.add(savePanel, BorderLayout.EAST);

		// 设置savePanel布局
		savePanel.setLayout(new BorderLayout(10, 0));
		savePanel.add(saveButton, BorderLayout.CENTER);
		savePanel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)), BorderLayout.EAST);

		// 设置图标
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("2048.png"));
		mainFrame.setIconImage(imageIcon.getImage());

		// 设置窗口关闭事件
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (saved) {
					exit();
					return;
				}
				int option = JOptionPane.showConfirmDialog(mainFrame,
						"Do you want to save your game?", "Exiting game",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
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
				labels[i][j]
						.setBackground(game.style[game.getTile(i, j)].bgColor);
				labels[i][j]
						.setForeground(game.style[game.getTile(i, j)].txColor);
				labels[i][j].setText(game.style[game.getTile(i, j)].label);
			}
		}
		scoreLabel.setText("SCORE: " + game.getScore());
		highScoreLabel.setText("High Score: " + highScore);
	}

	private boolean saveGame() {
		JFileChooser fChooser = new JFileChooser();
		fChooser.setCurrentDirectory(Main.defaultSavePath);
		fChooser.setFileFilter(new FileNameExtensionFilter(
				Main.savedGameDescription, Main.savedGameExtension));
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
			JOptionPane.showMessageDialog(mainFrame,
					"Saving failed!\n" + e.getMessage(), "Saving Failed",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void recordHighScore(long score) {
		long[] tmp = new long[Style.styleNum];
		try {
			@SuppressWarnings("resource")
			DataInputStream dataInput = new DataInputStream(
					new FileInputStream(highScoreRecord));
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
		} finally {

		}
		tmp[style] = score;
		try {
			DataOutputStream dataOutput = new DataOutputStream(
					new FileOutputStream(highScoreRecord));
			for (int i = 0; i < tmp.length; i++) {
				dataOutput.writeLong(tmp[i]);
			}
			dataOutput.close();
		} catch (IOException e) {
		}
	}

	private long loadHighScore() {
		long score;
		try {
			@SuppressWarnings("resource")
			DataInputStream dataInput = new DataInputStream(
					new FileInputStream(highScoreRecord));
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
