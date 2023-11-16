package fi.tuni.prog3.weatherapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX Weather App
 */
public class WeatherApp extends Application {

    // Events class object to interact with
    Events events;
    
    @Override
    public void start(Stage stage) {
        
        // This is called when interacting with Events interface/class
        events = new Events();
        
        // When function implemented, '//' removed
        events.startup();
        
        //Creating a new BorderPane.
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0, 0,0 , 0));
        
        //Adding HBox to the center of the BorderPane.
        root.setCenter(getCenterVBox());
        
        //Adding button to the BorderPane and aligning it to the right.
        var quitButton = getQuitButton();
        BorderPane.setMargin(quitButton, new Insets(0, 0, 0, 0));
        root.setBottom(quitButton);
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        
        Scene scene = new Scene(root, 500, 600);                      
        stage.setScene(scene);
        stage.setTitle("WeatherApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private VBox getCenterVBox() {
        //Creating an VBox.
        VBox page = new VBox(0);
        
        //Adding all components to the VBox.
        page.getChildren().addAll(upperMenu(), currentWeather(), weekDays(), weatherByHour());
       
        return page;
    }
    
    private VBox upperMenu() {
        //Creating a HBox for the logo, search bar and search button.
        HBox upperHBox = new HBox();
        upperHBox.setPrefHeight(40);
        upperHBox.setStyle("-fx-background-color: #232f75;");
        
        // placeholder for logo
        Label appName= new Label("<logo>");
        appName.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        Insets marginInsets3 = new Insets(15,0 , 10, 30);
        HBox.setMargin(appName, marginInsets3);
        
        // textfield for the search bar
        TextField search = new TextField();
        search.setPromptText("search place...");
        search.setPrefWidth(200);
        Insets marginInsets = new Insets(20,0 , 10, 50);
        HBox.setMargin(search, marginInsets);
        
        //search button
        Button searchBtn=new Button("Search");
        searchBtn.setPrefWidth(54);
        Insets marginInsets1 = new Insets(20,0 , 10, 10);
        HBox.setMargin(searchBtn, marginInsets1);
        upperHBox.getChildren().addAll(appName, search, searchBtn);
        
        //HBox for favorite searches
        HBox lowerHBox = new HBox();
        lowerHBox.setPrefHeight(40);
        lowerHBox.setAlignment(Pos.TOP_LEFT);
        
        Insets marginFave1 = new Insets(6,6 , 10, 110);
        Insets marginFave2 = new Insets(0,6 , 10, 0);
        Label favorites= new Label("Favorites:");
        favorites.setStyle("-fx-text-fill: white;");
        HBox.setMargin(favorites, marginFave1);
        lowerHBox.getChildren().add(favorites);
        
        for(int i=0; i<3;i++){
            //placeholders for the real places
            Button fave= new Button("Tampere");
            HBox.setMargin(fave, marginFave2);
            lowerHBox.getChildren().add(fave);
        }
        
        //if there is lot of favorites, then they should be 
        //found behind this button
        Button moreFaves= new Button("more");
        HBox.setMargin(moreFaves, marginFave2);
        lowerHBox.getChildren().add(moreFaves);
        lowerHBox.setStyle("-fx-background-color: #232f75;");
        
        //VBox for search HBox and favourites HBox
        VBox centerHBox = new VBox(0);
        centerHBox.getChildren().addAll(upperHBox, lowerHBox);
        return centerHBox;
    }
    
    private GridPane currentWeather() {
        // Creating HBox for the current wearther
        GridPane grid = new GridPane();
        grid.setPrefSize(500,200);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(15,15,15,15));
        grid.setStyle("-fx-background-color: #a0e6ff;");

        // Label that shows the city (and coordinates if multiple same)
        //in top left
        Label placeName = new Label("Tampere");
        placeName.setStyle("-fx-font-size: 20px;");
        grid.add(placeName, 0,0);
        
        //Add to favorites button in top right
        Button addFav = new Button ("Add to favorites");
        GridPane.setValignment(addFav, VPos.TOP);
        GridPane.setHalignment(addFav, HPos.RIGHT);
        grid.add(addFav, 2, 0);
        
        // Temperature and what it feels like in the center of the grid
        VBox temperatures= new VBox();
        temperatures.setAlignment(Pos.CENTER);
        // Label that shows the current temperature
        Label temperature = new Label("12C");
        temperature.setStyle("-fx-font-size: 40px;");
        // Label that shows the current temperature
        Label feelsLike = new Label("Feels like: 12C");
        feelsLike.setStyle("-fx-font-size: 20px;");
        temperatures.getChildren().addAll(temperature,feelsLike);
        grid.add(temperatures, 1,1);
        
        //Air quality in bottom left
        Label airQuality = new Label("AQI: 23");
        airQuality.setStyle("-fx-font-size: 12px;");
        GridPane.setValignment(airQuality, VPos.BOTTOM);
        GridPane.setHalignment(airQuality, HPos.CENTER);
        grid.add(airQuality, 0,2);
        
        // rain stuff in bottom center
        Label rain = new Label("rain: 4%");
        rain.setStyle("-fx-font-size: 12px;");
        GridPane.setValignment(rain, VPos.BOTTOM);
        GridPane.setHalignment(rain, HPos.CENTER);
        grid.add(rain, 1,2);
        
        //Label that shows current speed of the wind in bottom right
        Label windSpeed = new Label("wind: 10,0km/h"+"   ->");
        windSpeed.setStyle("-fx-font-size: 12px;");
        //Label that shows current direction of the wind
        GridPane.setValignment(windSpeed, VPos.BOTTOM);
        GridPane.setHalignment(windSpeed, HPos.CENTER);
        grid.add(windSpeed, 2, 2);
        
        //column and row constraints to get grid looking clean
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();   
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();

        grid.getColumnConstraints().addAll(column1, column2, column3);
        grid.getRowConstraints().addAll(row1,row2,row3);
        
        //assign precentages for rows and collumns
        column1.setPercentWidth(30);
        column2.setPercentWidth(40);
        column3.setPercentWidth(30);
        row1.setPercentHeight(20);
        row2.setPercentHeight(60);
        row3.setPercentHeight(20);
        
        return grid;
    }
    
    private HBox weekDays(){
        //HBox that presents all the days
        HBox days = new HBox();
        days.setPrefSize(500,100);
        days.setAlignment(Pos.CENTER_LEFT);
        days.setStyle("-fx-background-color: #e4e4e4;");
        return days;
    }
    
    private GridPane weatherByHour(){
        //shows 24 hour forecast
        GridPane weatherGrid = new GridPane();
        weatherGrid.setPrefSize(500,200);
        weatherGrid.setAlignment(Pos.CENTER);
        weatherGrid.setStyle("-fx-background-color: #d9d9d9;");
        return weatherGrid;
    }
    
    private Button getQuitButton() {
        //Creating a button.
        Button button = new Button("Quit");
        
        // When function updated, '//' removed
        // events.shut_down()
        
        //Adding an event to the button to terminate the application.
        button.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        
        return button;
    }

}