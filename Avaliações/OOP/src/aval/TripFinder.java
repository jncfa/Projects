package aval;

import java.util.ArrayList;
import java.util.List;
import aval.database.Segmento;

/**
 * Classe que lista todos os percursos possiveis para uma determinada cidade inicial e cidade final.
 * 
 * @author jncfa, srpg
 */
public class TripFinder{

    private List<Segmento> segmentos;
    private List<List<Segmento>> conjuntosPossiveis;
    
    /**
     * Algoritmo recursivo que atravessa a rede de segmentos (primeiro em profundidade, 'depth first'), listando todos os percursos possiveis na lista {@code conjuntosPossiveis}.
     *
     * @param cidadeInicial Cidade inicial do percurso;
     * @param cidadeFinal Cidade final do percurso;
     */
    private void pesquisaCaminhos(String cidadeInicial, String cidadeFinal){
        pesquisaCaminhos(cidadeInicial, cidadeFinal, new ArrayList<>());
    }
    /**
     * Algoritmo recursivo que atravessa a rede de segmentos (primeiro em profundidade, 'depth first'), listando todas as possibilidades na lista {@code conjuntosPossiveis}.
     * Usa o conjunto de segmentos {@code caminhoPercorrido} para indicar todos os segmentos percorridos ate chegar ao vertice atual;
     * 
     * @param cidadeAtual Cidade atual;
     * @param cidadeFinal Cidade a alcancar;
     * @param caminhoPercorrido Conjunto de segmentos percorridos;
     */
    private void pesquisaCaminhos(String cidadeAtual, String cidadeFinal, List<Segmento> caminhoPercorrido){
        if (!cidadeAtual.equals(cidadeFinal)){ // verificar se ja atingimos a cidade objetivo
            for (Segmento segmentoPossivel : segmentos) {
                if(segmentoPossivel.getOrigem().equals(cidadeAtual)){
                    boolean cidadePercorrida = false;
    
                    for (Segmento segmentoPercorrido : caminhoPercorrido) {
                        if (segmentoPercorrido.getOrigem().equalsIgnoreCase(segmentoPossivel.getDestino())){ // verificar se ja passamos pela cidade
                            cidadePercorrida = true;
                            break;
                        }
                    }
                    
                    if (!cidadePercorrida){ // nao percorremos 
                        List<Segmento> novoCaminho = new ArrayList<>(caminhoPercorrido); // criamos uma copia da lista para impedir colisoes

                        novoCaminho.add(segmentoPossivel); // acrescentamos o segmento ao nosso caminho percorrido, e tentamos
                        pesquisaCaminhos(segmentoPossivel.getDestino(), cidadeFinal, novoCaminho); // procurar novos caminhos a partir da proxima cidade
                    }
                }
            }
        }
        else{
            conjuntosPossiveis.add(new ArrayList<>(caminhoPercorrido)); // adicionamos o percurso a lista de todos os percursos
        }
    }

    public TripFinder(DatabaseManager db, String cidadeOrigem, String cidadeDestino){
        segmentos = db.getSegmentos();
        conjuntosPossiveis = new ArrayList<>();

        pesquisaCaminhos(cidadeOrigem, cidadeDestino); // pesquisa todos os caminhos possiveis
    
        boolean sorted = false;
        while(!sorted){ // bubble sort
            sorted = true;
            for(int idx_1 = 0; idx_1 < conjuntosPossiveis.size() - 1; idx_1++){
                if (conjuntosPossiveis.get(idx_1).size() > conjuntosPossiveis.get(idx_1 + 1).size()){
                    sorted = false;
                    List<Segmento> listSeg1 = conjuntosPossiveis.get(idx_1);

                    conjuntosPossiveis.set(idx_1, conjuntosPossiveis.get(idx_1 + 1)); // troca a ordem dos elementos
                    conjuntosPossiveis.set(idx_1 + 1, listSeg1);
                }
            }
        }
    } 

    public List<List<Segmento>> getPercursosPossiveis(){
        return conjuntosPossiveis;
    }
}