package game;

import java.awt.*;
import java.io.*;

class Game {
    static final int MOVE_UP = 1;
    static final int MOVE_DOWN = 2;
    static final int MOVE_LEFT = 3;
    static final int MOVE_RIGHT = 4;
    static final int NO_MOVEMENT = -1;
    static final int NORMAL_STEP = 0;
    static final int GAME_OVER = 1;
    static final int PLAYER_WON = 2;
    static final int DEFAULT_STYLE = 0;
    static final int CHINESE_DYNASTY = 1;
    static final int FIVE_FIVE = 2;
    private static final double probability = 0.1;
    final int colomns;
    final int rows;
    final TileStyle[] style;
    final Font font;
    final String hint;
    private final int winNum;
    private final int styleType;
    private final int tile[][];
    private long score;
    private boolean haveWon;

    Game(int gameStyle) {
        switch (gameStyle) {
            case CHINESE_DYNASTY:
                rows = 4;
                colomns = 4;
                winNum = 11;
                styleType = CHINESE_DYNASTY;
                font = Style.chineseFont;
                style = Style.dynastyStyle;
                hint = "Try to establish communism!";
                break;
            case FIVE_FIVE:
                rows = 5;
                colomns = 5;
                winNum = 14;
                styleType = FIVE_FIVE;
                font = Style.smallFont;
                style = Style.fiveStyle;
                hint = "Try to reach 16384!";
                break;
            default:
                rows = 4;
                colomns = 4;
                winNum = 11;
                styleType = DEFAULT_STYLE;
                font = Style.defaultFont;
                style = Style.defaultStyle;
                hint = "Try to reach 2048!";
        }

        tile = new int[rows][colomns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colomns; j++) {
                tile[i][j] = 0;
            }
        }

        generateTile();
        generateTile();
    }

    Game(File file) throws IOException {
        DataInputStream dataInput = new DataInputStream(new FileInputStream(file));
        int tmp = dataInput.readInt();
        switch (tmp) {
            case CHINESE_DYNASTY:
                rows = 4;
                colomns = 4;
                winNum = 11;
                styleType = CHINESE_DYNASTY;
                font = Style.chineseFont;
                style = Style.dynastyStyle;
                hint = "Try to establish communism!";
                break;
            case FIVE_FIVE:
                rows = 5;
                colomns = 5;
                winNum = 14;
                styleType = FIVE_FIVE;
                font = Style.smallFont;
                style = Style.fiveStyle;
                hint = "Try to reach 16384!";
                break;
            default:
                rows = 4;
                colomns = 4;
                winNum = 11;
                styleType = DEFAULT_STYLE;
                font = Style.defaultFont;
                style = Style.defaultStyle;
                hint = "Try to reach 2048!";
        }

        tile = new int[rows][colomns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colomns; j++) {
                tmp = dataInput.readInt();
                if (tmp < 0 || tmp > rows * colomns + 1) {
                    dataInput.close();
                    throw new IOException("Input logical error");
                }
                tile[i][j] = tmp;
            }
        }

        long tmpl = dataInput.readLong();
        if (tmpl < 0L) {
            dataInput.close();
            throw new IOException("Input logical error");
        }
        score = tmpl;

        tmp = dataInput.readInt();
        switch (tmp) {
            case 0:
                haveWon = false;
                break;
            case 1:
                haveWon = true;
                break;
            default:
                dataInput.close();
                throw new IOException("Input logical error");
        }
        dataInput.close();
    }

    void save(String path) throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(path));
        dataOutput.writeInt(styleType);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colomns; j++) {
                dataOutput.writeInt(tile[i][j]);
            }
        }
        dataOutput.writeLong(score);
        dataOutput.writeInt(haveWon ? 1 : 0);
        dataOutput.close();
    }

    int move(int direction) {
        boolean isMoved = false;
        switch (direction) {
            case MOVE_UP:
                isMoved = moveUp();
                break;
            case MOVE_DOWN:
                isMoved = moveDown();
                break;
            case MOVE_LEFT:
                isMoved = moveLeft();
                break;
            case MOVE_RIGHT:
                isMoved = moveRight();
        }
        if (!isMoved) {
            return NO_MOVEMENT;
        }
        generateTile();
        if (isGameOver()) {
            return GAME_OVER;
        }
        if (!haveWon) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < rows; j++) {
                    if (tile[i][j] == winNum) {
                        haveWon = true;
                        return PLAYER_WON;
                    }
                }
            }
        }
        return NORMAL_STEP;
    }

    private boolean isGameOver() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colomns; j++) {
                if (tile[i][j] == 0) {
                    return false;
                }
            }
        }
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < colomns - 1; j++) {
                if (tile[i][j] == tile[i][j + 1]
                        || tile[i][j] == tile[i + 1][j]) {
                    return false;
                }
            }
        }
        for (int j = 0; j < colomns - 1; j++) {
            if (tile[rows - 1][j] == tile[rows - 1][j + 1]) {
                return false;
            }
        }
        for (int i = 0; i < rows - 1; i++) {
            if (tile[i][colomns - 1] == tile[i + 1][colomns - 1]) {
                return false;
            }
        }
        return true;
    }

    private void generateTile() {
        EmptyTileList list = new EmptyTileList();
        list.n = 0;
        for (int i = 0; i < colomns; i++) {
            for (int j = 0; j < rows; j++) {
                if (tile[i][j] == 0) {
                    list.iList[list.n] = i;
                    list.jList[list.n] = j;
                    list.n++;
                }
            }
        }
        int position = (int) Math.floor(Math.random() * list.n);
        if (Math.random() <= probability) {
            tile[list.iList[position]][list.jList[position]] = 2;
        } else {
            tile[list.iList[position]][list.jList[position]] = 1;
        }
    }

    private boolean moveUp() {
        boolean isMoved = false;
        for (int j = 0; j < colomns; j++) {
            int k = 0;
            for (int i = 0, l = 0; k < colomns; k++, i++) {
                while (i < rows && tile[i][j] == 0) {
                    i++;
                }
                if (i > rows - 1) {
                    break;
                }
                if (i > k) {
                    tile[k][j] = tile[i][j];
                    isMoved = true;
                }
                if (k > l && tile[k][j] == tile[k - 1][j]) {
                    score += style[++tile[k - 1][j]].score;
                    l = k--;
                    isMoved = true;
                }
            }
            for (; k < rows; k++) {
                tile[k][j] = 0;
            }
        }
        return isMoved;
    }

    private boolean moveDown() {
        boolean isMoved = false;
        for (int j = 0; j < colomns; j++) {
            int k = rows - 1;
            for (int i = rows - 1, l = rows - 1; k > -1; k--, i--) {
                while (i > -1 && tile[i][j] == 0) {
                    i--;
                }
                if (i < 0) {
                    break;
                }
                if (i < k) {
                    tile[k][j] = tile[i][j];
                    isMoved = true;
                }
                if (k < l && tile[k][j] == tile[k + 1][j]) {
                    score += style[++tile[k + 1][j]].score;
                    l = k++;
                    isMoved = true;
                }
            }
            for (; k > -1; k--) {
                tile[k][j] = 0;
            }
        }
        return isMoved;
    }

    private boolean moveLeft() {
        boolean isMoved = false;
        for (int i = 0; i < rows; i++) {
            int k = 0;
            for (int j = 0, l = 0; k < colomns; k++, j++) {
                while (j < colomns && tile[i][j] == 0) {
                    j++;
                }
                if (j > colomns - 1) {
                    break;
                }
                if (j > k) {
                    tile[i][k] = tile[i][j];
                    isMoved = true;
                }
                if (k > l && tile[i][k] == tile[i][k - 1]) {
                    score += style[++tile[i][k - 1]].score;
                    l = k--;
                    isMoved = true;
                }
            }
            for (; k < colomns; k++) {
                tile[i][k] = 0;
            }
        }
        return isMoved;
    }

    private boolean moveRight() {
        boolean isMoved = false;
        for (int i = 0; i < rows; i++) {
            int k = colomns - 1;
            for (int j = colomns - 1, l = colomns - 1; k > -1; k--, j--) {
                while (j > -1 && tile[i][j] == 0) {
                    j--;
                }
                if (j < 0) {
                    break;
                }
                if (j < k) {
                    tile[i][k] = tile[i][j];
                    isMoved = true;
                }
                if (k < l && tile[i][k] == tile[i][k + 1]) {
                    score += style[++tile[i][k + 1]].score;
                    l = k++;
                    isMoved = true;
                }
            }
            for (; k > -1; k--) {
                tile[i][k] = 0;
            }
        }
        return isMoved;
    }

    long getScore() {
        return score;
    }

    int getTile(int row, int colomn) {
        return tile[row][colomn];
    }

    int getStyle() {
        return styleType;
    }

    boolean getHaveWon() {
        return haveWon;
    }

    class EmptyTileList {

        int n;
        int iList[], jList[];

        private EmptyTileList() {
            this.iList = new int[colomns * rows];
            this.jList = new int[colomns * rows];
        }
    }
}