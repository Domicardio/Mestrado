/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QualidadeDoTexto;

import QualidadeDoTexto.model.Inicializacoes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author hp_probook
 */
public class FXMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent principal = FXMLLoader.load(getClass().getResource("view/FXMLPrincipal.fxml"));
        Scene scene = new Scene(principal);
        stage.setScene(scene);
        stage.setTitle("Qualidade do texto");
        stage.setMaximized(true);
 stage.show();
    }

    public static void main(String[] args) throws Exception {

        launch(args);

    }

}
