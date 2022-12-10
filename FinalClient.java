package application;

//Server class 
//@author gbrown62

/**Creating a Client that will that connects to the Server via a port that once the client is 
connected, performs the task of finding the data and outputting the results**/

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.TreeSet;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.SortedSet;
import java.util.HashMap;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.application.Application;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.Scene;


public class FinalClient extends Application {

	DataOutputStream speaking = null;
	DataInputStream hearing = null;
	
	private static ObservableList<String> textArea = FXCollections.observableArrayList();
	private static ObservableList<String> actualWord = FXCollections.observableArrayList();
	private static boolean initialexe = true;
	private static boolean initialoutput =true;
	private static boolean verifyMistakes = true;
	private static int port = 4690;
	private static int dataentry = 0;
	private static int AddEmUp = 0;
	private static Socket channel = null;



@Override // Override the start method in the Application class
public void start(Stage mainStage) {
	  try {
			AnchorPane access = new AnchorPane();
			AnchorPane space = new AnchorPane();
			Scene backgroundaccess = new Scene(access, 400, 150);
			Scene seeText = new Scene(space, 300, 610);

			Text fileAccess = new Text();
			fileAccess.setText("Weclome to Gabriel's Final Project");
			fileAccess.setLayoutX(100);
			fileAccess.setLayoutY(20);
			
			Text fileR = new Text();
			ListView<String> file0 = new ListView<String>();
			ListView<String> file01 = new ListView<String>();
			ListView<Integer> adder = new ListView<Integer>();

			fileR.setText("Choose Results To Get The \nTop 20 Most Used Words");
			fileR.setLayoutX(80);
			fileR.setLayoutY(20);
			
			//Add Button
			Button btnAdd = new Button();
			btnAdd.setOnAction(e -> mainStage.setScene(seeText));
			btnAdd.setText("Results");
			btnAdd.setLayoutX(115);
			btnAdd.setLayoutY(50);
			
			file0.setLayoutX(100); 
			file0.setLayoutY(90);
			file0.setMaxWidth(75);
			file0.setMinHeight(480);
			
			file01.setLayoutX(175);
			file01.setLayoutY(90);
			file01.setMaxWidth(60);
			file01.setMinHeight(480);
			
			//Calculator
			adder.setLayoutX(65);
			adder.setLayoutY(90);
			adder.setMaxWidth(35);
			adder.setMinHeight(480);

			// Exit button
			Button quitIt2 = new Button();
			quitIt2.setText("Exit");
			quitIt2.setLayoutX(220);
			quitIt2.setLayoutY(580);

			quitIt2.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					Alert warning = new Alert(AlertType.CONFIRMATION);
					warning.setHeaderText("Are you certain you want to disconnect?");
					warning.setTitle("Mayday! You're stopping the connection");
					
					if (AddEmUp == 0) {
						warning.setContentText("\t\tYou didn't even try! :-( Maybe next time.");
					} else if (AddEmUp == 1) { 
						warning.setContentText("\t\tYou have attempted the calculations " + AddEmUp + " time.");
					} else if (AddEmUp >= 5) { 
						warning.setContentText("\t\tYou have attempted the calculations " + AddEmUp + " time(s).");
					} else if (AddEmUp > 10) {
						warning.setContentText("Wow!!! " + AddEmUp + " times!\n\n\t\t\t    SOMEONE'S BORED!!! :-P\n ");
					} else {
						warning.setContentText("\t\tYou have attempted the calculations " + AddEmUp + " times.");
					}
					Optional<ButtonType> output = warning.showAndWait();
					if (output.get() == ButtonType.OK) {
						System.exit(0);
					} else {
						// The dialog closed/Cancel
					}
				}
			});
			
			//Clear All Button
			Button clearAll = new Button();
			clearAll.setOnAction(e -> mainStage.setScene(seeText));
			clearAll.setText("Clear All");
			clearAll.setLayoutX(40);
			clearAll.setLayoutY(580);

			clearAll.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					try {
						deleteTable();
					} catch (Exception e) {
						e.printStackTrace();
					}
					verifyMistakes=true;
					btnAdd.setText("Done");
					clearAll.setText("Deleted!");
					file0.getItems().clear();
					file01.getItems().clear();
					adder.getItems().clear();
					if (channel ==null) {
					clearAll.setText("Uh Oh!");
					}
				}
			});

			btnAdd.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					try {
						deleteTable();
						createTable();
						wordFind();
						displayData();
					} catch (Exception e) {
						System.out.print(e);
					}
					verifyMistakes=true;
					AddEmUp++;
					file0.getItems().clear();
					file01.getItems().clear();
					adder.getItems().clear();
					file0.getItems().addAll(actualWord);
					file01.getItems().addAll(textArea);
					btnAdd.setText("Done!");
					clearAll.setText("Deleted!");
					if(channel != null){
						for (int i = 1; i < 21; i++) {
						adder.getItems().addAll(i);
						}
					}if (channel ==null) {
						btnAdd.setText("Uh Oh!");
					}
				}
			});

			//MAIN MENU BUTTON
			Button home = new Button();
			home.setText("Main Menu");
			home.setLayoutX(120);
			home.setLayoutY(580);

			home.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					try {
						deleteTable();
					} catch (Exception e) {
						e.printStackTrace();
					}
					mainStage.setScene(backgroundaccess);
					mainStage.setTitle("FinalClient: Main Menu");
					btnAdd.setText("Results");
					clearAll.setText("Delete");
					file0.getItems().clear();
					file01.getItems().clear();
					adder.getItems().clear();
				}
			});
			
			Button otherHome = new Button();
			otherHome.setText("New Program");
			otherHome.setLayoutX(210);
			otherHome.setLayoutY(50);
			
			otherHome.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					otherHome.setText("Coming Soon!");
				}
			});
			
			Button quitIt = new Button();
			quitIt.setText("Exit");
			quitIt.setLayoutX(175);
			quitIt.setLayoutY(100);

			quitIt.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					Alert warning = new Alert(AlertType.CONFIRMATION);
					warning.setHeaderText("Are you certain you want to disconnect?");
					warning.setTitle("Mayday! You're stopping the connection");
					
					if (AddEmUp == 0) {
						warning.setContentText("\t\tYou didn't even try! :-( Maybe next time.");
					} else if (AddEmUp == 1) { 
						warning.setContentText("\t\tYou have attempted the calculations " + AddEmUp + " time.");
					} else if (AddEmUp >= 5) { 
						warning.setContentText("\t\tYou have attempted the calculations " + AddEmUp + " time(s).");
					} else if (AddEmUp > 10) {
						warning.setContentText("Wow!!! " + AddEmUp + " times!\n\n\t\t\t    SOMEONE'S BORED!!! :-P\n ");
					} else {
						warning.setContentText("\t\tYou have attempted the calculations " + AddEmUp + " times.");
					}
					Optional<ButtonType> output = warning.showAndWait();
					if (output.get() == ButtonType.OK) {
						System.exit(0);
					} else {
						// The dialog closed/Cancel
					}
				}
			});
			
			
			Button btnWordOc = new Button();
			btnWordOc.setText("Word Count");
			btnWordOc.setLayoutX(65);
			btnWordOc.setLayoutY(50);

			// (Main Menu) WordClient Button action
			btnWordOc.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					mainStage.setScene(seeText);
					mainStage.setTitle("FinalClient");
					otherHome.setText("New Program");
					quitIt.setText("Exit");
				}
			});

			access.getChildren().add(fileAccess);
			access.getChildren().add(btnWordOc);
			access.getChildren().add(otherHome);
			access.getChildren().add(quitIt);

			space.getChildren().add(btnAdd);
			space.getChildren().add(fileR);
			space.getChildren().add(adder);
			space.getChildren().add(file0);
			space.getChildren().add(file01);
			space.getChildren().add(clearAll);
			space.getChildren().add(home);
			space.getChildren().add(quitIt2);

			mainStage.setScene(backgroundaccess);
			mainStage.setTitle("FinalClient: Main Menu");
			mainStage.setResizable(false);
			mainStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
		    // Channel to connect to the server
		    channel = new Socket("localhost", port);
	
	
		    // Input and Output stream
		    hearing = new DataInputStream(channel.getInputStream());
		    speaking = new DataOutputStream(channel.getOutputStream());
		    }
		    catch (IOException e) {
		    	e.printStackTrace();
		    }	
		}

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	public Collection<Word> wordFind() throws Exception {
		actualWord.clear(); 
		textArea.clear();
		
		Map<String, Word> tracker = new HashMap<String, Word>();
		Document textFile = Jsoup.connect("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm").get();

		String wholeWord = textFile.body().text();

		BufferedReader readIt = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(wholeWord.getBytes(StandardCharsets.UTF_8))));
		String line;

		while ((line = readIt.readLine()) != null) {
			String[] everyText = line.split(" ");
			for (String word : everyText) {
				Word wordOccur = tracker.get(word);
				if (wordOccur == null) {
					wordOccur = new Word();
					wordOccur.word = word;
					wordOccur.addEm = 0;
					tracker.put(word, wordOccur);
				}
				wordOccur.addEm++;
			}
		}

		readIt.close();

		SortedSet<Word> sortText = new TreeSet<Word>(tracker.values());
		
		int addEm = 1;
		int maxText = 21;
		
		for (Word presentText : sortText) {

			if (addEm >= maxText) {
				break;
			}			
			
			//Send server info
			long TextAddS = presentText.addEm;
			speaking.writeUTF(presentText.word);
			speaking.writeLong(TextAddS);
			speaking.writeInt(addEm);
	        speaking.flush();
			
	        String sText = hearing.readUTF();
	       	Long sTextAdd= hearing.readLong();
	        int convertSWC = sTextAdd.intValue();
	        
	        //add data to sql
	        addData(sText,convertSWC);
			
	        addEm++;
		}
		return sortText;
	}

	//Counts how frequent each word is used 
	public static class Word implements Comparable<Word> {
		String word;
		int addEm;

		@Override
		public int compareTo(Word a) {
			return a.addEm - addEm;
		}
	}
	
	// The SQL connection

	public static Connection sqlConnect() throws Exception {

		String Driver = "MySQL JDBC Driver 2";
		String Database = "wordCount";
		String URL = "jdbc:mysql://localhost:3306/sys";
		String User = "root";
		String Secret = "password";

		Class.forName(Driver);
		
		try {
			Connection connect = DriverManager.getConnection(URL + Database, User, Secret);
			
			if (channel == null&&verifyMistakes == true) {
				System.out.println("\n\nError: Connection Failed\nTo Fix The Error: Close FinalClient, Start FinalServer, Start FinalClient");
				verifyMistakes=false;
			}else if (initialexe == true) {
				System.out.println("\nUser: " + User + " has Connected to MySQL Database: " + Database);
			}

			return connect;

		} catch (Exception e) {
			System.out.println("\n"+e+"\n");
		}
		return null;
	}
	
	//Creating a table to hold 2 columns and 20 rows within the MySql Database.
	public static void createTable() throws Exception { 
		try {
			Connection connect = sqlConnect();
			PreparedStatement design = connect.prepareStatement("CREATE TABLE IF IT DOESN'T EXISTS word(id int(20) NOT NULL AUTO_INCREMENT, Word varchar(255), Count int, PRIMARY KEY(id))");
			design.executeUpdate();
			
			connect.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	

	public static void deleteTable() throws Exception { // create table
		try {
			Connection connect = sqlConnect();
			PreparedStatement getRid = connect.prepareStatement("REMOVE TABLE IF EXISTS word;");																				
			getRid.executeUpdate();
			
			dataentry=0;
			initialexe = false;
			connect.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	

	public static void addData(String text, int Count) throws Exception {
		
		try {
			Connection connect = sqlConnect();
			PreparedStatement inputData = connect.prepareStatement("INSERT INTO word(Text, Count) VALUES ('" + text + "','" + Count + "')");
			inputData.executeUpdate();
			dataentry++;
			if(initialoutput==true) {
			System.out.println("\nSuccessful Entry into SQL Database:"); 
			initialoutput=false;
			}
			connect.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static ObservableList<String> displayData() throws Exception { 
		int addEm=1;
		try {
			// display to console
			Connection connect = sqlConnect();
			PreparedStatement declaration = connect.prepareStatement("SELECT Text, Count FROM word "); 
			ResultSet result = declaration.executeQuery();
			if (dataentry == 20) { // prevent redundancy
				
				
				System.out.println("\nData Received From Database:"); 
				System.out.println("\nTop 20 Most Used Words:");
				while (result.next()) {
					
					System.out.println(addEm+". "+result.getString("Text") + ", " +result.getString("Count"));//display data to console
					addEm++;
					actualWord.add(result.getString("Text")); // send word data to mysql
					textArea.add(result.getString("Count")); // send Occur data to mysql
					
				}
			}
			connect.close();
			return textArea;
		}catch (Exception e) {
			System.out.println("\n~~Error: Cannot Send/Receive Info FROM Database.~~");
			System.out.println(e);
		}
		return null;
	}
}