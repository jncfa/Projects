

package aval.servidor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Leaderboard implements Serializable{

    private List<JogoResultado> resultados;

    public Leaderboard(){
        this.resultados = new ArrayList<>();
    }
    
    /**
     * Adiciona um novo resultado a lista de vencedores mais rapidos.
     * 
     * @param nomeJogador Nome do jogador que obteve o resultado.
     * @param numeroJogadas Numero de jogadas efetuadas pelo jogador.
     * @param jogoDificil Dificultado do jogo efetuado.
     * @return {@code true} caso tenha sido colocado na lista
     */
    public boolean adicionarResultado(String nomeJogador, int numeroJogadas, boolean jogoDificil){
        JogoResultado novoResultado = new JogoResultado(nomeJogador, numeroJogadas, jogoDificil);
        boolean jogoAdicionado = false;
        
        for(int idx = 1; idx < resultados.size(); idx++){
            if (resultados.get(idx - 1).pontuacaoTotal() < novoResultado.pontuacaoTotal() && novoResultado.pontuacaoTotal() <= resultados.get(idx).pontuacaoTotal()){
                resultados.add(idx, novoResultado);
                jogoAdicionado = true;
                break;
            }
        }
        
        if (jogoAdicionado){
            if (resultados.size() == 6){ // a lista deve ter no maximo 6 elementos
                resultados.remove(resultados.size());
            }
        }
        else{
            if (resultados.size() == 1){
                if (resultados.get(0).pontuacaoTotal() >= novoResultado.pontuacaoTotal()){
                    resultados.add(0, novoResultado);
                }
                else{
                    resultados.add(0, novoResultado);
                }
            }
            else if (resultados.size() < 5){
                resultados.add(novoResultado);
                jogoAdicionado = true;
                
            }
        }

        return jogoAdicionado;
    }

    /**
     * Devolve a lista de todos os vencedores mais rapidos, ordenada pela sua pontuacao.
     */
    public List<JogoResultado> getResultados() {
        return resultados;
    }
    
    private static final long serialVersionUID = -6439736927886378693L; // valor gerado aleatoriamente para a deserializacao do objeto
}