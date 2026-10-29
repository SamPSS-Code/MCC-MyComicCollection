package io.github.SamPSS_Code.ui;

import io.github.SamPSS_Code.cache.CacheManager;
import io.github.SamPSS_Code.database.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class App extends Application {

    private TilePane comicGrid;

    @Override
    public void init() throws Exception{
        //inicializador da base de dados
        DatabaseManager.initDatabase();
        CacheManager.initCache();
    }

    @Override
    public void start(Stage primaryStage){
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #121212;");

        // top bar
        HBox topBar = createTopBar();
        root.setTop(topBar);
        // center: scroll bar
        ScrollPane scrollPane = createCenterShelf();
        root.setCenter(scrollPane);

        //window configuration
        Scene scene = new Scene(root, 1150, 780);
        primaryStage.setTitle("MCC - My Comic Collection");
        primaryStage.setMinWidth(680);
        primaryStage.setMinHeight(500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopBar(){
        HBox topBar = new HBox(20); //20 pixels space between itens
        topBar.setPadding(new Insets(15, 25, 15, 25)); // inner margin
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #2d2d2d; -fx-border-width: 0 0 1 0;");

        // app title
        Label titleLabel = new Label("MCC");
        titleLabel.setStyle("-fx-text-fill: #e50914; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("My Comic Collection");
        subtitleLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");

        // item spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search for comic or publisher");
        searchField.setPrefWidth(280);
        searchField.setStyle(
                "-fx-background-color: #2b2b2b; "+
                "-fx-text-fill: white; " +
                "-fx-prompt-text-fill: #888888; "+
                "-fx-background-radius: 20; " +
                "-fx-padding: 8 15 8 15;"
        );
        topBar.getChildren().addAll(titleLabel, subtitleLabel, spacer, searchField);
        return topBar;
    }

    private ScrollPane createCenterShelf(){
        comicGrid = new TilePane();
        comicGrid.setPadding(new Insets(25)); // space between borders and itens
        comicGrid.setHgap(20); // horizontal space between each comic
        comicGrid.setVgap(25); // vertical space between each row
        comicGrid.setPrefColumns(5); // number of columns
        comicGrid.setAlignment(Pos.TOP_LEFT);
        comicGrid.setStyle("-fx-background-color: #121212;");



        io.github.SamPSS_Code.database.ComicDAO comicDAO = new io.github.SamPSS_Code.database.ComicDAO();
        java.util.List<io.github.SamPSS_Code.model.Comic> comics = comicDAO.getAll();

        for (io.github.SamPSS_Code.model.Comic comic : comics) {
            comicGrid.getChildren().add(new ComicCard(comic));
        }

        ScrollPane scrollPane = new ScrollPane(comicGrid);
        scrollPane.setFitToWidth(true); // obrligatory: tilePane forced to respect width
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // NO HORIZONTAL SCROLL BAR
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // VERTICAL SCROLL WHEN NEEDED
        scrollPane.setStyle("-fx-background: #121212; -fx-background-color: transparent;");

        return scrollPane;
    }

    public static void main(String[] args){
        launch(args);
    }
}
