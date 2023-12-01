package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.io.IOException;
import static java.lang.Double.NaN;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * JavaFX Weather App
 * @author Vilma Pirilä
 */
public class WeatherApp extends Application {

    // Events class object to interact with
    Events events;
    private Stage stage; 
    private VBox page;
    private TreeMap<String, Coord> top_5;
    private BorderPane root;
    private Scene scene1;
    private boolean favorite;
    private String units;
    private LocationWeather lastWeather;
    private Coord latLong;
    private String name;
    private String city;
    private Weather currentW;
    private HashMap<LocalDateTime, Weather> certainDayW;
    
    @Override
    public void start(Stage stage) throws InvalidUnitsException, APICallUnsuccessfulException, IOException {
        this.stage = stage;
        
        // This is called when interacting with Events interface/class
        events = new Events();        
        
        // When function implemented, '//' removed
        events.startup();
        
        //Creating a new BorderPane.
        root = new BorderPane();
        root.setPadding(new Insets(0, 0,0 , 0));
        
        //Adding button to the BorderPane and aligning it to the right.
        var quitBtn = getQuitButton();
        BorderPane.setMargin(quitBtn, new Insets(0, 0, 0, 0));
        Label openWeather= new Label("Weather data is provided by OpenWeather");
        AnchorPane bottom = new AnchorPane(); 
        bottom.getChildren().addAll(openWeather, quitBtn);
        AnchorPane.setLeftAnchor(openWeather, 5.0);
        AnchorPane.setRightAnchor(quitBtn, 5.0);
      
        root.setBottom(new AnchorPane(openWeather,quitBtn));
        BorderPane.setAlignment(quitBtn, Pos.TOP_RIGHT);
        
        //scene
        scene1 = new Scene(root, 600, 650); 
        stage.setScene(scene1);

        stage.setTitle("WeatherApp");
        stage.setOnCloseRequest(event -> {
            try {
                events.shut_down();
            } catch (IOException ex) {
                //handle IOException
            }
        });
        
        //shows the last searched place's weather
        root.setTop(upperMenu());
        try{
            lastWeather = events.get_last_weather();
        } catch (InvalidUnitsException ex) {
            getErrorWin("Invalid unit, must be metric or imperial");
        } catch (APICallUnsuccessfulException ex) {
            getErrorWin("Couldn't get weather information.\n" +
                " Check your network and try again");
        }
        
        if (lastWeather==null){
            units="metric";
            startScreen();
        }else{
            currentW= lastWeather.getCurrentWeather();
            units=lastWeather.getUnits();
            latLong= currentW.getCoord();
            favorite= events.is_favorite(latLong);
            name = currentW.getLocation();
            city = lastWeather.getCity_name();
            root.setCenter(show_start());  //!!!!!!!!!
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private VBox show_start() throws InvalidUnitsException, APICallUnsuccessfulException {
        //Creating a VBox.
        page = new VBox();
        page.getChildren().clear();
        page.getChildren().addAll( currentWeather(),weekDays());
        if (certainDayW != null){
            page.getChildren().add( weatherByHour());
        }
        
        return page;
    }
    
    private VBox upperMenu() throws InvalidUnitsException {
        //Creating a HBox for the logo, search bar and search button.
        HBox upperHBox = new HBox();
        upperHBox.setPrefHeight(40);
        upperHBox.setStyle("-fx-background-color: #232f75;");
        
        // placeholder for logo
        ImageView logo = getImage(12);
        logo.setFitWidth(50);
        logo.setFitHeight(50);
        Insets marginInsets3 = new Insets(15,0 , 10, 30);
        HBox.setMargin(logo, marginInsets3);
        Label space= new Label("            ");
        
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
        searchBtn.setMinWidth(60);
        Insets marginInsets1 = new Insets(20,0 , 10, 10);
        HBox.setMargin(searchBtn, marginInsets1);
        
        EventHandler<ActionEvent> buttonHandler = e -> {
            String place = search.getText();
                if(place.length()!=0){
                try {
                    top_5 = events.search(place);
                } catch (APICallUnsuccessfulException ex) {
                    //handle APICallUnsuccessfulException
                }
                    //new scene: the search results
                    //first lets check if there are no results
                    if(top_5==null){
                        //somekind of error: found 0 starting with given place
                    }else{
                        searchResult();
                    }
                }
        };
        searchBtn.setOnAction(buttonHandler);
        
        EventHandler<KeyEvent> enterKeyHandler = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                buttonHandler.handle(new ActionEvent());
                // Consume the event to prevent default behavior
                e.consume();
            }
        };
        search.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        
        Button unitBtn=getUnitBtn();
        unitBtn.setStyle("-fx-background-color: white;"
                + " -fx-background-radius: 5;");
        
        HBox.setMargin(unitBtn, marginInsets1);
        // add all elements to upperHBox
        upperHBox.getChildren().addAll(logo, search, searchBtn,space,
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
                moreFaves.setStyle("-fx-background-color: rgba(225, 225,225, 0.9);"
                + "-fx-background-radius: 10;");
                HBox.setMargin(moreFaves, marginFave2);
                
                moreFaves.setOnAction((ActionEvent e) -> {
                    ContextMenu contextMenu = new ContextMenu();
                    contextMenu.getItems().clear();
                    
                    //Show context menu below the text field
                    int counter2 =0;
                    for (var allFavorite : favoriteList.entrySet()) {
                        if(counter2<3){
                            counter2++;
                            continue;
                        }
                        Button allBtn= new Button(allFavorite.getKey());
                        allBtn.setMinWidth(contextMenu.getMaxWidth());
                        allBtn.setMaxWidth(contextMenu.getMaxWidth());
                        allBtn.setStyle("-fx-background-color: transparent;");
                        allBtn.setOnAction((ActionEvent f) -> {
                           try {
                                lastWeather=events.get_weather(allFavorite.getValue(), units);
                                currentW=lastWeather.getCurrentWeather();
                                name = currentW.getLocation();
                                city= allFavorite.getKey();
                                latLong =allFavorite.getValue();
                                favorite = events.is_favorite(latLong);

                            } catch (InvalidUnitsException | APICallUnsuccessfulException ex) {
                                //TODO: handle APICallUnsuccessfulException and InvalidUnitsException
                            }
                            try {
                                root.setCenter(show_start());
                            } catch (InvalidUnitsException | APICallUnsuccessfulException ex) {
                                //TODO: handle invalidUnitsException and APICallUnsuccesfulExcpetion
                            } 
                            
                        });
                        MenuItem item = new MenuItem("",allBtn);
                        contextMenu.getItems().add(item);
                    }
                    //location and if the context menu is on or off
                    double x = moreFaves.localToScreen(0, 0).getX();
                    double y = search.localToScreen(0, search.getHeight()+40).getY();
                    contextMenu.show(search, x, y);
                });
                
                lowerHBox.getChildren().add(moreFaves); 
                break;
            }
            
            Button favoriteButton = new Button(favoritePlace.getKey());
            favoriteButton.setStyle("-fx-background-color: rgba(225, 225,225, 0.9);"
                + "-fx-background-radius: 10;");
            
            //when clicked the favorite buttons it goes to it's place's weather view
            favoriteButton.setOnAction((ActionEvent e) -> {
                try {
                    lastWeather=events.get_weather(favoritePlace.getValue(), units);
                    currentW=lastWeather.getCurrentWeather();
                    name = currentW.getLocation();
                    city= favoritePlace.getKey();
                    latLong =favoritePlace.getValue();
                    favorite = events.is_favorite(latLong);
                    
                } catch (InvalidUnitsException | APICallUnsuccessfulException ex) {
                    //TODO: handle APICallUnsuccessfulException and InvalidUnitsException
                }
                try {
                    root.setCenter(show_start());
                } catch (InvalidUnitsException | APICallUnsuccessfulException ex) {
                    //TODO: handle invalidUnitsException and APICallUnsuccesfulExcpetion
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
    
    private void startScreen(){
        
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
        stackPane.setStyle("-fx-padding: 40;");
        
        root.setCenter(stackPane);
    }
    
    private StackPane currentWeather() {
        VBox grid = new VBox(5);
        grid.setStyle("-fx-background-color: #a0e6ff;"
                + " -fx-background-radius: 30;-fx-padding: 50;");
        
        // Set the alignment to center
        grid.setAlignment(javafx.geometry.Pos.CENTER);

        // Limit the maximum width of the VBox
        grid.setMaxWidth(500);
        grid.setMaxHeight(250);
        grid.setMinHeight(250);
        
        // Label that shows the area name, city and country
        //in top left and a favorite button in right
        grid.getChildren().add(topInfo());
        
        //temperature
        grid.getChildren().add(middleInfo());
        
        //get three stuff like wind, airquality and rain stuff
        grid.getChildren().add(bottomInfo());

        StackPane stackPane = new StackPane(grid);
        stackPane.setStyle("-fx-padding: 10;");
        
        return stackPane;
    }
    
    private HBox weekDays() throws InvalidUnitsException, APICallUnsuccessfulException{
        ArrayList<Button> buttons= new ArrayList();
        //HBox that presents all the days
        HBox daysHbox = new HBox(5);
        daysHbox.setAlignment(Pos.TOP_CENTER);
        daysHbox.setPrefSize(600,100);
        var days= lastWeather.getDays();
        Collections.sort(days, Comparator.naturalOrder());
        for(var day: days){
            //date
            //image
            //lowest and highest temperature
            String date_= day.getDayOfMonth()+"."+day.getMonthValue()+".";
            // get weather description of the weather in the middle of the day
            var weather=lastWeather.get_certain_day_weather(day);
            
            LinkedHashMap<LocalDateTime, Weather> sortedMap = new LinkedHashMap<>();
            weather.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
            
            var keys = new ArrayList<>(sortedMap.keySet());
            var middleKey = keys.get(keys.size() / 2);
            // Hae keskimmäinen arvo käyttäen keskimmäistä avainta
            var middleValue =sortedMap.get(middleKey);
            //image
            var image=getImage(middleValue.getWeather_id());
            image.setFitWidth(25);
            image.setFitHeight(25);

            int tempMin = (int) Math.round(lastWeather.getDayMinMax(day).getKey());
            int tempMax = (int) Math.round(lastWeather.getDayMinMax(day).getValue());
            String unit= getUnitFor("temperature");
            //var lowest=weather.
            Button dayBtn= new Button(date_+"\n"+tempMin+unit+"/"+tempMax+unit);
            buttons.add(dayBtn);
            
            dayBtn.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                    + "-fx-min-height: 70px; -fx-max-height: 70px;"
                    + "-fx-background-color: #a0e6ff;"
                    + "-fx-background-radius: 10;");
               
            dayBtn.setGraphic(image);
            
            dayBtn.setOnAction((ActionEvent e) ->{
                changeColor(buttons);
               dayBtn.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                    + "-fx-min-height: 70px; -fx-max-height: 70px;"
                    + "-fx-background-color: #a9c6f4;"
                    + "-fx-background-radius: 10;");
               certainDayW=sortedMap;
               page.getChildren().add(weatherByHour());
            });
            daysHbox.getChildren().add(dayBtn);
        }
        return daysHbox;
    }
    
    private ScrollPane weatherByHour(){
        //shows 24 hour forecast
        HBox weatherGrid = new HBox(20);
        weatherGrid.setPrefSize(500,200);
        weatherGrid.setAlignment(Pos.TOP_LEFT);
        weatherGrid.setStyle("-fx-background-color: white; -fx-padding: 10;");
       
        //we need to sort list that dates are in right order
        var keys = new ArrayList<>(certainDayW.keySet());
        Collections.sort(keys);
        for(int i=0;i< certainDayW.keySet().size();i++){
            //one collumn for one hour
            VBox hourForecast= new VBox(10);
            hourForecast.setMinWidth(60);
            hourForecast.setAlignment(Pos.TOP_CENTER);
            
            //hour label
            Label time=new Label("");
            time.setText(Integer.toString(keys.get(i).getHour()));           
            time.setStyle("-fx-background-color: white;");
            
            //get description of the weather that we can use to get the image
            var key = keys.get(i);
            var value = certainDayW.get(key);
            var image=getImage(value.getWeather_id());
            image.setFitWidth(30);
            image.setFitHeight(30);
            
            //temperature
            String tempUnit= getUnitFor("temperature");
            //no need for more specific temperature so we dont need decimals
            int roundedTemp = (int) Math.round(value.getCurrent_temp());
            Label temperature=new Label(Integer.toString(roundedTemp)+tempUnit);
            
            //get wind direction image
            ImageView windDirection= getImage(value.getWind_direction());
            windDirection.setFitWidth(30);
            windDirection.setFitHeight(30);
            
            Label windSpeed = new Label();
            String windUnit= getUnitFor("speed");
            windSpeed.setText(Long.toString((long) value.getWind_speed())+" "+windUnit);
            
            Label humidity = new Label();
            String humidityText= String.format("%.0f %%",currentW.getHumidity());
            humidity.setText(humidityText);
            
            //add all to the collumn
            hourForecast.getChildren().addAll(time, image, temperature,
                    windDirection,windSpeed,humidity);
            
            //if another day is clicked it removes the previous days hourly weather
            int size = page.getChildren().size();
            if(size==3){
                // Remove the last added element
                page.getChildren().remove(2);
            }
            //add the collumn to the HBox
            weatherGrid.getChildren().add(hourForecast);
        } 
        //add scroll feature
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(weatherGrid);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        //we dont need vertical scroll
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }
    
    private void searchResult(){
        //Creating an VBox.
        VBox results = new VBox(10);
        results.setPadding(new Insets(10, 10,10 , 10));
        results.setStyle("-fx-background-color: lightblue;");
        //add list of results that mach the input
        for(var entry : top_5.entrySet()){
            //1 button per place
            String key = entry.getKey();
            var parts = key.split(",");
            Coord coordinates = entry.getValue();
            String place= String.format(parts[0]+", "+parts[1]);
            Button result = new Button(place);
            result.setStyle("-fx-background-color: white;"
                + "-fx-background-radius: 10;");
            
            //action for button
            result.setOnAction((ActionEvent e) -> {
                try {
                    //get the right weather data
                    lastWeather = events.get_weather(coordinates,units);
                    currentW = lastWeather.getCurrentWeather();
                    name = currentW.getLocation();
                    latLong = entry.getValue();
                    city = parts[0];
                    favorite = events.is_favorite(entry.getValue());
                    root.setCenter(show_start());
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                } catch (APICallUnsuccessfulException ex) {
                    getErrorWin("Couldn't get weather information.\n" +
                                " Check your network and try again");
                }
            });
            results.getChildren().add(result);
        }
        root.setCenter(results);
    }
    
    private AnchorPane topInfo(){
        AnchorPane anchorPane = new AnchorPane();
        //has the area, city and country
        VBox nameInfo = new VBox();
        Label placeName = new Label("");
        Label cityName = new Label("");
        placeName.setText(name);
        placeName.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
        cityName.setText(city + ", " + currentW.getCountry());
        cityName.setStyle("-fx-font-size: 14px;");
        
        nameInfo.getChildren().addAll(placeName, cityName);
        
        nameInfo.setAlignment(Pos.CENTER_LEFT);
        
        //favorite button in the right upper corner
        Button addFavorite =getFavoriteButton();
        
        //layout is AnchorPane for this top row
        anchorPane.getChildren().addAll(nameInfo, addFavorite);
        AnchorPane.setLeftAnchor(nameInfo, 5.0);
        AnchorPane.setTopAnchor(nameInfo, 5.0);
        AnchorPane.setRightAnchor(addFavorite, 15.0);
        AnchorPane.setTopAnchor(addFavorite, 15.0);
        return anchorPane;
    }
    
    private VBox middleInfo(){
        VBox temperatures = new VBox();
        temperatures.setAlignment(Pos.CENTER);
        
        //image of the weather
        var image =getImage(currentW.getWeather_id());
        image.setFitWidth(60);
        image.setFitHeight(60);
        
        // Label that shows the current temperature
        Label temperature = new Label();
        temperature.setStyle("-fx-font-size: 23px;");
        
        // Label that shows the current temperature
        Label feelsLike = new Label();
        feelsLike.setStyle("-fx-font-size: 15px;");
        // Temperature and what it feels like in the center of the grid
        String unitString= getUnitFor("temperature");
        int roundedTemp = (int) Math.round(currentW.getCurrent_temp());
        temperature.setText(roundedTemp+ unitString);
        int roundedFeel = (int) Math.round(currentW.getFeels_like());
        feelsLike.setText("Feels like " + roundedFeel + unitString);
        //add temperature and what it feels like to the VBox
        temperatures.getChildren().addAll(image,temperature,feelsLike);
        return temperatures;
    }
    
    private HBox bottomInfo(){
        HBox bottomThree = new HBox(30);
        bottomThree.setAlignment(Pos.CENTER);
        
        //Air quality in bottom left
        String humidityString= String.format("humidity %.0f %%", currentW.getHumidity());
        Label humidity = new Label(humidityString);
        humidity.setStyle("-fx-font-size: 12px;");
        
        // rain stuff in bottom center
        HBox rainInfo = new HBox(5);
        rainInfo.setAlignment(Pos.CENTER);
        var rainImage= getImage(11);
        rainImage.setFitWidth(20);
        rainImage.setFitHeight(20);
        Double rain1h=currentW.getRain();
        Label rain = new Label(rain1h+" mm");
        if (rain1h.equals(NaN)){
            rain.setText("0.0 mm");
        }else{
            rain.setText(String.format("%.1f mm", rain1h));
        }
        
        rain.setStyle("-fx-font-size: 12px;");
        rainInfo.getChildren().addAll(rainImage,rain);
        
        //Label that shows current speed of the wind in bottom right
        HBox windInfo = new HBox(10);
        windInfo.setAlignment(Pos.CENTER);
        var windImage= getImage(10);
        windImage.setFitWidth(30);
        windImage.setFitHeight(30);
        String windS= getUnitFor("speed");
        Label windSpeed = new Label(Double.toString(currentW.
                getWind_speed())+" "+windS);
        windSpeed.setStyle("-fx-font-size: 12px;");
        double direction= currentW.getWind_direction();
        ImageView windDirImage= getImage(direction);
        windDirImage.setFitWidth(40);
        windDirImage.setFitHeight(40);
        windInfo.getChildren().addAll(windImage,windSpeed, windDirImage);
        
        bottomThree.getChildren().addAll(humidity, rainInfo, windInfo);
        return bottomThree;
    }
    
    private Button getFavoriteButton() {
        //create button
        Button addFavorite = new Button ();
        //set style
        addFavorite.setStyle("-fx-text-fill: white;-fx-background-color: #232f75;"
                + " -fx-background-radius: 30; -fx-font-weight: bold;");
        
        //button text
        if( favorite == false ){
            addFavorite.setText("Add to favorites");
        } else {
            addFavorite.setText("remove from favorites");
        }
        
        // action
        addFavorite.setOnAction((ActionEvent e) -> {
            if (favorite == false){
                events.add_favorite(latLong, city);
                favorite=events.is_favorite(latLong);
                try {
                    root.setTop(upperMenu());
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                }
            }else{
                events.remove_favorite(latLong, city);
                favorite= events.is_favorite(latLong);
                try {
                    root.setTop(upperMenu());
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                }
            }
            try {
                root.setCenter(show_start());
            } catch (InvalidUnitsException ex) {
                getErrorWin("Invalid unit, must be metric or imperial");
            } catch (APICallUnsuccessfulException ex) {
                getErrorWin("Couldn't get weather information.\n" +
                            " Check your network and try again");
            }
            
        });
        return addFavorite;
    }
    
    private ImageView getImage(int id){
        // Create Image and ImageView
        // first get weather icon, then drop icon for water,
        // wind icon for windspeed and direction
        // direction is shown as compass
        String pathFile;
        if (id == 600 || id == 601) {
            System.out.println(id);
            pathFile= "file:icons/snow.png";
        }else if(id == 800) {
            pathFile = "file:icons/clearsky.png";
        } else if (id == 801) {
            pathFile = "file:icons/fewclouds.png";
        } else if (id >= 803 && id <= 804) {
            pathFile = "file:icons/brokenclouds.png";
        } else if ((id >= 300 && id <= 321) || (id >= 520 && id <= 531)) {
            pathFile = "file:icons/showerrain.png";
        } else if ((id >= 500 && id <= 504) || (id >= 600 && id <= 622 && id != 616)) {
            pathFile = "file:icons/rain.png";
        } else if (id >= 200 && id <= 232) {
            pathFile = "file:icons/thunderstorm.png";
        } else if (id == 511 || (id >= 603 && id <= 615) || (id >= 617 && id <= 622)) {
            pathFile = "file:icons/snow.png";
        } else if (id >= 700 && id <= 771) {
            pathFile = "file:icons/mist.png";
        } else if (id == 804) {
            pathFile = "file:icons/brokenclouds.png";
        } else if (id == 616) {
            pathFile = "file:icons/rainandsnow.png";
        } else if (id == 781) {
            pathFile = "file:icons/tornado.png";
        } else if (id == 602) {
            pathFile = "file:icons/heavysnow.png";
        } else if (id == 802) {
            pathFile = "file:icons/scatteredclouds.png";
        } else if (id == 10) {
            pathFile = "file:icons/wind_icon.png";
        } else if (id == 11) {
            pathFile = "file:icons/water.png";
        } else if (id == 1) {
            pathFile = "file:icons/north.png";
        } else if (id == 4) {
            pathFile = "file:icons/west.png";
        } else if (id == 2) {
            pathFile = "file:icons/east.png";
        } else if (id == 3) {
            pathFile = "file:icons/south.png";
        } else if (id == 8) {
            pathFile = "file:icons/NW.png";
        } else if (id == 5) {
            pathFile = "file:icons/NE.png";
        } else if (id == 6) {
            pathFile = "file:icons/SE.png";
        } else if (id == 7) {
            pathFile = "file:icons/SW.png";
        }else if(id==12){
            pathFile = "file:icons/logo.png";
        } else {
            // Default case
            pathFile = "file:icons/showerrain.png";
        }  
        Image img = new Image(pathFile);
        ImageView imageView = new ImageView(img);

        return imageView;
    }

    private ImageView getImage(double direction){
        // converts degrees to directions
        // then gets the right images
        ImageView windDirImage;
        if(direction>22.5 && direction<=67.5){
            windDirImage= getImage(5);
        }else if(direction>67.5 && direction<=112.5){
            windDirImage= getImage(2);
        }else if(direction>112.5 && direction<=157.5){
            windDirImage= getImage(6);
        }else if ((direction>157.5 && direction<=202.5)){
            windDirImage= getImage(3);
        }else if(direction>202.5 && direction<=247.5){
            windDirImage= getImage(7);
        }else if(direction>247.5 && direction<=292.5){
            windDirImage= getImage(4);
        }else if ((direction>292.5 && direction<=337.5)){
            windDirImage= getImage(8);
        }else if(direction>337.5 || (direction>=0 && direction<=22.5)){
            windDirImage= getImage(1);
        }else{
            windDirImage= getImage(9);
        }
        return windDirImage;
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
        unitBtn.setOnAction((ActionEvent e) -> {
            if("metric".equals(units)){
                units = "imperial";
                try {
                    lastWeather=events.get_weather(latLong, units);
                    currentW=lastWeather.getCurrentWeather();
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                } catch (APICallUnsuccessfulException ex) {
                    getErrorWin("Couldn't get weather information.\n" +
                                " Check your network and try again");
                }
                
            }else{ 
                units = "metric";
                try {
                    lastWeather=events.get_weather(latLong, units);
                    currentW=lastWeather.getCurrentWeather();
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                } catch (APICallUnsuccessfulException ex) {
                    getErrorWin("Couldn't get weather information.\n" +
                                " Check your network and try again");
                }
            }
            try {
                root.setTop(upperMenu());
                root.setCenter(show_start());
            } catch (InvalidUnitsException ex) {
                getErrorWin("Invalid unit, must be metric or imperial");
            } catch (APICallUnsuccessfulException ex) {
                getErrorWin("Couldn't get weather information.\n" +
                            " Check your network and try again");
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
            try {
                events.shut_down();
            } catch (IOException ex) {
                //TODO: handle IOException
            }
            Platform.exit();
        });
        
        return button;
    }
    
    private void changeColor(ArrayList<Button> buttons) {
        for( Button button :buttons){
            button.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                    + " -fx-min-height: 70px; -fx-max-height: 70px;"
                    + "-fx-background-color: #a0e6ff;"
                    + " -fx-background-radius: 10;");
        }
    }
    
    private String getUnitFor(String info){
        switch (info) {
            case "temperature":
                if(units.equals("metric")) {
                    return "℃";
                } else {
                    return "°F";
                }
            case "speed":
                if(units.equals("metric")) {
                    return "m/s";
                } else {
                    return "mi/h";
                }
            default:
                return "Error";
        }
    }
    
    private void getErrorWin(String message){
        //shows error message window
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Error");
        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);      
        Scene scene = new Scene(label, 250, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
