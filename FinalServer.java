package application;

//Server class 
//@author gbrown62

/**Creating a Server that will deploy a GUI that does the calculations for the Client**/

import java.util.Optional;
import java.io.*;
import java.util.Date;
import java.net.*;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;


public class FinalServer extends Application {
	private static int port = 4690;
	private static int CountEmUp=0;

	@Override 
	public void start(Stage mainStage) {
		
	AnchorPane access = new AnchorPane(); 

	// Scroll activated Scene
	Scene background = new Scene(new ScrollPane(access), 490, 230);
	
	//Text space
	TextArea chat = new TextArea(); 
	
	
	// Stage title
	mainStage.setTitle("FinalServer"); 
	mainStage.setScene(background);
	mainStage.setResizable(false);
	
	//Display Stage
	mainStage.show(); 
	
	// Quit button
	Button quitIt = new Button();
	quitIt.setText("Quit Act");
	quitIt.setLayoutX(5);
	quitIt.setLayoutY(190);
	
	quitIt.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent arg0) {
			
			Alert warning = new Alert(AlertType.CONFIRMATION);
			warning.setHeaderText("Are you certain you want to disconnect?");
			warning.setTitle("Mayday! You're stopping the connection");
			
			if (CountEmUp == 0) {
				warning.setContentText("\t\tYou didn't even try! :-( Maybe next time.");
			} else if (CountEmUp == 1) { 
				warning.setContentText("\t\tYou have attempted the calculations " + CountEmUp + " time.");
			} else if (CountEmUp >= 5) { 
				warning.setContentText("\t\tYou have attempted the calculations " + CountEmUp + " time(s).");
			} else if (CountEmUp > 10) {
				warning.setContentText("Wow!!! " + CountEmUp + " times!\n\n\t\t\t    SOMEONE'S BORED!!! :-P\n ");
			} else {
				warning.setContentText("\t\tYou have attempted the calculations " + CountEmUp + " times.");
			}
			Optional<ButtonType> output = warning.showAndWait();
			if (output.get() == ButtonType.OK) {
				System.exit(0);
			} else {
				// The dialog closed/Cancel
			}
		}
	});
	
	access.getChildren().add(chat);
	access.getChildren().add(quitIt);
	
	new Thread( () -> {
	  try {
		//The server socket that will wait for requests to come in over the network.
	    ServerSocket ss = new ServerSocket(port);
	    Platform.runLater(() ->
	      chat.appendText("The Server started to connect at " + new Date() + " Port: " + port+"\nWating on Client...\n"));
	    
	    	// Listen for a connection request
	        Socket channel = ss.accept();
	  
	        // Data input and output streams
	        DataInputStream listening = new DataInputStream(
	          channel.getInputStream());
	        DataOutputStream talking = new DataOutputStream(
	          channel.getOutputStream());
	      

	        while (true) {
	    	String FinalServer = listening.readUTF();
	    	long CountEm = listening.readLong();
	    	int count = listening.readInt();
	    	
	    	if (count==1) {
	    		chat.appendText("\nSuccessful Retrieval!\n\nThe Top 20 Most Used Words Are: \n");
	    		CountEmUp++;
	    	}
	   
	       	talking.writeUTF(FinalServer);
	    	talking.writeLong(CountEm);
	    	 
	      Platform.runLater(() -> {
	    	  chat.appendText(count+".\t"+ FinalServer + ",\t"+CountEm+'\n'); 
	    	 
	      });
	     
	    }
	         
	  }
	  catch(IOException ex) {
	      ex.printStackTrace();
	      }
	    }).start();
	  }
	
	  public static void main(String[] args) {
	    launch(args);
	  }
}
