/**
 * Sistemas Informaticos - Avaliacao 2 (OOP)
 *
 * Ficheiro: Viagem.java
 * Data: 4/04/2019
*/

package aval.database;

import java.io.Serializable;
import java.util.List;

/**
 * Classe que define a informacao de cada viagem.
 * 
 * @author jncfa, srpg
 */
public class Viagem implements Serializable{
    private List<Segmento> segmentos;
    private float custo;
    
    public Viagem(List<Segmento> segmentos, float custo){
        this.segmentos = segmentos;
        this.custo = custo;
    }

    /**
     * Devolve o conjunto de segmentos que constituem a viagem indicada.
     */
    public List<Segmento> getSegmentos(){
       return this.segmentos; 
    }

    /**
     * Modifica o conjunto de segmentos que compoem a viagem.
     * @param segmentos - Novo conjunto de segmentos a associar a esta viagem;
     */
    public void setSegmentos(List<Segmento> segmentos){
        this.segmentos = segmentos;
    }

    /**
     * Devolve o custo da viagem.
     */
    public float getCusto(){
        return this.custo;
    }
    
    /**
     * Modifica o preco da viagem indicada.
     * @param custo - Novo preco da viagem;
     */
    public OperationResult setCusto(float custo){
        this.custo = custo;
        return new OperationResult();
    }

    /**
     * Indica a cidade onde ira comecar a viagem indicada.
     */
    public String getCidadeOrigem(){
        return this.segmentos.get(0).getOrigem();
    }
    
    /**
     * Indica a cidade onde ira acabar a viagem indicada.
     */
    public String getCidadeDestino(){
        return this.segmentos.get(this.segmentos.size() - 1).getDestino();
    }

    @Override
    public String toString(){
        return this.toString(false);
    }

    public String toString(boolean imprimirDetalhes){
        /**
         * Formato:
         * Percurso: a -> b (via aviao 'blabla'), b -> c (via aviao 'blabla'), 
         * Custo: 10 euros
         */
        if (imprimirDetalhes){
            String str = "Percurso: ";
            for (int idx = 0; idx < this.segmentos.size(); idx++) {
                str += this.segmentos.get(idx).toString();
    
                if (idx < this.segmentos.size() - 1){
                    str += ", ";
                }
                else{
                    str += ";\n";
                }
            }
            str += "Custo: " + this.custo + " euros;";
            return str;
        }

        /**
         * Formato:
         * Viagem: a -> b (100€)
         */
        else{
            return String.format("Viagem: %s -> %s (%.2f euros)", this.getCidadeOrigem(), this.getCidadeDestino(), this.custo);
        }
    }
    private static final long serialVersionUID = 4457756668036401522L; // valor gerado aleatoriamente para a serializacao do objeto
}