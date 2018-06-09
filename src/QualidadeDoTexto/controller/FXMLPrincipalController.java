
package QualidadeDoTexto.controller;

import QualidadeDoTexto.model.Ficheiros;
import QualidadeDoTexto.model.Inicializacoes;
import QualidadeDoTexto.model.Normalizar;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Domingos Carlos Dionísio
 */
public class FXMLPrincipalController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
//

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            new Inicializacoes();
        } catch (Exception ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handlerAbrirCorpora() throws Exception {
        Parent sobre = FXMLLoader.load(getClass().getResource("/QualidadeDoTexto/view/FXMLCorpora.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(sobre));
        stage.setTitle("Informação do córpora");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void handlerAbrirDiversidadeLexical() throws Exception {
        AnchorPane ap = (AnchorPane) FXMLLoader.load(getClass().getResource("/QualidadeDoTexto/view/FXMLVocabulario.fxml"));
        anchorPane.setTopAnchor(ap, 40.0);
        anchorPane.setBottomAnchor(ap, 0.0);
        anchorPane.setLeftAnchor(ap, 0.0);
        anchorPane.setRightAnchor(ap, 0.0);
        anchorPane.getChildren().add(ap);
    }

    public void handlerAbrirCoesaoECoerencia() throws Exception {
        AnchorPane ap = (AnchorPane) FXMLLoader.load(getClass().getResource("/QualidadeDoTexto/view/FXMLCoesaoECoerencia.fxml"));
        anchorPane.setTopAnchor(ap, 40.0);
        anchorPane.setBottomAnchor(ap, 0.0);
        anchorPane.setLeftAnchor(ap, 0.0);
        anchorPane.setRightAnchor(ap, 0.0);
        anchorPane.getChildren().add(ap);
    }

    public void handlerNormalizar() throws Exception {
        Normalizar.lista();
    }

    public void handlergGerarTextosFractais() throws Exception {
        Alert dialogoAviso;
        if (Ficheiros.gerarTextos()) {
            dialogoAviso = new Alert(Alert.AlertType.INFORMATION);
            dialogoAviso.setTitle("Informação");
            dialogoAviso.setHeaderText("Operação concluida com sucesso!");
            dialogoAviso.setContentText("Deve verificar os ficheiros criados na pasta específica(\n"
                    + "../Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/Bons (ou Maus))");
            dialogoAviso.showAndWait();
        } else {
            dialogoAviso = new Alert(Alert.AlertType.INFORMATION);
            dialogoAviso.setTitle("Erro!");
            dialogoAviso.setHeaderText("Operação não concluida!");
            dialogoAviso.setContentText("Um ou mais erros foram registados durante o processo.\n Verifica as configurações e tenta novamente.");
            dialogoAviso.showAndWait();
        }

    }

    public void handlerFractais() throws Exception {
        Ficheiros.fractais();
    }
}
