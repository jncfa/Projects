package servidor;

import java.io.Serializable;

/**
 * Guarda a informacao de uma vitoria obtida por um jogador.
 * 
 * @author jncfa, srpg
 */
public class Resultado implements Serializable {
    private String nomeJogador;
    private int numeroJogadas;
    private int numeroJogadasPossiveis;
    private boolean jogoDificil;

    public Resultado(String nomeJogador, int numeroJogadas, int numeroJogadasPossiveis, boolean jogoDificil) {  
        this.nomeJogador = nomeJogador;
        this.numeroJogadasPossiveis = numeroJogadasPossiveis;
        this.numeroJogadas = numeroJogadas;
        this.jogoDificil = jogoDificil;
    }

    /**
     * Devolve o nome do jogador que efetuou este jogo.
     */
    public String nomeJogador() {
        return this.nomeJogador;
    }

    /**
     * Devolve o numero de jogadas efetuadas neste jogo.
     */
    public int numeroJogadas() {
        return this.numeroJogadas;
    }

    /**
     * Indica a pontuacao obtida por este jogador.
     */
    public float pontuacaoTotal() {
        return (this.numeroJogadasPossiveis + 1 - this.numeroJogadas) * (this.jogoDificil ? 2 : 1);
    }

    /**
     * Indica se o jogo efetuado foi no modo dificil.
     */
    public boolean jogoDificil() {
        return this.jogoDificil;
    }

    // valor gerado aleatoriamente para a deserializacao do objeto
    private static final long serialVersionUID = -4092821419704206084L;
}