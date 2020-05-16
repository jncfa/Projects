package aval;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aval.database.Aviao;
import aval.database.OperationResult;
import aval.database.Segmento;
import aval.database.Utilizador;
import aval.database.Viagem;



/**
 * Classe que gere a serializacao e deserializacao dos dados (lista de utilizadores e de viagens) a serem guardados no disco.
 * 
 * @author jncfa, srpg
 */
public class DatabaseManager implements java.io.Serializable {
	private List<Utilizador> utilizadores;
	private List<Viagem> viagens;
	private List<Segmento> segmentos;
	private List<Aviao> avioes;

	public DatabaseManager() {
		loadData(); // lemos os dados gravados no sistema, caso nao existam, cria uma base de dados nova
	}

	public void loadData() {
		try (FileInputStream fis = new FileInputStream(databasePath)) { // abre o ficheiro 
			ObjectInputStream ois = new ObjectInputStream(fis); // 'handler' que permite deserializar os dados

			DatabaseManager db = (DatabaseManager) ois.readObject(); // le o ficheiro e deserializa a base de dados

			this.utilizadores = db.utilizadores; // atualiza as referencias de todas as listas de objetos
			this.viagens = db.viagens; 
			this.segmentos = db.segmentos;
			this.avioes = db.avioes;
			 
			ois.close(); // encerra a stream para prevenir 'resource leaks'  (mas o bloco try-catch em principio deve faze-lo autonomamente ?)
		} catch (ClassCastException | ClassNotFoundException | IOException e) { // ficheiro nao encontrado ou dados corrumpidos, criamos uma nova base de dados (sem viagens) e apenas com um admin definido
			utilizadores = new ArrayList<>(); // inicializa a base de utilizadores
			viagens = new ArrayList<>(); // e de viagens
			segmentos = new ArrayList<>();
			avioes = new ArrayList<>();
			
			utilizadores.add(new Utilizador("admin", "admin123", true)); // registo de admin 'por default'

			System.out.println("Foi inicializada uma nova base de dados neste sistema!");
			System.out.println("Foram geradas as seguintes credenciais para o administrador:");
			System.out.println("Username: admin");
			System.out.println("Password: admin123\n");

			saveData(); //escreve dados atuais no disco
		}
	}

	public void saveData() {
		try (FileOutputStream fos = new FileOutputStream(databasePath)) { // abre o ficheiro (cria um novo caso nao exista)
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this); //escreve a base de dados no disco
			oos.close(); //encerra a stream para evitar 'resource leaks' (mas o bloco try-catch deve em principio fa-lo autonomamente ?)
		} catch (IOException e){ //erro na serializacao do objeto
			e.printStackTrace();
		}
	}

	public List<Utilizador> getUtilizadores() {
		return this.utilizadores;
	}

	public List<Viagem> getViagens() {
		return this.viagens;
	}

	public List<Segmento> getSegmentos() {
		return this.segmentos;
	}

	public List<Aviao> getAvioes() {
		return this.avioes;
	}

	public OperationResult removerAviao(Aviao aviao){
		if (this.avioes.remove(aviao)){
			return new OperationResult();
		}
		
		return new OperationResult(false, "Erro, o aviao que pretende eliminar nao existe.");
	}	

	public OperationResult adicionarAviao(String codigoAviao){
		for(Aviao aviao : this.avioes){
			if (aviao.getCodigo().equals(codigoAviao)){	
				return new OperationResult(false, "Erro, o aviao que pretende adicionar ja existe.");
			}
		}

		this.avioes.add(new Aviao(codigoAviao));
		return new OperationResult();
	}
	
	public OperationResult removerSegmento(Segmento segmento){
		for (Viagem v : this.viagens){
			if (v.getSegmentos().contains(segmento)){ // segmento encontrado
				return new OperationResult(false, "Erro, o segmento esta em uso, nao e possivel remover.");
			}
		}

		segmento.getAviaoAtual().desassociarSegmento(segmento);
		this.segmentos.remove(segmento);
		return new OperationResult();
	}	

	public OperationResult adicionarSegmento(String cidadeOrigem, String cidadeDestino, Aviao aviaoEscolhido){
		for (Segmento segmento : this.segmentos){
			if (segmento.getDestino().equals(cidadeDestino) && segmento.getOrigem().equals(cidadeOrigem) && segmento.getAviaoAtual().equals(aviaoEscolhido)){
				return new OperationResult(false, "Erro, o segmento que pretende adicionar ja existe.");
			}
		}

		if(aviaoEscolhido != null){
			if (aviaoEscolhido.getUsoAviao() < 3){
				this.segmentos.add(new Segmento(aviaoEscolhido, cidadeOrigem, cidadeDestino));
				return new OperationResult();
			}
			else{
				return new OperationResult(false, "Erro, o aviao esta no limite de viagens que pode realizar.");
			}
		}
		else{
			return new OperationResult(false, "Erro, o aviao que pretende adicionar nao existe.");	
		}
			
	}

	public OperationResult adicionarViagem(Viagem viagem){
		this.viagens.add(viagem);
		return new OperationResult();
	}

	public OperationResult removerViagem(Viagem viagem, boolean removerSegmentos){
		for (Utilizador cliente : this.utilizadores){
			if (cliente != null){
				if (cliente.getViagensCompradas() != null){
					if (cliente.getViagensCompradas().contains(viagem)){ // viagem encontrado
						return new OperationResult(false, "Erro, a viagem ja foi comprada por um cliente, nao e possivel remover.");
					}
				}
			}
		}
		this.viagens.remove(viagem);
		
		if (removerSegmentos){
			for (Viagem outraViagem : this.viagens){
				if (!Collections.disjoint(viagem.getSegmentos(), outraViagem.getSegmentos())){ // existe uma outra viagem que usa os mesmos segmentos
					return new OperationResult(false, "Erro, os segmentos encontram se em uso.");
				}
			}
			this.segmentos.removeAll(viagem.getSegmentos()); // remove todos os segmentos da viagem da base de dados
		}


		return new OperationResult();
	}	

	protected final String databasePath = "database.db"; // caminho para a base de dados
	private static final long serialVersionUID = -5381974117293476411L; // verificacao para deserializacao do objeto
}