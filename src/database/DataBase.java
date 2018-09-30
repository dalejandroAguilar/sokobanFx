package database;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sokobanfx.MenuAndroid;

public class DataBase{


static public void iniciarLogin(final Stage loginStage) {
	//Creamos Objetos
	final PasswordField passwordField = new PasswordField();
							  passwordField.setPromptText("Enter your Password");
	final Label labelPassword = new Label("Input Password");
					labelPassword.setStyle("-fx-text-fill: black;");

	final Label labelUser = new Label("Select a user");
					labelUser.setStyle("-fx-text-fill: blue;");

	final TableView tableSession = new TableView();			
	TableColumn NIPColum = new TableColumn("NIP");
					//NIPColum.setResizable(false);
	TableColumn userColumn = new TableColumn<>("User Name");
					userColumn.setPrefWidth(120);
					//userColumn.setResizable(false);
	TableColumn progressColum = new TableColumn("Progress");
					//progressColum.setResizable(false);
	TableColumn scoreColum = new TableColumn("Score");
				  // scoreColum.setResizable(false);

	NIPColum.setCellValueFactory(
		 new PropertyValueFactory<Person,String>("NIP")
	);
	userColumn.setCellValueFactory(
		 new PropertyValueFactory<Person,String>("UserName")
	);
	progressColum.setCellValueFactory(
		 new PropertyValueFactory<Person,String>("Progress")
	);
	scoreColum.setCellValueFactory(
		 new PropertyValueFactory<Person,String>("Score")
	);

	ArrayList<Person> groupPerson = CreateUser.getAllUsers();
	final ObservableList<Person> data = FXCollections.observableArrayList(groupPerson);
	tableSession.setItems(data);
	tableSession.getColumns().addAll(NIPColum,userColumn,progressColum,scoreColum);
	Button botonStarSession = new Button("Start Session");
	
	//Añadir listener a la TablaView con El objeto actual cogemos el usuario
	tableSession.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(tableSession.getSelectionModel().getSelectedItem() != null) {
                    Person actualPerson = (Person)newValue;
                    labelUser.setText(actualPerson.userName);
						  passwordField.clear();
						  labelPassword.setStyle("-fx-text-fill: black;");
						  labelPassword.setText("Input password");
                }
            }
        });
	
	//		if(groupPerson.isEmpty())
	//		botonStarSession.setDisable(true);
	
			
	botonStarSession.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
					try {
						Person person = (Person)tableSession.getSelectionModel().getSelectedItem();
						System.out.println("El NIP del Usuario (o nombre de archivo): " + person.NIP);
						System.out.println("la contraseña ingresada: " + passwordField.getText());
						if(passwordField.getText().equals(person.password)){
							System.out.println("Contraseña correcta");
							labelPassword.setStyle("-fx-text-fill: green;");
							labelPassword.setText("Corectly Password");
							loginStage.close();
							Stage menuStage = new Stage(StageStyle.TRANSPARENT);
							MenuAndroid.iniciarMenu(menuStage,person);
							menuStage.show();
						}
						else{
							labelPassword.setStyle("-fx-text-fill: red;");
							labelPassword.setText("Wrong Password");
						}
					} catch (Exception ex) {
						System.out.println("No ha seleccionado ningun usuario: " + ex);
					}
                
            }
        });
		
	Button delete = new Button("Delete Session");
   delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
					try{
					System.out.println("El NIP es: " + ((Person)tableSession.getSelectionModel().getSelectedItem()).NIP);
					CreateUser.deleteTxt((Person)tableSession.getSelectionModel().getSelectedItem());
					data.remove((Person)tableSession.getSelectionModel().getSelectedItem());
					}catch(Exception ex){
						System.out.println("No ha seleccionado ningun usuario: " + ex);
					}
            }
        });
	
		Button botonNewSession = new Button("Create New Session");
      botonNewSession.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
					CreateUser.createUser(data);
            }
        });
	
	VBox panelV = new VBox(20);
		  panelV.getChildren().addAll(tableSession);
		  panelV.setPadding(new Insets(10, 10, 10, 10));
	VBox panelVButtons = new VBox(20);
		  panelVButtons.getChildren().addAll(labelUser,passwordField,labelPassword,botonStarSession,delete,botonNewSession);
		  panelVButtons.setPadding(new Insets(10, 10, 10, 10));
	HBox panelH = new HBox(20);
		  panelH.getChildren().addAll(panelV,panelVButtons);
		
	StackPane root = new StackPane();
	root.getChildren().add(panelH);
	Scene scene = new Scene(root);
	loginStage.setTitle("Sokoban Ultimate 1.0");
	loginStage.setScene(scene);
	loginStage.show();
	}
}

//import javafx.application.Application;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.ListView;
//import javafx.scene.control.PasswordField;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import javafx.util.Callback;
// 
//public class DataBase extends Application {
// 
//    ListView<String> list = new ListView<>();
//    ObservableList<String> data = FXCollections.observableArrayList(
//            "chocolate", "salmon", "gold", "coral", "darkorchid",
//            "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
//            "blueviolet", "brown");
//    final Label label = new Label();
// 
//    @Override
//    public void start(Stage stage) {
//	
//
//        VBox box = new VBox();
//        
//        stage.setTitle("ListViewSample");
//        box.getChildren().addAll(list,label);
//       // VBox.setVgrow(list, Priority.ALWAYS);
//		  Scene scene = new Scene(box, 200, 200);
//        stage.setScene(scene);
//		  
//        label.setLayoutX(10);
//        label.setLayoutY(115);
//        label.setFont(Font.font("Verdana", 20));
// 
//        list.setItems(data);
// 
//        list.setCellFactory(new Callback<ListView<String>, 
//            ListCell<String>>() {
//                @Override 
//                public ListCell<String> call(ListView<String> list) {
//                    return new ColorRectCell();
//                }
//            }
//        );
// 
//        list.getSelectionModel().selectedItemProperty().addListener(
//            new ChangeListener<String>() {
//                public void changed(ObservableValue<? extends String> ov, 
//                    String old_val, String new_val) {
//                        label.setText(new_val);
//                        label.setTextFill(Color.web(new_val));
//            }
//        });
//        stage.show();
//    }
//    
//    static class ColorRectCell extends ListCell<String> {
//        @Override
//        public void updateItem(String item, boolean empty) {
//            super.updateItem(item, empty);
//            Rectangle rect = new Rectangle(100, 20);
//            if (item != null) {
//                rect.setFill(Color.web(item));
//                setGraphic(rect);
//            }
//        }
//    }
//    
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
