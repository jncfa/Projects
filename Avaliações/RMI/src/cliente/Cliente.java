package cliente;

import java.util.List;
import java.util.Scanner;

import cliente.LigacaoRMI;
import servidor.DadosJogo;
import servidor.Resultado;
import servidor.Jogo.ResultadoJogada;

/**
 * Classe que permite iniciar uma versao distribuida do jogo 'N-em-Linha', junto do respetivo
 * servidor.
 * 
 * @author jncfa, srpg
 */
public class Cliente {
	private static Scanner scanner;
	private static LigacaoRMI ligacaoRemota;
	private static String nomeUtilizador;

	public static void main(String args[]) {
		nomeUtilizador = null;
		scanner = new Scanner(System.in);
		ligacaoRemota = new LigacaoRMI();

		// falha em obter o objeto remoto
		if (!ligacaoRemota.sessaoAtiva()){ 
			return;
		}
		
		/** mensagem de boas vindas */
		System.out.println("Bem vindo ao jogo N-em-linha!\n");
		System.out.println("Para terminar a sessao a qualquer momento, introduza a tecla 'x'.\n");

		/** pedir nome de utilizador */
		System.out.print("Indique o seu nome de utilizador (use apenas letras e numeros): ");
		String input;
		while (!((input = scanner.nextLine()).length() > 0)) {
			System.out.println("Erro, indique um nome valido (use apenas letras e numeros): ");
		}
		nomeUtilizador = input;
		ligacaoRemota.iniciarSessao(nomeUtilizador);

		while (ligacaoRemota.sessaoAtiva()) {
			/** menu inicial */
			System.out.println("\nEscolha a opcao que pretende efetuar:");
			System.out.println("1) Comecar um novo jogo;");
			System.out.println("2) Ver jogos em aberto;");
			System.out.println("3) Ver tabela dos vencedores mais rapidos. \n");
			System.out.print("Insira o numero da operacao a efetuar (1-3, ou 'x' para sair): ");

			String valorInserido = scanner.nextLine();

			if (valorInserido.equalsIgnoreCase("1")) {
				novoJogo();
			} else if (valorInserido.equalsIgnoreCase("2")) {
				jogosEmAberto();
			} else if (valorInserido.equalsIgnoreCase("3")) {
				imprimirTabelaVencedores();
			} else if (valorInserido.equalsIgnoreCase("x")) {
				ligacaoRemota.terminarSessao();
			} else {
				System.out.println("Erro, indique apenas uma das opcoes da lista.");
			}
		}

		System.out.println("Sessao terminada.");
		scanner.close(); 
	}

	/**
	 * Cria um novo jogo, pedindo ao utilizador as diferentes propriedades do tabuleiro, e inicia um novo jogo.
	 */
	private static void novoJogo() {
		String input = null;
		int alturaTabuleiro = -1;
		int larguraTabuleiro = -1;
		int valorObjetivo = -1;
		boolean modoDificil = false;

		System.out.print(String.format("%s, indique a altura do tabuleiro: ", nomeUtilizador));
		boolean valorValido = false;
		while (!valorValido) {
			while (!((input = scanner.nextLine()).matches("\\d+"))) {
				System.out.print("Erro, introduza um inteiro para a altura do tabuleiro: ");
			}
			alturaTabuleiro = Integer.parseInt(input);

			if (!(alturaTabuleiro > 0 && alturaTabuleiro < 99)) {
				System.out.print(
						"Erro, introduza um inteiro entre 0 e 99 para a altura do tabuleiro: ");
			} else {
				valorValido = true;
			}
		}

		System.out.print(String.format("%s, indique a largura do tabuleiro: ", nomeUtilizador));
		valorValido = false;
		while (!valorValido) {
			while (!((input = scanner.nextLine()).matches("\\d+"))) {
				System.out.print("Erro, introduza um inteiro para a largura do tabuleiro: ");
			}
			larguraTabuleiro = Integer.parseInt(input);

			if (!(larguraTabuleiro > 0 && larguraTabuleiro < 99)) {
				System.out.print(
						"Erro, introduza um inteiro entre 0 e 99 para a largura do tabuleiro: ");
			} else {
				valorValido = true;
			}
		}

		System.out.print(String.format("%s, introduza o valor de pecas consecutivas para ganhar: ", nomeUtilizador));
		valorValido = false;
		while (!valorValido) {
			while (!((input = scanner.nextLine()).matches("\\d+"))) {
				System.out.print(
						"Erro, introduza um inteiro para o valor de pecas consecutivas para ganhar: ");
			}
			valorObjetivo = Integer.parseInt(input);

			if (!(valorObjetivo <= larguraTabuleiro && valorObjetivo <= alturaTabuleiro)) {
				System.out.print(
						"Erro, limite excedido. Introduza um N inferior ou igual a altura e a largura: ");
			} else {
				valorValido = true;
			}
		}

		System.out.print(String.format(
				"%s, indique a dificuldade (1 para facil, 2 para dificil): ", nomeUtilizador));
		
				while (!((input = scanner.nextLine()).matches("[12]"))) {
			System.out.print("Erro, indique apenas 1 para facil ou 2 para dificil: ");
		}
		modoDificil = input.equals("1") ? false : true;

		/** Criar e iniciar novo jogo */
		efetuarJogo(ligacaoRemota.iniciarJogo(alturaTabuleiro, larguraTabuleiro, valorObjetivo, modoDificil));
	}

	/**
	 * Joga um determinado jogo, com as propriedades indicadas.
	 * 
	 * @param jogoAtual Propriedades do jogo.
	 */
	private static void efetuarJogo(DadosJogo jogoAtual) {
		boolean pararJogo = false;
		String input = null;
		while (!pararJogo) {
			System.out.println(ligacaoRemota.imprimirTabuleiro());
			System.out.print(String.format("%s (R), indique a coluna onde pretende inserir o disco (0-%d, 'm' para voltar ao menu ou 'v' para voltar atras na jogada): ",
					nomeUtilizador, jogoAtual.getLarguraTabuleiro() -1));
			
			while (!((input = scanner.nextLine()).matches("(\\d+|x|m|v)"))) {
				System.out.print(String.format("Erro, introduza um inteiro entre 0-%d: ", jogoAtual.getLarguraTabuleiro() -1));
			}
			if (input.equalsIgnoreCase("x")) { // terminar aplicacao
				System.out.print("Pretende guardar antes de sair do jogo (y/n): ");
				pararJogo = true;

				while (!((input = scanner.nextLine()).matches("(y|n)"))) {
					System.out.print("Indique apenas 'y' para gravar, e 'n' para sair do jogo sem guardar: ");
				}
				if (input.equalsIgnoreCase("y")) {  
					guardarJogo();
				}

				ligacaoRemota.terminarSessao(); // descarta diretamente o jogo

			} else if (input.equalsIgnoreCase("m")) {
				System.out.print("Pretende guardar antes de voltar ao menu (y/n): ");
				pararJogo = true;

				while (!((input = scanner.nextLine()).matches("(y|n)"))) {
					System.out.print("Indique apenas 'y' para gravar, e 'n' para voltar ao menu sem guardar: ");
				}
				if (input.equalsIgnoreCase("y")) {  
					guardarJogo();
				}

			} else if (input.equalsIgnoreCase("v")){
				if (!ligacaoRemota.desfazerUltimaJogada()){
					System.out.println("Nao e possivel desfazer a jogada.");
				}
			} else {
				int valorColuna = Integer.parseInt(input);
				if (!(valorColuna >= 0 && valorColuna < jogoAtual.getLarguraTabuleiro())) { 
					System.out.print(String.format("Erro, limite excedido. introduza um inteiro entre 0-%d para inserir o disco: ", jogoAtual.getLarguraTabuleiro()-1));
				}
				else{
					ResultadoJogada jogada = ligacaoRemota.efetuarJogada(valorColuna);

					switch(jogada){
						case ERRO_COLUNA_PREENCHIDA:
							System.out.println("Erro, coluna preenchida!");
							break;
						case JOGO_EMPATE:
							System.out.println("O jogo terminou com um empate! \n");
							pararJogo = true;
							break;
						case JOGADOR_VENCEU:
							System.out.println(ligacaoRemota.imprimirTabuleiro());
							System.out.println(String.format("PARABENS, %s! Ganhou este jogo. \n", nomeUtilizador));
							pararJogo = true;
							break;
						case NPC_VENCEU:
							System.out.println(ligacaoRemota.imprimirTabuleiro());
							System.out.println("Infelizmente o computador ganhou, tente outra vez! \n ");
							pararJogo = true;
							break;
						case JOGADA_EFETUADA:
							// nao e necessario fazer nada neste caso, pois sera pedido outra vez ao utilizador para jogar novamente
							break;
					}
				}
			}
		}
	}

	/**
	 * Mostra ao utilizador quais sao os seus jogos em aberto, e permite recomecar um deles a partir da lista.
	 */
	private static void jogosEmAberto(){
		
		List<DadosJogo> jogosEmAberto = ligacaoRemota.jogosEmAberto();
		if (jogosEmAberto != null){
			System.out.println("Lista atual de jogos em aberto:");
			System.out.println(" Id do jogo | Largura do tabuleiro | Altura do tabuleiro | Numero de jogadas | Dificuldade");
			
			int idx = 1;
			for (DadosJogo jogo : jogosEmAberto){
				System.out.println(String.format(" %-10d | %-20d | %-19d | %-17d | %-11s",
				idx++,
				jogo.getLarguraTabuleiro(),
				jogo.getAlturaTabuleiro(),
				jogo.getJogadaAtual(), 
				jogo.getModoDificil() ? "Dificil" : "Facil"
				));
			}

			
			System.out.print("Insira o id do jogo que pretende continuar ou [ENTER] para voltar ao menu inicial: ");
			
			String input;
			while (!((input = scanner.nextLine()).matches("\\d?"))) {
				System.out.print("Indique apenas um inteiro de 1-5 ou [ENTER] para descartar o jogo atual: ");
			}

			
			if (!input.isEmpty()){
				int id = Integer.parseInt(input);
				if (id > 0 && id <= jogosEmAberto.size()){	
					efetuarJogo(ligacaoRemota.iniciarJogo(id - 1));
				}
				else{
					System.out.println("Erro, nao existe nenhum jogo com esse id.");
				}
			}
			else if (input.equalsIgnoreCase("x")){
				ligacaoRemota.terminarSessao();
			}
		}
		else{
			System.out.println("Nao tem jogos em aberto!\n");
		}
	}

	/**
	 * Imprime a tabela de vencedores mais rapidos.
	 */
	private static void imprimirTabelaVencedores() {
		List<Resultado> tabelaVencedores = ligacaoRemota.listaDeVencedores();
		if (tabelaVencedores.size() > 0) {
			System.out.println("Lista de vencedores:");
			System.out.println("Nome do jogador | Modo de jogo | Numero de jogadas | Pontuacao");

			for (Resultado jogo : ligacaoRemota.listaDeVencedores()) {
				System.out.println(String.format("%-15s | %-12s | %-17d | %-9.0f",
				"'" + jogo.nomeJogador() + "'",
				jogo.jogoDificil() ? "Dificil" : "Facil", jogo.numeroJogadas(),
				jogo.pontuacaoTotal()));
			}
		} else {
			System.out.println("Ainda nao existem vencedores registados!");
		}
	}

	/**
	 * Guarda o jogo atual, caso seja atingido o limite de memoria, pergunta ao utilizador
	 * se pretende apagar um dos jogos previamente guardados
	 */
	private static void guardarJogo(){
		String input;
		if(ligacaoRemota.guardarJogoAtual()) // Guardou com sucesso
		{
			System.out.println("Guardado com sucesso!");
		}
		else{
			System.out.println("Limite maximo de jogos possiveis de guardar atingido.");
			System.out.println("Lista atual de jogos em aberto:");
			System.out.println(" Id do jogo | Largura do tabuleiro | Altura do tabuleiro | Numero de jogadas | Dificuldade");
			
			int idx = 1;
			for (DadosJogo jogo : ligacaoRemota.jogosEmAberto()){
				System.out.println(String.format(" %-10d | %-20d | %-19d | %-17d | %-11s",
				idx++,
				jogo.getLarguraTabuleiro(),
				jogo.getAlturaTabuleiro(),
				jogo.getJogadaAtual(), 
				jogo.getModoDificil() ? "Dificil" : "Facil"
				));
			}

			System.out.println("Indique o id do jogo que pretende substituir ou [ENTER] para descartar o jogo atual: ");
			
			while (!((input = scanner.nextLine()).matches("([12345]|$)"))) {
				System.out.print("Indique apenas um inteiro de 1-5 para substituir ou [ENTER] para descartar o jogo atual: ");
			}
			
			if (input.isEmpty()){
				ligacaoRemota.descartarJogoAtual();
			}
			else{
				ligacaoRemota.guardarJogoAtual(Integer.parseInt(input) - 1);
				System.out.println("Guardado com sucesso!");
			}
		}
	}
}