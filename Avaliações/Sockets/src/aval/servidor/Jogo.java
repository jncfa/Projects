package aval.servidor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe que gere a logica do jogo 'N-em-linha'.
 * 
 * @author jncfa, srpg
 */
public class Jogo {
	public enum ResultadoJogada{
		/** Indica que a jogada nao foi efetuada pois a coluna escolhida encontra se preenchida. */
		ERRO_COLUNA_PREENCHIDA,

		/** Indica que a jogada foi efetuada, ninguem venceu, e nao e possivel continuar a jogar, tendo entao um empate entre o computador e o jogador.*/
		JOGO_EMPATE,

		/** Indica que a jogada foi efetuada, ninguem venceu, e ainda e possivel continuar a jogar.*/
		JOGADA_EFETUADA,

		/** Indica que a jogada terminou com o jogador a vencer. */
		JOGADOR_VENCEU,
		
		/** Indica que a jogada terminou com o computador a vencer. */
		NPC_VENCEU,
	}

	private static final char[] ICONS_JOGADORES = {'R', 'Y', 'B'};
	private static final Random rand = new Random();

	char[][] tabuleiro;
	
	private int alturaTabuleiro, larguraTabuleiro, valorObjetivo;
	private int jogadaAtual, randJogo; // variavel aleatoria para decidir quem comeca a jogar
	private int numeroJogadores;
	private int totalJogadas;

	public Jogo(int alturaTabuleiro, int larguraTabuleiro, int valorObjetivo, boolean modoDificil){
		this.alturaTabuleiro = alturaTabuleiro;
		this.larguraTabuleiro = larguraTabuleiro;
		this.valorObjetivo = valorObjetivo;
		this.numeroJogadores = modoDificil ? 3 : 2;
		this.jogadaAtual = 0;
		this.totalJogadas = 0;
		this.randJogo = rand.nextInt(this.numeroJogadores);
		this.tabuleiro = new char[this.alturaTabuleiro][this.larguraTabuleiro];
		
		for (int x = 0; x < this.larguraTabuleiro; x++) {
			for(int y = 0; y < this.alturaTabuleiro; y++){
				this.tabuleiro[y][x] = ' ';
			}
		}

		if (randJogo != 0){
			for (int idx = randJogo; idx < this.numeroJogadores; idx++){
				this.totalJogadas++;

				List<Integer> colunasDisponiveis = new ArrayList<>();
				for(int larguraAtual = 0; larguraAtual < this.larguraTabuleiro; larguraAtual++){
					if (this.tabuleiro[this.alturaTabuleiro - 1][larguraAtual] == ' '){ // coluna nao esta preenchida
						colunasDisponiveis.add(larguraAtual);
					}
				}
				/** garante que a coluna escolhida aleatoriamente nao esta totalmente preenchida */
				int jogadaColuna = colunasDisponiveis.get(rand.nextInt(colunasDisponiveis.size())); 
				
				for(int altura = 0; altura < this.alturaTabuleiro; altura++){
					if (this.tabuleiro[altura][jogadaColuna] == ' '){
						this.tabuleiro[altura][jogadaColuna] = ICONS_JOGADORES[idx];
						break;
					}
				}
			}
		}		
	}

	/**
	 * Imprime o tabuleiro atual, e indica a jogada atual.
	 */
	public String imprimeTabuleiro() {
		String strTabuleiro = "\nJogada " + (this.jogadaAtual + 1) + "\n  ";
		
		// Imprime o topo do tabuleiro
		for (int j = 0; j < this.larguraTabuleiro; j++) { 
			strTabuleiro += String.format("%3d ", (j + 1));
		}
		strTabuleiro += '\n';

		// Imprime o tabuleiro linha a linha (os asteriscos ficam nas bordas do tabuleiro)
		for (int i = this.alturaTabuleiro - 1; i >= 0 ; i--) {
			strTabuleiro += "* ";
			for (int j = 0; j < this.larguraTabuleiro; j++) {
				strTabuleiro += String.format("%3s ", this.tabuleiro[i][j]);
			}
			strTabuleiro += "  *\n";
		}

		// Imprime o fim do tabuleiro
		for (int j = 0; j <= this.larguraTabuleiro + 1; j++) {
			strTabuleiro += "*   ";
		}
		return strTabuleiro + "\n";
	} 

	/**
	 * Tenta colocar a ficha na tabela, e caso nao consiga
	 * @param valorInserido A coluna onde o jogador pretende colocar a ficha.
	 * @return Codigo que indica o estado da jogada, ver {@link ResultadoJogada}.
	 */
	public ResultadoJogada efetuarJogada(int valorInserido){
		/**
		 * idx = 0 -> jogador atual e o cliente
		 * idx = 1 -> jogador atual e bot_1
		 * idx = 2 -> jogador atual e o bot_2
		 */
		for(int idx = 0; idx < this.numeroJogadores; idx++){
			// tabuleiro preenchido
			if(this.totalJogadas == this.larguraTabuleiro * this.alturaTabuleiro){
				return ResultadoJogada.JOGO_EMPATE;
			}
			else{
				int jogadaColuna = -1, jogadaAltura = -1;

				if (idx == 0){ // jogador atual e o cliente
					if (this.tabuleiro[this.alturaTabuleiro - 1][valorInserido] != ' '){ // coluna esta preenchida
						return ResultadoJogada.ERRO_COLUNA_PREENCHIDA;
					}
					else{
						jogadaColuna = valorInserido;
						this.jogadaAtual++;
					}
				}
				else{ // jogador atual e um bot
					List<Integer> colunasDisponiveis = new ArrayList<>();
					for(int larguraAtual = 0; larguraAtual < this.larguraTabuleiro; larguraAtual++){
						if (this.tabuleiro[this.alturaTabuleiro - 1][larguraAtual] == ' '){ // coluna nao esta preenchida
							colunasDisponiveis.add(larguraAtual);
						}
					}
					/** garante que a coluna escolhida aleatoriamente nao esta totalmente preenchida */
					jogadaColuna = colunasDisponiveis.get(rand.nextInt(colunasDisponiveis.size())); 
				}
				for(int altura = 0; altura < this.alturaTabuleiro; altura++){
					if (this.tabuleiro[altura][jogadaColuna] == ' '){
						this.tabuleiro[altura][jogadaColuna] = ICONS_JOGADORES[idx];
						jogadaAltura = altura;
						break;
					}
				}
				
				this.totalJogadas++;

				if (jogadorVence(jogadaColuna, jogadaAltura)){ // um jogador venceu
					// indica qual se venceu o computador ou o cliente
					return (idx == 0) ? ResultadoJogada.JOGADOR_VENCEU : ResultadoJogada.NPC_VENCEU; 
				}
			}
		}
		return ResultadoJogada.JOGADA_EFETUADA;
	}
	
	/**
	 * Devolve o numero de jogadas feitas.
	 */
	public int numeroDeJogadasFeitas() {
		return this.jogadaAtual;
	}

		
	/**
	 * Verifica se o jogador atual venceu o jogo.
	 * 
	 * @param x - Coordenada 'x' do disco inserido pelo jogador;
	 * @param y - Coordenada 'y' do disco inserido pelo jogador;
	 * 
	 * @return {@code true} se o jogador atual venceu a partida, caso contrario
	 *         {@code false};
	 */
	private boolean jogadorVence(int x, int y) {
		int dx = 1;
		int dy = -1;

		for (int i = -1; i <= 2; i++) {
			// direcoes -> {1, -1}, {1, 0}, {1, 1}, {0, 1}
			if (i == 2) {
				dx = 0; 
			} else {
				dy = i; 
			}

			if (contarDiscos(dx, dy, x, y) >= this.valorObjetivo) { // + de N em linha, jogador venceu
				substituirDiscos(dx, dy, x, y);
				return true;
			}
		}
		return false;
	}

	/**
	 * Conta todos os discos em volta de uma peca (inclusive) numa determinada
	 * direcao.
	 * 
	 * @param dx Componente x da direcao;
	 * @param dy Componente y da direcao;
	 * @param x  Componente x da posicao atual;
	 * @param y  Componente y da posicao atual;
	 */
	private int contarDiscos(int dx, int dy, int x, int y) {
		int discos = 1;

		// Verifica se a posicao seguinte, segundo uma determinada direcao (dx, dy), e valida
		for (int i = 1; ((x + i * dx) >= 0 && ((x + i * dx) < this.larguraTabuleiro)) && ((y + i * dy) >= 0 && (y + i * dy) < this.alturaTabuleiro); i++) {
			if (this.tabuleiro[y][x] == this.tabuleiro[y + i * dy][x + i * dx]) { // incrementa o numero de discos se tem o mesmo valor que a posicao inicial
				discos += 1;
			} else
				break;
		}

		// Verifica se a posicao anterior, segundo uma determinada direcao (dx, dy), e valida
		for (int i = 1; ((x - i * dx) >= 0 && ((x - i * dx) < this.larguraTabuleiro)) && ((y - i * dy) >= 0 && (y - i * dy) < this.alturaTabuleiro); i++) {
			if (this.tabuleiro[y][x] == this.tabuleiro[y - i * dy][x - i * dx]) {
				discos += 1;
			} 
			else break;
		}

		return discos;
	}

	/**
	 * Substitui todos os discos em volta de uma pe�a (inclusive) numa determinada
	 * direcao.
	 * 
	 * @param dx Componente x da direcao;
	 * @param dy Componente y da direcao;
	 * @param x  Componente x da posicao atual;
	 * @param y  Componente y da posicao atual;
	 */
	private void substituirDiscos(int dx, int dy, int x, int y) {
		// Verifica se a posicao seguinte, segundo uma determinada direcao (dx, dy), e
		// valida
		for (int i = 1; ((x + i * dx) >= 0 && ((x + i * dx) < this.larguraTabuleiro)) && ((y + i * dy) >= 0 && (y + i * dy) < this.alturaTabuleiro); i++) {
			if (this.tabuleiro[y][x] == this.tabuleiro[y + i * dy][x + i * dx]) {
				this.tabuleiro[y + i * dy][x + i * dx] = '#';
			} 
			else break;
		}

		// Verifica se a posicao anterior, segundo uma determinada direcao (dx, dy), e
		// valida
		for (int i = 1; ((x - i * dx) >= 0 && ((x - i * dx) < this.larguraTabuleiro)) && ((y - i * dy) >= 0 && (y - i * dy) < this.alturaTabuleiro); i++) {
			if (this.tabuleiro[y][x] == this.tabuleiro[y - i * dy][x - i * dx]) {
				this.tabuleiro[y - i * dy][x - i * dx] = '#';
			} 
			else break;
		}

		this.tabuleiro[y][x] = '#'; // falta apenas a posicao inicial
	}
}