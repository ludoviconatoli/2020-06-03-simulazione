/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	double x;
    	try {
    		x = Double.parseDouble(this.txtGoals.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Indica prima un numero minimo di goal");
    		return;
    	}
    	
    	model.creaGrafo(x);
    	this.txtResult.appendText("GRAFO CREATO\n\n");
    	this.txtResult.appendText("#vertici: " + model.getNVertici() +"\n");
    	this.txtResult.appendText("#archi: " + model.getNEdges());
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	this.txtResult.clear();
    	int k;
    	try {
    		k = Integer.parseInt(this.txtK.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima inserisci k, ossia un numero di giocatori");
    		return;
    	}
    	
    	List<Player> best = new ArrayList<>(model.getDreamTeam(k));
    	this.txtResult.appendText("DreamTeam: \n\n");
    	
    	for(Player p: best) {
    		this.txtResult.appendText(p.toString() + "\n");
    	}
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	this.txtResult.clear();
    	double x;
    	try {
    		x = Double.parseDouble(this.txtGoals.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Indica prima un numero minimo di goal");
    		return;
    	}
    	
    	Player best = model.getTopPlayer();
    	List<Adiacenza> bat = new ArrayList<>(model.getBattuti());
    	    	
    	if(best == null || bat == null) {
    		this.txtResult.setText("Prima crea il grafo");
    		return;
    	}
    	
    	this.txtResult.appendText("Giocatore migliore: " + best.toString() +"\n\n");
    	for(Adiacenza a: bat) {
    		this.txtResult.appendText(a.getP2().toString() +"| " + a.getPeso() +"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
