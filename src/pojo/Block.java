package pojo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Block {

	// ľ��Ŀ�͸�
	private int width;
	private int height;

	// ľ�������
	private int x;
	private int y;

	// ľ��Ļ��Χ
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	// ������ɫ��ѡ�к����ɫ
	private Color bgColor = Color.orange;
	private Color selectedColor = Color.red;

	// ľ������
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
	 * ����ľ��Ļ����
	 * 
	 * @param minX
	 *            ���������Сֵ
	 * @param minY
	 *            ���������Сֵ
	 * @param maxX
	 *            ����������ֵ
	 * @param maxY
	 *            ����������ֵ void
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
	 * @Description ����ľ������ṩ��Ų���
	 * 
	 * @author xinglei
	 * @param number
	 *            ���
	 */
	public Block(int number) {
		this.number = number;
	}

	/**
	 * 
	 * @Description ȫ���캯��
	 * @author xinglei
	 * @param number
	 *            ���
	 * @param x
	 *            ������
	 * @param y
	 *            ������
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
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
	 * @Description ��������ľ��
	 * @param g
	 *            void
	 * @author xinglei
	 */
	public void draw(Graphics g) {
		g.setColor(this.bgColor);
		g.fill3DRect(this.x, this.y, this.width, this.height, true);
		g.setColor(Color.white);
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString(number + "", this.x + 5, this.y + 25);
	}

	/**
	 * 
	 * @Description ����ѡ�е�ľ��
	 * @param g
	 *            void
	 * @author xinglei
	 */
	public void drawSelected(Graphics g) {
		g.setColor(this.selectedColor);
		g.fill3DRect(this.x, this.y, this.width, this.height, true);
		g.setColor(Color.white);
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString(number + "", this.x + 5, this.y + 25);
	}

	/**
	 * �ж�ĳ���Ƿ���ľ���ϣ��������ж�����Ƿ�����ľ�飬���߽�����ײ���
	 * 
	 * @param x
	 * @param y
	 * @return boolean �õ���ľ���Ϸ���true�����򷵻�false
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
	 * ����Ƿ�Խ�磬Խ�緵��true�����򷵻�false <br>
	 * ʹ�ø÷���ǰ��ȷ���Ѿ�ʹ��setActivedArea���������˱߽�
	 * 
	 * @param strict
	 *            �Ƿ��ϸ�Խ��<br>
	 *            true��ʾ�ϸ�Խ�磬��ֻҪ��һ������Խ�缴��ľ��Խ��<br>
	 *            false��ʾ���ϸ�Խ�磬��ֻҪ��һ�������ڽ��ڣ��Ͳ���Խ��
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
	 * ����Ƿ�Խ�磬Խ�緵��true�����򷵻�false
	 * 
	 * @param minX
	 *            ���������Сֵ
	 * @param minY
	 *            ���������Сֵ
	 * @param maxX
	 *            ����������ֵ
	 * @param maxY
	 *            ����������ֵ
	 * @param strict
	 *            �Ƿ��ϸ�Խ��<br>
	 *            true��ʾ�ϸ�Խ�磬��ֻҪ��һ������Խ�缴��ľ��Խ��<br>
	 *            false��ʾ���ϸ�Խ�磬��ֻҪ��һ�������ڽ��ڣ��Ͳ���Խ��
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
	 * �ϸ�Խ����֤���κ�һ������Խ�綼������true
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
	 * ���ϸ�Խ����֤���κ�һ������ûԽ�綼������false
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
	 * �ж�x,y�Ƿ��ڸ���������Χ��
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
