package io.github.SamPSS_Code.ui;

import io.github.SamPSS_Code.cache.CacheManager;
import io.github.SamPSS_Code.model.Comic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.nio.file.Path;

public class ComicCard extends VBox {

    private static final double CARD_WIDTH = 160;
    private static final double COVER_HEIGHT = 240;

    private final Comic comic;

    public ComicCard(Comic comic) {
        this.comic = comic;

        // Settings of the main card box (VBox)
        this.setPrefWidth(CARD_WIDTH);
        this.setMaxWidth(CARD_WIDTH);
        this.setSpacing(8); // space between cover and title
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(8));
        this.setCursor(Cursor.HAND); // Change mouse pointer

        // base style
        String defaultStyle = "-fx-background-color: #1a1a1a; -fx-background-radius: 10; -fx-border-color: #2a2a2a; -fx-border-radius: 10; -fx-border-width: 1;";
        String hoverStyle = "-fx-background-color: #242424; -fx-background-radius: 10; -fx-border-color: #e50914; -fx-border-radius: 10; -fx-border-width: 1.5;";
        this.setStyle(defaultStyle);

        //shadow below card
        DropShadow shadow = new DropShadow(10, Color.rgb(0, 0, 0, 0.5));
        this.setEffect(shadow);

        // cover image placeholder/real image
        StackPane coverContainer = createCoverView();

        // title text below cover
        Label titleLabel = new Label(comic.getTitle());
        titleLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 12px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true); // allows line break if the title its too big
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setPrefHeight(36); // standart heigh to align rows
        titleLabel.setMaxWidth(CARD_WIDTH - 16);

        this.getChildren().addAll(coverContainer, titleLabel);

        // interactive mouse hover effects
        this.setOnMouseEntered(e ->{
            this.setStyle(hoverStyle);
            this.setScaleX(1.04); // 4% height
            this.setScaleY(1.04); // 4% width
        });

        this.setOnMouseExited(e ->{
            this.setStyle(defaultStyle);
            this.setScaleX(1.0);
            this.setScaleY(1.0);
        });

        this.setOnMouseClicked(e ->{
            IO.println("comic selected " + comic.getTitle() + "(ID: " + comic.getId() + ")");
        });
    }

    private StackPane createCoverView(){
        StackPane container = new StackPane();
        container.setPrefSize(CARD_WIDTH - 16, COVER_HEIGHT);
        container.setMaxSize(CARD_WIDTH - 16, COVER_HEIGHT);
        container.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");

        File coverFile = CacheManager.getCoverFile(comic.getId());
        if (comic.isCoverCached() && coverFile.exists()){
            Image image = new Image(coverFile.toURI().toString(), CARD_WIDTH - 16, COVER_HEIGHT, false, true, true);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(CARD_WIDTH - 16);
            imageView.setFitHeight(COVER_HEIGHT);

            // ROUNDED BORDERS
            Rectangle clip = new Rectangle(CARD_WIDTH - 16, COVER_HEIGHT);
            clip.setArcWidth(12);
            clip.setArcHeight(12);
            imageView.setClip(clip);

            container.getChildren().add(imageView);
        } else {
            VBox placeholder = new VBox(5);
            placeholder.setAlignment(Pos.CENTER);

            Label formatBadge = new Label(comic.getFormat() != null ? comic.getFormat() : "COMIC");
            formatBadge.setStyle("-fx-background-color: #3d3d3d; -fx-text-fill: #aaaaaa; -fx-padding: 3 8 3 8;" +
                    " -fx-background-radius: 4; -fx-font-size: 11px; -fx-font-weight: bold;");

            Label noCoverText = new Label("no cover");

            placeholder.getChildren().addAll(formatBadge, noCoverText);
            container.getChildren().add(placeholder);
        }
        return container;

    }

    public Comic getComic(){
        return comic;
    }

}
