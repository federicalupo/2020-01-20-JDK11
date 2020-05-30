package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola artisti connessi\n");
    	
    	//se seleziona artisti connessi e grafo non creato difficile da gestire (riesco a gestire solo la prima) => creo grafo automaticamente
    	String ruolo = this.boxRuolo.getValue();
    	
    	model.creaGrafo(ruolo);
    	
    	
    	if(model.archiDecrescenti().size()==0) {
    		txtResult.appendText("Nessuna coppia artisti");
    	}
    	else {
    		for(Arco a: model.archiDecrescenti()) {
    			txtResult.appendText(a+"\n");
    		}
    	}

    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo\n");
    	
    	String ruolo = this.boxRuolo.getValue();
    	
    	model.creaGrafo(ruolo);
    	txtResult.appendText("Numero vertici:   "+model.getNVertici()+"  Numero archi:   "+model.getNArchi()+"\n");
    }

    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxRuolo.getItems().addAll(model.listaRuoli());
    	this.boxRuolo.setValue(model.listaRuoli().get(0));
    	
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
