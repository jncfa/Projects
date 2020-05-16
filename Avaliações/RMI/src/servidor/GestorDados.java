package servidor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Classe que gere a serializacao e deserializacao dos dados (lista de utilizadores e de viagens) a
 * serem guardados no disco.
 * 
 * @author jncfa, srpg
 */
public class GestorDados implements java.io.Serializable {
    private Hashtable<String, List<Jogo>> jogosEmAberto;
    private List<Resultado> listaDeVencedores;

    public GestorDados() {
        // lemos os dados gravados no sistema, e caso nao existam, criamos uma base de sdados nova
        carregarDados(); 
    }

    /**
     * Carrega os dados que estejam guardados no disco e deserializa para as listas de jogos em aberto e concluidos. 
     * Em caso de falha a base de dados e apagada, e e criada uma nova.
     */
    public void carregarDados() {
        try (FileInputStream fis = new FileInputStream(CAMINHO_BASE_DE_DADOS)) { // abre o ficheiro
            ObjectInputStream ois = new ObjectInputStream(fis); // 'handler' que permite
                                                                // deserializar os dados

            GestorDados db = (GestorDados) ois.readObject(); // le o ficheiro e deserializa a base
                                                             // de dados

            this.jogosEmAberto = db.jogosEmAberto; // atualiza as referencias de todas as listas de
                                                   // objetos
            this.listaDeVencedores = db.listaDeVencedores;

            ois.close(); // encerra a stream para prevenir vazamentos de memoria

        } catch (ClassCastException | ClassNotFoundException | IOException e) { 
            // ficheiro nao encontrado ou dados corrompidos                

            this.jogosEmAberto = new Hashtable<>(); // inicializa lista de jogos em aberto
            this.listaDeVencedores = new ArrayList<>(); // incializa a lista de vencedores

            System.out.println("Nao foram encontrados dados guardados: foi inicializada uma nova base de dados!");
            guardarDados(); // escreve dados atuais no disco
        }
    }

    /**
     * Serializa os dados e escreve-os no disco para uso numa sessao posterior.
     */
    public void guardarDados() {
        // abre o ficheiro (cria um novo caso nao exista)
        try (FileOutputStream fos = new FileOutputStream(CAMINHO_BASE_DE_DADOS)) { 
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this); // escreve a base de dados no disco
            oos.close(); // encerra a stream para evitar  vazamentos de memoria
        } catch (IOException e) { // erro na serializacao do objeto
            e.printStackTrace();
        }
    }

    /**
     * Adiciona um novo resultado a lista de vencedores mais rapidos. Descarta os dados atuais caso 
     * 
     * @param nomeJogador   Nome do jogador que obteve o resultado.
     * @param numeroJogadas Numero de jogadas efetuadas pelo jogador.
     * @param jogoDificil   Dificultado do jogo efetuado.
     */
    public void adicionarResultado(String nomeJogador, DadosJogo jogo) {
        Resultado novoResultado = new Resultado(nomeJogador, jogo.getJogadaAtual(), jogo.getMaximoDeJogadas(), jogo.getModoDificil());
        boolean jogoAdicionado = false;

        for (int idx = 1; idx < this.listaDeVencedores.size() && !jogoAdicionado; idx++) {
            if (this.listaDeVencedores.get(idx - 1).pontuacaoTotal() >= novoResultado.pontuacaoTotal()) {
                if (novoResultado.pontuacaoTotal() <= this.listaDeVencedores.get(idx).pontuacaoTotal()){
                    this.listaDeVencedores.add(idx, novoResultado);
                    jogoAdicionado = true;
                }
            }
        }

        if (jogoAdicionado) {
            if (this.listaDeVencedores.size() == MAXIMO_RESULTADOS + 1) { // lista ultrapassa o valor maximo definido
                // a lista esta ordenada por pontuacao, o ultimo elemento e removido
                this.listaDeVencedores.remove(this.listaDeVencedores.size());
            }
        } else {
            if (this.listaDeVencedores.size() == 1) {
                jogoAdicionado = true;
                if (this.listaDeVencedores.get(0).pontuacaoTotal() < novoResultado.pontuacaoTotal()) {
                    this.listaDeVencedores.add(0, novoResultado);
                } else {
                    this.listaDeVencedores.add(novoResultado);
                }
            } else if (this.listaDeVencedores.size() < MAXIMO_RESULTADOS) {
                // a lista ainda tem espaco para adicionar novos elementos, adicionamos para o final da lista
                this.listaDeVencedores.add(novoResultado);
            }
        }
    }

    /**
     * Adiciona um jogo a lista de jogos em aberto de um determinado utilizador. Caso seja atingido o limite 
     * de memoria, sera entao apagado o jogo indicado e inserido o novo jogo.
     * 
     * @param nomeUtilizador Nome do utilizador que efetuou o jogo indicado
     * @param jogoAtual Jogo a guardar
     * @param idParaApagar Id do jogo a apagar, caso seja atingido o limite de memoria
     * @return {@code true} se foi possivel adicionar o jogo a lista, {@code false} caso nao seja possivel.
     */
    public boolean adicionarJogoPorAcabar(String nomeUtilizador, Jogo jogoAtual, int idParaApagar) {
        List<Jogo> jogosEmAberto = this.jogosEmAberto().get(nomeUtilizador);

        // jogador nao tem jogos em aberto
        if (jogosEmAberto == null){
            this.jogosEmAberto().put(nomeUtilizador, jogosEmAberto = new ArrayList<>());
        }
        else if (jogosEmAberto.size() >= MAXIMO_JOGOS_EM_ABERTO){ // limite de jogos excedido
            if (idParaApagar >= 0 && idParaApagar < jogosEmAberto.size()){
                jogosEmAberto.remove(idParaApagar); // remove um jogo para adicionar este ultimo
            }
            else{
                return false; // nao e possivel remover o elemento indicado
            }
        }

        jogosEmAberto.add(0, jogoAtual); // o primeiro elemento 
        return true;
    }

    /**
     * Devolve a tabela que associa a cada utilizador os seus respetivos jogos em aberto.
     */
    public Hashtable<String, List<Jogo>> jogosEmAberto() {
        return this.jogosEmAberto;
    }

    /**
     * Devolve a lista de vencedores mais rapidos.
     */
    public List<Resultado> listaDeVencedores() {
        return this.listaDeVencedores;
    }

    // valores maximos para guardar nas tabelas
    private static final int MAXIMO_JOGOS_EM_ABERTO = 5;
    private static final int MAXIMO_RESULTADOS = 5;

    // caminho para a base de dados
    private static final String CAMINHO_BASE_DE_DADOS = "database.db";

    // valor gerado aleatoriamente para deserializacao do objeto
    private static final long serialVersionUID = -5381974117293476411L;
}