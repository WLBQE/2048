package game;

import java.awt.*;

class TileStyle {

    String label;
    Color txColor;
    Color bgColor;
    int score;

    TileStyle(String label, Color txcolor, Color bgcolor, int score) {
        this.label = label;
        this.txColor = txcolor;
        this.bgColor = bgcolor;
        this.score = score;
    }
}
