/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QualidadeDoTexto.controller;

import QualidadeDoTexto.model.Corpora;
import QualidadeDoTexto.model.Ficheiros;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

/**
 * FXML Controller class
 *
 * @author hp_probook
 */
public class FXMLCorporaController implements Initializable {

    @FXML
    RadioButton radioButtonCTextoBons;
    @FXML
    RadioButton radioButtonCTextoMaus;
    @FXML
    Label totalFicheiros;
    @FXML
    Label totalFrases;
    @FXML
    Label totalTokens;
    @FXML
    Label totalTypes;
    @FXML
    Label totalPalavras;

    private Corpora corpora;
    Ficheiros ficheiros = new Ficheiros();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void radioButtonSelected(ActionEvent e) throws Exception {
        if (radioButtonCTextoBons.isSelected()) {
           limpar();
            carregarCorpora("Bons");
        } else if (radioButtonCTextoMaus.isSelected()) {
           limpar();
            carregarCorpora("Maus");
        }

    }

    public void carregarCorpora(String tipo) throws Exception {
        corpora = ficheiros.getCorpora(tipo);
        totalFicheiros.setText(String.valueOf(corpora.getTotalFicheiros()));
        totalFrases.setText(String.valueOf(corpora.getTotalFrases()));
        totalTokens.setText(String.valueOf(corpora.getTotalTokens()));
        totalTypes.setText(String.valueOf(corpora.getTotalTypes()));
        totalPalavras.setText(String.valueOf(corpora.getTotalPalavras()));
    }

    public void limpar() {
        totalFicheiros.setText("0");
        totalFrases.setText("0");
        totalTokens.setText("0");
        totalTypes.setText("0");
        totalPalavras.setText("0");
    }
}
