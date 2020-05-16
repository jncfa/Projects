package cliente;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import servidor.Jogo.ResultadoJogada;
import servidor.DadosJogo;
import servidor.Resultado;

public class LigacaoRMI {
    private IControlo objetoRemoto; 
    private boolean terminarSessao; 

    public LigacaoRMI() {
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                this.objetoRemoto = (IControlo) LocateRegistry.getRegistry("localhost", 2000).lookup("controlo");
                this.terminarSessao = false;
                invocacaoBemSucedida = true;
                return;
            } 
            catch (NotBoundException e){ // erro no registo
                System.out.println("Ocorreu um erro:");
                e.printStackTrace();
            }
            catch (RemoteException e) { // erro de comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){ // excesso de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.objetoRemoto = null;
            this.terminarSessao = true;
        }
    }

    /**
     * Indica ao servidor que o cliente pretende iniciar sessao.
     * 
     * @param nomeUtilizador Nome do utilizador
     */
    public void iniciarSessao(String nomeUtilizador) {
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                objetoRemoto.iniciarSessao(nomeUtilizador); 
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){ // excesso de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
    }

    /**
     * Forca o cliente a cessar comunicacoes com o servidor. 
     */
    public void terminarSessao() {
        // a comunicacao sera terminada, independemente da invocacao remota falhar ou nao
        this.terminarSessao = true;    
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                objetoRemoto.terminarSessao();
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){ // excesso de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
        }
    }

    /**
     * Indica ao servidor que o cliente pretende recomecar um jogo previamente aberto, com o id
     * previamente definido.
     * 
     * @param id Identificador do jogo a iniciar
     * @return Informacao sobre o jogo iniciado
     */
    public DadosJogo iniciarJogo(int id){
        DadosJogo dados = null;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                dados = objetoRemoto.iniciarJogo(id);
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){
            // ultrapassou o numero de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
        
        return dados; // valor do servidor, ou 'null' em caso de falha
    }

    /**
     * Indica ao servidor que o cliente pretende iniciar um novo jogo com estas propriedades.
     * 
     * @param alturaTabuleiro  Altura do tabuleiro
     * @param larguraTabuleiro Largura do tabuleiro
     * @param valorObjetivo    Valor objetivo para vencer o jogo
     * @param modoDificil      Modo dificil (indica se o jogador joga contra 1 ou 2 bots controlados
     *                         pelo computador)
     * @return Informacao sobre o jogo iniciado
     */
    public DadosJogo iniciarJogo(int alturaTabuleiro, int larguraTabuleiro, int valorObjetivo, boolean modoDificil){
        DadosJogo dados = null;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                dados = objetoRemoto.iniciarJogo(alturaTabuleiro, larguraTabuleiro,  valorObjetivo, modoDificil);
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){
            // ultrapassou o numero de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
        
        return dados; // valor do servidor, ou 'null' em caso de falha
    }

    /**
     * Tenta colocar a ficha na tabela, e devolve um resultado que reflete o estado atual do
     * tabuleiro.
     * 
     * @param valorInserido A coluna onde o jogador pretende colocar a ficha
     * @return Codigo que indica o estado da jogada, ver {@link ResultadoJogada}
     */
    public ResultadoJogada efetuarJogada(int valorColuna) {
        ResultadoJogada resultado = null;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                resultado = objetoRemoto.efetuarJogada(valorColuna);
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){
            // ultrapassou o numero de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
        
        return resultado; // valor do servidor, ou 'null' em caso de falha
    }
   
    /**
     * Tenta desfazer a ultima jogada efetuada, chegando a um limite de 3 undo's.
     * 
     * @return {@code true} se foi possivel desfazer a jogada, senao devolve {@code false}
     * @throws RemoteException
     */
    public boolean desfazerUltimaJogada(){
        boolean resultado = false;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                resultado = objetoRemoto.desfazerUltimaJogada();
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){ // excesso de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }

        return resultado;
    }
    
    /**
     * Indica ao servidor para guardar o jogo atual. No caso de ser ultrapassado o limite de jogos guardados,
     * e necessario indicar ao servidor qual o jogo a apagar para ser guardado o jogo atual.
     * 
     * @return {@code true} se o jogo foi guardado, {@code false} caso nao seja possivel guardar na lista
     */
    public boolean guardarJogoAtual(){
        boolean resultado = false;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                resultado = objetoRemoto.guardarJogoAtual(-1); 
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){ // excesso de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }

        return resultado;
    }

    /**
     * Indica ao servidor para guardar o jogo atual. No caso de ser ultrapassado o limite de jogos guardados,
     * e necessario indicar ao servidor qual o jogo a apagar para ser guardado o jogo atual.
     * 
     * @param idParaApagar Indica o indice do jogo a apagar no caso de ser ultrapassado o limite de memoria
     * @return {@code true} se o jogo foi guardado, {@code false} caso nao seja possivel guardar na lista
     */
    public void guardarJogoAtual(int idParaApagar){
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                objetoRemoto.guardarJogoAtual(idParaApagar); 
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){ // excesso de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
    }
    
    /**
     * Indica ao servidor para descartar o jogo atual.
     * 
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public void descartarJogoAtual() {
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                objetoRemoto.descartarJogoAtual(); 
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(tempoEspera);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){ // excesso de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
    }

    /**
     * Indica se o utilizador tem jogos em aberto, e devolve a lista deles caso tenha.
     * 
     * @return Lista de jogos em aberto
     */
    public List<DadosJogo> jogosEmAberto() {
        List<DadosJogo> jogosEmAberto = null;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                jogosEmAberto = objetoRemoto.jogosEmAberto();
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){
            // ultrapassou o numero de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
        
        return jogosEmAberto; // valor do servidor, ou 'null' em caso de falha
    }

    /**
     * Devolve uma lista com os melhores resultados obtidos no jogo, ordenada de forma descendente
     * por pontuacao.
     * 
     * @return Lista com melhores resultados obtidos no jogo 'N-em-Linha'
     */
    public List<Resultado> listaDeVencedores() {
        List<Resultado> vencedores = null;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas & !invocacaoBemSucedida; tentativas++) {
            try {
                vencedores = objetoRemoto.listaDeVencedores();
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){
            // ultrapassou o numero de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }
        return vencedores; // valor do servidor, ou 'null' em caso de falha
    }

    /**
     * Devolve uma String que contem o tabuleiro formatado.
     * 
     * @return O tabuleiro de jogo em formato de texto
     */
    public String imprimirTabuleiro() {
        String tabuleiro = null;
        boolean invocacaoBemSucedida = false;
        for (int tentativas = 0; tentativas < maximoTentativas && !invocacaoBemSucedida; tentativas++) {
            try {
                tabuleiro = objetoRemoto.imprimirTabuleiro();
                invocacaoBemSucedida = true;
            } catch (RemoteException e) { // erro na comunicacao
                try {
                    tentativas++; // incrementa o numero de tentativas
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (!invocacaoBemSucedida){
            // ultrapassou o numero de tentativas
            System.out.println("Erro de comunicacao com o servidor, nao e possivel prosseguir.");
            this.terminarSessao = true;
        }

        return tabuleiro; // valor do servidor, ou 'null' em caso de falha
    }

    /**
     * Indica se o cliente se encontra atualmente ligado ao servidor.
     */
    public boolean sessaoAtiva() {
        return !this.terminarSessao;
    }

    /** Indica o numero maximo de tentativas antes de terminar a ligacao. */
    private static final int maximoTentativas = 10;

     /** Indica que o tempo de espera ate voltar a tentar estabelecer contacto. */
    private static final int tempoEspera = 1000;
}