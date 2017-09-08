package game;

import java.awt.Color;

/**
 * 封装游戏中格子的样式
 *
 * @author 王力博
 */
public class TileStyle {

	protected String label;
	protected Color txColor;
	protected Color bgColor;
	protected int score;

	/**
	 * 构造指定类型的格子样式
	 *
	 * @param label
	 *            标签文字
	 * @param txcolor
	 *            前景色
	 * @param bgcolor
	 *            背景色
	 * @param score
	 *            对应分数
	 */
	public TileStyle(String label, Color txcolor, Color bgcolor, int score) {
		this.label = label;
		this.txColor = txcolor;
		this.bgColor = bgcolor;
		this.score = score;
	}
}
