package pojo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Block {

	// 木块的宽和高
	private int width;
	private int height;

	// 木块的坐标
	private int x;
	private int y;

	// 木块的活动范围
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	// 北京颜色和选中后的颜色
	private Color bgColor = Color.orange;
	private Color selectedColor = Color.red;

	// 木块的序号
	private int number;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getNumber() {
		return this.number;
	}

	/**
	 * 设置木块的活动区域
	 * 
	 * @param minX
	 *            横坐标的最小值
	 * @param minY
	 *            纵坐标的最小值
	 * @param maxX
	 *            横坐标的最大值
	 * @param maxY
	 *            纵坐标的最大值 void
	 * @author xinglei
	 */
	public void setActivedArea(int minX, int minY, int maxX, int maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.maxY = maxY;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}

	/**
	 * 
	 * @Description 构造木块必须提供序号参数
	 * 
	 * @author xinglei
	 * @param number
	 *            序号
	 */
	public Block(int number) {
		this.number = number;
	}

	/**
	 * 
	 * @Description 全构造函数
	 * @author xinglei
	 * @param number
	 *            序号
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public Block(int number, int x, int y, int width, int height) {
		this.number = number;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * 
	 * @Description 正常绘制木块
	 * @param g
	 *            void
	 * @author xinglei
	 */
	public void draw(Graphics g) {
		g.setColor(this.bgColor);
		g.fill3DRect(this.x, this.y, this.width, this.height, true);
		g.setColor(Color.white);
		g.setFont(new Font("黑体", Font.BOLD, 20));
		g.drawString(number + "", this.x + 5, this.y + 25);
	}

	/**
	 * 
	 * @Description 绘制选中的木块
	 * @param g
	 *            void
	 * @author xinglei
	 */
	public void drawSelected(Graphics g) {
		g.setColor(this.selectedColor);
		g.fill3DRect(this.x, this.y, this.width, this.height, true);
		g.setColor(Color.white);
		g.setFont(new Font("黑体", Font.BOLD, 20));
		g.drawString(number + "", this.x + 5, this.y + 25);
	}

	/**
	 * 判断某点是否在木块上，可用于判断鼠标是否点击了木块，或者进行碰撞检测
	 * 
	 * @param x
	 * @param y
	 * @return boolean 该点在木块上返回true；否则返回false
	 * @author xinglei
	 */
	public boolean isContain(int x, int y) {
		if (x < this.getX())
			return false;
		if (y < this.getY())
			return false;
		if (x > this.getX() + this.width)
			return false;
		if (y > this.getY() + this.height)
			return false;
		return true;
	}

	/**
	 * 检查是否越界，越界返回true，否则返回false <br>
	 * 使用该方法前请确保已经使用setActivedArea方法设置了边界
	 * 
	 * @param strict
	 *            是否严格越界<br>
	 *            true表示严格越界，即只要有一个顶点越界即算木块越界<br>
	 *            false表示不严格越界，即只要有一个顶点在界内，就不算越界
	 * @return boolean
	 * @author xinglei
	 */

	public boolean isOutOfBound(boolean strict) {
		if (strict)
			return strictOutOfBound(this.minX, this.minY, this.maxX, this.maxY);
		return notStrictOutOfBound(minX, minY, maxX, maxY);
	}

	/**
	 * 
	 * 检查是否越界，越界返回true，否则返回false
	 * 
	 * @param minX
	 *            横坐标的最小值
	 * @param minY
	 *            纵坐标的最小值
	 * @param maxX
	 *            横坐标的最大值
	 * @param maxY
	 *            纵坐标的最大值
	 * @param strict
	 *            是否严格越界<br>
	 *            true表示严格越界，即只要有一个顶点越界即算木块越界<br>
	 *            false表示不严格越界，即只要有一个顶点在界内，就不算越界
	 * @return boolean
	 * @author xinglei
	 */
	public boolean isOutOfBound(int minX, int minY, int maxX, int maxY,
			boolean strict) {
		if (strict)
			return strictOutOfBound(minX, minY, maxX, maxY);
		return notStrictOutOfBound(minX, minY, maxX, maxY);
	}

	/**
	 * 严格越界验证，任何一个顶点越界都将返回true
	 * 
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return boolean
	 * @author xinglei
	 */
	private boolean strictOutOfBound(int minX, int minY, int maxX, int maxY) {
		if (isOutOfBound(this.getX(), this.getY(), minX, minY, maxX, maxY))
			return true;

		if (isOutOfBound(this.getX() + this.getWidth(), this.getY(), minX,
				minY, maxX, maxY))
			return true;
		if (isOutOfBound(this.getX(), this.getY() + this.getHeight(), minX,
				minY, maxX, maxY))
			return true;
		if (isOutOfBound(this.getX() + this.getWidth(),
				this.getY() + this.getHeight(), minX, minY, maxX, maxY))
			return true;
		return false;
	}

	/**
	 * 不严格越界验证，任何一个顶点没越界都将返回false
	 * 
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return boolean
	 * @author xinglei
	 */
	private boolean notStrictOutOfBound(int minX, int minY, int maxX, int maxY) {
		if (!isOutOfBound(this.getX(), this.getY(), minX, minY, maxX, maxY))
			return false;
		if (!isOutOfBound(this.getX() + this.getWidth(), this.getY(), minX,
				minY, maxX, maxY))
			return false;
		if (!isOutOfBound(this.getX(), this.getY() + this.getHeight(), minX,
				minY, maxX, maxY))
			return false;
		if (!isOutOfBound(this.getX() + this.getWidth(),
				this.getY() + this.getHeight(), minX, minY, maxX, maxY))
			return false;
		return true;
	}

	/**
	 * 判断x,y是否在给定的区域范围内
	 * 
	 * @param x
	 * @param y
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return boolean
	 * @author xinglei
	 */
	private boolean isOutOfBound(int x, int y, int minX, int minY, int maxX,
			int maxY) {
		if (x < minX || x > maxX) {
			return true;
		}
		if (y < minY || y > maxY)
			return true;
		return false;
	}

}
