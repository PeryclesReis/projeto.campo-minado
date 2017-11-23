package br.unip.aps.campominado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author peryc,dani,jul,gabri
 * @since
 *
 */
public class Campominado implements ActionListener {

	JButton[][] buttons;
	int[][] counts;
	int tamLinha, tamColuna, qtdMinas;

	JFrame frame = new JFrame("Minesweeper");
	JButton reset = new JButton("Reset");
	Container grid = new Container();
	final int MINE = 10;

	/**
	 * Define o tamanho do tabuleiro do jogo de acordo com as entradas
	 * informadas pelo usuário.
	 */
	public void tamanhoTabuleiro() {
		String resp = JOptionPane
				.showInputDialog("Deseja escolher "
						+ "o tamanho do campo minado? "
						+ "'s' para SIM e 'n' para NÃO");
		if (resp.equalsIgnoreCase("n")) {
			tamLinha = 10;
			tamColuna = 10;

			buttons = new JButton[tamLinha][tamColuna];
			counts = new int[tamLinha][tamColuna];
		} else {
			System.out.println("O tamado do Campo Minado deve"
					+ " ser de no mínimo de 8x8 e máximo de 20x60 posições)");
			do {
				tamLinha = Integer.parseInt(JOptionPane.showInputDialog
						("Digite o tamado da linha (mínimo 8, máximo 20): "));
			} while (tamLinha < 8 || tamLinha > 20);

			do {
				tamColuna = Integer.parseInt(JOptionPane.showInputDialog
								("Digite o tamado da coluna (mínimo 8, máximo 60): "));
			} while (tamColuna < 8 || tamColuna > 60);

			buttons = new JButton[tamLinha][tamColuna];
			counts = new int[tamLinha][tamColuna];
		}
	}

	/**
	 * Define a quantidade de minas de acorda com a escolha do usuario
	 */
	public void quantidadeMina() {
		int porcent = 0;
		String resp2 = JOptionPane.showInputDialog
				("Deseja escolher a porcentagem de minas no tabuleiro?");

		if (resp2.equalsIgnoreCase("n")) {
			qtdMinas = (((tamLinha * tamColuna) * 20) / 100);
		} else {

			do {
				porcent = Integer.parseInt(JOptionPane.showInputDialog
				("Digite um numero referente a porcentagem de minas entre 10% e 80%: "));
			} while (porcent < 10 || porcent > 80);
			qtdMinas = (((tamLinha * tamColuna) * porcent) / 100);
		}
	}

	/**
	 * Define o tamanho da frame
	 */
	private void pegarResolucao() {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension dimensao = t.getScreenSize();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Define as grades do campo minado de acordo com a dimensão do frame
	 */
	public Campominado() {
		pegarResolucao();
		tamanhoTabuleiro();
		quantidadeMina();
		frame.setLayout(new BorderLayout());
		frame.add(reset, BorderLayout.NORTH);
		reset.addActionListener(this);
		// botão grid
		grid.setLayout(new GridLayout(tamLinha, tamColuna));
		for (int a = 0; a < buttons.length; a++) {
			for (int b = 0; b < buttons[0].length; b++) {
				buttons[a][b] = new JButton();
				buttons[a][b].addActionListener(this);
				buttons[a][b].setMargin(new Insets(1, 1, 1, 1));
				grid.add(buttons[a][b]);
			}
		}
		frame.add(grid, BorderLayout.CENTER);
		createRandomMines();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Programa principal
	 * 
	 */
	public static void main(String[] args) {
		new Campominado();
	}

	/**
	 * Define aleatoriamente onde as minas serão alojadas
	 */
	public void createRandomMines() {
		// inicializar lista de pares aleatórios
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {
				list.add(x * 100 + y);
			}
		}
		// restabelece a matriz counts, sorteia as minas.
		counts = new int[tamLinha][tamColuna];
		for (int a = 0; a < qtdMinas; a++) {
			int choice = (int) (Math.random() * list.size());
			counts[list.get(choice) / 100][list.get(choice) % 100] = MINE;
			list.remove(choice);
		}
		// inicialize contagem das vizinhas.
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {
				if (counts[x][y] != MINE) {

					int neighborcount = 0;

					if (x > 0 && y > 0 && counts[x - 1][y - 1] == MINE && neighborcount < 2) { // cima
																								// esquerda
						neighborcount++;
					}
					if (y > 0 && counts[x][y - 1] == MINE && neighborcount < 2) { // cima
						neighborcount++;
					}
					if (x < counts.length - 1 && y > 0 && counts[x + 1][y - 1] == MINE && neighborcount < 2) { // cima
																												// direita
						neighborcount++;
					}
					if (x > 0 && counts[x - 1][y] == MINE && neighborcount < 2) { // esquerda
						neighborcount++;
					}
					if (x < counts.length - 1 && counts[x + 1][y] == MINE && neighborcount < 2) { // direita
						neighborcount++;
					}
					if (x > 0 && y < counts[0].length - 1 && counts[x - 1][y + 1] == MINE && neighborcount < 2) { // baixo
																													// esquerda
						neighborcount++;
					}
					if (y < counts[0].length - 1 && counts[x][y + 1] == MINE && neighborcount < 2) { // baixo
						neighborcount++;
					}
					if (x < counts.length - 1 && y < counts[0].length - 1 && counts[x + 1][y + 1] == MINE
							&& neighborcount < 2) { // baixo direita
						neighborcount++;
					}
					counts[x][y] = neighborcount;
				}
			}
		}
	}

	/**
	 * Define quando o jogador perde a partida
	 */
	public void lostGame(JButton exploded) {
		for (int x = 0; x < buttons.length; x++) {
			for (int y = 0; y < buttons[0].length; y++) {
				if (buttons[x][y].isEnabled()) {
					if (counts[x][y] != MINE) {
						buttons[x][y].setText(counts[x][y] + "");
						buttons[x][y].setEnabled(false);
					} else {
						JButton jButton = buttons[x][y];
						try {
							Image img = ImageIO.read(new File("C:/Users/peryc/Downloads/imagens.png"));
							jButton.setIcon(new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
						} catch (IOException e) {
							e.printStackTrace();
							jButton.setText("x");
						}
						jButton.setEnabled(jButton.equals(exploded));
					}
				}
			}
		}
	}

	/**
	 * Revela as grades que tem numeros iguais a 0 durante o jogo
	 * 
	 * @param toClear
	 */
	public void clearZeros(ArrayList<Integer> toClear) {
		if (toClear.size() == 0) {
			return;
		} else {
			int x = toClear.get(0) / 100;
			int y = toClear.get(0) % 100;
			toClear.remove(0);
			if (x > 0 && y > 0 && buttons[x - 1][y - 1].isEnabled()) { // cima
																		// esquerda
				buttons[x - 1][y - 1].setText(counts[x - 1][y - 1] + "");
				buttons[x - 1][y - 1].setEnabled(false);
				if (counts[x - 1][y - 1] == 0) {
					toClear.add((x - 1) * 100 + (y - 1));
				}
			}
			if (y > 0 && buttons[x][y - 1].isEnabled()) { // cima
				buttons[x][y - 1].setText(counts[x][y - 1] + "");
				buttons[x][y - 1].setEnabled(false);
				if (counts[x][y - 1] == 0) {
					toClear.add(x * 100 + (y - 1));
				}
			}
			if (x < counts.length - 1 && y > 0 && buttons[x + 1][y - 1].isEnabled()) { // cima
																						// direita
				buttons[x + 1][y - 1].setText(counts[x + 1][y - 1] + "");
				buttons[x + 1][y - 1].setEnabled(false);
				if (counts[x + 1][y - 1] == 0) {
					toClear.add((x + 1) * 100 + (y - 1));
				}
			}
			if (x > 0 && buttons[x - 1][y].isEnabled()) { // esquerda
				buttons[x - 1][y].setText(counts[x - 1][y] + "");
				buttons[x - 1][y].setEnabled(false);
				if (counts[x - 1][y] == 0) {
					toClear.add((x - 1) * 100 + y);
				}
			}
			if (x < counts.length - 1 && buttons[x + 1][y].isEnabled()) { // direita
				buttons[x + 1][y].setText(counts[x + 1][y] + "");
				buttons[x + 1][y].setEnabled(false);
				if (counts[x + 1][y] == 0) {
					toClear.add((x + 1) * 100 + y);
				}
			}
			if (x > 0 && y < counts[0].length - 1 && buttons[x - 1][y + 1].isEnabled()) { // baixo
																							// esquerda
				buttons[x - 1][y + 1].setText(counts[x - 1][y + 1] + "");
				buttons[x - 1][y + 1].setEnabled(false);
				if (counts[x - 1][y + 1] == 0) {
					toClear.add((x - 1) * 100 + (y + 1));
				}
			}
			if (y < counts[0].length - 1 && buttons[x][y + 1].isEnabled()) { // baixo
				buttons[x][y + 1].setText(counts[x][y + 1] + "");
				buttons[x][y + 1].setEnabled(false);
				if (counts[x][y + 1] == 0) {
					toClear.add(x * 100 + (y + 1));
				}
			}
			if (x < counts.length - 1 && y < counts[0].length - 1 && buttons[x + 1][y + 1].isEnabled()) { // baixo
																											// direita
				buttons[x + 1][y + 1].setText(counts[x + 1][y + 1] + "");
				buttons[x + 1][y + 1].setEnabled(false);
				if (counts[x + 1][y + 1] == 0) {
					toClear.add((x + 1) * 100 + (y + 1));
				}
			}
			clearZeros(toClear);
		}
	}

	/**
	 * Defini quando o usuário ganhou a partida
	 */
	public void checkwin() {
		boolean ganhou = true;
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {
				if (counts[x][y] != MINE && buttons[x][y].isEnabled() == true) {
					ganhou = false;
				}
			}
		}
		if (ganhou == true) {
			showWinMessage();
		}
	}

	/**
	 * Define as ações durante o jogo, quando clica,
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(reset)) {
			for (int x = 0; x < buttons.length; x++) {
				for (int y = 0; y < buttons[0].length; y++) {
					buttons[x][y].setEnabled(true);
					buttons[x][y].setText("");
					buttons[x][y].setIcon(null);
				}
			}
			createRandomMines();
		} else {
			for (int x = 0; x < buttons.length; x++) {
				for (int y = 0; y < buttons[0].length; y++) {
					if (event.getSource().equals(buttons[x][y])) {
						if (counts[x][y] == MINE) {
							buttons[x][y].setForeground(Color.red);
							buttons[x][y].setText("x");
							// abre todas as grades
							lostGame((JButton) event.getSource());
							showLostMessage();
						} else if (counts[x][y] == 0) {
							buttons[x][y].setText(counts[x][y] + "");
							buttons[x][y].setEnabled(false);
							ArrayList<Integer> toClear = new ArrayList<Integer>();
							toClear.add(x * 100 + y);
							clearZeros(toClear);
							checkwin();
						} else {
							buttons[x][y].setText(counts[x][y] + "");
							buttons[x][y].setEnabled(false);
							checkwin();
						}
					}
				}
			}
		}
	}

	/**
	 * Mostra messagens de quando o usuário ganhou ou perdeu a partida
	 */
	private void showLostMessage() {
		showMessage("VOCÊ PERDEU!!!  :´(");
	}

	private void showWinMessage() {
		showMessage("VOCÊ VENCEU!!!!!  \\o/");
	}

	private void showMessage(String mensagem) {
		JOptionPane.showMessageDialog(frame, mensagem);
	}

}
