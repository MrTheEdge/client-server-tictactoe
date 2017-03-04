
package schroedere1.tictactoe;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="grid00"
    private ImageView grid00; // Value injected by FXMLLoader

    @FXML // fx:id="grid01"
    private ImageView grid01; // Value injected by FXMLLoader

    @FXML // fx:id="grid02"
    private ImageView grid02; // Value injected by FXMLLoader

    @FXML // fx:id="grid10"
    private ImageView grid10; // Value injected by FXMLLoader

    @FXML // fx:id="grid11"
    private ImageView grid11; // Value injected by FXMLLoader

    @FXML // fx:id="grid12"
    private ImageView grid12; // Value injected by FXMLLoader

    @FXML // fx:id="grid20"
    private ImageView grid20; // Value injected by FXMLLoader

    @FXML // fx:id="grid21"
    private ImageView grid21; // Value injected by FXMLLoader

    @FXML // fx:id="grid22"
    private ImageView grid22; // Value injected by FXMLLoader

    private Client client;
    private ImageView[] imageViews;
    private Image xImage;
    private Image oImage;
    private boolean isAllowedToMove;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert grid00 != null : "fx:id=\"grid00\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid01 != null : "fx:id=\"grid01\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid02 != null : "fx:id=\"grid02\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid10 != null : "fx:id=\"grid10\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid11 != null : "fx:id=\"grid11\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid12 != null : "fx:id=\"grid12\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid20 != null : "fx:id=\"grid20\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid21 != null : "fx:id=\"grid21\" was not injected: check your FXML file 'tictactoegui.fxml'.";
        assert grid22 != null : "fx:id=\"grid22\" was not injected: check your FXML file 'tictactoegui.fxml'.";

        client = new Client(7788);

        imageViews = new ImageView[]{
                grid00, grid01, grid02,
                grid10, grid11, grid12,
                grid20, grid21, grid22
        };

        isAllowedToMove = false;

        xImage = new Image(getClass().getResource("x.png").toString());
        oImage = new Image(getClass().getResource("o.png").toString());


        for (ImageView iv : imageViews){

        }
        for (int i = 0; i < imageViews.length; i++){
            int finalI = i;
            imageViews[i].setOnMouseClicked((MouseEvent c) -> {
                imageViewOnClick(finalI);
            });
        }

    }

    private void imageViewOnClick(int i) {
        // i is an index to the imageview array

        ImageView iv = imageViews[i];
        if (isAllowedToMove && iv.getImage() == null) { // It's image has not been set yet, so the slot is open.
            iv.setImage(oImage);

            // client.sendMove(i / 3, i % 3);

            isAllowedToMove = false;
        }
    }
}