/**
 * Sistemas Informaticos - Avaliacao 2 (OOP)
 *
 * Ficheiro: Interface.java
 * Data: 4/04/2019
*/

package aval;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import aval.database.Aviao;
import aval.database.Segmento;
import aval.database.Utilizador;
import aval.database.Viagem;

/**
 * Classe que implementa a comunicacao com o cliente / administrador do sistema
 * atraves da consola.
 * 
 * @author jncfa, srpg
 */
public class Interface {

    private static Scanner sc = new Scanner(System.in);
    private static DatabaseManager database = new DatabaseManager();

    public static void main(String[] args) {
        Utilizador utilizadorAtual = null;
        boolean terminarPrograma = false;

        System.out.println("Bem vindo ao programa de gestao de viagens da 'CoimbraAirLines'!");

        while (!terminarPrograma) {
            if (utilizadorAtual == null) {
                System.out.print("\nPretende fazer 'login' ou fazer um 'registo' novo (ou 'sair' para terminar sessao): ");
                
                switch (sc.nextLine()) {
                case "login":
                    utilizadorAtual = loginUtilizador();
                    break;
                case "registo":
                    utilizadorAtual = registarNovoUtilzador();
                    break;
                case "sair":
                    terminarPrograma = true;
                    break;
                default:
                    System.out.println("Erro, comando nao reconhecido!");
                    break;
                }
            } else {
                if (utilizadorAtual.hasAdministrativePriviledges()) { // o utilizador atual e admin do sistema
                    System.out.println("\nPretende efetuar que tipo de operacoes: ");
                    System.out.println(" 1) Gerir avioes;");
                    System.out.println(" 2) Gerir segmentos de viagem;");
                    System.out.println(" 3) Gerir viagens.\n");
                    System.out.print("Insira o numero da operacao (ou 'sair' para terminar sessao): ");

                    String comandoInserido = sc.nextLine().toLowerCase();

                    if (comandoInserido.equals("1")) {
                        System.out.println("\nQue operacao pretente efetuar: ");
                        System.out.println(" 1) Adicionar aviao;");
                        System.out.println(" 2) Remover aviao;");
                        System.out.println(" 3) Atualizar aviao;");
                        System.out.println(" 4) Listar todos os avioes.\n");
                        System.out.print("Insira o numero da operacao (1-4, ou 'sair' para terminar sessao): ");

                        switch (sc.nextLine().toLowerCase()) {
                        case "1":
                            adicionarAviao();
                            break;
                        case "2":
                            removerAviao();
                            break;
                        case "3":
                            alterarAviao();
                            break;
                        case "4":
                            listarAviao();
                            break;
                        case "sair":
                            terminarPrograma = true;
                            break;
                        default:
                            System.out.println("Erro, comando nao reconhecido!");
                            break;
                        }

                    }

                    else if (comandoInserido.equals("2")) {
                        System.out.println("\nQue operacao pretente efetuar: ");
                        System.out.println(" 1) Adicionar segmento;");
                        System.out.println(" 2) Remover segmento;");
                        System.out.println(" 3) Alterar segmento;");
                        System.out.println(" 4) Listar todos os segmentos.\n");

                        System.out.print("Insira o numero da operacao (1-4, ou 'sair' para terminar sessao): ");

                        switch (sc.nextLine().toLowerCase()) {
                        case "1":
                            adicionarSegmento();
                            break;
                        case "2":
                            removerSegmento();
                            break;
                        case "3":
                            alterarSegmento();
                            break;
                        case "4":
                            listarSegmentos();
                            break;
                        case "sair":
                            terminarPrograma = true;
                            break;
                        default:
                            System.out.println("Erro, comando nao reconhecido!");
                            break;
                        }

                    }

                    else if (comandoInserido.equals("3")) {
                        System.out.println("\nQue operacao pretente efetuar: ");
                        System.out.println(" 1) Adicionar viagem;");
                        System.out.println(" 2) Remover viagem;");
                        System.out.println(" 3) Alterar viagem;");
                        System.out.println(" 4) Listar todas as viagens;");
                        System.out.println(" 5) Ver estatisticas.\n");

                        System.out.print("Insira o numero da operacao (1-5, ou 'sair' para terminar sessao): ");

                        switch (sc.nextLine().toLowerCase()) {
                        case "1":
                            adicionarViagem();
                            break;
                        case "2":
                            removerViagem();
                            break;
                        case "3":
                            alterarViagem();
                            break;
                        case "4":
                            listarViagem();
                            break;
                        case "5":
                            verEstatisticas();
                            break;
                        case "sair":
                            terminarPrograma = true;
                            break;
                        default:
                            System.out.println("Erro, comando nao reconhecido!");
                            break;
                        }
                    }

                    else if (comandoInserido.equals("sair")) {
                        terminarPrograma = true;
                    }

                    else {
                        System.out.println("Erro, comando nao reconhecido!");
                    }
                } else { // o utilzador atual e cliente
                    System.out.println("\nQue operacao pretende efetuar: ");
                    System.out.println(" 1) Listar todas as viagens disponiveis;");
                    System.out.println(" 2) Pesquisar por uma determinada viagem;");
                    System.out.println(" 3) Compra determinada viagem;");   
                    System.out.println(" 4) Listar o historico das suas viagens compradas;");

                    System.out.print("Insira o numero da operacao (1-4, ou 'sair' para terminar sessao): ");

                    switch(sc.nextLine().toLowerCase()){
                        case "1":
                            listarViagem();
                            break;
                        case "2":
                            pesquisarViagem();
                            break;
                        case "3":
                            comprarViagem(utilizadorAtual);
                            break;
                        case "4":
                            historicoViagem(utilizadorAtual);
                            break;
                        case "sair":
                            terminarPrograma = true;
                            break;
                        default:
                            System.out.println("Erro, comando nao reconhecido!");
                            break;
                    }
                }
            }
        }
        System.out.println("\nSessao terminada.");
        database.saveData(); // guardar alteracoes no disco
        sc.close(); // fechar stream
    }

    public static Utilizador registarNovoUtilzador() {
        System.out.print("\nInsira o nome de utilizador: ");

        String username;
        while ((username = sc.nextLine()).isEmpty()) {
            System.out.print("Erro, indique um nome de utilizador valido: ");
        }

        System.out.print("Insira o nome de password: ");

        String password;
        while ((password = sc.nextLine()).isEmpty()) {
            System.out.print("Erro, indique uma password valida: ");
        }

        List<Utilizador> utilizadores = database.getUtilizadores();
        boolean utilizadorJaExiste = true;

        while (utilizadorJaExiste) {
            utilizadorJaExiste = false;
            for (Utilizador user : utilizadores) {
                if (user.getUsername().equalsIgnoreCase(username)) { // verifica se ja existe algum utilizador com este
                                                                     // nome de utilizador
                    utilizadorJaExiste = true;
                    break;
                }
            }
            if (utilizadorJaExiste) {
                System.out.print("Erro, nome de utilizador ja esta em uso! Tente outro: ");
                while ((username = sc.nextLine()).isEmpty()) {
                    System.out.print("Erro, indique um nome de utilizador valido: ");
                }
            }
        }

        System.out.println(String.format("\nBem vindo, %s!", username));
        System.out.println(String.format("Como esta a registar-se pela primeira vez, foi lhe creditado um bonus de 1000 euros no seu saldo!"));

        Utilizador user = new Utilizador(username, password);
        utilizadores.add(user); // adiciona cliente a base de dados
        return user;
    }

    public static Utilizador loginUtilizador() {
        System.out.print("\nInsira o nome de utilizador: ");

        String username;
        while ((username = sc.nextLine()).isEmpty()) {
            System.out.print("Erro, indique um nome de utilizador valido: ");
        }

        System.out.print("Insira o nome de password: ");

        String password;
        while ((password = sc.nextLine()).isEmpty()) {
            System.out.print("Erro, indique uma password valida: ");
        }

        List<Utilizador> utilizadores = database.getUtilizadores();
        for (Utilizador user : utilizadores) {
            if (user.matchCredentials(username, password)) { // verifica se ja existe algum utilizador com estas
                                                             // credenciais
                System.out.println(String.format("\nBem vindo, %s!", username));
                return user;
            }
        }

        System.out.println("Erro, nome de utilizador ou password errada!");
        return null; // nao existe nenhum utilizador com estas credenciais
    }

    public static void adicionarAviao() {
        System.out.print("\nIndique o codigo do novo aviao: ");
        String codigoAviao;
        while ((codigoAviao = sc.nextLine()).isEmpty()) {
            System.out.print("Erro, indique codigo valido: ");
        }
        System.out.println(database.adicionarAviao(codigoAviao));
    }

    public static void removerAviao() {
        List<Aviao> avioesRemoviveis = new ArrayList<>();

        for (Aviao aviao : database.getAvioes()) {
            if(!aviao.aviaoUsado()){
                avioesRemoviveis.add(aviao);
            }
        }
        if (avioesRemoviveis.size() > 0) {
            System.out.println("\nLista de codigos de avioes possiveis de remover: ");
            for (int idx = 0; idx < avioesRemoviveis.size(); idx++) {
                System.out.println(String.format(" %d) %s;", idx + 1, avioesRemoviveis.get(idx)));
            }

            Aviao aviaoParaRemover = null;

            if(avioesRemoviveis.size() > 1){
                aviaoParaRemover = avioesRemoviveis.get(pedirValorRestringido(1, avioesRemoviveis.size(), "Indique o numero do aviao a remover") - 1);
            }
            else{
                aviaoParaRemover = avioesRemoviveis.get(0);
            }

            System.out.println("\n" + aviaoParaRemover);
            System.out.print("Confirme se pretende remover este aviao (y/n): ");

            if (sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println(database.removerAviao(aviaoParaRemover));
            } else {
                System.out.println("Operacao cancelada.");
            }
        } else {
            System.out.println("\nNao existem avioes possiveis de remover.");
        }
    }

    public static void alterarAviao() {
        if (database.getAvioes().size() > 0) {
            listarAviao();

            Aviao aviaoParaAlterar = null;

            if(database.getAvioes().size() > 1){
                aviaoParaAlterar = database.getAvioes().get(pedirValorRestringido(1, database.getAvioes().size(), "Indique o numero do aviao a alterar") - 1);
            }
            else{
                aviaoParaAlterar = database.getAvioes().get(0);
            }

            System.out.println("\nAviao: " + aviaoParaAlterar);
            System.out.print("Confirme se pretende alterar este aviao (y/n): ");

            if (sc.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Introduza o codigo novo: ");
                String novoCodigo;
    
                while((novoCodigo = sc.nextLine()).isEmpty()){
                    System.out.print("Indique um codigo valido: ");
                }

                aviaoParaAlterar.setCodigo(novoCodigo);
            } else {
                System.out.println("Operacao cancelada.");
            }
            
        } else {
            System.out.println("\nNao existem avioes registados na base de dados.");
        }
    }

    public static void listarAviao() {
        if (database.getAvioes().size() > 0) {
            System.out.println("\nLista de codigos de avioes: ");

            for (int idx = 0; idx < database.getAvioes().size(); idx++) {
                System.out.println(String.format(" %d) %s;", idx + 1, database.getAvioes().get(idx)));
            }
        } else {
            System.out.println("\nNao existem avioes registados na base de dados.");
        }
    }

    public static void adicionarSegmento() {
        System.out.print("\nIndique a cidade de origem: ");

        String cidadeOrigem;
        while ((cidadeOrigem = sc.nextLine()).isEmpty()) {
            System.out.print("Erro, indique uma cidade valida: ");
        }

        System.out.print("Indique a cidade de destino: ");

        String cidadeDestino;
        while ((cidadeDestino = sc.nextLine()).isEmpty()) {
            System.out.print("Erro, indique uma cidade valida: ");
        }

        listarAviao();
        Aviao aviaoParaAssociar = null;

        if(database.getAvioes().size() > 1){
            aviaoParaAssociar = database.getAvioes().get(pedirValorRestringido(1, database.getAvioes().size(), "\nIndique o codigo do aviao que pretende associar a este segmento") - 1);
        }
        else{
            aviaoParaAssociar = database.getAvioes().get(0);
        }

        System.out.println("\nAviao: " + aviaoParaAssociar);
        System.out.print("Confirme se pretende alterar este aviao (y/n): ");

        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(database.adicionarSegmento(cidadeOrigem, cidadeDestino, aviaoParaAssociar));
        }
        else{
            System.out.println("Operacao cancelada.");
        }
    }

    public static void removerSegmento() {
        if (database.getSegmentos().size() > 0){
            System.out.println("\nLista de segmentos: ");
            for (int i = 1; i <= database.getSegmentos().size(); i++) {
                Segmento segmento = database.getSegmentos().get(i - 1);
                System.out.println(String.format(" %d) %s;", i, segmento));
            }

            Segmento segmentoParaRemover;
            if (database.getSegmentos().size() > 1){
                segmentoParaRemover = database.getSegmentos().get(pedirValorRestringido(1, database.getSegmentos().size(),"\nIndique o segmento a remover") - 1 );
            }
            else{
                segmentoParaRemover = database.getSegmentos().get(0);
            }

            System.out.print(String.format("Confirme se pretende eliminar este segmento (y/n) � %s: ", segmentoParaRemover));

            if (sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println(database.removerSegmento(segmentoParaRemover));
            } else {
                System.out.println("Operacao cancelada.");
            }
        }
        else{
            System.out.println("\nNao existem segmentos na base de dados.");
        }
    }

    public static void alterarSegmento() {
        if (database.getSegmentos().size() > 0) {
            System.out.println("\nLista de segmentos: ");
            for (int i = 1; i <= database.getSegmentos().size(); i++) {
                Segmento segmento = database.getSegmentos().get(i - 1);
                System.out.println(String.format(" %d) %s;", i, segmento));
            }

            Segmento segmentoParaAlterar = database.getSegmentos().get(pedirValorRestringido(1, database.getSegmentos().size(), "Indique o segmento a alterar") - 1);
            
            listarAviao();

            Aviao aviaoEscolhido = null;

            if(database.getAvioes().size() > 1){
                aviaoEscolhido = database.getAvioes().get(pedirValorRestringido(1, database.getAvioes().size(), "Indique o numero do aviao a associar ao segmento") - 1);
            }
            else{
                aviaoEscolhido = database.getAvioes().get(0);
            }

            System.out.println("\n" + aviaoEscolhido);
            System.out.print("Confirme se pretende escolher este aviao (y/n): ");

            if (sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println(segmentoParaAlterar.modificarAviao(aviaoEscolhido));
            } else {
                System.out.println("Operacao cancelada.");
            }
            
        } else {
            System.out.println("\nNao existem segmentos na base de dados.");
        }
    }

    public static void listarSegmentos() {
        if (database.getSegmentos().size() > 0) {
            System.out.println("\nLista de segmentos: ");
            for (int i = 1; i <= database.getSegmentos().size(); i++) {
                Segmento segmento = database.getSegmentos().get(i - 1);
                System.out.println(String.format(" %d) %s;", i, segmento));
            }
        } else {
            System.out.println("\nNao existem segmentos na base de dados.");
        }
    }

    public static void adicionarViagem() {
        if (database.getSegmentos().size() > 0) {
            System.out.print("\nIndique a cidade de origem: ");
            String cidadeOrigem;
            while ((cidadeOrigem = sc.nextLine()).isEmpty()) {
                System.out.print("Erro, indique uma cidade valida: ");
            }

            System.out.print("Indique a cidade de destino: ");
            String cidadeDestino;
            while ((cidadeDestino = sc.nextLine()).isEmpty()) {
                System.out.print("Erro, indique uma cidade valida: ");
            }
            
            TripFinder finder = new TripFinder(database, cidadeOrigem, cidadeDestino);
            List<List<Segmento>> percursosPossiveis = finder.getPercursosPossiveis();

            if (percursosPossiveis.size() > 0) {
                System.out.println("Como pretende criar o percurso da viagem: ");
                System.out.println(" 1) Automaticamente;");
                System.out.println(" 2) Manualmente.");
                System.out.print("Insira o numero da operacao (1-2): ");

                String comando = sc.nextLine();

                if (comando.equals("1")) {
                    List<Segmento> percursoEscolhido = percursosPossiveis.get(0);

                    System.out.print("Percurso escolhido: ");
                    for (int idx = 0; idx < percursoEscolhido.size(); idx++) {
                        System.out.print(percursoEscolhido.get(idx));

                        if (idx < percursoEscolhido.size() - 1) {
                            System.out.print(", ");
                        } else {
                            System.out.println(";");
                        }
                    }

                    float custoAtual = pedirValorRestringido(0, Float.MAX_VALUE, "Insira o custo da viagem", false);
                    Viagem viagem = new Viagem(percursoEscolhido, custoAtual);

                    System.out.println(viagem.toString());
                    System.out.print("Confirme se pretende criar esta viagem (y/n): ");

                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        System.out.println(database.adicionarViagem(viagem));
                    } else {
                        System.out.println("Operacao cancelada.");
                    }
                }
                else if (comando.equals("2")) {
                    for (int idxViagens = 0; idxViagens < percursosPossiveis.size(); idxViagens++) {
                        System.out.print(String.format(" %d) ", idxViagens + 1));

                        List<Segmento> percurso = percursosPossiveis.get(idxViagens);
                        for (int idx = 0; idx < percurso.size(); idx++) {
                            System.out.print(percurso.get(idx).toString());
                            if (idx < percurso.size() - 1){
                                System.out.print(", ");
                            }
                            else{
                                System.out.println(";");
                            }
                        }
                    }

                    List<Segmento> percursoEscolhido;
                    if (percursosPossiveis.size() > 1){
                        percursoEscolhido = percursosPossiveis.get(pedirValorRestringido(1, percursosPossiveis.size(), "\nIndique qual o conjunto de segmentos que pretende usar") - 1);
                    }
                    else{
                        percursoEscolhido = percursosPossiveis.get(0);
                        System.out.println("Existe apenas um percurso possivel, foi automaticamente selecionado esse percurso.");
                    }
                    
                    System.out.print("\nPercurso selecionado: ");
                    for (int idx = 0; idx < percursoEscolhido.size(); idx++) {
                        System.out.print(percursoEscolhido.get(idx).toString());
            
                        if (idx < percursoEscolhido.size() - 1){
                            System.out.print(", ");
                        }
                        else{
                            System.out.println(";");
                        }
                    }

                    float custoAtual = pedirValorRestringido(0, Float.MAX_VALUE, "Insira o custo da viagem", false);             
                    Viagem viagem = new Viagem(percursoEscolhido, custoAtual);

                    System.out.println(viagem.toString());
                    System.out.print("Confirme se pretende criar esta viagem (y/n): ");

                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        System.out.println(database.adicionarViagem(viagem));
                    } else {
                        System.out.println("Operacao cancelada.");
                    }
                } else {
                    System.out.println("Erro, comando nao reconhecido!");
                }
            } else {
                System.out.println("Nao e possivel criar um percurso com a origem e destino escolhidos.");
            }
        } else {
            System.out.println("\nNao existem segmentos na base de dados.");
        }
    }

    public static void removerViagem() {
        if (database.getViagens().size() > 0){
            System.out.println("\nLista de viagens: ");
            for (int i = 1; i <= database.getViagens().size(); i++) {
                Viagem viagem = database.getViagens().get(i - 1);
                System.out.println(String.format(" %d) %s;", i, viagem));
            }
    
            Viagem viagemSelecionada;
            if (database.getViagens().size() > 1){
                viagemSelecionada = database.getViagens().get(pedirValorRestringido(1, database.getViagens().size(), "Indique a viagem que pretende remover") - 1);
            }
            else{
                viagemSelecionada = database.getViagens().get(0);
            }
    
            System.out.print(String.format("\nConfirme se pretende eliminar esta viagem (y/n):\n%s: ", viagemSelecionada));

            if (sc.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Pretende tambem remover todos os segmentos associados (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    System.out.println(database.removerViagem(viagemSelecionada, true));
                }
                else {
                    System.out.println(database.removerViagem(viagemSelecionada, false));
                }
            } else {
                System.out.println("Operacao cancelada.");
            } 
        }
        else{
            System.out.println("\nNao existem viagens na base de dados.");
        }
    }

    public static void alterarViagem() {
        if (database.getViagens().size() > 0){
            listarViagem();

            Viagem viagemEscolhida;
            if (database.getViagens().size() > 1){
                viagemEscolhida = database.getViagens().get(pedirValorRestringido(1, database.getViagens().size(), "\nIndique a viagem que pretende alterar") - 1);
            }
            else{
                viagemEscolhida = database.getViagens().get(0);
            }

            System.out.println("\n" + viagemEscolhida.toString(true));
            System.out.print("\nConfirme se pretende alterar esta viagem (y/n):");
            
            if (sc.nextLine().equalsIgnoreCase("y")){
                boolean viagemComprada = false;

                for (Utilizador cliente : database.getUtilizadores()){
                    if (cliente.getViagensCompradas().contains(viagemEscolhida)){ // viagem encontrado
                        System.out.println("Erro, a viagem ja foi comprada por um cliente, nao e possivel alterar.");
                        viagemComprada = true;
                        break;
                    }
                }
                if (!viagemComprada){
                    System.out.println("\nO que pretende alterar na viagem: ");
                    System.out.println(" 1) O custo;");
                    System.out.println(" 2) Os segmentos;");
                    System.out.println(" 3) Ambos.");
                    System.out.print("Insira o numero da operacao (1-3): ");
    
                    String comando = sc.nextLine();
    
                    if (comando.equals("1") || comando.equals("3")) {
                        System.out.println(String.format("\nO custo atual da viagem e %.2f.", viagemEscolhida.getCusto()));
                        System.out.println(viagemEscolhida.setCusto(pedirValorRestringido(0, Float.MAX_VALUE, "Qual e o custo da viagem", false)));
                    }
    
                    if (comando.equals("2") || comando.equals("3")) {
                        float custoAtual = viagemEscolhida.getCusto();
                        database.removerViagem(viagemEscolhida, false);
                        
                        System.out.print("\nIndique a cidade de origem: ");
                        String cidadeOrigem;
                        while ((cidadeOrigem = sc.nextLine()).isEmpty()) {
                            System.out.print("Erro, indique uma cidade valida: ");
                        }
    
                        System.out.print("\nIndique a cidade de destino: ");
                        String cidadeDestino;
                        while ((cidadeDestino = sc.nextLine()).isEmpty()) {
                            System.out.print("Erro, indique uma cidade valida: ");
                        }
                        
                        TripFinder finder = new TripFinder(database, cidadeOrigem, cidadeDestino);
                        List<List<Segmento>> percursosPossiveis = finder.getPercursosPossiveis();
                        
                        for (int idxViagens = 0; idxViagens < percursosPossiveis.size(); idxViagens++) {
                            System.out.print(String.format(" %d) ", idxViagens + 1));
                            List<Segmento> percurso = percursosPossiveis.get(idxViagens);
                            for (int idx = 0; idx < percurso.size(); idx++) {
                                System.out.print(percurso.get(idx));
        
                                if (idx < percurso.size() - 1) {
                                    System.out.print(", ");
                                } else {
                                    System.out.println(";");
                                }
                            }
                        }
    
                        List<Segmento> percursoEscolhido;
                        if (percursosPossiveis.size() > 1){
                            percursoEscolhido = percursosPossiveis.get(pedirValorRestringido(1, percursosPossiveis.size(), "Indique qual o conjunto de segmentos que pretende usar") - 1);
                        }
                        else{
                            percursoEscolhido = percursosPossiveis.get(0);
                            System.out.println("Existe apenas um percurso possivel, foi automaticamente selecionado esse percurso.");
                        }

                        Viagem viagemAlterada = new Viagem(percursoEscolhido, custoAtual);
                        System.out.println("\n" + viagemAlterada.toString(true));
                        System.out.print("Confirme se pretende alterar para esta viagem (y/n):");
                        if (sc.nextLine().equalsIgnoreCase("y")){
                            System.out.println(database.adicionarViagem(viagemAlterada));
                        }
                        else{
                            System.out.println("Operacao cancelada.");
                        }
                    }
    
                    if (!comando.equals("1") && !comando.equals("2") && !comando.equals("3")) {
                        System.out.println("Erro, comando nao reconhecido!");
                    }
                }    
            }
         }
        else{
            System.out.println("\nNao existem viagens na base de dados.");
        }
    }
 
    public static void verEstatisticas() {
        System.out.println("Pretende ver que informacoes: ");
        System.out.println(" 1) Valor total de vendas: ");
        System.out.println(" 2) Valor total de vendas para uma certa cidade de origem: ");
        System.out.println(" 3) Valor total de vendas para uma certa cidade destino: ");
        System.out.println(" 4) Valor medio gasto em compras por utilizador: ");
        System.out.println(" 5) Valor medio de escalas de viagem comprada: ");

        System.out.print("Insira o numero da operacao (1-5): ");
        String comando = sc.nextLine();

        if (comando.equals("1")){
            float valorTotal = 0;

            for (Utilizador utilizador : database.getUtilizadores()){
                if (!utilizador.hasAdministrativePriviledges()){
                    for (Viagem v : utilizador.getViagensCompradas()){
                        valorTotal += v.getCusto();
                    }
                }
            }

            if (valorTotal == 0){
                System.out.println("Nao existem vendas registadas na base de dados!");
            }
            else{
                System.out.println(String.format("Valor total de vendas: %.2f", valorTotal));
            }   
        }
        else if (comando.equals("2")){
            System.out.print("Qual e a cidade de origem: ");
            String cidadeOrigem;

            while((cidadeOrigem = sc.nextLine()).isEmpty()){
                System.out.print("Erro, indique um nome de uma cidade: ");
            }

            float valorTotal = 0;
            for (Utilizador utilizador : database.getUtilizadores()){
                if (!utilizador.hasAdministrativePriviledges()){
                    for (Viagem v : utilizador.getViagensCompradas()){
                        if (v.getCidadeOrigem().equalsIgnoreCase(cidadeOrigem)){
                            valorTotal += v.getCusto();
                        }
                    }
                }
            }
            if (valorTotal == 0){
                System.out.println("Nao existem vendas registadas na base de dados!");
            }
            else{
                System.out.println(String.format("Valor total de vendas (com origem em %s): %.2f", cidadeOrigem, valorTotal));
            }  
        }
        else if (comando.equals("3")){
            System.out.print("Qual e a cidade de destino: ");
            String cidadeDestino;

            while((cidadeDestino = sc.nextLine()).isEmpty()){
                System.out.print("Erro, indique um nome de uma cidade: ");
            }

            float valorTotal = 0;
            for (Utilizador utilizador : database.getUtilizadores()){
                if (!utilizador.hasAdministrativePriviledges()){
                    for (Viagem v : utilizador.getViagensCompradas()){
                        if (v.getCidadeOrigem().equalsIgnoreCase(cidadeDestino)){
                            valorTotal += v.getCusto();
                        }
                    }
                }
            }

            if (valorTotal == 0){
                System.out.println("Nao existem vendas registadas na base de dados!");
            }
            else{
                System.out.println(String.format("Valor total de vendas (com origem em %s): %.2f", cidadeDestino, valorTotal));
            }  
        }
        else if (comando.equals("4")){
            if (database.getUtilizadores().size() - 1 > 0){ // o admin esta incluido na lista dos utilizadores
                float valorTotal = 0;
                for (Utilizador utilizador : database.getUtilizadores()){
                    if (!utilizador.hasAdministrativePriviledges()){
                        for (Viagem v : utilizador.getViagensCompradas()){
                            valorTotal += v.getCusto();
                        }
                    }
                }
    
                if (valorTotal == 0){
                    System.out.println("Nao existem vendas registadas na base de dados!");
                }
                else{
                    System.out.println(String.format("Valor medio de vendas por utilizador: %.2f", valorTotal / (database.getUtilizadores().size() - 1)));
                } 
            }     
            else{
                System.out.println("Nao existem utilizadores na base de dados");
            }   
        }
        else if (comando.equals("5")){
            if (database.getUtilizadores().size() - 1 > 0){ // o admin esta incluido na lista dos utilizadores, entao temos de remove-lo
                float totalEscalasPorViagem = 0;
                int totalViagensCompradas = 0;

                for (Utilizador utilizador : database.getUtilizadores()){
                    if (utilizador.getViagensCompradas() != null){
                        for (Viagem v : utilizador.getViagensCompradas()){
                            totalViagensCompradas++;
                            totalEscalasPorViagem += (v.getSegmentos().size() - 1);
                        }
                    }
                }
    
                if (totalEscalasPorViagem == 0){
                    System.out.println("\nNao existem vendas registadas na base de dados!");
                }
                else{
                    System.out.println(String.format("Valor medio de escalas por viagem comprada: %.2f", totalEscalasPorViagem / totalViagensCompradas));
                } 
            }     
            else{
                System.out.println("\nNao existem vendas registadas na base de dados!");
            }   
        }
        else {
            System.out.println("\nErro, comando nao reconhecido!");
        }   
    }
  
    public static void listarViagem() {
        if (database.getViagens().size() > 0) {
            System.out.println("\nLista de viagens: ");
            for (int i = 1; i <= database.getViagens().size(); i++) {
                Viagem viagem = database.getViagens().get(i - 1);
                System.out.println(String.format(" %d) %s;", i, viagem));
            }
        } else {
            System.out.println("\nNao existem viagens na base de dados.");
        }
    }

    public static void pesquisarViagem() {
        if (database.getViagens().size() > 0) {
            System.out.println("\nQue tipo de pesquisa pretende efetuar: ");
            System.out.println(" 1) Especificando a origem;");
            System.out.println(" 2) Especificando o destino;");
            System.out.println(" 3) Especificando a origem e o destino.\n");

            System.out.print("Insira o numero da operacao (1-3): ");
            String comandoInserido = sc.nextLine();
            
            if (comandoInserido.equalsIgnoreCase("1")) {
            	List<Viagem> viagensPossiveis = new ArrayList<>();
            	System.out.print("Qual e a cidade de origem que pretende pesquisar: ");
                String origem = sc.nextLine();
                
            	//verificar se foi colocada uma cidade valida
            	//criar lista para as viagens possiveis
            	for (Viagem v : database.getViagens()){
            		if(v.getCidadeOrigem().equals(origem)) {
            			viagensPossiveis.add(v);
            		}
            	}
            	if (viagensPossiveis.size() == 0) {
            		System.out.println("Nao ha viagens com a cidade de origem que definiu.");
        		}
            	else {
            		System.out.println("\nLista de viagens possiveis: ");
            		for (int i = 1; i <= viagensPossiveis.size(); i++) {
                        Viagem viagem = viagensPossiveis.get(i - 1);
                        System.out.println(String.format(" %d) %s;", i, viagem));
                    }
            	}
        	}
            else if (comandoInserido.equalsIgnoreCase("2")) {
            	List<Viagem> viagensPossiveis = new ArrayList<>();
            	System.out.print("Qual e a cidade de destino que pretende pesquisar: ");
            	String destino = sc.nextLine();
            	//verificar se foi colocada uma cidade valida
            	for (Viagem v : database.getViagens()){
            		if(v.getCidadeDestino().equals(destino)) {
            			viagensPossiveis.add(v);
            		}
            	}
            	if (viagensPossiveis.size() == 0) {
            		System.out.print("\nNao ha viagens com a cidade de destino que definiu.");
        		}
            	else {
            		System.out.println("\nLista de viagens possiveis: ");
            		for (int i = 1; i <= viagensPossiveis.size(); i++) {
                        Viagem viagem = viagensPossiveis.get(i - 1);
                        System.out.println(String.format(" %d) %s;", i, viagem));
                    }
            	}
        	}
            	
            else if (comandoInserido.equalsIgnoreCase("3")) {
            	List<Viagem> viagensPossiveis = new ArrayList<>();
            	System.out.print("\nQual a cidade onde pretende comecar a viagem: ");
            	String origem = sc.nextLine();
            	System.out.print("\nE qual e a cidade onde pretende acabar: ");
                String destino = sc.nextLine();
                

            	for (Viagem v : database.getViagens()){
            		if(v.getCidadeDestino().equals(destino) && v.getCidadeOrigem().equals(origem)) {
                        viagensPossiveis.add(v);
                    }
                }
            	
            	if (viagensPossiveis.size() == 0) {
            		System.out.print("Nao ha viagens com a origem e o destino que definiu.");
        		}
            	else {
            		System.out.println("Lista de viagens possiveis: ");
            		for (int i = 1; i <= viagensPossiveis.size(); i++) {
                        Viagem viagem = viagensPossiveis.get(i - 1);
                        System.out.println(String.format(" %d) %s;", i, viagem));
                    }
            	}
            }
            else {
                System.out.println("Erro, comando nao reconhecido!");
            }
        }
        else {
          System.out.println("Nao existem viagens na base de dados.");
        }
    }

    public static void comprarViagem(Utilizador cliente){
        listarViagem();
        int valorInserido = pedirValorRestringido(1, database.getViagens().size(), "Indique a viagem que pretende comprar");
        Viagem viagem = database.getViagens().get(valorInserido - 1);
        if(cliente.getSaldoAtual() >= viagem.getCusto()){
            System.out.print(String.format("%s\nConfirme se pretende comprar esta viagem, fica com %.2f euros na conta (y/n):", viagem, cliente.getSaldoAtual() - viagem.getCusto()));
            if (sc.nextLine().equalsIgnoreCase("y")) {
                cliente.comprarViagem(viagem);
                System.out.println("Viagem comprada.");
            }
            else {
                System.out.println("Operacao cancelada.");
            }
        }
        else{
            System.out.println("Saldo insuficiente.");
        }
                 
    }
    
    public static void historicoViagem(Utilizador cliente) {
        if (cliente.getViagensCompradas().size() > 0) {
            System.out.println("Lista de viagens no historico: ");
            for (int i = 1; i <= cliente.getViagensCompradas().size(); i++) {
                Viagem viagem = cliente.getViagensCompradas().get(i - 1);
                System.out.println(String.format(" %d) %s;", i, viagem));
            }
        } else {
            System.out.println("Nao tem viagens no seu historico.");
        }
    }

    public static int pedirValorRestringido(int min, int max, String mensagem){
        return pedirValorRestringido(min, max, mensagem, true);
    }

    public static int pedirValorRestringido(int min, int max, String mensagem, boolean indicarMargens){
        if (indicarMargens){
            System.out.print(String.format("%s (%d-%d): ", mensagem, min, max));
        }   
        else{
            System.out.print(String.format("%s: ", mensagem));
        }
                
        int numeroInserido = 0;
        boolean numeroValido = false;

        while (!numeroValido) {
            while(!sc.hasNextInt()){
                sc.nextLine(); //limpar buffer
                System.out.print("Erro, indique um numero valido: ");
            }
            numeroInserido = sc.nextInt();
            sc.nextLine(); //limpar buffer
            
            if ((numeroInserido >= min && numeroInserido <= max)){
                numeroValido = true;
            }
            else{
                if (indicarMargens){
                    System.out.print(String.format("Erro, indique apenas um valor entre %d e %d: ", min, max));
                }   
                else{
                    System.out.print("Erro, indique apenas um valor numerico: ");
                }
            }
        }

        return numeroInserido;
    }
    
    public static float pedirValorRestringido(float min, float max, String mensagem){
        return pedirValorRestringido(min, max, mensagem, true);
    }

    public static float pedirValorRestringido(float min, float max, String mensagem, boolean indicarMargens){
        if (indicarMargens){
            System.out.print(String.format("%s (%.2f-%.2f): ", mensagem, min, max));
        }   
        else{
            System.out.print(String.format("%s: ", mensagem));
        }  

        float numeroInserido = 0;
        boolean numeroValido = false;

        while (!numeroValido) {
            while(!sc.hasNextFloat()){
                sc.nextLine(); //limpar buffer
                System.out.print("Erro, indique um numero valido: ");
            }
            numeroInserido = sc.nextFloat();
            sc.nextLine(); //limpar buffer
            
            if ((numeroInserido >= min && numeroInserido <= max)){
                numeroValido = true;
            }
            else{
                if (indicarMargens){
                    System.out.print(String.format("Erro, indique apenas um valor entre %.2f e %.2f: ", min, max));
                }   
                else{
                    System.out.print("Erro, indique apenas um valor numerico: ");
                }
            }
        }

        return numeroInserido;
    }
}