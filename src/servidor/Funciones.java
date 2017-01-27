package servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface Funciones extends Remote{
	
	public String muestraMenu()throws RemoteException;
	
	public boolean login(String usuario, String password) throws RemoteException;
	
	public HashMap<String, String> muestraContactos() throws RemoteException;
	
	public String nuevoContacto(String nombre, long telefono, String email) throws RemoteException;
	
	public String modificaContacto(long telefono) throws RemoteException;
	
	public String contactoModificado(long telefonoViejo, String nombre, long telefono, String email) throws RemoteException;
	
	public String eliminaContacto(long telefono) throws RemoteException;
	
}
