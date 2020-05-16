package aval.cliente; 

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe que permite iniciar uma versao online do modo de jogo 'N-em-Linha'.
 * 
 * @author jncfa, srpg
 */
public class Cliente
{
	public static void main(String args[]) throws IOException 
	{
		// Cria ligacao com o servidor na porta 1234
		Socket socket = new Socket("localhost", 1234);
		
		// Cria DataStreams (input e output) para ler/escrever mensagens do/para o servidor
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

		// Leitura da consola local (do utilizador)
		Scanner scan = new Scanner(System.in);

		boolean manterLigacaoAtiva = true;

		while(manterLigacaoAtiva){
			// obtem a resposta vinda do servidor
			String serverResponse = dataIn.readUTF();
			
			// escreve resposta do servidor na consola
			System.out.print(serverResponse); 

			if (serverResponse.equals("Sessao terminada.")){
				manterLigacaoAtiva = false;
			}
			else{	
				dataOut.writeUTF(scan.nextLine());
				dataOut.flush();
			}
		}
		
		// Encerra todas as streams (e a socket) e liberta todos os recursos alocados
		scan.close();
		dataOut.close();
		dataIn.close();
		socket.close();
	}
}