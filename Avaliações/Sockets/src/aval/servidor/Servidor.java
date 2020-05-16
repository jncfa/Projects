package aval.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 */
public class Servidor {
	public static void main(String args[]) throws IOException {
		// Regista servico na porta 1234
		ServerSocket s = new ServerSocket(1234);
		System.out.println("Servidor iniciado!");

		// iniciamos uma nova tabela de vencedores sempre que o servidor inicia
		Leaderboard tabelaVencedores = new Leaderboard(); 
		while (true) {
			System.out.println("Esperando por uma nova ligacao...");

			// Interrompe execucao do codigo ate conectar com um cliente
			Socket socket = s.accept();
			System.out.println("Novo cliente conectado! Comecando novo jogo...");

			// Cria DataStreams (input e output) para ler/escrever mensagens do/para o cliente
			DataInputStream dataIn = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

			Protocolo protocolo = new Protocolo(tabelaVencedores); // inicia protocolo de comunicacao com o cliente
			dataOut.writeUTF(protocolo.processarInput("")); // envia mensagem de boas vindas ao cliente

			// mantem ligacao ativa com o cliente ate receber sinal para terminar
			while (protocolo.manterLigacaoAtiva()) { 
				// processa o informacao vinda do cliente usando o protocolo definido
				dataOut.writeUTF(protocolo.processarInput(dataIn.readUTF()));
				dataOut.flush();
			}

			System.out.println("Sessao com o cliente terminada.");
			dataOut.close();
			dataIn.close();
			socket.close();
		}
	}
}