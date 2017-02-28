package schroedere1.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene( new Scene(createContent()) );
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Parent createContent() {
        VBox mainLayout = new VBox();
        mainLayout.setPrefSize(600, 700);

        Label titleLabel = new Label("It's Tic Tac Toe!!!");
        titleLabel.setPrefHeight(100);
        titleLabel.setAlignment(Pos.CENTER);

        Pane ticTacToeGrid = new Pane();
        ticTacToeGrid.setPrefSize(600, 600);
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                Tile t = new Tile();

                t.setTranslateX(j * 200);
                t.setTranslateY(i * 200);

                ticTacToeGrid.getChildren().add(t);
            }
        }

        mainLayout.getChildren().addAll(titleLabel, ticTacToeGrid);

        return mainLayout;



    }

    private class Tile extends StackPane {

        Text text = new Text();

        public Tile(){
            Rectangle border = new Rectangle(200, 200);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(border);
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
