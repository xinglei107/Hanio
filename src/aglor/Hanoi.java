package aglor;

import java.util.ArrayList;

import pojo.MoveInfo;

public class Hanoi {

	private ArrayList<MoveInfo> moveList = new ArrayList<MoveInfo>();

	private void han(int n, int a, int b, int c) {
		if (n == 1) {
			MoveInfo inf = new MoveInfo(n, a, c);
			moveList.add(inf);
			return;
		}
		han(n - 1, a, c, b);
		MoveInfo inf = new MoveInfo(n, a, c);
		moveList.add(inf);
		han(n - 1, b, a, c);
	}

	public ArrayList<MoveInfo> hanoi(int n, int a, int b, int c) {
		han(n, a, b, c);
		return moveList;
	}

}
