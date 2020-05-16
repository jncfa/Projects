/**
 * Sistemas Informaticos - Avaliacao 2 (OOP)
 *
 * Ficheiro: Aviao.java
 * Data: 4/04/2019
*/

package aval.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que define a informacao de cada viagem.
 * 
 * @author jncfa, srpg
 */
public class Aviao implements Serializable{    
    private String codigoAviao;
    private List<Segmento> segmentos;

    public Aviao(String codigoAviao){
        this.codigoAviao = codigoAviao;
        this.segmentos = null;
    }
    
    public String getCodigo(){
        return this.codigoAviao;
    }

    public void setCodigo(String codigoAviao){
        this.codigoAviao = codigoAviao;
    }
    
    public List<Segmento> getSegmentos(){
        return this.segmentos;
    }
    
    public boolean associarSegmento(Segmento segmento){
        if (this.segmentos == null){
            this.segmentos = new ArrayList<>();
        }
        return this.segmentos.add(segmento);
    }

    public boolean desassociarSegmento(Segmento segmento){
        return this.segmentos.remove(segmento);
    }

    public int getUsoAviao(){
        if (this.segmentos != null){
            return this.segmentos.size();
        }
        return 0;
    }

    public boolean aviaoUsado() {
        return this.segmentos != null;
    }
    
    @Override
    public String toString(){
        String str = String.format("'%s'", this.codigoAviao);

        if (this.getUsoAviao() > 0){
            str += " (Segmentos atuais: ";
            for(int idx = 0; idx < this.segmentos.size(); idx++){
                str += this.segmentos.get(idx).toString(false);
    
                if(idx != this.segmentos.size() - 1){
                    str += ", ";
                }
                else{
                    str += ")";
                }
            }
        }
        

        return str;
    }

    private static final long serialVersionUID = 9204990050316309309L; // valor gerado aleatoriamente para a serializacao do objeto
}