package aval;

import java.util.Random;
import java.util.Scanner;

/**
 * Classe que permite executar uma versao digital do jogo 4-em-Linha.
 * 
 * @author jncfa, srpg
 */
public class Jogo
{
	static Scanner scan = new Scanner(System.in);
	static Random rand = new Random();

	static char[][] base;
	static int jogada;
	static int randJogo; // variavel aleatoria para decidir quem comeca a jogar

	public static void main(String[] args)
	{
		boolean acabarJogo = false;

		System.out.println("Bem vindo ao 4 em Linha!");

		System.out.print("Introduza o nome do jogador R: ");
		String jogadorR = scan.nextLine();
		System.out.print("Introduza o nome do jogador Y: ");
		String jogadorY = scan.nextLine();

		System.out.println("\nBom jogo, " + jogadorR + " e " + jogadorY + "!");

		novoJogo();
		imprimeTabuleiro();

		while (!acabarJogo)
		{
			if (jogada == 65)
			{ // tabuleiro preenchido
				if (lerConsola("snx", "Empate, tabuleiro preenchido! Quer comecar um novo jogo (s/n)").equals("s"))
				{
					novoJogo();
					imprimeTabuleiro();
				}
				else
				{
					acabarJogo = true;
				}
			}
			else
			{
				String jogadorAtual;
				char discoAtual;

				if ((jogada + randJogo) % 2 == 0)
				{
					jogadorAtual = jogadorR;
					discoAtual = 'R';
				}
				else
				{
					jogadorAtual = jogadorY;
					discoAtual = 'Y';
				}

				String input = lerConsola("01234567x", jogadorAtual + " (" + discoAtual + ")"
						+ ", indique a coluna onde pretende inserir o disco (0-7) ou 'x' para terminar a aplicacao");

				if (!input.equals("x"))
				{
					int coluna = Integer.parseInt(input); // Integer.parseInt- transforma uma string numa int
					int altura;
					boolean fichaColocada = false;

					for (altura = 8; altura >= 1; altura--)
					{
						if (base[altura][coluna + 1] == ' ')
						{
							base[altura][coluna + 1] = discoAtual;
							fichaColocada = true;
							break;
						}
					}

					if (fichaColocada)
					{ // a jogada e valida, caso contrario a coluna esteja completamente preenchida,
						// enviamos mensagem de erro
						if (jogadorVence(coluna + 1, altura))
						{
							imprimeTabuleiro();
							if (lerConsola("snx", "Parab�ns, "+jogadorAtual + "! Venceu este jogo! Quer comecar um novo (s/n)").equals("s"))
							{ // jogo acabou
								novoJogo();
								imprimeTabuleiro();
							}
							else
							{
								acabarJogo = true;
							}

						}
						else
						{
							jogada += 1;
							imprimeTabuleiro();
						}
					}
					else
					{
						System.out.println("Coluna preenchida! Por favor, tente de novo.");
					}
				}
				else
				{
					acabarJogo = true;
				}
			}
		}

		System.out.println("Jogo terminado.");
		scan.close();
	}

	/**
	 * Gera um novo tabuleiro e reinicia o numero de jogadas.
	 */
	static void novoJogo()
	{
		base = new char[][] { { ' ', '0', '1', '2', '3', '4', '5', '6', '7', ' ' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*' },
				{ '*', '*', '*', '*', '*', '*', '*', '*', '*', '*' } };

		jogada = 1;
		randJogo = rand.nextInt(2);
	}

	/**
	 * Pede input ao utilizador (de apenas 1 caracter), e verifica se faz parte do conjunto aceitavel (whitelist) de
	 * respostas.
	 * 
	 * @param inputsValidos
	 *            Conjunto de respostas validas;
	 * @param mensagem
	 *            Mensagem a mostrar ao utilizador;
	 * @return Caracter inserido pelo utilizador;
	 */
	static String lerConsola(String inputsValidos, String mensagem)
	{
		System.out.print(mensagem + ": ");

		String input;

		// verifica se o carater inserido esta na whitelist de respostas
		while (!(inputsValidos.contains(input = scan.nextLine().toLowerCase()) && input.length() == 1))
		{
			System.out.print("Erro. Coloque uma instrucao valida: ");
		}
		return input;
	}

	/**
	 * Imprime o tabuleiro atual, e indica a jogada atual.
	 */
	static void imprimeTabuleiro()
	{
		System.out.println("\nJogada " + jogada); // indica a jogada atual
		for (int i = 0; i <= 9; i++)
		{
			for (int j = 0; j <= 9; j++)
			{
				System.out.print(base[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Verifica se o jogador atual venceu o jogo.
	 * 
	 * @param x
	 *            - Coordenada 'x' do disco inserido pelo jogador;
	 * @param y
	 *            - Coordenada 'y' do disco inserido pelo jogador;
	 * 
	 * @return {@code true} se o jogador atual venceu a partida, caso contrario {@code false};
	 */
	static boolean jogadorVence(int x, int y)
	{

		int[] direcao = { 1, -1 };

		for (int i = -1; i <= 2; i++)
		{
			if (i == 2)
			{
				direcao[0] = 0; // direcao = {0, 1}
			}
			else
			{
				direcao[1] = i; // direcao = {1, i} -> {1, -1}, {1, 0}, {1, 1}
			}

			if (contarDiscos(direcao[0], direcao[1], x, y) >= 4)
			{ // + de 4 em linha, jogador venceu
				substituirDiscos(direcao[0], direcao[1], x, y);
				return true;
			}
		}
		return false;
	}

	/**
	 * Conta todos os discos em volta de uma pe�a (inclusive) numa determinada direcao.
	 * 
	 * @param dx
	 *            Componente x da direcao;
	 * @param dy
	 *            Componente y da direcao;
	 * @param x
	 *            Componente x da posicao atual;
	 * @param y
	 *            Componente y da posicao atual;
	 */
	static int contarDiscos(int dx, int dy, int x, int y)
	{
		int discos = 1;

		// Verifica se a posicao seguinte, segundo uma determinada direcao (dx, dy), e valida
		for (int i = 1; ((x + i * dx) > 0 && ((x + i * dx) < 9)) && ((y + i * dy) > 0 && (y + i * dy) < 9); i++)
		{
			if (base[y][x] == base[y + i * dy][x + i * dx])
			{ // incrementa o numero de discos se tem o mesmo valor que a posicao inicial
				discos += 1;
			}
			else
				break;
		}

		// Verifica se a posicao anterior, segundo uma determinada direcao (dx, dy), e valida
		for (int i = -1; ((x + i * dx) > 0 && ((x + i * dx) < 9)) && ((y + i * dy) > 0 && (y + i * dy) < 9); i--)
		{
			if (base[y][x] == base[y + i * dy][x + i * dx])
			{
				discos += 1;
			}
			else
				break;
		}

		return discos;
	}

	/**
	 * Substitui todos os discos em volta de uma pe�a (inclusive) numa determinada direcao.
	 * 
	 * @param dx
	 *            Componente x da direcao;
	 * @param dy
	 *            Componente y da direcao;
	 * @param x
	 *            Componente x da posicao atual;
	 * @param y
	 *            Componente y da posicao atual;
	 */
	static void substituirDiscos(int dx, int dy, int x, int y)
	{
		// Verifica se a posicao seguinte, segundo uma determinada direcao (dx, dy), e valida
		for (int i = 1; ((x + i * dx) > 0 && ((x + i * dx) < 9)) && ((y + i * dy) > 0 && (y + i * dy) < 9); i++)
		{
			if (base[y][x] == base[y + i * dy][x + i * dx])
			{
				base[y + i * dy][x + i * dx] = '#';
			}
			else
				break;
		}

		// Verifica se a posicao anterior, segundo uma determinada direcao (dx, dy), e valida
		for (int i = -1; ((x + i * dx) > 0 && ((x + i * dx) < 9)) && ((y + i * dy) > 0 && (y + i * dy) < 9); i--)
		{
			if (base[y][x] == base[y + i * dy][x + i * dx])
			{
				base[y + i * dy][x + i * dx] = '#';
			}
			else
				break;
		}

		base[y][x] = '#'; // falta apenas a posicao inicial
	}
}