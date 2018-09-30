package database;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateUser {
static String NIP;
static void createUser(final ObservableList<Person> data){
		//Creamos Objetos en la ventana createUser
		final TextField userNameField = new TextField();
							 userNameField.setPromptText("Enter your name");
		final PasswordField passwordField = new PasswordField();
								  passwordField.setPromptText("Enter a new password");
		final PasswordField passwordCheck = new PasswordField();
								  passwordCheck.setPromptText("Enter again your password");
		Button botonOk = new Button("Ok");
		//Creamos Layoults con un VBox Basta y añadimos los botones
		VBox panelV = new VBox(10);
			  panelV.getChildren().addAll(userNameField,passwordField,passwordCheck,botonOk);
		final Stage createUserStage = new Stage(StageStyle.TRANSPARENT);
				createUserStage.setScene(new Scene(panelV));
				
   botonOk.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
						if (!passwordField.getText().equals("") && !passwordField.getText().equals("") && passwordField.getText().equals(passwordCheck.getText())){
						createTxt(userNameField.getText(),passwordField.getText());
//   					data.clear();
//    			   data.addAll(FXCollections.observableArrayList(Person.getAllUsers()));	
						data.add(fromFileToPerson(new File("Users/User_" + NIP + ".txt")));
						createUserStage.close();
						}
						else
							System.out.println("No es válido el usuario");
            }
        });
		createUserStage.show();
	}	
	
private static void createTxt(String userName, String password){
		FileWriter fichero = null;
		PrintWriter printWriter = null;
		Date fecha = new Date();
		NIP = userName + fecha.getTime();

      try {
            fichero = new FileWriter("Users/User_" + NIP +".txt");
            printWriter = new PrintWriter(fichero);
			   printWriter.println(userName);
				printWriter.println(encript(password));
				printWriter.println("0");
				printWriter.println("0");
				System.out.println("He creado Archivo con exito");
        } catch (Exception e) {
           System.out.println("No se ha creado Archivo " + e);
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }

static void deleteTxt(Person personToDelete){
		File fichero = new File("Users/" + personToDelete.NIP);
		System.out.println("El NIP es: " + personToDelete.NIP);
		if(fichero.delete()){
			System.out.println("El usuario ha sido borrado con exito");
		}else
			System.out.println("No se ha borrado el archivo");
	}

static ArrayList<Person> getAllUsers(/*String url*/){
	//creamos directorio Users
		try{
			File directorio = new File("Users");
			directorio.mkdir();
			System.out.println("He creado Carpeta Users");
		}catch(Exception ex){
			System.out.println("No he creado carpeta Users");
		}
		
		ArrayList<Person> user_All = new ArrayList<>();
		
		String[] ficheros = getFicheros();
		for(String ficheroName:ficheros)
			user_All.add(fromFileToPerson(new File("Users/"+ficheroName)));
		
		return  user_All;
	}

static Person fromFileToPerson(File file){
		String[] user = new String[4];
		Person person = new Person(null, null, null, null);
			try{
				Scanner fileScanned = new Scanner(file);
				for(int i = 0 ; i < user.length; i++){
					user[i] = fileScanned.nextLine();
				}
			person = new Person(user[0], desencript(user[1]), user[2] ,user[3]);
			person.NIP = file.getName();
			}catch(Exception ex){
				System.out.println("Archivo no encontrado "+ex);
			 }
		return person;
	}
		
private static String[] getFicheros(){
	File directorio = new File("Users");
	String[] usersAll = directorio.list();
	for(String ficheroName:usersAll)
			System.out.println("Fichero recogido: " + ficheroName);
	return usersAll;
}
		
static String desencript (String string){
			char cadena[] = new char[string.length()];
			for (int i = 0; i<string.length();i++)
				cadena[i] = (char)(string.charAt(i) - 1) ;
			return String.copyValueOf(cadena);
		}

static String encript (String string){
			char cadena[] = new char[string.length()];
			for (int i = 0; i<string.length();i++)
				cadena[i] = (char)(string.charAt(i) + 1) ;
			return String.copyValueOf(cadena);
		}

public static void updateFile(Person personToUpdate){
	try(PrintWriter fileToUpdate = new PrintWriter(new File("Users/" + personToUpdate.NIP))){
		System.out.println("He encontrado el archivo ha actualizar");
		fileToUpdate.println(personToUpdate.userName);
		fileToUpdate.println(encript(personToUpdate.password));
		fileToUpdate.println(personToUpdate.progress);
		fileToUpdate.println(personToUpdate.score);
		System.out.println("He actualizado Archivo con exito");
		fileToUpdate.close();
	} catch (Exception e) {
		System.out.println("No encontrado el archivo ha actualizar: "+ e);
	}
}

}