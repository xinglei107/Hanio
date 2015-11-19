package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pojo.Block;
import pojo.MoveInfo;
import aglor.Hanoi;

public class MyPanel extends JPanel implements ComponentListener,
		ActionListener, MouseListener, MouseMotionListener, Runnable {

	// ��ǰ��ľ���б�
	private ArrayList<Block> blockList = new ArrayList<Block>();

	// ������ʱ��ľ������������λ�õĲ�ֵ
	private Point drag = new Point();

	// ��¼��ǰľ���λ�ã����ľ����ΪԽ����Ҫ���أ���ʹ�ø�λ��
	private Point oldPos = null;

	private boolean clickable = true;

	// ��ǰѡ�е�ľ��
	private Block curBlock;

	private boolean autoPlay = false;

	private int totalStep = 0;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author xinglei
	 */
	public MyPanel(int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.setSize(width, height);
		this.setBorder(BorderFactory.createEtchedBorder());
		this.addComponentListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * ���ָ��������ľ��
	 * 
	 * @param count
	 *            void
	 * @author xinglei
	 */
	private void addBlock(int count) {
		for (int i = 1; i <= count; ++i) {
			Block b = new Block(i);
			b.setActivedArea(0, 0, this.getWidth(), this.getHeight());
			blockList.add(b);
		}
		autoAdjustBlock(0);
		repaint();
	}

	/**
	 * ���ľ��
	 * 
	 * void
	 * 
	 * @author xinglei
	 */
	private void clearBlock() {
		blockList.clear();
		repaint();
	}

	/**
	 * ����
	 * 
	 * @param count
	 *            void
	 * @author xinglei
	 */
	public void reset(int count) {
		this.clearBlock();
		this.addBlock(count);
		autoPlay = false;
	}

	public void start() {
		autoPlay = true;
	}

	/**
	 * �Զ�����ָ��������ľ��Ŀ�͸�
	 * 
	 * @author xinglei
	 */
	public void autoAdjustBlock(int areaIndex) {
		if (blockList.size() <= 0)
			return;
		int blockNumber = blockList.size();
		// ����ľ�����Ե�ľ���
		int widthSpace = 20;
		// �����һ��Ϊ3
		int maxWidth = (this.getWidth() - widthSpace * 6) / 3;
		// ���߶�ƽ��Ϊx*5�ݣ�x��ʾľ���������5����ȡ������ȡ���е�x�ݡ�
		// ����ľ����6������x=2����ʾ���߶�ƽ��Ϊ10�ݣ�Ȼ��ȡ6��
		int maxHeight = (int) (this.getHeight()
				* (blockNumber / ((blockNumber / 5 + 1) * 5.0)) / blockNumber);
		// �����������ڵ�ľ��֮ǰ�Ŀ�Ȳ�ֵ
		int widthSplit = (maxWidth) / blockNumber;
		// �Դ˼���ÿ��ľ���λ�á���С
		for (Block b : blockList) {
			b.setWidth(maxWidth - (blockNumber - b.getNumber()) * widthSplit);
			b.setHeight(maxHeight);
			b.setX((blockNumber - b.getNumber()) * widthSplit / 2 + widthSpace
					+ this.getWidth() / 3 * areaIndex);
			b.setY(this.getHeight() - (blockNumber - b.getNumber() + 1)
					* maxHeight);
		}
	}

	/**
	 * ����ľ������򣬽�ľ�����Ϊ���λ�ã��������������������򷵻���һ�κϷ���λ��
	 * 
	 * @param b
	 * @param areaIndex
	 * @return Point
	 * @author xinglei
	 */
	private Point adjustBlockPos(Block b, int areaIndex) {

		int blockCount = blockList.size();
		// ����ľ�����Ե�ľ���
		int widthSpace = 20;
		// �����һ��Ϊ3
		int maxWidth = (this.getWidth() - widthSpace * 6) / 3;
		// ���߶�ƽ��Ϊx*5�ݣ�x��ʾľ���������5����ȡ������ȡ���е�x�ݡ�
		// ����ľ����6������x=2����ʾ���߶�ƽ��Ϊ10�ݣ�Ȼ��ȡ6��
		int maxHeight = (int) (this.getHeight()
				* (blockCount / ((blockCount / 5 + 1) * 5.0)) / blockCount);
		// �����������ڵ�ľ��֮ǰ�Ŀ�Ȳ�ֵ
		int widthSplit = (maxWidth) / blockCount;
		int x = (blockCount - b.getNumber()) * widthSplit / 2 + widthSpace
				+ this.getWidth() / 3 * areaIndex;

		// ��¼��b�Ŀ�ȴ��ľ�����
		int countWidthThanB = 0;
		// ��¼��ȴ���b��ľ���п�ȵ���Сֵ�������ж�b�Ƿ���÷��õ�������
		int areaMinWidth = 1000;
		for (Block block : blockList) {
			if (areaIndex == transLoc(block.getX())) {
				if (block.getWidth() > b.getWidth()) {
					countWidthThanB++;
				}
				if (areaMinWidth > block.getWidth()) {
					areaMinWidth = block.getWidth();
				}
			}
		}
		// ��������ľ���bС�����ܷ��ã�������һ��������λ��
		if (isExceedMinWidth(b.getWidth(), areaMinWidth))
			return oldPos;

		int y = this.getHeight() - (countWidthThanB + 1) * maxHeight;

		if (transLoc(x) != transLoc(oldPos.x)) {
			totalStep++;
			System.out.println(totalStep);
		}
		return new Point(x, y);

	}

	/**
	 * �ж�ָ���������Ƿ�Խ��
	 * 
	 * @param areaIndex
	 * @return boolean
	 * @author xinglei
	 */
	private boolean isOutOfBound(int areaIndex) {
		if (areaIndex < 0 || areaIndex > 2)
			return true;
		return false;
	}

	/**
	 * ɨ���������򣬶�ÿ�������ľ���������Сľ���ȷֱ�洢
	 * 
	 * @param eachAreaBlockCount
	 *            �洢ÿ�������ڵ�ľ������
	 * @param eachAreaBlockMinWidth
	 *            �洢ÿ�������ڵ�ľ����С��ȣ���ʼֵ��С��ľ���ȵ����ֵ���˴�ȡ1000 void
	 * @author xinglei
	 */
	private void scanArea(int[] eachAreaBlockCount, int[] eachAreaBlockMinWidth) {

		for (Block b : blockList) {
			// ��ȡľ��b���ڵ�����
			int bAreaIndex = transLoc(b.getX());

			// ����Ӧ�����ľ��������1
			eachAreaBlockCount[bAreaIndex]++;
			// ��¼��Ӧ����ľ�����С���
			if (eachAreaBlockMinWidth[bAreaIndex] > b.getWidth()) {
				eachAreaBlockMinWidth[bAreaIndex] = b.getWidth();
			}
		}
	}

	/**
	 * 
	 * ���㽫ľ��curBlock�ƶ���ָ�����������λ�ã�����������ƶ��������򷵻�ԭλ��
	 * 
	 * @param curBlock
	 * @param areaIndex
	 *            void
	 * @author xinglei
	 */
	private Point getBlockPosByAreaIndex(Block curBlock, int areaIndex) {

		Point futurePos = new Point(curBlock.getX(), curBlock.getY());

		// ���Խ�磬����ԭλ��
		if (isOutOfBound(areaIndex)) {
			return futurePos;
		}

		int width = this.getWidth() / 3;

		// ��¼ÿ��������ľ�����Ŀ�����ڼ�����ľ���������
		int[] areaBlockCount = new int[] { 0, 0, 0 };
		// ��¼ÿ����������Сľ��Ŀ�ȣ������ж���ľ���Ƿ�ɷ���
		int[] areaBlockMinWidth = new int[] { 1000, 1000, 1000 };

		// ɨ���������򣬲��������¼����
		scanArea(areaBlockCount, areaBlockMinWidth);

		// �жϵ�ǰľ��Ŀ���Ƿ���ڶ�Ӧ�������С���
		// ����ǣ���ָ�ԭλ��
		// ����ɷ��ã�������µ����λ��
		if (isExceedMinWidth(curBlock.getWidth(), areaBlockMinWidth[areaIndex])) {
			System.out.println("���ܽ����ľ�������Сľ������");
		} else {
			int oldPosAreaIndex = transLoc(curBlock.getX());

			futurePos = new Point((areaIndex - oldPosAreaIndex) * width
					+ curBlock.getX(), this.getHeight()
					- (areaBlockCount[areaIndex] + 1) * curBlock.getHeight());
			// ������Ƿ���ԭλ�ã����¼����һ��
			if (transLoc(curBlock.getX()) != areaIndex) {
				totalStep++;
				System.out.println(totalStep);
			}
		}
		return futurePos;

	}

	/**
	 * ������ǰľ��Ŀ�Ⱥ��������е���С��ȣ��ж��Ƿ񳬹���С���
	 * 
	 * @param curWidth
	 * @param minWidth
	 * @return boolean
	 * @author xinglei
	 */
	private boolean isExceedMinWidth(int curWidth, int minWidth) {
		if (curWidth > minWidth)
			return true;
		return false;
	}

	/**
	 * 
	 * ��ľ���ƶ���ָ��λ�����ڵ����䣬���Զ����������λ��
	 * 
	 * @param block
	 * @param x
	 *            void
	 * @author xinglei
	 */
	public Point moveBlockToLoc(Block block, int x) {
		return moveBlockToArea(block, transLoc(x));
	}

	/**
	 * ��ľ���ƶ���ָ�������䣬���Զ��������λ��
	 * 
	 * @param block
	 * @param areaIndex
	 *            void
	 * @author xinglei
	 */
	public Point moveBlockToArea(Block block, int areaIndex) {
		return getBlockPosByAreaIndex(block, areaIndex);
	}

	/**
	 * ���ݺ������ж������ĸ����䣬0��1��2����������
	 * 
	 * @param x
	 * @return int
	 * @author xinglei
	 */
	private int transLoc(int x) {
		int width = this.getWidth() / 3;

		if (x < 0) {
			return -1;
		}
		return x / width;
	}

	/**
	 * ����������ʱ���¼���ѡ�е�ǰ�����ľ�飬��¼��ԭλ�ã���¼���ָ����ԭλ�õĲ�ֵ
	 * 
	 * @param mx
	 *            ��갴��ʱ�ĺ�����
	 * @param my
	 *            ����ʱ�������� void
	 * @author xinglei
	 */
	private void mouseLeftPressed(int mx, int my) {
		for (Block b : blockList) {
			if (b.isContain(mx, my)) {
				// �������Ĳ��ǵ�һ��ľ�飬��ѡ��
				if (b.getNumber() != getTopOneByAreaIndex(transLoc(mx)))
					continue;
				curBlock = b;
				// ��¼�������λ����ľ������Ĳ�ֵ
				drag.x = b.getX() - mx;
				drag.y = b.getY() - my;

				// ����ʱ��¼��ǰ��λ�ã����ڻָ�
				if (oldPos == null) {
					oldPos = new Point();
					oldPos.x = b.getX();
					oldPos.y = b.getY();
				}
				break;
			}
		}
	}

	/**
	 * ��ȡָ�������ڵ�һ��ľ��ı��
	 * 
	 * @param areaIndex
	 * @return int
	 * @author xinglei
	 */
	private int getTopOneByAreaIndex(int areaIndex) {
		int minNum = 1000;
		for (Block b : blockList) {
			if (transLoc(b.getX()) == areaIndex) {
				if (minNum > b.getNumber()) {
					minNum = b.getNumber();
				}
			}
		}
		return minNum;
	}

	/**
	 * ����Ҽ����ʱ���¼���ȡ��ѡ�е�ǰѡ�е�ľ��
	 * 
	 * void
	 * 
	 * @author xinglei
	 */
	private void mouseRightClicked() {
		if (curBlock != null) {
			curBlock = null;
		}
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		for (Block b : blockList) {
			b.draw(g);
		}
		if (curBlock != null) {
			curBlock.drawSelected(g);
		}

		g.setColor(Color.white);
		int x1 = this.getWidth() / 3;
		g.drawLine(x1, 0, x1, this.getHeight());
		g.drawLine(x1 * 2, 0, x1 * 2, this.getHeight());
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (blockList.size() > 0) {
			autoAdjustBlock(transLoc(blockList.get(0).getX()));
			for (Block b : blockList) {
				b.setActivedArea(0, 0, this.getWidth(), this.getHeight());
			}
		}
		repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (autoPlay)
			return;
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeftPressed(e.getX(), e.getY());
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

		if (autoPlay)
			return;
		// BUTTON1 ��� 2�м� 3�Ҽ�
		if (e.getButton() == MouseEvent.BUTTON1) {
			// ����ɿ����ʱľ��Խ�磬��ָ���һ�ε�λ��
			if (curBlock != null && curBlock.isOutOfBound(true)) {
				curBlock.setX(oldPos.x);
				curBlock.setY(oldPos.y);
			}

			if (curBlock != null) {
				Point p = adjustBlockPos(curBlock, transLoc(curBlock.getX()));
				curBlock.setX(p.x);
				curBlock.setY(p.y);
			}

			curBlock = null;
			oldPos = null;

		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (autoPlay)
			return;
		if (curBlock != null) {
			curBlock.setX(e.getX() + drag.x);
			curBlock.setY(e.getY() + drag.y);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			while (!autoPlay) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Hanoi h = new Hanoi();
			ArrayList<MoveInfo> moveInfoList = h.hanoi(blockList.size(), 0, 1,
					2);

			for (MoveInfo m : moveInfoList) {
				if (!autoPlay)
					break;
				Block b = blockList.get(m.getNumber() - 1);
				Point newPos = moveBlockToArea(b, m.getTo());
				b.setX(newPos.x);
				b.setY(newPos.y);
				repaint();

				try {
					/*
					 * int y = b.getY(); for (int i = y; i > 30; i -= 10) {
					 * b.setY(i); repaint(); Thread.sleep(1); } int x =
					 * b.getX(); if (x < futurePos.x) { for (int i = x; i <
					 * futurePos.x; i += 10) { b.setX(i); repaint();
					 * Thread.sleep(1); } } else { System.out.println("da"); for
					 * (int i = futurePos.x; i < x; i += 10) { b.setX(i);
					 * repaint(); Thread.sleep(1); } } int y1 = b.getY(); for
					 * (int i = y1; i < futurePos.y; i += 10) { b.setY(i);
					 * repaint(); Thread.sleep(1); }
					 */
					Thread.sleep((10 - blockList.size()) * 100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			autoPlay = false;
		}
	}
}
