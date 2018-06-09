/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QualidadeDoTexto.controller;

import QualidadeDoTexto.model.Vocabulario;
import QualidadeDoTexto.model.Ficheiros;
import QualidadeDoTexto.model.Metricas;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author hp_probook
 */
public class FXMLVocabularioController implements Initializable {

    Metricas metricas = new Metricas();
    Ficheiros ficheiros = new Ficheiros();
    @FXML
    AnchorPane anchoPane;
    @FXML
    private TableView<Vocabulario> tableViewDiversidadeLexical;
    @FXML
    private TableColumn<Vocabulario, String> tableColumnDLNomeFicheiros;
    @FXML
    private TableColumn<Vocabulario, Integer> tableColumnDLNumeroFrases;
    @FXML
    private TableColumn<Vocabulario, Integer> tableColumnDLNumeroTokens;
    @FXML
    private TableColumn<Vocabulario, Integer> tableColumnDLNumeroPalavras;
    @FXML
    private TableColumn<Vocabulario, Integer> tableColumnDLNumeroType;
    @FXML
    private TableColumn<Vocabulario, Double> tableColumnDLMtld;
    @FXML
    private TableColumn<Vocabulario, Double> tableColumnDLTaxaDeRepeticao;
    @FXML
    private TableColumn<Vocabulario, Integer> tableColumnDLVocabularioDeConteudo;
    @FXML
    private TableColumn<Vocabulario, Integer> tableColumnDLMaturidade;
    @FXML
    private TableColumn<Vocabulario, Integer> tableColumnDLDensidadeLexical;
    @FXML
    private RadioButton radioButtonDLTextoBons;
    @FXML
    private RadioButton radioButtonDLTextoMaus;
    ToggleGroup group = new ToggleGroup();
    private List<Vocabulario> listDiversidadeLexical;
    private ObservableList<Vocabulario> observableDiversidadeLexical;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void carregarTableViewDiversidadeLexical(String tipo) throws Exception {

        tableColumnDLNomeFicheiros.setCellValueFactory(new PropertyValueFactory<>("nomeDosFicheiros"));
        //tableColumnDLNumeroFrases.setCellValueFactory(new PropertyValueFactory<>("numeroFrases"));
        //tableColumnDLNumeroTokens.setCellValueFactory(new PropertyValueFactory<>("numeroTokens"));
        //tableColumnDLNumeroPalavras.setCellValueFactory(new PropertyValueFactory<>("numeroPalavras"));
        //tableColumnDLNumeroType.setCellValueFactory(new PropertyValueFactory<>("numeroType"));
        tableColumnDLMtld.setCellValueFactory(new PropertyValueFactory<>("mtld"));
        tableColumnDLTaxaDeRepeticao.setCellValueFactory(new PropertyValueFactory<>("taxaDeRepeticoes"));
        //tableColumnDLVocabularioDeConteudo.setCellValueFactory(new PropertyValueFactory<>("vocabularioDeConteudo"));
        tableColumnDLMaturidade.setCellValueFactory(new PropertyValueFactory<>("maturidade"));
        tableColumnDLDensidadeLexical.setCellValueFactory(new PropertyValueFactory<>("densidadeLexical"));

        listDiversidadeLexical = ficheiros.diversidadeLexical(tipo);

        observableDiversidadeLexical = FXCollections.observableArrayList(listDiversidadeLexical);
        tableViewDiversidadeLexical.setItems(observableDiversidadeLexical);
        listDiversidadeLexical.clear();
    }

    public void radioButtonSelected(ActionEvent e) throws Exception {
        if (radioButtonDLTextoBons.isSelected()) {
            tableViewDiversidadeLexical.setItems(null);
            carregarTableViewDiversidadeLexical("Bons");

        } else if (radioButtonDLTextoMaus.isSelected()) {
            tableViewDiversidadeLexical.setItems(null);

            carregarTableViewDiversidadeLexical("Maus");

        }
    }
     @FXML
    private void sair() {
        anchoPane.setVisible(false);
    }
}
