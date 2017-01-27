package cliente;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import servidor.Contacto;
import servidor.Funciones;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cliente {

	static boolean loginCorrecto = false;

	public static void main(String[] args) {
		Funciones agenda = null;
		try {
			System.out.println("Localizando el registro de objetos remotos");
			Registry registry = LocateRegistry.getRegistry("localhost", 5555);
			System.out.println("Obteniendo el stab del objeto remoto");
			agenda = (Funciones) registry.lookup("Agenda");

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (agenda != null) {
			Scanner scan = new Scanner(System.in);

			while (!loginCorrecto) {

				System.out.println("Por favor, inicia sesión:");
				System.out.println("Usuario: ");
				String usuario = scan.nextLine();
				System.out.println("Contraseña: ");
				String password = scan.nextLine();

				try {
					loginCorrecto = agenda.login(usuario, password);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				if (loginCorrecto) {
					System.out.println("¡Logeado con éxito!");

				} else {
					System.err.println("Usuario o contraseña incorrectos.");
				}
			}

			int opcion = 1;

			while (opcion != 0) {
				try {
					System.out.println(agenda.muestraMenu());
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				System.out.println("¿Qué quieres hacer?");
				opcion = scan.nextInt();
				scan.nextLine();
				switch (opcion) {
				case 1:
					try {
						HashMap<String, String> contactos = agenda.muestraContactos();
						System.out.println("\nLISTA DE CONTACTOS\n");
						for (Map.Entry<String, String> entry : contactos.entrySet()) {
							System.out.println("··················");
							System.out.println(entry.getValue());
							System.out.println("··················\n");
						}

					} catch (RemoteException e) {
						System.err.println("Error");
						e.printStackTrace();
					}
					break;

				case 2:
					System.out.println("\nNUEVO CONTACTO\n");
					System.out.println("Introduce nombre del contacto:");
					String nombreC = scan.nextLine();
					System.out.println("Introduce telefono del contacto:");
					long telefonoC = scan.nextLong();
					System.out.println("Introduce email del contacto:");
					String emailC = scan.next();
					try {
						System.out.println("\nTu contacto ha sido creado:\n"
								+ agenda.nuevoContacto(nombreC, telefonoC, emailC) + "\n");
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;

				case 3:
					System.out.println("\nMODIFICAR CONTACTO\n");
					System.out.println("Introduce telefono del contacto:");
					long telefono = scan.nextLong();
					scan.nextLine();
					try {
						System.out.println(agenda.modificaContacto(telefono));
						System.out.println("Introduce los nuevos datos: ");
						System.out.println("Nuevo nombre:");
						String nuevoNombre = scan.nextLine();
						System.out.println("Nuevo telefono:");
						long nuevoNumero = scan.nextLong();
						System.out.println("Nuevo email:");
						String nuevoEmail = scan.next();

						System.out.println(agenda.contactoModificado(telefono, nuevoNombre, nuevoNumero, nuevoEmail));

					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;

				case 4:
					System.out.println("\nELIMINAR CONTACTO\n");
					System.out.println("Introduce telefono del contacto:");
					long telefonoE = scan.nextLong();
					try {
						System.out.println(agenda.eliminaContacto(telefonoE));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;

				case 0:
					System.out.println("\n¡Hasta pronto!");
					break;

				default:
					break;
				}

			}

		}
	}
}
