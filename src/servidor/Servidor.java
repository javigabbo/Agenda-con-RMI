package servidor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Servidor implements Funciones {

	private Connection conexion;
	private Statement st;
	private ResultSet rs;
	Scanner scan = new Scanner(System.in);
	Properties propiedades = new Properties();
	InputStream entrada = null;
	String usuarioLogeado = "";
	Contacto contacto;

	public Servidor() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			entrada = new FileInputStream("configuracion.properties");
			propiedades.load(entrada);
			conexion = DriverManager.getConnection(propiedades.getProperty("url"), propiedades.getProperty("login"),
					"");
			System.out.println("Conectado a la base de datos\n");
			entrada.close();
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public static void main(String[] args) {

		Registry reg = null;
		try {
			System.out.println("Crea el registro de objetos, escuchando en el puerto 5555");
			reg = LocateRegistry.createRegistry(5555);
		} catch (Exception e) {
			System.out.println("ERROR: No se ha podido crear el registro");
			e.printStackTrace();
		}
		System.out.println("Creando el objeto servidor");
		Servidor serverObject = new Servidor();
		try {
			System.out.println("Inscribiendo el objeto servidor en el registro");
			System.out.println("Se le da un nombre único: Agenda");
			reg.rebind("Agenda", (Funciones) UnicastRemoteObject.exportObject(serverObject, 0));
		} catch (Exception e) {
			System.out.println("ERROR: No se ha podido inscribir el objeto servidor.");
			e.printStackTrace();
		}
	}

	@Override
	public boolean login(String usuario, String password) throws RemoteException {
		usuarioLogeado = usuario;
		boolean loginExitoso = false;
		String query = "SELECT * FROM agenda.usuarios";

		try {
			st = conexion.createStatement();
			rs = st.executeQuery(query);
			String rsPassword = "";
			String rsUser = "";

			while (rs.next()) {

				rsUser = rs.getString("username");
				rsPassword = rs.getString("password");

				if (rsUser.equals(usuario) && rsPassword.equals(password)) {
					loginExitoso = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginExitoso;

	}

	@Override
	public HashMap<String, String> muestraContactos() throws RemoteException {

		HashMap<String, String> contactos = new HashMap<String, String>();

		String query = "SELECT * FROM agenda.contactos WHERE username_fk = '" + usuarioLogeado + "'";

		try {
			st = conexion.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {

				String nombre = rs.getString("nombre");
				String email = rs.getString("email");
				String telefono = rs.getString("telefono");
				String info = "Nombre: " + nombre + "\nTelefono: " + telefono + "\nEmail: " + email;
				contactos.put(telefono, info);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return contactos;
	}

	@Override
	public String nuevoContacto(String nombre, long telefono, String email) throws RemoteException {
		String contactoCreado = "";
		String query = "INSERT INTO `agenda`.`contactos` (`username_fk`, `telefono`, `nombre`, `email`) " + "VALUES ('"
				+ usuarioLogeado + "', '" + telefono + "', '" + nombre + "', '" + email + "');";

		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		contactoCreado = "Nombre: " + nombre + "\nTelefono: " + telefono + "\nEmail: " + email;
		return contactoCreado;
	}

	@Override
	public String muestraMenu() {

		String textoMenu = "·················\nMENÚ PRINCIPAL\nEscribe 1 para ver tu agenda de contactos."
				+ "\nEscribe 2 para crear un nuevo contacto.\nEscribe 3 para editar un contacto existente."
				+ "\nEscribe 4 para eliminar un contacto existente."
				+ "\nEscribe 0 para cerrar sesión y salir del programa.\n·················";

		return textoMenu;

	}

	@Override
	public String modificaContacto(long telefono) throws RemoteException {
		String contactoAModificar = "";
		String query = "SELECT * FROM agenda.contactos WHERE username_fk = '" + usuarioLogeado + "' and telefono = '"
				+ telefono + "'";

		try {
			st = conexion.createStatement();
			rs = st.executeQuery(query);
			String nom = "";
			String tel = "";
			String mail = "";

			while (rs.next()) {
				nom = rs.getString("nombre");
				tel = rs.getString("telefono");
				mail = rs.getString("email");

			}

			contactoAModificar = "Contacto seleccionado:\n" + "Nombre: " + nom + "\nTelefono: " + tel + "\nEmail: "
					+ mail + "\n";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactoAModificar;
	}

	@Override
	public String contactoModificado(long telefonoViejo, String nombre, long telefono, String email) throws RemoteException {
		String query = "UPDATE `agenda`.`contactos` SET `telefono`='"+telefono+"', `nombre`='"+nombre+"', `email`='"+email+"'"
				+ " WHERE `telefono`='"+telefonoViejo+"';";

		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.executeUpdate();
			return "Se ha modificado el contacto";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";
	}

	

	@Override
	public String eliminaContacto(long telefono) throws RemoteException {
		String query = "DELETE FROM agenda.contactos WHERE telefono = '" + telefono + "'";

		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.executeUpdate();
			return "Se ha eliminado el contacto con telefono " + telefono;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";
	}

}
