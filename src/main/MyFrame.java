package main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MyFrame extends JFrame implements ComponentListener {

	private MyPanel myPanel = new MyPanel(10, 110, 700, 500);
	private Thread myPanelThread = new Thread(myPanel);

	// private JButton startBtn = new JButton("重置");
	private JButton blockCountBtn = new JButton("5");
	private JButton autoPlayBtn = new JButton("自动");
	private Font font = new Font("黑体", Font.PLAIN, 20);

	private int blockCount = 5;

	public MyFrame() {
		this.addComponentListener(this);
	}

	private void resizeMyPanel() {
		myPanel.setSize(this.getWidth() - 30, this.getHeight() - 155);
	}

	private void init() {
		this.setSize(1000, 600);
		resizeMyPanel();
		// this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.setLocation(100, 100);
		this.setLayout(null);
		this.add(myPanel);

		// addStartBtn();
		addAutoPlayBtn();
		addBlockCountBtn();
		myPanelThread.start();

		this.setVisible(true);
		reset();
	}

	private void reset() {
		myPanel.reset(blockCount);
	}

	/*
	 * private void addStartBtn() { startBtn.setSize(100, 40);
	 * startBtn.setLocation(120, 30); startBtn.setFont(font); // 不绘制文字周围的框
	 * startBtn.setFocusPainted(false); startBtn.addActionListener(new
	 * ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { // TODO
	 * Auto-generated method stub MyFrame.this.reset(); } });
	 * this.add(startBtn); }
	 */

	private void addAutoPlayBtn() {
		autoPlayBtn.setSize(100, 40);
		autoPlayBtn.setLocation(130, 30);
		autoPlayBtn.setFont(font);
		// 不绘制文字周围的框
		autoPlayBtn.setFocusPainted(false);
		autoPlayBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				myPanel.start();
			}
		});
		this.add(autoPlayBtn);
	}

	private void addBlockCountBtn() {
		blockCountBtn.setSize(100, 40);
		blockCountBtn.setLocation(10, 30);
		blockCountBtn.setFont(font);
		// 不绘制文字周围的框
		blockCountBtn.setFocusPainted(false);
		blockCountBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MyFrame.this.reset();
				blockCount = blockCount % 9 + 1;
				blockCountBtn.setText(blockCount + "");
			}
		});
		this.add(blockCountBtn);
	}

	public static void main(String[] args) {
		new MyFrame().init();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		resizeMyPanel();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
