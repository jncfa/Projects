package servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import cliente.IControlo;
import servidor.Jogo.ResultadoJogada;

/**
 * Objeto remoto usado pelo cliente para determinar a comunicacao.
 */
public class Controlo extends UnicastRemoteObject implements IControlo {
    private String nomeUtilizador;
    private Jogo jogoAtual;
    private GestorDados gestorDados;

    public Controlo(GestorDados gestorDados) throws RemoteException {
        this.jogoAtual = null;
        this.gestorDados = gestorDados;
    }

    /** {@inheritDoc} */
    @Override
    public void iniciarSessao(String nomeUtilizador) throws RemoteException {
        if (this.nomeUtilizador != null){
            // sessao anterior terminou inesperadamente
            // como ainda nao foi guardada a base de dados, temos de faze-lo agora
            this.gestorDados.guardarDados();
        }
        this.nomeUtilizador = nomeUtilizador;
        this.jogoAtual = null;
    }

    /** {@inheritDoc} */
    @Override
    public void terminarSessao() throws RemoteException {
        this.gestorDados.guardarDados(); // ao terminar a sessao do utilizador, os dados sao guardados no disco
        this.nomeUtilizador = null;
        this.jogoAtual = null;
    }

    /** {@inheritDoc} */
    @Override
    public DadosJogo iniciarJogo(int id) throws RemoteException {
        List<Jogo> jogosEmAberto = this.gestorDados.jogosEmAberto().get(this.nomeUtilizador);

        if (jogosEmAberto != null){
            this.jogoAtual = jogosEmAberto.get(id);
            jogosEmAberto.remove(id); // removemos o jogo da lista

            if (jogosEmAberto.size() == 0){ // remove a entrada na hashtable se estiver vazia
                this.gestorDados.jogosEmAberto().remove(this.nomeUtilizador);
            }
        }
        return this.jogoAtual.dadosJogo();
    }

    /** {@inheritDoc} */
    @Override
    public DadosJogo iniciarJogo(int alturaTabuleiro, int larguraTabuleiro, int valorObjetivo,
            boolean modoDificil) throws RemoteException {
        this.jogoAtual = new Jogo(alturaTabuleiro, larguraTabuleiro, valorObjetivo, modoDificil);
        return this.jogoAtual.dadosJogo();
    }
    
    /** {@inheritDoc} */
    @Override
    public ResultadoJogada efetuarJogada(int valorInserido) throws RemoteException {
        ResultadoJogada jogada = this.jogoAtual.efetuarJogada(valorInserido);
           
        if (jogada.equals(ResultadoJogada.JOGADOR_VENCEU)){
            this.gestorDados.adicionarResultado(this.nomeUtilizador, this.jogoAtual.dadosJogo());
        }

        return jogada;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean desfazerUltimaJogada() throws RemoteException {
        return this.jogoAtual.desfazerUltimaJogada();
    }

    /** {@inheritDoc} */
    @Override
    public boolean guardarJogoAtual(int idMaisAntigo) throws RemoteException {
        if(this.gestorDados.adicionarJogoPorAcabar(this.nomeUtilizador, this.jogoAtual, idMaisAntigo)){
            this.jogoAtual = null;
            return true;
        }
        else{
            return false;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void descartarJogoAtual() throws RemoteException {
        this.jogoAtual = null; // descarta o jogo atual
    }
    
    /** {@inheritDoc} */
    @Override
    public List<DadosJogo> jogosEmAberto() throws RemoteException {
        List<Jogo> listaJogos = this.gestorDados.jogosEmAberto().get(this.nomeUtilizador);
        if (listaJogos != null) {
            List<DadosJogo> jogosEmAberto = new ArrayList<>();
            for (Jogo jogo : this.gestorDados.jogosEmAberto().get(nomeUtilizador)) {
                jogosEmAberto.add(jogo.dadosJogo());
            }

            // devolve a lista de jogos (sem a informacao do tabuleiro)
            return jogosEmAberto;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public List<Resultado> listaDeVencedores() throws RemoteException {
        return this.gestorDados.listaDeVencedores();
    }

        
    /** {@inheritDoc} */
    @Override
    public String imprimirTabuleiro() throws RemoteException {
        return this.jogoAtual.imprimirTabuleiro();
    }

    // valor gerado aleatoriamente para a serializacao do objeto
    private static final long serialVersionUID = -256188378006575673L;
}