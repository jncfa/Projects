package cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import servidor.DadosJogo;
import servidor.Resultado;
import servidor.Jogo.ResultadoJogada;

/**
 * Interface usada para determinar as diferentes funcoes que pretendemos invocar remotamente.
 * 
 * @author jncfa, srpg
 */
public interface IControlo extends Remote {

    /**
     * Indica ao servidor que o cliente pretende iniciar sessao.
     * 
     * @param nomeUtilizador Nome do utilizador
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public void iniciarSessao(String nomeUtilizador) throws RemoteException;
  
    /**
     * Indica ao servidor que o cliente pretende terminar sessao
     * 
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public void terminarSessao() throws RemoteException;

    /**
     * Indica ao servidor que o cliente pretende recomecar um jogo previamente aberto, com o id
     * previamente definido.
     * 
     * @param id Identificador do jogo a iniciar
     * @return Informacao sobre o jogo iniciado
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public DadosJogo iniciarJogo(int id) throws RemoteException;


    /**
     * Indica ao servidor que o cliente pretende iniciar um novo jogo com estas propriedades.
     * 
     * @param alturaTabuleiro  Altura do tabuleiro
     * @param larguraTabuleiro Largura do tabuleiro
     * @param valorObjetivo    Valor objetivo para vencer o jogo
     * @param modoDificil      Modo dificil (indica se o jogador joga contra 1 ou 2 bots controlados
     *                         pelo computador)
     * @return Informacao sobre o jogo iniciado
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public DadosJogo iniciarJogo(int alturaTabuleiro, int larguraTabuleiro, int valorObjetivo,
            boolean modoDificil) throws RemoteException;

    /**
     * Tenta colocar a ficha na tabela, e devolve um resultado que reflete o estado atual do
     * tabuleiro.
     * 
     * @param valorInserido A coluna onde o jogador pretende colocar a ficha
     * @return Codigo que indica o estado da jogada, ver {@link ResultadoJogada}
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public ResultadoJogada efetuarJogada(int valorColuna) throws RemoteException;

    /**
     * Tenta desfazer a ultima jogada efetuada, chegando a um limite de 3 undo's.
     * 
     * @return {@code true} se foi possivel desfazer a jogada, senao devolve {@code false}
     * @throws RemoteException
     */
    public boolean desfazerUltimaJogada() throws RemoteException;

    /**
     * Indica ao servidor para guardar o jogo atual. No caso de ser ultrapassado o limite de jogos guardados,
     * e necessario indicar ao servidor qual o jogo a apagar para ser guardado o jogo atual.
     * 
     * @param idParaApagar Indica o indice do jogo a apagar no caso de ser ultrapassado o limite de memoria
     * @return {@code true} se o jogo foi guardado, {@code false} caso nao seja possivel guardar na lista
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public boolean guardarJogoAtual(int idParaApagar) throws RemoteException;

    /**
     * Indica ao servidor para descartar o jogo atual.
     * 
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public void descartarJogoAtual() throws RemoteException;

    /**
     * Indica se o utilizador tem jogos em aberto, e devolve a lista deles caso tenha.
     * 
     * @return Lista de jogos em aberto
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public List<DadosJogo> jogosEmAberto() throws RemoteException;

    /**
     * Devolve uma lista com os melhores resultados obtidos no jogo, ordenada de forma descendente
     * por pontuacao.
     * 
     * @return Lista com melhores resultados obtidos no jogo 'N-em-Linha'
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public List<Resultado> listaDeVencedores() throws RemoteException;

    /**
     * Devolve uma String que contem o tabuleiro formatado.
     * 
     * @return O tabuleiro de jogo em formato de texto
     * @throws RemoteException Caso exista uma falha de comunicacao entre o cliente e o servidor
     */
    public String imprimirTabuleiro() throws RemoteException;
}
