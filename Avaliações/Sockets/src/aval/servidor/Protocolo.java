package aval.servidor;

import aval.servidor.Jogo.ResultadoJogada;

/** 
 * Protocolo de comunicacao do servidor. Aqui e validada a informacao que vem do cliente, e a partir dessa e gerada uma resposta que lhe e enviada.
 * @author jncfa, srpg
 */
public class Protocolo {
    public enum Estado {
        /** Foi iniciada uma nova sessao, e que ainda nao foi estabelecida comunicacao com o cliente. */
        INCIO_DE_SESSAO,
        
        /** Foi pedido ao cliente informacao sobre se pretende jogar ou ver a leaderboard. */
        MENU_INICIAL_APRESENTADO,

        /** Foi pedido o nome de utilizador do cliente. */
        NOME_JOGADOR_REQUISITADO,

        /** Foi pedido a altura do tabuleiro ao cliente. */
        ALTURA_TABULEIRO_REQUISITADA,

        /** Foi pedido a largura do tabuleiro ao cliente. */
        LARGURA_TABULEIRO_REQUISITADA,

        /** Foi pedido o valor de pecas consecutivas necessarias para vencer ao cliente. */
        VALOR_OBJETIVO_REQUISITADO,

        /** Foi pedida a dificuldade do jogo ao cliente. */
        DIFICULTADE_JOGO_REQUISITADA,

        /** Foi pedido ao jogador a sua nova jogada. */
        JOGO_A_DECORRER,
    }

    private boolean manterLigacaoAtiva;
    private Estado estadoAtual;

    private Jogo jogoAtual;
    private String jogadorAtual;
    private int alturaTabuleiro, larguraTabuleiro, valorObjetivo;
    private boolean modoDificil;
    private Leaderboard tabelaVencedores;
    
    public Protocolo(Leaderboard tabelaVencedores) {
        this.tabelaVencedores = tabelaVencedores;
        this.manterLigacaoAtiva = true;
        this.estadoAtual = Estado.INCIO_DE_SESSAO;
        this.jogoAtual = null;
        this.jogadorAtual = null;
        this.alturaTabuleiro = this.larguraTabuleiro = -1;
        this.modoDificil = false;
    }

    /**
     * @return the manterLigacaoAtiva
     */
    public boolean manterLigacaoAtiva() {
        return manterLigacaoAtiva;
    }

    /**
     * TODO
     * @param input
     * @return
     */
    public String processarInput(String input){
        String output = new String();
        
        if (input.equals("x")){            
            output = "Sessao terminada.";
            this.manterLigacaoAtiva = false;
        }
        else{
            switch(this.estadoAtual){
                case INCIO_DE_SESSAO:
                    output += mensagemBoasVindas();
                    output += menuInicial();

                    this.estadoAtual = Estado.MENU_INICIAL_APRESENTADO;
                    break;

                case MENU_INICIAL_APRESENTADO:
                    if (input.equals("1")){ // comecar novo jogo
                        output += comecarNovoJogo();
                        this.estadoAtual = Estado.NOME_JOGADOR_REQUISITADO;
                    }
                    else if (input.equals("2")){ // mostrar tabela dos vencedores
                        output += listarVencedores();
                        output += menuInicial(); 
                    }
                    else{
                        output += "Erro, introduza apenas '1' se pretende comecar um novo jogo, ou '2' se pretende ver a tabela: ";
                    }
                    break;

                case NOME_JOGADOR_REQUISITADO:
                    if (input.length() > 0){                 
                        this.jogadorAtual = input;
                        output += String.format("%s, indique a altura do tabuleiro: ", this.jogadorAtual);
                        this.estadoAtual = Estado.ALTURA_TABULEIRO_REQUISITADA;
                    }
                    else{
                        output += "Erro, indique um nome valido (use apenas letras e numeros): ";
                    }
                    break;

                case ALTURA_TABULEIRO_REQUISITADA:
                    if (input.matches("\\d+")){
                        Integer valorInserido = Integer.parseInt(input);
                        if(valorInserido <= 99) {
                            this.alturaTabuleiro = valorInserido;
                            System.out.println(this.alturaTabuleiro);
                        	output += String.format("%s, indique a largura do tabuleiro: ", this.jogadorAtual);
                            this.estadoAtual = Estado.LARGURA_TABULEIRO_REQUISITADA;
                        }
                        else {
                        	output += "Erro, limite excedido. Introduza uma altura inferior a 100: ";
                        }
                    }
                    else{
                        output += "Erro, introduza um inteiro para a altura do tabuleiro: ";
                    }
                    break;

                case LARGURA_TABULEIRO_REQUISITADA:
                    if (input.matches("\\d+")){
                        Integer valorInserido = Integer.parseInt(input);
                        if (valorInserido <= 99) {
                            this.larguraTabuleiro = valorInserido;
                            System.out.println(this.larguraTabuleiro);
                        	output += String.format("%s, introduza o valor de pecas consecutivas para ganhar: ", this.jogadorAtual);
                            this.estadoAtual = Estado.VALOR_OBJETIVO_REQUISITADO;
                        }
                        else {
                        	output += "Erro, limite excedido. Introduza uma largura inferior a 100: ";
                        }
                        
                    }
                    else{
                        output += "Erro, introduza um inteiro para a largura do tabuleiro: ";
                    }
                    break;
                case VALOR_OBJETIVO_REQUISITADO:
                	if (input.matches("\\d+")){
                        Integer valorInserido = Integer.parseInt(input);
                        if (valorInserido <= this.larguraTabuleiro && valorInserido <= this.alturaTabuleiro) {
                            System.out.println(valorInserido);
                        	this.valorObjetivo = valorInserido;
                        	output += String.format("%s, indique a dificuldade (1 para facil, 2 para dificil): ", this.jogadorAtual);
                            this.estadoAtual = Estado.DIFICULTADE_JOGO_REQUISITADA;
                        }
                        else {
                        	output += "Erro, limite excedido. Introduza um N inferior ou igual a altura e a largura: ";
                        }
                        
                    }
                    else{
                        output += "Erro, introduza um inteiro para o N: ";
                    }
                    break;
                	
                case DIFICULTADE_JOGO_REQUISITADA:
                    if (input.matches("[12]")){
                        this.modoDificil = input.equals("1") ? false : true;
                        this.jogoAtual = new Jogo(this.alturaTabuleiro, this.larguraTabuleiro, this.valorObjetivo, this.modoDificil);
                        
                        output += this.jogoAtual.imprimeTabuleiro();
                        output += String.format("%s, indique a coluna onde pretende inserir o disco: ", this.jogadorAtual);
                        this.estadoAtual = Estado.JOGO_A_DECORRER;
                    }
                    else{
                        output += "Erro, indique apenas 1 para facil ou 2 para dificil: ";
                    }
                    break;
                
                case JOGO_A_DECORRER:
                    if(input.matches("\\d+")){ // verifica que o utilizador inseriu um numero inteiro
                        int valorInserido = Integer.parseInt(input);
                        if (valorInserido > 0 && valorInserido <= this.larguraTabuleiro){

                            ResultadoJogada resultado = this.jogoAtual.efetuarJogada(valorInserido - 1);
                            if(resultado.equals(ResultadoJogada.ERRO_COLUNA_PREENCHIDA)){ // 
                                output += "Erro, coluna preenchida! Escolha outra: ";
                            }
                            else if (resultado.equals(aval.servidor.Jogo.ResultadoJogada.JOGADA_EFETUADA)){
                                output += this.jogoAtual.imprimeTabuleiro();
                                output += String.format("%s, indique a coluna onde pretende inserir o disco: ", this.jogadorAtual);
                            }
                            else if (resultado.equals(aval.servidor.Jogo.ResultadoJogada.JOGADOR_VENCEU)){
                                output += this.jogoAtual.imprimeTabuleiro();
                                output += String.format("Parabens %s! Voce venceu o jogo!\n", this.jogadorAtual);

                                if (this.tabelaVencedores.adicionarResultado(this.jogadorAtual, this.jogoAtual.numeroDeJogadasFeitas(), this.modoDificil)){
                                    output += listarVencedores();
                                    output += "Voce ficou na lista de vencedores! ";
                                }
                                output += menuInicial();
                                this.estadoAtual = Estado.MENU_INICIAL_APRESENTADO;
                            }
                            else if (resultado.equals(aval.servidor.Jogo.ResultadoJogada.NPC_VENCEU)){
                                output += this.jogoAtual.imprimeTabuleiro();
                                output += String.format("O computador ganhou o jogo!\n");

                                output += menuInicial();
                                this.estadoAtual = Estado.MENU_INICIAL_APRESENTADO;
                            }
                            else if (resultado.equals(aval.servidor.Jogo.ResultadoJogada.JOGO_EMPATE)){
                                output += this.jogoAtual.imprimeTabuleiro();
                                output += String.format("Empatou com o computador!\n");

                                output += menuInicial();
                                this.estadoAtual = Estado.MENU_INICIAL_APRESENTADO;
                            }
                        }
                        else{
                            output += "Valor inserido nao e valido! Insira um numero inteiro entre 1 e a largura do tabuleiro: ";
                        }
                    }
                    else{
                        output += "Valor inserido nao e valido! Insira um numero inteiro entre 1 e a largura do tabuleiro: ";
                    }
                    break;
            }
        }
        
        return output;
    }
    public String mensagemBoasVindas(){
        String output = "Bem vindo ao servidor N-em-linha!\n";
        output += "Para terminar a sessao a qualquer momento, introduza a tecla 'x'.\n";
        return output;
    }

    public String menuInicial(){
        String output = "Escolha a opcao que pretende efetuar:\n\n";
        output += " 1) Comecar um novo jogo;\n";
        output += " 2) Ver tabela dos vencedores mais rapidos;\n\n";
        output += "Insira o numero da operacao a efetuar (1-2, ou 'x' para sair): ";
        return output;
    }

    public String listarVencedores(){
        if (this.tabelaVencedores.getResultados().size() > 0){
            String output = "\nLista de vencedores:\n";
            output += "Nome do jogador | Modo de jogo | Numero de jogadas | Pontuacao\n";
            
            for(JogoResultado resultado : this.tabelaVencedores.getResultados()){
                output += String.format("%-15s | %-12s | %-17d | %-9.0f \n", "'" + resultado.nomeJogador() + "'", resultado.jogoDificil() ? "Dificil" : "Facil", resultado.numeroJogadas(), resultado.pontuacaoTotal());
            }
            return output + "\n";
        }
        else{
            return "Ainda nao existem vencedores registados!\n";
        }
    }

    public String comecarNovoJogo(){
        this.jogoAtual = null;
        this.jogadorAtual = null;
        this.alturaTabuleiro = this.larguraTabuleiro = -1;
        this.modoDificil = false;

        return "Indique o seu nome de utilizador (use apenas letras e numeros): ";
    }
}
