package aval.servidor;

import java.io.Serializable;

class JogoResultado implements Serializable {
    private String nomeJogador;
    private int numeroJogadas;
    private boolean jogoDificil;

    public JogoResultado(String nomeJogador, int numeroJogadas, boolean jogoDificil) {
        this.nomeJogador = nomeJogador;
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
    public float pontuacaoTotal(){
        if (this.jogoDificil){
            return this.numeroJogadas / 2.0f;
        }
        else{
            return this.numeroJogadas;
        }
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
