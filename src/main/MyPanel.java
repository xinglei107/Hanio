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

	// 当前的木块列表
	private ArrayList<Block> blockList = new ArrayList<Block>();

	// 鼠标点下时，木块的坐标与鼠标位置的差值
	private Point drag = new Point();

	// 记录当前木块的位置，如果木块因为越界需要撤回，则使用该位置
	private Point oldPos = null;

	private boolean clickable = true;

	// 当前选中的木块
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
	 * 添加指定数量的木块
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
	 * 清空木块
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
	 * 重置
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
	 * 自动调整指定区域内木块的宽和高
	 * 
	 * @author xinglei
	 */
	public void autoAdjustBlock(int areaIndex) {
		if (blockList.size() <= 0)
			return;
		int blockNumber = blockList.size();
		// 最大的木块与边缘的距离
		int widthSpace = 20;
		// 将宽度一分为3
		int maxWidth = (this.getWidth() - widthSpace * 6) / 3;
		// 将高度平分为x*5份（x表示木块个数除以5向上取整），取其中的x份。
		// 比如木块有6个，则x=2，表示将高度平分为10份，然后取6份
		int maxHeight = (int) (this.getHeight()
				* (blockNumber / ((blockNumber / 5 + 1) * 5.0)) / blockNumber);
		// 计算两个相邻的木块之前的宽度差值
		int widthSplit = (maxWidth) / blockNumber;
		// 以此计算每个木块的位置、大小
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
	 * 给定木块和区域，将木块调整为最佳位置，如果不满足调整条件，则返回上一次合法的位置
	 * 
	 * @param b
	 * @param areaIndex
	 * @return Point
	 * @author xinglei
	 */
	private Point adjustBlockPos(Block b, int areaIndex) {

		int blockCount = blockList.size();
		// 最大的木块与边缘的距离
		int widthSpace = 20;
		// 将宽度一分为3
		int maxWidth = (this.getWidth() - widthSpace * 6) / 3;
		// 将高度平分为x*5份（x表示木块个数除以5向上取整），取其中的x份。
		// 比如木块有6个，则x=2，表示将高度平分为10份，然后取6份
		int maxHeight = (int) (this.getHeight()
				* (blockCount / ((blockCount / 5 + 1) * 5.0)) / blockCount);
		// 计算两个相邻的木块之前的宽度差值
		int widthSplit = (maxWidth) / blockCount;
		int x = (blockCount - b.getNumber()) * widthSplit / 2 + widthSpace
				+ this.getWidth() / 3 * areaIndex;

		// 记录比b的宽度大的木块个数
		int countWidthThanB = 0;
		// 记录宽度大于b的木块中宽度的最小值，用于判断b是否可用放置到最上面
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
		// 如果下面的木块比b小，则不能放置，返回上一次正常的位置
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
	 * 判断指定的区域是否越界
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
	 * 扫描整个区域，对每个区域的木块个数和最小木块宽度分别存储
	 * 
	 * @param eachAreaBlockCount
	 *            存储每个区域内的木块数量
	 * @param eachAreaBlockMinWidth
	 *            存储每个区域内的木块最小宽度，初始值不小于木块宽度的最大值，此处取1000 void
	 * @author xinglei
	 */
	private void scanArea(int[] eachAreaBlockCount, int[] eachAreaBlockMinWidth) {

		for (Block b : blockList) {
			// 获取木块b所在的区域
			int bAreaIndex = transLoc(b.getX());

			// 将对应区域的木块数量加1
			eachAreaBlockCount[bAreaIndex]++;
			// 记录对应区域木块的最小宽度
			if (eachAreaBlockMinWidth[bAreaIndex] > b.getWidth()) {
				eachAreaBlockMinWidth[bAreaIndex] = b.getWidth();
			}
		}
	}

	/**
	 * 
	 * 计算将木块curBlock移动到指定区域后的最佳位置，如果不满足移动条件，则返回原位置
	 * 
	 * @param curBlock
	 * @param areaIndex
	 *            void
	 * @author xinglei
	 */
	private Point getBlockPosByAreaIndex(Block curBlock, int areaIndex) {

		Point futurePos = new Point(curBlock.getX(), curBlock.getY());

		// 如果越界，返回原位置
		if (isOutOfBound(areaIndex)) {
			return futurePos;
		}

		int width = this.getWidth() / 3;

		// 记录每个区域中木块的数目，用于计算新木块的纵坐标
		int[] areaBlockCount = new int[] { 0, 0, 0 };
		// 记录每个区域中最小木块的宽度，用于判断新木块是否可放置
		int[] areaBlockMinWidth = new int[] { 1000, 1000, 1000 };

		// 扫描整个区域，并将结果记录下来
		scanArea(areaBlockCount, areaBlockMinWidth);

		// 判断当前木块的宽度是否大于对应区域的最小宽度
		// 如果是，则恢复原位置
		// 如果可放置，则计算新的最佳位置
		if (isExceedMinWidth(curBlock.getWidth(), areaBlockMinWidth[areaIndex])) {
			System.out.println("不能将大的木块放置在小木块上面");
		} else {
			int oldPosAreaIndex = transLoc(curBlock.getX());

			futurePos = new Point((areaIndex - oldPosAreaIndex) * width
					+ curBlock.getX(), this.getHeight()
					- (areaBlockCount[areaIndex] + 1) * curBlock.getHeight());
			// 如果不是返回原位置，则记录走了一步
			if (transLoc(curBlock.getX()) != areaIndex) {
				totalStep++;
				System.out.println(totalStep);
			}
		}
		return futurePos;

	}

	/**
	 * 给定当前木块的宽度和区域运行的最小宽度，判断是否超过最小宽度
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
	 * 将木块移动到指定位置所在的区间，并自动调整到最佳位置
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
	 * 将木块移动到指定的区间，并自动调整最佳位置
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
	 * 根据横坐标判断属于哪个区间，0、1、2代表左中右
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
	 * 鼠标左键按下时的事件，选中当前点击的木块，记录其原位置，记录鼠标指针与原位置的差值
	 * 
	 * @param mx
	 *            鼠标按下时的横坐标
	 * @param my
	 *            按下时的纵坐标 void
	 * @author xinglei
	 */
	private void mouseLeftPressed(int mx, int my) {
		for (Block b : blockList) {
			if (b.isContain(mx, my)) {
				// 如果点击的不是第一个木块，则不选中
				if (b.getNumber() != getTopOneByAreaIndex(transLoc(mx)))
					continue;
				curBlock = b;
				// 记录鼠标点击的位置与木块坐标的差值
				drag.x = b.getX() - mx;
				drag.y = b.getY() - my;

				// 按下时记录当前的位置，用于恢复
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
	 * 获取指定区域内第一个木块的编号
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
	 * 鼠标右键点击时的事件，取消选中当前选中的木块
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
		// BUTTON1 左键 2中键 3右键
		if (e.getButton() == MouseEvent.BUTTON1) {
			// 如果松开鼠标时木块越界，则恢复上一次的位置
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
