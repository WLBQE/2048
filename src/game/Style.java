package game;

import java.awt.Color;
import java.awt.Font;

/**
 * 封装了游戏中用到的一些字体和格子样式
 *
 * @author 王力博
 */
public final class Style {

	/**
	 * 游戏实现的模式数目
	 */
	protected static final int styleNum = 3;

	/**
	 * 默认字体
	 */
	protected static final Font defaultFont = new Font(Font.DIALOG, Font.PLAIN,
			24);

	/**
	 * 中文字体
	 */
	protected static final Font chineseFont = new Font("宋体", Font.BOLD, 24);

	/**
	 * 小字体
	 */
	protected static final Font smallFont = new Font(Font.DIALOG, Font.PLAIN,
			20);

	/**
	 * 默认格子样式
	 */
	protected static final TileStyle defaultStyle[] = {
			new TileStyle("", Color.black, Color.white, 0),
			new TileStyle("2", Color.black, Color.white, 2),
			new TileStyle("4", Color.black, Color.white, 4),
			new TileStyle("8", Color.black, Color.white, 8),
			new TileStyle("16", Color.blue, Color.white, 16),
			new TileStyle("32", Color.blue, Color.white, 32),
			new TileStyle("64", Color.blue, Color.white, 64),
			new TileStyle("128", Color.blue, Color.white, 128),
			new TileStyle("256", Color.magenta, Color.yellow, 256),
			new TileStyle("512", Color.magenta, Color.yellow, 512),
			new TileStyle("1024", Color.magenta, Color.yellow, 1024),
			new TileStyle("2048", Color.red, Color.green, 2048),
			new TileStyle("4096", Color.red, Color.green, 4096),
			new TileStyle("8192", Color.white, Color.black, 8192),
			new TileStyle("16384", Color.white, Color.black, 16384),
			new TileStyle("32768", Color.white, Color.black, 32768),
			new TileStyle("65536", Color.white, Color.black, 65536),
			new TileStyle("131072", Color.white, Color.black, 131072), };

	/**
	 * 中国朝代格子样式
	 */
	protected static final TileStyle dynastyStyle[] = {
			new TileStyle("", Color.black, Color.white, 0),
			new TileStyle("夏", Color.black, Color.white, 2),
			new TileStyle("商", Color.black, Color.white, 4),
			new TileStyle("周", Color.black, Color.white, 8),
			new TileStyle("汉", Color.blue, Color.white, 16),
			new TileStyle("唐", Color.blue, Color.white, 32),
			new TileStyle("宋", Color.blue, Color.white, 64),
			new TileStyle("元", Color.blue, Color.white, 128),
			new TileStyle("明", Color.magenta, Color.yellow, 256),
			new TileStyle("清", Color.magenta, Color.yellow, 512),
			new TileStyle("民国", Color.white, Color.blue, 1024),
			new TileStyle("天朝", Color.yellow, Color.red, 2048),
			new TileStyle("民主国", Color.black, Color.green, 4096),
			new TileStyle("自由邦", Color.black, Color.green, 8192),
			new TileStyle("米利坚", Color.red, Color.blue, 16384),
			new TileStyle("理想国", Color.white, Color.black, 32768),
			new TileStyle("永生国", Color.white, Color.black, 65536),
			new TileStyle("不灭国", Color.white, Color.black, 131072), };

	/**
	 * 5×5模式格子样式
	 */
	protected static final TileStyle fiveStyle[] = {
			new TileStyle("", Color.black, Color.white, 0),
			new TileStyle("2", Color.black, Color.white, 2),
			new TileStyle("4", Color.black, Color.white, 4),
			new TileStyle("8", Color.black, Color.white, 8),
			new TileStyle("16", Color.black, Color.white, 16),
			new TileStyle("32", Color.blue, Color.white, 32),
			new TileStyle("64", Color.blue, Color.white, 64),
			new TileStyle("128", Color.blue, Color.white, 128),
			new TileStyle("256", Color.blue, Color.white, 256),
			new TileStyle("512", Color.blue, Color.white, 512),
			new TileStyle("1024", Color.magenta, Color.yellow, 1024),
			new TileStyle("2048", Color.magenta, Color.yellow, 2048),
			new TileStyle("4096", Color.magenta, Color.yellow, 4096),
			new TileStyle("8192", Color.magenta, Color.yellow, 8192),
			new TileStyle("16384", Color.red, Color.green, 16384),
			new TileStyle("32768", Color.red, Color.green, 32768),
			new TileStyle("65536", Color.white, Color.black, 65536),
			new TileStyle("131072", Color.white, Color.black, 131072),
			new TileStyle("262144", Color.white, Color.black, 262144),
			new TileStyle("524288", Color.white, Color.black, 524288),
			new TileStyle("1.05M", Color.white, Color.black, 1048576),
			new TileStyle("2.10M", Color.white, Color.black, 2097152),
			new TileStyle("4.19M", Color.white, Color.black, 4194304),
			new TileStyle("8.39M", Color.white, Color.black, 8388608),
			new TileStyle("16.8M", Color.white, Color.black, 16777216),
			new TileStyle("33.6M", Color.white, Color.black, 33554432),
			new TileStyle("67.1M", Color.white, Color.black, 67108864), };
}
