
package sokobanfx;

import database.DataBase;
import database.Person;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MenuAndroid {
	private static  double initX;
   private static  double initY;
	
	static public void iniciarMenu(final Stage menuStage, final Person personPlayer) {
		System.out.println("He abierto el Menu");
		
		//Creamos Objetos
		ImageView menuAndroidImagen = new ImageView(new Image("sokobanfx/images/menu/Android_Menu.png"));
		Button botonPlay = new Button("Let's Play");
		Button botonLogOut = new Button("Log out");
		Button botonSession = new Button("Change/Create Session");
		botonSession.setDisable(false);
		Label userNameLabel = new Label("Welcome " + personPlayer.userName);
				userNameLabel.setStyle("-fx-font-size: 20;-fx-font-family: \"Comic Sans MS\";");
                
                                Label credits = new Label("Programmed by Daniel Aguilar 2014");
				credits.setStyle("-fx-font-size: 15;-fx-font-family: \"Comic Sans MS\";");
		//Creamos Layoutl y añadimos objetos
		VBox vBox = new VBox(15);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(userNameLabel,botonPlay,botonSession,botonLogOut,credits);
		
		//Creamos root y añadimos objetos y layoults
		StackPane root = new StackPane();
		root.getChildren().add(menuAndroidImagen);
		root.getChildren().add(vBox);
		
		//creamos scene le añadimos el root, la dimension y la caractristica
		Scene scene = new Scene(root, 512, 597, Color.TRANSPARENT);
		
		//Creamos stage (que entra transparenciado) que es la que impulsa el sistema a crear la ventana y la mostramos
		menuStage.setTitle("Sokoban Ultimate"); /*No se ve porqu ees Color.Transparent  la scene*/
		menuStage.setScene(scene);
		
		//Eventos para mover el muñeco
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        initX = me.getScreenX() - menuStage.getX();
                        initY = me.getScreenY() - menuStage.getY();
                    }
                });
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        menuStage.setX(me.getScreenX() - initX);
                        menuStage.setY(me.getScreenY() - initY);
                    }
                });
		
		//Eventos Para Aplastadas de Boton
		botonPlay.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
					 Stage gameStage = new Stage(StageStyle.DECORATED);
					 gameStage.setResizable(true);
					 ManifestarLevel.iniciarNivel(gameStage, personPlayer);
					 menuStage.close();
					 gameStage.show();
                System.out.println("Se ha abierto el juego con exito");
            }
        });

		botonLogOut.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
					 Stage stageLogIn = new Stage(StageStyle.DECORATED);
					 DataBase.iniciarLogin(stageLogIn);
					 menuStage.close();
					 stageLogIn.show();
            }
        });
	}
}
