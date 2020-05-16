package servidor;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
	public static void main(String args[]) throws IOException {
		try {
			GestorDados gestorDados = new GestorDados();
			Controlo controlo = new Controlo(gestorDados);

			// cria um registo que aceita pedidos na porta 2000
			Registry registry = LocateRegistry.createRegistry(2000);

			// ligamos o nosso objeto ao registo, para ser acedido remotamente
			registry.bind("controlo", controlo);

		} catch (AlreadyBoundException e) {
			System.out.println("Ocorreu um erro:");
			e.printStackTrace();
		}
	}
}