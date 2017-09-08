package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 主类，实现游戏的入口和模式选择
 *
 * @author 王力博
 */
public class Main {

	/**
	 * 默认的存档文件扩展名
	 */
	protected static final String savedGameExtension = "2048";

	/**
	 * JFileChooser中显示的存档文件类型说明
	 */
	protected static final String savedGameDescription = "2048 Saved Game";

	/**
	 * 默认存档路径
	 */
	protected static final File defaultSavePath = new File(
			System.getProperty("user.dir")
					+ System.getProperty("file.separator") + "Saved Games");

	private static JFrame starter;

	/**
	 * 程序入口
	 *
	 * @param args
	 *            输入参数
	 */
	public static void main(String args[]) {

		// 若系统为Windows，设置为Windows质感
		try {
			if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
		}

		// 若默认存档文件夹不存在，生成默认存档文件夹
		if (!defaultSavePath.exists()) {
			defaultSavePath.mkdir();
		}

		// 组件初始化
		starter = new JFrame("选择游戏模式");
		JLabel upLabel = new JLabel("2048");
		JLabel downLabel = new JLabel("作者：王力博");
		JButton button1 = new JButton("经典模式");
		JButton button2 = new JButton("中国朝代");
		JButton button3 = new JButton("5×5模式");
		JButton button4 = new JButton("读取存档");

		// 设置starter背景色
		starter.getContentPane().setBackground(Color.lightGray);
		starter.getContentPane().setVisible(true);

		// 设置upLabel, downLabel
		upLabel.setHorizontalAlignment(SwingConstants.CENTER);
		downLabel.setHorizontalAlignment(SwingConstants.CENTER);
		upLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		downLabel.setVerticalAlignment(SwingConstants.TOP);
		upLabel.setForeground(Color.black);
		downLabel.setForeground(Color.darkGray);
		upLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 48));
		downLabel.setFont(new Font("黑体", Font.BOLD, 28));

		// 设置buttons
		button1.setFont(new Font("楷体", Font.BOLD, 28));
		button2.setFont(new Font("楷体", Font.BOLD, 28));
		button3.setFont(new Font("楷体", Font.BOLD, 28));
		button4.setFont(new Font("楷体", Font.BOLD, 28));
		button1.setFocusable(false);
		button2.setFocusable(false);
		button3.setFocusable(false);
		button4.setFocusable(false);

		// 设置starter布局
		starter.setLayout(new GridLayout(6, 3, -200, 30));
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(upLabel);
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(button1);
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(button2);
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(button3);
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(button4);
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));
		starter.add(downLabel);
		starter.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, 0)));

		starter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		starter.setBounds(400, 50, 400, 600);
		starter.setMinimumSize(new Dimension(400, 600));
		starter.setVisible(true);

		// 设置按钮事件监听器
		button1.addActionListener((ActionEvent e) -> {
			new Interface(Game.DEFAULT_STYLE);
			exit();
		});
		button2.addActionListener((ActionEvent e) -> {
			new Interface(Game.CHINESE_DYNASTY);
			exit();
		});
		button3.addActionListener((ActionEvent e) -> {
			new Interface(Game.FIVE_FIVE);
			exit();
		});
		button4.addActionListener((ActionEvent e) -> {
			try {
				new Interface(loadGame());
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(starter,
						"读档失败！\n" + ex.getMessage(), "错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			} catch (NullPointerException ex) {
				return;
			}
			exit();
		});
	}

	private static File loadGame() {
		JFileChooser fChooser = new JFileChooser();
		fChooser.setCurrentDirectory(defaultSavePath);
		fChooser.setFileFilter(new FileNameExtensionFilter(
				savedGameDescription, savedGameExtension));
		int ret = fChooser.showOpenDialog(starter);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return fChooser.getSelectedFile();
	}

	private static void exit() {
		starter.dispose();
		System.gc();
	}
}
