package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.HashMap;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX Weather App
 * @author Vilma Pirilä
 */
public class WeatherApp extends Application {

    // Events class object to interact with
    Events events;
    private Stage stage; 
    private TreeMap<String, Coord> top_5;
    private BorderPane root;
    private Scene scene1;
    private boolean favorite;
    private String units;
    private LocationWeather lastWeather;
    private Coord latLong;
    private String name;
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        // This is called when interacting with Events interface/class
        events = new Events();        
        
        // When function implemented, '//' removed
        events.startup();
        
        //Creating a new BorderPane.
        root = new BorderPane();
        root.setPadding(new Insets(0, 0,0 , 0));
        
        //Adding button to the BorderPane and aligning it to the right.
        var quitButton = getQuitButton();
        BorderPane.setMargin(quitButton, new Insets(0, 0, 0, 0));
        root.setBottom(quitButton);
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        
        
        //scene
        scene1 = new Scene(root, 600, 650); 
        stage.setScene(scene1);

        stage.setTitle("WeatherApp");
        
        //shows the last searched place's weather
        lastWeather = events.get_last_weather();
        favorite = false;
        units = "metric";
        show_start();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private void show_start() {
        try {
            //uppermenu is always in same place
            root.setTop(upperMenu());
        } catch (InvalidUnitsException ex) {
            
        }
        //Creating an VBox.
        VBox page = new VBox(0);
        if (lastWeather==null){
            page.getChildren().addAll(startScreen());
        }else{
            page.getChildren().clear();
            page.getChildren().addAll( currentWeather(),
                weekDays(), weatherByHour());
        }
        
        root.setCenter(page);
        stage.show();
    }
    
    private void searchResults(){
        //Creating an VBox.
        VBox searches = new VBox(0);
        
        //Adding all components to the VBox.
        searches.getChildren().addAll(searchResult());
        root.setCenter(searches);
        stage.show();
    }
    
    private VBox upperMenu() throws InvalidUnitsException {
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
        search.setPromptText("Search place...");
        search.setPrefWidth(200);
        search.setStyle("-fx-background-color: white;"
                + " -fx-background-radius: 10;");
        Insets marginInsets = new Insets(20,0 , 10, 50);
        HBox.setMargin(search, marginInsets);
          
        //search button
        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: white;"
                + " -fx-background-radius: 10;");
        searchBtn.setPrefWidth(54);
        Insets marginInsets1 = new Insets(20,0 , 10, 10);
        HBox.setMargin(searchBtn, marginInsets1);
        
        EventHandler<ActionEvent> buttonHandler = e -> {
            String place = search.getText();
                if(place.length()!=0){
                    top_5 = events.search(place);
                    //new scene: the search results
                    //first lets check if there are no results
                    if(top_5==null){
                        //somekind of error: found 0 starting with given place
                    }else{
                        searchResults();
                    }
                }
        };
        searchBtn.setOnAction(buttonHandler);
        
        EventHandler<KeyEvent> enterKeyHandler = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                // Handle the action directly without using button.fire()
                buttonHandler.handle(new ActionEvent());
                // Consume the event to prevent default behavior
                e.consume();
            }
        };
        search.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        
        Button unitBtn=getUnitBtn();
        
        HBox.setMargin(unitBtn, marginInsets1);
        // add all elements to upperHBox
        upperHBox.getChildren().addAll(appName, search, searchBtn,
                unitBtn);
        
        //HBox for favorite searches
        HBox lowerHBox = new HBox();
        lowerHBox.setPrefHeight(40);
        lowerHBox.setAlignment(Pos.TOP_LEFT);
        
        Insets marginFave1 = new Insets(6,6 , 10, 110);
        Insets marginFave2 = new Insets(0,6 , 10, 0);
        Label favorites = new Label("Favorites:");
        favorites.setStyle("-fx-text-fill: white;");
        HBox.setMargin(favorites, marginFave1);
        lowerHBox.getChildren().add(favorites);
        var favoriteList = events.get_favourites();
        int counter =0;
        for (var favoritePlace : favoriteList.entrySet()) {
            if(counter == 3){
                //if there is lot of favorites, then they should be 
                //found behind this button
                Button moreFaves = new Button("more");
                moreFaves.setStyle("-fx-background-color: white;"
                + " -fx-background-radius: 10;");
                HBox.setMargin(moreFaves, marginFave2);
                lowerHBox.getChildren().add(moreFaves); 
                break;
            }
            
            var placeInfo = events.get_weather(favoritePlace.getValue(), units);
            Button favoriteButton = new Button(favoritePlace.getKey());
            favoriteButton.setStyle("-fx-background-color: rgba(225, 225,225, 0.9);"
                + "-fx-background-radius: 10;");
            
            //when clicked the favorite buttons it goes to it's place's weather view
            favoriteButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                    public void handle(ActionEvent e) {
                        try {
                            lastWeather=events.get_weather(favoritePlace.getValue(), units);
                            name = favoritePlace.getKey();
                            latLong =favoritePlace.getValue();
                            favorite = events.is_favorite(latLong);
                            
                        } catch (InvalidUnitsException ex) {
                        }
                        show_start();
                        }           
                });
            HBox.setMargin(favoriteButton, marginFave2);
            lowerHBox.getChildren().add(favoriteButton);
            counter++;
        }
        
        lowerHBox.setStyle("-fx-background-color: #232f75;");
        //VBox for search HBox and favourites HBox
        VBox centerHBox = new VBox(0);
        centerHBox.getChildren().addAll(upperHBox, lowerHBox);
        return centerHBox;
    }
    
    private StackPane startScreen(){
        // Creating HBox for the current wearther
        VBox box = new VBox();
        box.setPrefSize(400,500);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: rgba(35, 47,117, 0.7);"
                + "-fx-background-radius: 10;");
        
        //text
        Label infoText= new Label("Welcome to the starting page\n"
                +"of our Weather app. Get Started\n "+"by searching a city.\n");
        infoText.setStyle("-fx-text-fill: white;-fx-font-size: 30px;");
        
        box.getChildren().add(infoText);
        // Create a StackPane to contain the VBox
        StackPane stackPane = new StackPane(box);
        stackPane.setStyle("-fx-padding: 40;"); // Adjust padding as needed
        
        return stackPane;
    }
    
    private StackPane currentWeather() {        
        // Creating HBox for the current wearther
        VBox grid = new VBox(5);
        grid.setStyle("-fx-background-color: #a0e6ff;"
                + " -fx-background-radius: 30;-fx-padding: 7;");
        
        // Set the alignment to center
        grid.setAlignment(javafx.geometry.Pos.CENTER);

        // Limit the maximum width of the VBox
        grid.setMaxWidth(500);
        grid.setMaxHeight(220);
        grid.setMinHeight(220);
        
        // Label that shows the area name, city and country
        //in top left and a favorite button in right
        grid.getChildren().add(topInfo());
        
        //temperature
        grid.getChildren().add(middleInfo());
        
        //get three stuff like wind, airquality and rain stuff
        grid.getChildren().add(bottomInfo());

        StackPane stackPane = new StackPane(grid);
        stackPane.setStyle("-fx-padding: 20;");
        
        return stackPane;
    }
    
    private HBox weekDays(){
        //HBox that presents all the days
        HBox days = new HBox(10);
        days.setAlignment(Pos.CENTER);
        days.setPrefSize(500,100);
        
        for(int i=0; i<=3;i++){
            Button day= new Button("maanantai");
            day.setStyle("-fx-min-width: 100px; -fx-max-width: 60px;"
                    + " -fx-min-height: 80px; -fx-max-height: 70px;"
                    + "-fx-background-color: #a0e6ff; -fx-background-radius: 10;"
                    + "-fx-padding: 5;");
            days.getChildren().add(day);
        }
        
        return days;
    }
    
    private GridPane weatherByHour(){
        //shows 24 hour forecast
        GridPane weatherGrid = new GridPane();
        weatherGrid.setPrefSize(500,200);
        weatherGrid.setAlignment(Pos.CENTER);
        weatherGrid.setStyle("-fx-background-color: #d9d9d9;");
        
        Label time=new Label("");
        for(int i=1; i<=24;i++){
            if(i==24){
                time.setText("0");   
            }
            else{
                time.setText(Integer.toString(i));
            }
            
        }

        return weatherGrid;
    }
    
    private VBox searchResult(){
        //Creating an VBox.
        VBox results = new VBox();
        results.setPadding(new Insets(10, 10,10 , 10));
        results.setStyle("-fx-background-color: lightblue;");
        //add list of results that mach the input
        System.out.print(top_5.size());
        for(var entry : top_5.entrySet()){
            String key = entry.getKey();
            LocationWeather placeInfo;
            Coord coordinates = entry.getValue();
            Button result = new Button(key);
            result.setStyle("-fx-background-color: white;"
                + "-fx-background-radius: 10;");
            result.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    try {
                        //get the right weather data
                        lastWeather=events.get_weather(coordinates,units);
                        name =key;
                        latLong = entry.getValue();
                        favorite = events.is_favorite(coordinates);
                    } catch (InvalidUnitsException ex) {
                        //some error
                    }
                    //new scene: the search results
                    show_start();
                }
            });
            results.getChildren().add(result);
        }
        return results;
    }
    
    private AnchorPane topInfo(){
        AnchorPane anchorPane = new AnchorPane();
        //has the area, city and country
        VBox nameInfo = new VBox();
        var place = lastWeather.currentWeather;
        if(name==null){
            startScreen();
            return anchorPane;
        }
        var parts= name.split(",");
        Label placeName = new Label("");
        Label cityName = new Label("");
        cityName.setText(parts[0]+", "+parts[1]);
        if (!place.getLocation().equals(parts[0])){
            placeName.setText(place.getLocation());
            placeName.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
            cityName.setText(parts[0]+", "+parts[1]);
            cityName.setStyle("-fx-font-size: 14px;");
            //add to VBox
            nameInfo.getChildren().addAll(placeName, cityName);
        }else {
           cityName.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;"); 
           nameInfo.getChildren().addAll(cityName);
        }
        
        nameInfo.setAlignment(Pos.CENTER_LEFT);
        
        //favorite button in the right upper corner
        Button addFavorite =getFavoriteButton();
        
        //layout is AnchorPane for this top row
        
        anchorPane.getChildren().addAll(nameInfo, addFavorite);
        AnchorPane.setLeftAnchor(nameInfo, 10.0);
        AnchorPane.setRightAnchor(addFavorite, 10.0);
        
        return anchorPane;
    }
    
    private VBox middleInfo(){
        // Temperature and what it feels like in the center of the grid
        String unitString="";
        if(units.equals("metric")){
            unitString="℃";
        }else{
          unitString="°F";  
        }
        VBox temperatures = new VBox();
        temperatures.setAlignment(Pos.CENTER);
        
        //image of the weather
        var image =getImage(lastWeather.getCurrentWeather().getDescription());
        image.setFitWidth(60);
        image.setFitHeight(60);
        
        // Label that shows the current temperature
        Label temperature = new Label(Double.
                toString(lastWeather.currentWeather.getCurrent_temp())
                +unitString);
        temperature.setStyle("-fx-font-size: 23px;");
        
        // Label that shows the current temperature
        Label feelsLike = new Label("Feels like: "+ Double.
                toString(lastWeather.getCurrentWeather().getFeels_like())+
                unitString);
        feelsLike.setStyle("-fx-font-size: 15px;");
        
        //add temperature and what it feels like to the VBox
        temperatures.getChildren().addAll(image,temperature,feelsLike);
        return temperatures;
    }
    
    private HBox bottomInfo(){
        HBox bottomThree = new HBox(30);
        bottomThree.setAlignment(Pos.CENTER);
        
        //Air quality in bottom left
        Label airQuality = new Label("AQI: 23");
        airQuality.setStyle("-fx-font-size: 12px;");
        
        // rain stuff in bottom center
        Label rain = new Label("rain: 4%");
        rain.setStyle("-fx-font-size: 12px;");
        
        //Label that shows current speed of the wind in bottom right
        Label windSpeed = new Label("wind: 10,0km/h"+"   ->");
        windSpeed.setStyle("-fx-font-size: 12px;");
        
        bottomThree.getChildren().addAll(airQuality, rain, windSpeed);
        return bottomThree;
    }
    
    private Button getFavoriteButton() {
        Button addFavorite = new Button ();
        addFavorite.setStyle("-fx-text-fill: white;-fx-background-color: #232f75;"
                + " -fx-background-radius: 30; -fx-font-weight: bold;");
        if( favorite == false ){
            addFavorite.setText("Add to favorites");
        } else {
            addFavorite.setText("remove from favorites");
        }
        addFavorite.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (favorite == false){
                    events.add_favorite(latLong, name);
                    favorite= true;
                    try {
                        root.setTop(upperMenu());
                    } catch (InvalidUnitsException ex) {
                        
                    }
                   show_start();
                }else{
                    events.remove_favorite(latLong, lastWeather.getCurrentWeather().getCountry());
                    favorite= events.is_favorite(latLong);
                    try {
                        root.setTop(upperMenu());
                    } catch (InvalidUnitsException ex) {
                        
                    }
                    show_start();
                }
            }
        });
        return addFavorite;
    }
    
    private ImageView getImage(String description){
        // Create Image and ImageView
        String pathFile;
        switch (description) {
            case "clear sky":
                pathFile= "file:icons/clearsky.png";
                break;
            case "few clouds":
                pathFile= "file:icons/fewclouds.png";
                break;
            case "broken clouds":
                pathFile= "file:icons/brokenclouds.png";
                break;
            case "shower rain":
                pathFile= "file:icons/showerrain.png";
                break;
            case "rain":
                pathFile= "file:icons/rain.png";
                break;
            case "thunderstorm":
                pathFile= "file:icons/thunderstorm.png";
                break;
            case "snow":
                pathFile= "file:icons/snow.png";
                break;
            case "mist":
                pathFile= "file:icons/mist.png";
                break;
            case "overcast clouds":
                pathFile= "file:icons/brokenclouds.png";
                break;
            default:
                //until the images can be displayed smarter
                pathFile= "file:icons/showerrain.png";
                break;
        }
        Image img = new Image(pathFile);
        ImageView imageView = new ImageView(img);

        return imageView;
    }
    
    private Button getUnitBtn(){
       //units button
        String btnText;
        if("metric".equals(units)){
            btnText= "change to imperial";
        }else{
            btnText= "change to metric";
        }
        Button unitBtn = new Button(btnText);
        unitBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if("metric".equals(units)){
                    units = "imperial";
                }else{
                    units = "metric"; 
                }
                show_start();
            }
        }); 
        return unitBtn;
    }
    
    private Button getQuitButton() {
        //Creating a button.
        Button button = new Button("Quit");
        
        EventHandler<KeyEvent> enterKeyHandler = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                // Consume the event to prevent default behavior
                e.consume();
            }
        };
        // Add the Enter key handler to the other button
        button.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        
        
        
        //Adding an event to the button to terminate the application.
        button.setOnAction((ActionEvent event) -> {
            events.shut_down();
            Platform.exit();
        });
        
        return button;
    }
}
