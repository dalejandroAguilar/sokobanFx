package sokobanfx;

import database.CreateUser;
import database.DataBase;
import database.Person;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ManifestarLevel {
	
//Objetos a poner en el panel de status
private static Label steps = new Label();

private static int indexLevel;
//private static  StackPane root = new StackPane();
static ImageView guyImage;
static ImageView[] groupBoxImage;
//static ImageView[] groupBoxFlippyImage;

final static int L = 40; //Factor Escala

static EventHandler<KeyEvent> eventoMueveGuy;
static EventHandler<MouseEvent> eventoPezcadito;

public static void iniciarNivel(final Stage stageGame, final Person person){
		 //Cargamos todos los nivels de un url dado en un ArrayLis<String[]>
		 final ArrayList<String[]> level_All = LevelsManage.getAllFiles();
		 indexLevel = Integer.parseInt(person.progress);
		 final Map game = new Map(level_All.get(indexLevel));
		 
		 Button botonGoHome = new Button("Go Home");
                 Button credits = new Button("Programmed by Daniel Aguilar 2014");
		 final Pane pane = new Pane();
		 final VBox panelV = new VBox();
						panelV.setAlignment(Pos.CENTER);
						panelV.setStyle("-fx-background-color: \n" +
											 "#095a0c,\n" +
											 "linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
											 "linear-gradient(#20262b, #191d22),\n" +
											 "radial-gradient(center 50% 0%, radius 100%, rgba(130,131,125,0.9), rgba(255,255,255,0));");
		 final HBox panelH = new HBox();
		 final Scene scene = new Scene(panelH);
		 stageGame.setScene(scene);
		 stageGame.setTitle("Sokoban Ultimate Game");
	
		 Text texto = new Text("Welcome " + person.userName);
		      texto.setStyle("-fx-font-size: 16;-fx-fill: white;");

		 draw(game,pane); //Alteramos el pane y nada mas imprimiendo en ellos los elementos de juego
		 
		 //agregamos los objetos a los layoults
		 panelV.getChildren().addAll(texto,steps,botonGoHome,credits);
		 panelH.getChildren().addAll(pane,panelV);
		 
		 //audio
		 final AudioClip mainSong = new AudioClip(ManifestarLevel.class.getResource("sounds/Pocketful_Of_Sunshine.mp3").toString());
		 mainSong.play();
		 
		 //Creamos dos EventHandler de teclado y de mouse
                    eventoMueveGuy = new EventHandler<KeyEvent>(){
                             public void handle(KeyEvent key){
                                     if(key.getCode() == KeyCode.UP){game.move(Sokoban.UP); key.consume();}
                                                           switch(key.getText()){
                                                                     case "8":
                                                                     case "W":
                                                                     case "w":
                                                                             game.move(Sokoban.UP);
                                                                             break;
                                                                     case "6":
                                                                     case "D":
                                                                     case "d":
                                                                            game.move(Sokoban.RIGHT);
                                                                             break;
                                                                     case "5":
                                                                     case "S":
                                                                     case "s":
                                                                             game.move(Sokoban.DOWN);
                                                                             break;
                                                                     case "4":
                                                                     case "A":
                                                                     case "a":
                                                                             game.move(Sokoban.LEFT);
                                                                             break;
                                                                    case "U":
                                                                    case "u":
                                                                             game.undo();
                                                                             break;
                                                                    case "R":
                                                                    case "r":
                                                                             game.restart();
                                                                             break;
                                                                    case "Y":
                                                                    case "y":
                                                                             game.redo();
                                                                             break;
                                                           }

                                     reposition(game);

                                     if(game.win()){
                                                             FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), panelH);
                                                             panelH.removeEventHandler(KeyEvent.KEY_PRESSED,eventoMueveGuy);
                                                             pane.removeEventHandler(MouseEvent.MOUSE_CLICKED,eventoPezcadito);
                                                             fadeTransition.play();
                                                             fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                                                                     @Override 
                                                             public void handle(ActionEvent actionEvent) {
                                                                                     if(indexLevel < level_All.size()-1){
                                                                                             indexLevel++;
                                                                                             person.progress = String.valueOf(indexLevel);
                                                                                             CreateUser.updateFile(person);
                                                                                             game.setLevel(level_All.get(indexLevel));
                                                                                             draw(game,pane);
                                                                                             //scene.setRoot(panelH);
                                                                                             //stageGame.setScene(scene);
                                                                                             stageGame.close();
                                                                                             stageGame.show();
                                                                                     }else{
                                                                                             indexLevel=0;
                                                                                             game.setLevel(level_All.get(indexLevel));
                                                                                             draw(game,pane);
                                                                                             stageGame.close();
                                                                                             stageGame.show();
                                                                                     }
                                                                                     System.out.println("He cambiado de nivel");
                                                                                     panelH.addEventHandler(KeyEvent.KEY_PRESSED,eventoMueveGuy);
                                                                                     pane.addEventHandler(MouseEvent.MOUSE_CLICKED,eventoPezcadito);
                                                                                     System.out.println("He prendido los listeners");
                                                                             }
                                                                   });
                                                           } 
                                             }
                                     };
                    eventoPezcadito = new EventHandler<MouseEvent>(){
                             public void handle(MouseEvent me) {
                                           Path pathGuy = new Path();
                                           PathTransition pathTransition = new PathTransition(Duration.ZERO,pathGuy,guyImage);
                                           pathGuy.getElements().add(new MoveTo(game.guy.x*L+L/2,game.guy.y*L+L/2));
                                           int duration = 120; //duartion de la transiocion por cuadro

                                           System.out.println("Clicked on " + toRelative((int)me.getX(),(int)me.getY()));
                                           //Ponemos el if para ver si el click coincide con la posicion del guy, 
                                           //por que si eso ocurre no funcionará el código en el if

                                           if(!compare(toRelative((int)me.getX(),(int)me.getY()),new Point(game.guy.x,game.guy.y))){
                                           try {
                                                   PathFinding smartPath = new PathFinding(game,new Node(toRelative((int)me.getX(),(int)me.getY()).x,toRelative((int)me.getX(),(int)me.getY()).y));
                                                   smartPath.print();

                                                     for(int i=1; i< toDirection(smartPath.stack).length; i++){
                                                             game.move(toDirection(smartPath.stack)[i]);
                                                             pathGuy.getElements().add(new LineTo(smartPath.stack.get(i).x*L+L/2,smartPath.stack.get(i).y*L+L/2));
                                                     }

                                                     reposition(game);
                                                     pane.removeEventHandler(MouseEvent.MOUSE_CLICKED,eventoPezcadito);
                                                     panelH.removeEventHandler(KeyEvent.KEY_PRESSED,eventoMueveGuy);

                                                     pathTransition.setDuration(Duration.millis(duration*smartPath.stack.size()));
                                                     pathTransition.play();
                                                     pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                                                     @Override 
                                                                     public void handle(ActionEvent actionEvent) {
                                                                                   System.out.println("\nTermine de Moverme como pescadito");
                                                                                   pane.addEventHandler(MouseEvent.MOUSE_CLICKED,eventoPezcadito);
                                                                                   panelH.addEventHandler(KeyEvent.KEY_PRESSED,eventoMueveGuy);
                                                                                   System.out.println("He prendido los listeners\n");
                                                                     }
                                                            });

                                     } catch (Exception ex) {
                                                   System.out.println("No puedo ir por esa Ruta, se ha producido la excepcion: " + ex);
                                             }
                                           }
                             } 
                    };

		 //Le ponemos
		 panelH.addEventHandler(KeyEvent.KEY_PRESSED,eventoMueveGuy);
		 pane.addEventHandler(MouseEvent.MOUSE_CLICKED,eventoPezcadito);
		 
		 //Eventos de aplastar botones
		 botonGoHome.setOnAction(new EventHandler<ActionEvent>() {
			  public void handle(ActionEvent event) {
					Stage stageMenu = new Stage(StageStyle.DECORATED);
					MenuAndroid.iniciarMenu(stageMenu,person);
					stageGame.close();
					stageMenu.show();
			  }
		 });
	}
	
private static void draw(Map map,Pane pane){
		  
        pane.getChildren().clear();
		  steps.setText("0");
		  
		  ImageView[] groupFloorImage = new ImageView[map.allFloors().size()];
		  ImageView[] groupDeadFloorImage = new ImageView[map.allDeathSpaces().size()];
		  ImageView[] groupBrickImage = new ImageView[map.groupBrick.size()];
		  ImageView[] groupReceptacleImage = new ImageView[map.groupReceptacle.size()];
		  groupBoxImage = new ImageView[map.groupBox.size()];
		  guyImage = new ImageView("sokobanfx/images/guy/DOWN.png");
		  //Animador Fade de todos los Receptacles
		  FadeTransition[] fade = new FadeTransition[groupReceptacleImage.length];

        guyImage.setX(map.guy.x*L);
        guyImage.setY(map.guy.y*L);
        
		  for (int i = 0; i< groupFloorImage.length;i++){
               groupFloorImage[i] = new ImageView("sokobanfx/images/world/Floor_1.png");
               groupFloorImage[i].setX(map.allFloors().get(i).x*L);
               groupFloorImage[i].setY(map.allFloors().get(i).y*L);
               pane.getChildren().add(groupFloorImage[i]);
         }
		  
		  	for (int i = 0; i < groupDeadFloorImage.length;i++){
               groupDeadFloorImage[i] = new ImageView("sokobanfx/images/world/Dead_Floor_1.png");
               groupDeadFloorImage[i].setX(map.allDeathSpaces().get(i).x*L);
               groupDeadFloorImage[i].setY(map.allDeathSpaces().get(i).y*L);
               pane.getChildren().add(groupDeadFloorImage[i]);
         }
		  
        for (int i = 0; i<groupBrickImage.length;i++){
               groupBrickImage[i] = new ImageView("sokobanfx/images/world/Brick.png");
               groupBrickImage[i].setX(map.groupBrick.get(i).x*L);
               groupBrickImage[i].setY(map.groupBrick.get(i).y*L);
               pane.getChildren().add(groupBrickImage[i]);
         }
           
           for (int i = 0; i<groupReceptacleImage.length;i++){
               groupReceptacleImage[i] = new ImageView("sokobanfx/images/world/Receptacle.png");
               groupReceptacleImage[i].setX(map.groupReceptacle.get(i).x*L);
               groupReceptacleImage[i].setY(map.groupReceptacle.get(i).y*L);
               pane.getChildren().add(groupReceptacleImage[i]);
					
					//Cuestiones De Animacion Fade
					fade[i] = new FadeTransition(Duration.millis(500), groupReceptacleImage[i]);
				   fade[i].setAutoReverse(true);
					fade[i].setFromValue(2);
					fade[i].setToValue(0.5);
					fade[i].setCycleCount(Timeline.INDEFINITE);
           }
           
           for (int i = 0; i<groupBoxImage.length;i++){
				  if(!map.groupBox.get(i).isSlippy){
               groupBoxImage[i] = new ImageView("sokobanfx/images/world/Box.png");
               for(int j=0 ; j < map.groupReceptacle.size();j++)
                 if(map.groupReceptacle.get(j).status)
                     if(map.groupBox.get(i).x == map.groupReceptacle.get(j).x && map.groupBox.get(i).y == map.groupReceptacle.get(j).y)
                          groupBoxImage[i] = new ImageView("sokobanfx/images/world/Embonated.png");
               groupBoxImage[i].setX(map.groupBox.get(i).x*L);
               groupBoxImage[i].setY(map.groupBox.get(i).y*L);
               pane.getChildren().add(groupBoxImage[i]);
				  }
				  else{
					  groupBoxImage[i] = new ImageView("sokobanfx/images/world/Box_Flippy.png");
               for(int j=0 ; j < map.groupReceptacle.size();j++)
                 if(map.groupReceptacle.get(j).status)
                     if(map.groupBox.get(i).x == map.groupReceptacle.get(j).x && map.groupBox.get(i).y == map.groupReceptacle.get(j).y)
                          groupBoxImage[i] = new ImageView("sokobanfx/images/world/Embonated_Flippy.png");
               groupBoxImage[i].setX(map.groupBox.get(i).x*L);
               groupBoxImage[i].setY(map.groupBox.get(i).y*L);
               pane.getChildren().add(groupBoxImage[i]);
				  }
           }
			  
         pane.getChildren().add(guyImage);
			  
			for (int i = 0; i<fade.length;i++)	
			fade[i].play();
			
			System.out.println("He impreso el nuevo mapa en el pane");
    }

static void reposition(Map game){
		switch (game.guy.orientation){
			case Sokoban.UP:
				if (!game.guy.push)
					guyImage.setImage(new Image("sokobanfx/images/guy/TOP.png"));
				else
					guyImage.setImage(new Image("sokobanfx/images/guy/TOP_PUSH.png"));
				break;
			case Sokoban.DOWN:
				if (!game.guy.push)
					guyImage.setImage(new Image("sokobanfx/images/guy/DOWN.png"));
				else
					guyImage.setImage(new Image("sokobanfx/images/guy/DOWN_PUSH.png"));
				break;
			case Sokoban.RIGHT:
				if (!game.guy.push)
					guyImage.setImage(new Image("sokobanfx/images/guy/RIGHT.png"));
				else
					guyImage.setImage(new Image("sokobanfx/images/guy/RIGHT_PUSH.png"));
				break;
			case Sokoban.LEFT:
				if (!game.guy.push)
					guyImage.setImage(new Image("sokobanfx/images/guy/LEFT.png"));
				else
					guyImage.setImage(new Image("sokobanfx/images/guy/LEFT_PUSH.png"));
				break;
		}
		guyImage.setX(game.guy.x * L);
		guyImage.setY(game.guy.y * L);
		
		for (int i = 0; i< groupBoxImage.length;i++){
			if (!game.groupBox.get(i).isSlippy){
			if (game.isEmbonated(game.groupBox.get(i))){
				groupBoxImage[i].setImage(new Image("sokobanfx/images/world/Embonated.png"));
				final AudioClip embonatedSonund = new AudioClip(ManifestarLevel.class.getResource("sounds/Note1.wav").toString());
		      embonatedSonund.play();
			}else{
				if (game.isAlive(game.groupBox.get(i)))
					groupBoxImage[i].setImage(new Image("sokobanfx/images/world/Box.png"));
				else
					groupBoxImage[i].setImage(new Image("sokobanfx/images/world/Box_Dead.png"));
			}
			groupBoxImage[i].setX(game.groupBox.get(i).x * L);
			groupBoxImage[i].setY(game.groupBox.get(i).y * L);
			}
			else{
			if (game.isEmbonated(game.groupBox.get(i))){
				groupBoxImage[i].setImage(new Image("sokobanfx/images/world/Embonated_Flippy.png"));

			}else{
				if (game.isAlive(game.groupBox.get(i))){
					groupBoxImage[i].setImage(new Image("sokobanfx/images/world/Box_Flippy.png"));
					Path pathBoxFlippy = new Path();
					pathBoxFlippy.getElements().add(new MoveTo(3 * L ,3  * L ));
					pathBoxFlippy.getElements().add(new LineTo(game.groupBox.get(i).x * L + L/2 ,game.groupBox.get(i).y * L + L/2));
					PathTransition pathTransitionFlippy = new PathTransition(Duration.seconds(1),pathBoxFlippy, groupBoxImage[i]);
					pathTransitionFlippy.play();
				}
				else
					groupBoxImage[i].setImage(new Image("sokobanfx/images/world/Box_Flippy_Dead.png"));
			}
			groupBoxImage[i].setX(game.groupBox.get(i).x * L);
			groupBoxImage[i].setY(game.groupBox.get(i).y * L);
			}
		}
	steps.setText(Integer.toString(game.moves));
	}
	
private static Point toRelative(int absoluteX, int absoluteY){
		return new Point( absoluteX/L,  absoluteY/L);
	}
	
private static int[] toDirection(Stack<Node> stack){
		int[] result = new int[stack.size()];
		for(int i=1; i<result.length; i++){
			if (compare(delta(stack.get(i),stack.get(i-1)),new Point(0,-1)))
				result[i] = Sokoban.UP;
			if (compare(delta(stack.get(i),stack.get(i-1)),new Point(1,0)))
				result[i] = Sokoban.RIGHT;
			if (compare(delta(stack.get(i),stack.get(i-1)),new Point(0,1)))
				result[i] = Sokoban.DOWN;
			if (compare(delta(stack.get(i),stack.get(i-1)),new Point(-1,0)))
				result[i] = Sokoban.LEFT;
			}
		return result;
	}
	
public static boolean compare(Point point1, Point point2){
		if(point1.x == point2.x && point1.y == point2.y)
			return true;
		return false;
	}
	
private static Point delta(Node node1, Node node2){
		return new Point(node1.x-node2.x,node1.y-node2.y);
	}
}