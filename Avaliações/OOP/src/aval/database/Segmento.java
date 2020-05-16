/**
 * Sistemas Informaticos - Avaliacao 2 (OOP)
 *
 * Ficheiro: Segmento.java
 * Data: 4/04/2019
*/

package aval.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que define cada segmento de uma viagem.
 * 
 * @author jncfa, srpg
 */
public class Segmento implements Serializable{

    private List<Aviao> historicoAvioes;
    private String cidadeOrigem;
    private String cidadeDestino;
    
    public Segmento(Aviao aviao, String cidadeOrigem, String cidadeDestino){
        this.historicoAvioes = new ArrayList<>();
        this.historicoAvioes.add(aviao); // adiciona aviao ao historico
        aviao.associarSegmento(this); // associa o segmento ao aviao
        
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
    }

    /**
     * Altera o aviao que atualmente percorre o segmento de viagem indicado.
     * @param novoAviao Aviao a substituir o atual aviao.
     * @return 
     */
    public OperationResult modificarAviao(Aviao novoAviao){
        if (this.getAviaoAtual().equals(novoAviao)){
            return new OperationResult(false, "O aviao que pretende inserir ja se encontra associado a este segmento.");
        }
        else {
            this.getAviaoAtual().desassociarSegmento(this);
            this.historicoAvioes.add(novoAviao);
            novoAviao.associarSegmento(this);
            return new OperationResult();
        }
    }

    /**
     * Indica a cidade onde comeca o segmento indicado.
     */
    public String getOrigem(){
        return this.cidadeOrigem;
    }


    /**
     * Indica a cidade onde termina o segmento indicado.
     */
    public String getDestino(){
        return this.cidadeDestino;
    }


    /**
     * Indica o aviao atualmente utilizado para percorrer o segmento indicado.
     */
    public Aviao getAviaoAtual(){
        return this.historicoAvioes.get(historicoAvioes.size() - 1);
    }


    /**
     * Indica o conjunto de todos os avioes que percorreram e ou que ainda percorrem o segmento indicado.
     */
    public List<Aviao> getHistorico(){
        return this.historicoAvioes;
    }

    @Override
    public String toString() {
        return this.toString(true);
    }

    public String toString(boolean printAviao) {
        if (printAviao){
            return String.format("%s -> %s (via aviao '%s')", this.cidadeOrigem, this.cidadeDestino, this.getAviaoAtual().getCodigo());
        }
        else{
            return String.format("%s -> %s", this.cidadeOrigem, this.cidadeDestino);
        }
    }
    private static final long serialVersionUID = -5771357557665537440L;  // valor gerado aleatoriamente para a serializacao do objeto
}