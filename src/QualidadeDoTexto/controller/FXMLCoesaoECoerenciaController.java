/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QualidadeDoTexto.controller;

import QualidadeDoTexto.model.CoesaoECoerencia;
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
public class FXMLCoesaoECoerenciaController implements Initializable {

    Metricas metricas = new Metricas();
    Ficheiros ficheiros = new Ficheiros();
    @FXML
    private javafx.scene.control.Button btnSair;
    @FXML
    AnchorPane anchoPane;
    @FXML
    private TableView<CoesaoECoerencia> tableViewCoesaoECoerencia;
    @FXML
    private TableColumn<CoesaoECoerencia, String> tCDLNomeFicheiro;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLCoerenciaLocal;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLTransicaoSS;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLTransicaoSO;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLTransicaoOO;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLTransicaoOS;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLMOperadorLogicoPorFrase;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLDensidadeDePronomes;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLSobreposicaoDeSubstantivos;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLProporcaoDePronomes;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLSemelhancaEntreFrasesIF;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLSemelhancaEntreFrases;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLSobreposicaoDePalavras;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLPronomesPorSubstantivos;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLMTLDPalavrasDeConteudo;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLComprimentoDaPalavra;
    @FXML
    private TableColumn<CoesaoECoerencia, Double> tCDLComprimentoDaFrase;
    @FXML
    private RadioButton radioButtonDLTextoBons;
    @FXML
    private RadioButton radioButtonDLTextoMaus;
    ToggleGroup group = new ToggleGroup();
    private List<CoesaoECoerencia> listCoesaoECoerencia;
    private ObservableList<CoesaoECoerencia> observableCoesaoECoerencia;

    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void carregarTableViewCoesaoECoerencia(String tipo) throws Exception {

        tCDLNomeFicheiro.setCellValueFactory(new PropertyValueFactory<>("nomeDosFicheiros"));
        tCDLCoerenciaLocal.setCellValueFactory(new PropertyValueFactory<>("coerenciaPAcc"));
        tCDLTransicaoSS.setCellValueFactory(new PropertyValueFactory<>("trasicaoSS"));
        tCDLTransicaoSO.setCellValueFactory(new PropertyValueFactory<>("trasicaoSO"));
        tCDLTransicaoOO.setCellValueFactory(new PropertyValueFactory<>("trasicaoOO"));
        tCDLTransicaoOS.setCellValueFactory(new PropertyValueFactory<>("trasicaoOS"));
        tCDLMOperadorLogicoPorFrase.setCellValueFactory(new PropertyValueFactory<>("mediaDeOperadoresLogicosPorFrases"));
        tCDLDensidadeDePronomes.setCellValueFactory(new PropertyValueFactory<>("densidadeDePronomes"));
        tCDLSobreposicaoDeSubstantivos.setCellValueFactory(new PropertyValueFactory<>("mediaDaSobreposicaoDeSubstantivos"));
        tCDLProporcaoDePronomes.setCellValueFactory(new PropertyValueFactory<>("mediaDePronomesPorFrase"));
        tCDLSemelhancaEntreFrasesIF.setCellValueFactory(new PropertyValueFactory<>("similaridadeInicioFim"));
        tCDLSemelhancaEntreFrases.setCellValueFactory(new PropertyValueFactory<>("mediaDaSemelhancaSemanticaEntreFrases"));
        tCDLSobreposicaoDePalavras.setCellValueFactory(new PropertyValueFactory<>("sobreposicaoDePalavrasEmFrasesAdjcentes"));
        tCDLPronomesPorSubstantivos.setCellValueFactory(new PropertyValueFactory<>("razaoDePronomesPorSubsitantivos"));
        tCDLMTLDPalavrasDeConteudo.setCellValueFactory(new PropertyValueFactory<>("mtldPalavrasDeConteudos"));
        tCDLComprimentoDaPalavra.setCellValueFactory(new PropertyValueFactory<>("comprimentoMedioDaPalavra"));
        tCDLComprimentoDaFrase.setCellValueFactory(new PropertyValueFactory<>("comprimentoMedioDaFrase"));

        listCoesaoECoerencia = ficheiros.getCoesaoECoerencia(tipo);
            
        observableCoesaoECoerencia = FXCollections.observableArrayList(listCoesaoECoerencia);
        tableViewCoesaoECoerencia.setItems(observableCoesaoECoerencia);
        listCoesaoECoerencia.clear();
    }

    public void radioButtonSelected(ActionEvent e) throws Exception {
        if (radioButtonDLTextoBons.isSelected()) {
            tableViewCoesaoECoerencia.setItems(null);
            carregarTableViewCoesaoECoerencia("BonsPalavras");

        } else if (radioButtonDLTextoMaus.isSelected()) {
            tableViewCoesaoECoerencia.setItems(null);
            carregarTableViewCoesaoECoerencia("MausPalavras");
        }
    }

    @FXML
    private void sair() {
        anchoPane.setVisible(false);
        //Stage stage = (Stage) btnSair.getScene().getWindow(); //Obtendo a janela atual
        // stage.close(); //Fechando o Stage
    }
}
