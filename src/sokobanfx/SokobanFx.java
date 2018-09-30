
package sokobanfx;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import database.DataBase;

/**
 * @author Paul y Daniel A.
 */

public class SokobanFx extends Application {

   public void start(Stage stageMenu) {
		System.out.println("He iniciado la aplicación con el login");	
		
		stageMenu = new Stage(StageStyle.DECORATED);
		
		DataBase.iniciarLogin(stageMenu);
		stageMenu.show();
		
	}
	 
	 public static void main(String[] args) {
        launch(args);
    }
	 
}