package servidor;

import java.io.Serializable;

public class DadosJogo implements Serializable {
    private int alturaTabuleiro; // altura do tabuleiro atual
    private int larguraTabuleiro; // largura do tabuleiro atual
    private int valorObjetivo; // valor objetivo ('N') do jogo atual
    private int jogadaAtual; // numero de jogadas efetuadas pelo cliente
    private boolean modoDificil; // indica se o jogo encontra-se em modo dificil
    private int totalFichasUsadas; // total de fichas utilizadas, incluindo as do cliente e dos
                                   // npc's

    public DadosJogo(int alturaTabuleiro, int larguraTabuleiro, int valorObjetivo, int jogadaAtual,
            int numeroJogadores, int totalFichasUsadas) {
        this.alturaTabuleiro = alturaTabuleiro;
        this.larguraTabuleiro = larguraTabuleiro;
        this.valorObjetivo = valorObjetivo;
        this.jogadaAtual = jogadaAtual;
        this.modoDificil = numeroJogadores == 3 ? true : false;
        this.totalFichasUsadas = totalFichasUsadas;
    }

    public int getAlturaTabuleiro() {
        return this.alturaTabuleiro;
    }

    public int getLarguraTabuleiro() {
        return this.larguraTabuleiro;
    }

    public int getValorObjetivo() {
        return this.valorObjetivo;
    }

    public int getJogadaAtual() {
        return this.jogadaAtual;
    }

    public boolean getModoDificil() {
        return this.modoDificil;
    }

    public int getFichasUsadas() {
        return this.totalFichasUsadas;
    }

    public int getMaximoDeJogadas(){
        return this.alturaTabuleiro * this.larguraTabuleiro / (this.modoDificil ? 3 : 2);
    }
    // valor gerado aleatoriamente para a serializacao do objeto
    private static final long serialVersionUID = 804109928378938987L; 
}