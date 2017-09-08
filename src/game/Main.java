package game;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class Main {

    static final String savedGameExtension = "2048";
    static final String savedGameDescription = "2048 Saved Game";
    static final File defaultSavePath = new File(System.getProperty("user.dir")
            + System.getProperty("file.separator") + "Saved Games");

    private static JFrame starter;

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (!defaultSavePath.exists() && !defaultSavePath.mkdir()) {
            JOptionPane.showMessageDialog(starter, "存档目录初始化失败！\n", "错误",
                    JOptionPane.ERROR_MESSAGE);
            exit();
        }

        starter = new JFrame("选择游戏模式");
        JLabel upLabel = new JLabel("2048");
        JLabel downLabel = new JLabel("作者：王力博");
        JButton button1 = new JButton("经典模式");
        JButton button2 = new JButton("中国朝代");
        JButton button3 = new JButton("5×5模式");
        JButton button4 = new JButton("读取存档");

        starter.getContentPane().setBackground(Color.lightGray);
        starter.getContentPane().setVisible(true);

        upLabel.setHorizontalAlignment(SwingConstants.CENTER);
        downLabel.setHorizontalAlignment(SwingConstants.CENTER);
        upLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        downLabel.setVerticalAlignment(SwingConstants.TOP);
        upLabel.setForeground(Color.black);
        downLabel.setForeground(Color.darkGray);
        upLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 48));
        downLabel.setFont(new Font("黑体", Font.BOLD, 28));

        button1.setFont(new Font("楷体", Font.BOLD, 28));
        button2.setFont(new Font("楷体", Font.BOLD, 28));
        button3.setFont(new Font("楷体", Font.BOLD, 28));
        button4.setFont(new Font("楷体", Font.BOLD, 28));
        button1.setFocusable(false);
        button2.setFocusable(false);
        button3.setFocusable(false);
        button4.setFocusable(false);

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

        starter.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        starter.setBounds(400, 50, 400, 600);
        starter.setMinimumSize(new Dimension(400, 600));
        starter.setVisible(true);

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
                JOptionPane.showMessageDialog(starter, "读档失败！\n" + ex.getMessage(), "错误",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                return;
            }
            exit();
        });
    }

    private static File loadGame() {
        JFileChooser fChooser = new JFileChooser();
        fChooser.setCurrentDirectory(defaultSavePath);
        fChooser.setFileFilter(new FileNameExtensionFilter(savedGameDescription, savedGameExtension));
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
