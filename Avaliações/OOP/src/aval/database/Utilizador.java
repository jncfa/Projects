/**
 * Sistemas Informaticos - Avaliacao 2 (OOP)
 *
 * Ficheiro: Utilizador.java
 * Data: 4/04/2019
*/

package aval.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que define os utilizadores do sistema (clientes e administradores).
 * 
 * @author jncfa, srpg
 */
public class Utilizador implements Serializable {
    private String username, password;
    private float saldoAtual;
    private List<Viagem> viagensCompradas;
    private boolean hasAdministrativePriviledges;

    public Utilizador(String username, String password) {
        this.username = username;
        this.password = password;
        this.hasAdministrativePriviledges = false;
        this.viagensCompradas = new ArrayList<>();
        this.saldoAtual = 1000;
    }

    public Utilizador(String username, String password, boolean hasAdministrativePriviledges) {
        this.username = username;
        this.password = password;
        this.hasAdministrativePriviledges = hasAdministrativePriviledges;

        this.viagensCompradas = new ArrayList<>();
        this.saldoAtual = 0;
    }

    /**
     * Verifica se as credenciais indicadas sao iguais a do utilizador indicado.
     * @param username Nome de utilizador;
     * @param password Palavra-passe do utilizador;
     * @return {@code true} se as credenciais forem iguais, senao devolve {@code false}.
     */
    public boolean matchCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Devolve o nome de utilizador. 
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Devolve o saldo atual do utilizador.
     */
    public float getSaldoAtual(){
        return this.saldoAtual;
    }

    /**
     * Indica que viagens foram adquiridas pelo cliente.
     */
    public List<Viagem> getViagensCompradas(){
        return viagensCompradas;
    }

    /**
     * Tenta adquirir a viagem indicada, caso o custo da viagem nao ultrapasse o saldo do cliente.
     * @param viagem Viagem a adquirir;
     * @return {@code true} caso o cliente tenha saldo suficiente para adquirir a viagem.
     */
    public boolean comprarViagem(Viagem viagem){
        if(viagem.getCusto() <= this.saldoAtual){ 
            this.saldoAtual -= viagem.getCusto();

            if (viagensCompradas == null){
               viagensCompradas = new ArrayList<>();
            }
            viagensCompradas.add(viagem);
            return true;
        }
        return false;
    }
    
    /**
     * Indica se o utilizador tem privilegios de administrador. 
     */
    public boolean hasAdministrativePriviledges() {
        return this.hasAdministrativePriviledges;
    }
    
    private static final long serialVersionUID = -1640877519160777176L; // valor gerado aleatoriamente para a serializacao do objeto
}