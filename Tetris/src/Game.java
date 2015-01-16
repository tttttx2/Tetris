import java.awt.Color;
import java.util.Random;

import javax.swing.JPanel;

public class Game {
	Tetrominos tetr;
	Background backgr;
	Main main;
	Preview prev = new Preview();
	Hold hold = new Hold();
	int pause = 800;

	// int pausedef = pause;

	public Game() {

		backgr = new Background();
		main = new Main(this);
		gentetr();
		backgr.init();
		while (true) {
			try {
				Thread.sleep(pause);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
			step();
		}
		/*
		 * draw(); step(); draw();
		 */
	}

	public void up() {
		tetr.rot();
		if (!check(tetr.getPosX(), tetr.getPosY())) {
			tetr.resetRot();
		}
		draw();
	}

	/*
	 * public void down() { pause = pausedef / 2;
	 * 
	 * }
	 */
	public void left() {
		if (check(tetr.getPosX() - 1, tetr.getPosY())) {
			tetr.move(0);
		}
		draw();
	}

	public void right() {
		if (check(tetr.getPosX() + 1, tetr.getPosY())) {
			tetr.move(2);
		}
		draw();
	}

	public void hold() {
		if (hold.isUsed()) {
			tetr = new Tetrominos(hold.swap(tetr.getForm(2, 1)));
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 6; y++) {
					main.setPixelhold(x, y, hold.getColor(x, y));
				}
			}
		}
		else {
			hold.newSwap(tetr.getForm(2, 1));
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 6; y++) {
					main.setPixelhold(x, y, hold.getColor(x, y));
				}
			}
			gentetr();
		}
	}

	public boolean check(int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (tetr.getForm(i, j) != 0) {
					if (x + i > 9 | x + i < 0 | y - j < 0 | y - j > 19) {// check
																			// if
																			// out
																			// of
																			// bounds
						return false;

					} else if (backgr.getaZ(x + i, y - j) != 0) {
						return false;
					}
				}

			}
		}
		return true;
	}

	public void draw() {

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {
				main.setPixel(x, y, backgr.getColor(x, y));
			}
		}
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (tetr.getForm(x, y) != 0) {
					main.setPixel(x + tetr.getPosX(), tetr.getPosY() - y,
							tetr.getColor(x, y));
				}
			}
		}
	}

	public void step() {
		if (check(tetr.getPosX(), tetr.getPosY() - 1)) {
			tetr.move(1);
		} else {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (tetr.Form[i][j] != 0) {
						backgr.setaZ(tetr.getPosX() + i, tetr.getPosY() - j,
								tetr.Form[i][j]);
					}
				}
			}
			gentetr();
			for (int y = 0; y < 20; y++) {
				if (backgr.checkLine(y)) {
					backgr.delLine(y);
					y--;
					speedup(90);
				}
			}
			speedup(99);
			if (!check(tetr.getPosX(), tetr.getPosY())) {
				gameover();
			}
		}
		// pause = pausedef;
		draw();
	}

	public void gentetr() {
		tetr = new Tetrominos(prev.next());
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 6; y++) {
				main.setPixelprev(x, y, prev.getColor(x, y));
			}
		}
		
	}

	public void gameover() {
		System.exit(0);
	}

	public void speedup(int x) {
		pause = pause * x / 100;
	}
}
