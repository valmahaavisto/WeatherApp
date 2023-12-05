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
import java.util.stream.Collectors;
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
    private TreeMap<String, Coord> top5 = new TreeMap<>();
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
    private LocalDateTime dayShown;
    private ContextMenu history;
    
    @Override
    public void start(Stage stage) throws InvalidUnitsException,
            APICallUnsuccessfulException, IOException {
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
        scene1 = new Scene(root, 660, 650); 
        stage.setScene(scene1);

        stage.setTitle("WeatherApp");
        stage.setOnCloseRequest(event -> {
            try {
                events.shutDown();
            } catch (IOException ex) {
                //handle IOException
            }
        });
        //shows the last searched place's weather
        root.setTop(menuBar());
        try{
            lastWeather = events.getLastWeather();
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
            favorite= events.isFavorite(latLong);
            name = currentW.getLocation();
            city = lastWeather.getCityName();
            root.setCenter(showStart());  //!!!!!!!!!
        }
        stage.show();
    }

    /**
     * main function launches the application
     * @param args 
     */
    public static void main(String[] args) {
        launch();
    }
    
    /**
     * creates the center of the whole application and updates the view of
     * forecast and searches 
     * preconditions: application is launched and all private methods are
     * initialized
     * postconditions: weather is shown to user and it is shown with right units
     * and other information
     * @return VBox containing current weather, days and possible 24 hour 
     * forecast of one day
     * @throws InvalidUnitsException if unit is not "metric" or "imperial"
     * @throws APICallUnsuccessfulException if apicall was not successful
     */
    private VBox showStart() throws InvalidUnitsException, APICallUnsuccessfulException {
        //Creating a VBox.
        page = new VBox();
        page.getChildren().clear();
        page.getChildren().addAll( currentWeather(),weekDays()); 
        
        if (certainDayW != null){
            page.getChildren().add( weatherByHour());
        }
        
        return page;
    }
    
    /**
     * builds whole menubar on top of the app where user can search places from
     * favorites search history or writing in the search
     * preconditions:GUI and Events are working and ready to add vbox in the
     * layout
     * postconditions: vbox is returned and errors are handled without program
     * crashing
     * @return Vbox containing logo, searchbar with search button, unit button,
     * shortcuts to favorite places, history showing when user writes in search
     * @throws InvalidUnitsException if unit is not "metric" or "imperial"
     */
    private VBox menuBar() throws InvalidUnitsException {
        //Creating a HBox for the logo, search bar and search button.
        AnchorPane menuSearch = new AnchorPane();
        menuSearch.setStyle("-fx-background-color: #232f75;");
        
        //logo
        ImageView logo = getImage(12);
        logo.setFitWidth(45);
        logo.setFitHeight(45);
        Button logoBtn= new Button();
        logoBtn.setGraphic(logo);
        logoBtn.setStyle("-fx-background-color: transparent;");
        AnchorPane.setLeftAnchor(logoBtn, 10.0);
        AnchorPane.setTopAnchor(logoBtn, 10.0);      
        logoBtn.setOnAction(e->{
            startScreen();
        });
        
        // textfield for searching places
        TextField search = new TextField();
        search.setPromptText("Search place...");
        search.setMinSize(170, 25.59);
        search.setMaxSize(170, 25.5);
        search.setStyle("-fx-background-color: white;"
                + " -fx-background-radius: 10 0 0 10;");
        
        //button to search
        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: #a0e6ff;"
                + " -fx-background-radius: 0 10 10 0;");
        searchBtn.setMaxSize(60, 25);
        searchBtn.setMinSize(60, 25);

        //put search components in good place
        AnchorPane.setTopAnchor(search, 15.1);
        AnchorPane.setLeftAnchor(search, 100.0);
        AnchorPane.setLeftAnchor(searchBtn, 270.0);
        AnchorPane.setTopAnchor(searchBtn, 15.0);
        
        // action for search button
        EventHandler<ActionEvent> buttonHandler = e -> {
            String place = search.getText();
                if(place.length()!=0){
                    try {
                        top5 = events.search(place);
                    } catch (APICallUnsuccessfulException ex) {
                        getErrorWin("Couldn't get weather information.\n" +
                        " Check your network and try again");
                    }
                    searchResult();
                }
        };
        searchBtn.setOnAction(buttonHandler);
        
        // Create a context menu for history
        history = new ContextMenu();
 
        search.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String input = search.getText().toLowerCase();
                history.getItems().clear();

                //Filter suggestions based on user input
                ArrayList<String> filteredSuggestions = (ArrayList<String>) 
                        events.getSearchHistory().stream()
                        .filter(suggestion -> suggestion.toLowerCase()
                                .startsWith(input))
                        .collect(Collectors.toList());
                
                // Show context menu below the text field
                for (var suggestion : filteredSuggestions) {
                    Button itemBtn = new Button(suggestion);
                    itemBtn.setStyle("-fx-background-color: transparent;");
                    itemBtn.setOnAction(e -> {
                            String place =suggestion;
                            if(place.length()!=0){
                                try{
                                    top5 = events.search(place);
                                } catch (APICallUnsuccessfulException ex) {
                                    getErrorWin("Couldn't get weather "
                                            + "information.\n" +
                                        " Check your network and try again");
                                }
                                searchResult();
                            }    
                    });
                    MenuItem item = new MenuItem("",itemBtn);
                    history.getItems().add(item);
                }
                //location and if the context menu is on or off
                double x = search.localToScreen(15, 0).getX();
                double y = search.localToScreen(0, search.getHeight())
                        .getY();
                history.show(search, x, y);
            }
        });
        
        //button to change metric and imperial units
        Button unitBtn=getUnitBtn();
        unitBtn.setStyle("-fx-background-color: white;"
                + " -fx-background-radius: 5;");
        AnchorPane.setRightAnchor(unitBtn, 20.0);
        AnchorPane.setTopAnchor(unitBtn, 15.0);
        
        // add all elements to menuSearch part
        menuSearch.getChildren().addAll(logoBtn, search, searchBtn,
                unitBtn);
        
        //HBox for favorite searches
        HBox favoriteHbox = new HBox(5);
        favoriteHbox.setMinHeight(33);
        favoriteHbox.setMaxHeight(33);
        favoriteHbox.setAlignment(Pos.TOP_LEFT);
        
        Insets marginFave1 = new Insets(0,0 , 0, 90);
        ImageView favoriteImg = new ImageView("file:icons/remove.png");
        favoriteImg.setScaleX(0.55);
        favoriteImg.setScaleY(0.50);
        Label favorites = new Label();
        favorites.setGraphic(favoriteImg);
        
        favorites.setStyle("-fx-text-fill: white;");
        HBox.setMargin(favorites, marginFave1);
        favoriteHbox.getChildren().add(favorites);
        
        // now we are ready to get the favourites
        var favoriteList = events.getFavorites();
        // 3 first show and then all can be found behind more button
        int counter =0;
        for (var favoritePlace : favoriteList.entrySet()) {
            if(counter == 3){
                // here is the more button coded
                Button moreFaves = new Button("more");
                moreFaves.setStyle("-fx-background-color: rgba(225, 225,225, 0.9);"
                + "-fx-background-radius: 10;");
                
                // create contextbox to display more favorites
                moreFaves.setOnAction((ActionEvent e) -> {
                    ContextMenu contextMenu = new ContextMenu();
                    contextMenu.getItems().clear();
                    
                    int counter2 =0;
                    for (var allFavorite : favoriteList.entrySet()) {
                        if(counter2<3){
                            counter2++;
                            continue;
                        }
                        String key = allFavorite.getKey();
                        var splitted = key.split(";");
                        Button moreBtn= new Button(splitted[0] + ", "+
                                splitted[1]);
                        moreBtn.setStyle("-fx-background-color: transparent;");
                        
                        //add action to the button to search the right place's 
                        //weather
                        moreBtn.setOnAction((ActionEvent f) -> {
                            try {
                                lastWeather = events.getWeather(allFavorite.getValue(), units);
                                currentW=lastWeather.getCurrentWeather();
                                name = currentW.getLocation();
                                city = splitted[0];
                                latLong = allFavorite.getValue();
                                favorite = events.isFavorite(latLong);
                                root.setCenter(showStart());
                            } catch (InvalidUnitsException ex) {
                                getErrorWin("Invalid unit, must be metric or imperial");
                            } catch (APICallUnsuccessfulException ex) {
                                getErrorWin("Couldn't get weather information.\n" +
                                " Check your network and try again");
                            }
                            
                        });
                        MenuItem item = new MenuItem("",moreBtn);
                        contextMenu.getItems().add(item);
                    }
                    //location and if the context menu
                    double x = moreFaves.localToScreen(0, 0).getX();
                    double y = search.localToScreen(0, search.getHeight()+40)
                            .getY();
                    contextMenu.show(search, x, y);
                });
                
                favoriteHbox.getChildren().add(moreFaves); 
                break;
            }
            var splittedKey = favoritePlace.getKey().split(";");
            Button favoriteButton = new Button(splittedKey[0] + ", " +
                    splittedKey[1]);
            favoriteButton.setStyle("-fx-background-color: rgba(225, 225,225, 0.9);"
                + "-fx-background-radius: 10;");
            
            //when clicked the favorite buttons it goes to it's place's weather view
            favoriteButton.setOnAction((ActionEvent e) -> {
              
                try {
                    lastWeather = events.getWeather(favoritePlace.getValue(), units);
                    currentW = lastWeather.getCurrentWeather();
                    name = currentW.getLocation();
                    city = splittedKey[0];
                    latLong = favoritePlace.getValue();
                    favorite = events.isFavorite(latLong);
                    root.setCenter(showStart());
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                } catch (APICallUnsuccessfulException ex) {
                    getErrorWin("Couldn't get weather information.\n" +
                        " Check your network and try again");
                }
            });
            favoriteHbox.getChildren().add(favoriteButton);
            counter++;
        }
        
        favoriteHbox.setStyle("-fx-background-color: #232f75;");
        //VBox for search HBox and favourites HBox
        VBox centerHBox = new VBox();
        centerHBox.getChildren().addAll(menuSearch, favoriteHbox);
        return centerHBox;
    }
    
    /**
     * Screen is shown first time opening the app
     * precondition: function can get the image wanted from getImage(int)
     * postcondition: starting screen is shown and user can use search bar
     */
    private void startScreen(){
        // Box tho show starting page
        VBox box = new VBox(50);
        box.setPrefSize(400,500);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: rgba(35, 47,117, 0.8);"
                + "-fx-background-radius:30;");
        
        //text
        Label infoText= new Label("Welcome to the starting page\n"
                +"of our Weather app. Get Started\n "+"by searching a city.\n");
        infoText.setStyle("-fx-text-fill: white;-fx-font-size: 25px;");
        
        //logo
        var logo= getImage(12); 
        logo.setFitWidth(120);
        logo.setFitHeight(120);
        box.getChildren().addAll(infoText, logo); 
        
        StackPane stack = new StackPane(box);
        stack.setMinSize(500, 400);
        stack.setMaxSize(500, 400);         
   
        root.setCenter(stack);
    }
    
    /**
     * returns current weather information in a StackPane
     * preconditions:place has been chosen from search or the program got last
     * weather from previous time
     * postconditions:current weather layout is ready and returned
     * @return stackpane containing all current weather information
     */
    private StackPane currentWeather() {
        //vbox for all components for current weather
        VBox grid = new VBox();
        grid.setStyle("-fx-background-color: #a0e6ff;"
                + " -fx-background-radius: 30;-fx-padding: 50;");
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setMaxWidth(500);
        grid.setMinWidth(500);
        grid.setMaxHeight(200);
        grid.setMinHeight(200);
        
        // Label that shows the area name, city and country
        //in top left and a favorite button in right
        grid.getChildren().add(topInfo());
        
        //temperature
        grid.getChildren().add(middleInfo());
        
        //get humidity, rain and wind info
        grid.getChildren().add(bottomInfo());

        StackPane stackPane = new StackPane(grid);
        stackPane.setStyle("-fx-padding: 10;");
        return stackPane;
    }
    
    /**
     * returns 5 day buttons in a row, buttons contain weather image and lowest
     * and highest temperatures of the days
     * preconditions: place has been chosen from search or the program got last
     * weather from previous time
     * postconditions: returns Hbox
     * @return HBox containing 5 day buttons with right information
     * @throws InvalidUnitsException if unit is not "metric" or "imperial"
     * @throws APICallUnsuccessfulException if the apicall was not successful
     */
    private HBox weekDays() throws InvalidUnitsException, APICallUnsuccessfulException{
        ArrayList<Button> buttons= new ArrayList();
        //HBox that presents all the days
        HBox daysHbox = new HBox(10);
        daysHbox.setMinHeight(60);
        daysHbox.setMaxHeight(60);
        daysHbox.setAlignment(Pos.CENTER);
        
        //get days and sort them
        var days= lastWeather.getDays();
        Collections.sort(days, Comparator.naturalOrder());
        for(var day: days){
            //date
            //image
            //lowest and highest temperature
            String date_= day.getDayOfMonth()+"."+day.getMonthValue()+".";
            // get weather description of the weather in the middle of the day
            var weather=lastWeather.getCertainDayWeather(day);
            
            LinkedHashMap<LocalDateTime, Weather> sortedMap =
                    new LinkedHashMap<>();
            weather.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sortedMap.put(entry.getKey(),
                        entry.getValue()));
            
            var keys = new ArrayList<>(sortedMap.keySet());
            var middleKey = keys.get(keys.size() / 2);
            // Hae keskimmäinen arvo käyttäen keskimmäistä avainta
            var middleValue =sortedMap.get(middleKey);
            // get image
            var image=getImage(middleValue.getWeatherID());
            image.setFitWidth(25);
            image.setFitHeight(25);

            // get minimum temperature and maximum temperature of the day
            int tempMin = (int) Math.round(lastWeather.getDayMinMax(day).getKey());
            int tempMax = (int) Math.round(lastWeather.getDayMinMax(day).getValue());
            String unit= getUnitFor("temperature");
            
            Button dayBtn= new Button(date_+"\n"+tempMin+unit+"/"+tempMax+unit);
            buttons.add(dayBtn);
            dayBtn.setAlignment(Pos.TOP_CENTER);
            dayBtn.setMinHeight(50);
            dayBtn.setMaxHeight(50);
            
            dayBtn.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                    + "-fx-min-height: 50px; -fx-max-height: 50px;"
                    + "-fx-background-color: #a0e6ff;"
                    + "-fx-background-radius: 10;");
            if (day.equals(dayShown)){
                //place this button lower
                dayBtn.setTranslateY(5);
                dayBtn.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                    + "-fx-min-height: 50px; -fx-max-height: 50px;"
                    + "-fx-background-color: #a9c6f4;"
                    + "-fx-background-radius: 10 10 0 0;");
                // get forecast of the 
                certainDayW=sortedMap;
                if (2 < page.getChildren().size()) {
                    page.getChildren().set(2,weatherByHour());
                }
                dayShown= day;
            }  
            dayBtn.setGraphic(image);
            
            // action for day button
            dayBtn.setOnAction((ActionEvent e) ->{  
                if(dayBtn.getStyle().contains("-fx-background-color: #a9c6f4;")){
                    dayBtn.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                    + "-fx-min-height: 50px; -fx-max-height: 50px;"
                    + "-fx-background-color: #a0e6ff;"
                    + "-fx-background-radius: 10 10 10 10;"); 
                    // if button is clicked twise the forecast disappears
                    dayShown=null;
                    certainDayW=null;
                    dayBtn.setTranslateY(0);
                    page.getChildren().remove(2);
                }else{
                    dayBtn.setTranslateY(5);
                    changeColor(buttons);
                    dayBtn.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                      + "-fx-min-height: 50px; -fx-max-height: 50px;"
                      + "-fx-background-color: #a9c6f4;"
                      + "-fx-background-radius: 10 10 0 0;");
                    // show thr forecast of the day
                    certainDayW=sortedMap;
                    page.getChildren().add(weatherByHour());
                    dayShown= day;
                }
               
            });
            daysHbox.getChildren().add(dayBtn);
        }
        return daysHbox;
    }
    
    /** returns scrollable component showing days 24 hour forecast
     * preconditions: day button is clicked.
     * postconditions:returns scrollpane
     * @return scollpane containing hour, image, temperature, compass,
     * wind speed and humidity
     */
    private ScrollPane weatherByHour(){
        //shows 24 hour forecast
        HBox weatherGrid = new HBox();
        weatherGrid.setPrefSize(500,260);
        weatherGrid.setAlignment(Pos.CENTER_LEFT);
        weatherGrid.setStyle("-fx-background-color: white; -fx-padding: 10;");
       
        //we need to sort list so dates are in right order
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
            
            //get id of the weather that we can use to get the image
            var key = keys.get(i);
            var value = certainDayW.get(key);
            ImageView image=getImage(value.getWeatherID());
            image.setFitWidth(30);
            image.setFitHeight(30);
            
            //temperature
            String tempUnit= getUnitFor("temperature");
            //no need for more specific temperature so we dont need decimals
            int roundedTemp = (int) Math.round(value.getCurrentTemp());
            Label temperature=new Label(Integer.toString(roundedTemp)+tempUnit);
            
            //get wind direction image
            ImageView windDirection= getImage(value.getWindDirection());
            windDirection.setFitWidth(30);
            windDirection.setFitHeight(30);
            //wind speed
            Label windSpeed = new Label();
            String windUnit= getUnitFor("speed");
            windSpeed.setText(Long.toString((long) value.getWindSpeed())+" "+windUnit);
            
            //get humidity
            Label humidity = new Label();
            String humidityText= String.format("%.0f %%",value.getHumidity());
            humidity.setText(humidityText);
            
            //add all to the collumn
            hourForecast.getChildren().addAll(time, image, temperature,
                    windDirection,windSpeed,humidity);
            
            //if another day is clicked it removes the previous days forecast
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
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }
    
    /**
     * search 5 or less placess that maches the input user has written in the 
     * searchfield
     * preconditions: search button has been pressed, input is not empty
     * postconditions:shows 5 or less places that match the user's input 
     * if nothing matches, it shows error message.
     */
    private void searchResult(){
        //Creating a VBox to display the results
        VBox results = new VBox(10);
        results.setPadding(new Insets(10, 10,10 , 10));
        results.setStyle("-fx-background-color: lightblue;");
        
        if (top5.isEmpty()){
            Label noMatches= new Label("no results :(\n"
                    + "try to search using different words");
            noMatches.setStyle("-fx-font-weight: bold;"
                    + "-fx-font-size: 15px;"
                    + "-fx-background-color: white;"
                    + "-fx-background-radius: 10px;"
                    + "-fx-padding:20;");
            
            results.getChildren().add(noMatches);
            results.setPadding(new Insets(20));
            root.setCenter(results);
            return ;
        }
                
        //add list of results that mach the input
        for(var entry : top5.entrySet()){
            // 1 button per place
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
                    lastWeather = events.getWeather(coordinates,units);
                    currentW = lastWeather.getCurrentWeather();
                    name = currentW.getLocation();
                    latLong = entry.getValue();
                    city = parts[0];
                    favorite = events.isFavorite(entry.getValue());
                    certainDayW = null;
                    root.setCenter(showStart());
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
    
    /**
     * gets top information of the current weather( what is shown in the top):
     * the place of the coodinates, name of the city and country. in right
     * upper corner also is button to add place to favorites or delete from
     * favorites.
     * preconditions:GUI is working and place has been chosen from search or 
     * the program is not used for the first time.
     * postconditions: AnchorPane is shown in top of the current weather forecast
     * @return  Anchorpane containing place, city, country and button
     */
    private AnchorPane topInfo(){
        // place name, favorite button
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
        AnchorPane.setLeftAnchor(nameInfo, -20.0);
        AnchorPane.setTopAnchor(nameInfo, 8.0);
        AnchorPane.setRightAnchor(addFavorite, -20.0);
        AnchorPane.setTopAnchor(addFavorite, 8.0);
        return anchorPane;
    }
    
    /**
     * gets middle info of the current weather( what is shown in the middle):
     * image of the weather, real temperature and what the temperature feels
     * like
     * preconditions: GUI is working and place has been chosen from search or 
     * the program is not used for the first time.
     * postconditions: Vbox is shown in the center of current weather forecast
     * @return  VBox containing image and temperatures
     */
    private VBox middleInfo(){
        // temperature and what it feels like and image
        VBox temperatures = new VBox();
        temperatures.setAlignment(Pos.CENTER);
        
        //image of the weather
        var image =getImage(currentW.getWeatherID());
        image.setFitWidth(50);
        image.setFitHeight(50);
        
        // Label that shows the current temperature
        Label temperature = new Label();
        temperature.setStyle("-fx-font-size: 20px;");
        
        // Label that shows what current temperature feels like
        Label feelsLike = new Label();
        feelsLike.setStyle("-fx-font-size: 15px;");
        
        // get right unit for temperatures
        String unitString= getUnitFor("temperature");
        int roundedTemp = (int) Math.round(currentW.getCurrentTemp());
        temperature.setText(roundedTemp+ unitString);
        int roundedFeel = (int) Math.round(currentW.getFeelsLike());
        feelsLike.setText("Feels like " + roundedFeel + unitString);
        
        //add image, temperature and what it feels like to the VBox
        temperatures.getChildren().addAll(image,temperature,feelsLike);
        return temperatures;
    }
    
    /**
     * gets bottom info of the current weather (what is shown in the bottom)
     * humidity, rain and wind speed and direction
     * preconditions: GUI is working and place has been chosen from search or 
     * the program is not used for the first time.
     * postconditions: vbox is shown in the bottom of the current weather 
     * forecast
     * @return HBox containing humidity, rain and wind information
     */
    private HBox bottomInfo(){
        //humidity, rain, wind
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
        Label windSpeed = new Label(Double.toString(currentW.getWindSpeed())+" "+windS);
        windSpeed.setStyle("-fx-font-size: 12px;");
        double direction= currentW.getWindDirection();
        ImageView windDirImage= getImage(direction);
        windDirImage.setFitWidth(40);
        windDirImage.setFitHeight(40);
        windInfo.getChildren().addAll(windImage,windSpeed, windDirImage);
        
        bottomThree.getChildren().addAll(humidity, rainInfo, windInfo);
        return bottomThree;
    }
    
    /**
     * returns button to add or delete a place from favorites
     * preconditions: favorite function is working, program knows if the place
     * is in favorites or not
     * postconditions: adds or removes favorite. if removed, place should
     * disappear from menubar. If added it should appear to menubar.
     * @return button to add/remove from favorites
     */
    private Button getFavoriteButton() {
        Button addFavorite = new Button ();
        addFavorite.setMaxSize(40, 30);
        addFavorite.setMinSize(40, 30);
        addFavorite.setStyle("-fx-text-fill: white;-fx-background-color: #232f75;"
                + " -fx-background-radius: 10; -fx-font-weight: bold;");
        
        //button text
        ImageView image;
        if( favorite == false ){
            image = new ImageView("file:icons/add.png");
        } else {
            image = new ImageView("file:icons/remove.png");
        }
        image.setScaleX(0.65);
        image.setScaleY(0.60); 
        addFavorite.setGraphic(image);
        // action
        addFavorite.setOnAction((ActionEvent e) -> {
            String nameString =city+";"+currentW.getCountry();
            if (favorite == false){
                events.addFavorite(latLong, nameString);
                favorite=events.isFavorite(latLong);
                try {
                    root.setTop(menuBar());
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                }
            }else{
                events.removeFavorite(latLong, nameString);
                favorite= events.isFavorite(latLong);
                try {
                    root.setTop(menuBar());
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                }
            }
            try {
                root.setCenter(showStart());
            } catch (InvalidUnitsException ex) {
                getErrorWin("Invalid unit, must be metric or imperial");
            } catch (APICallUnsuccessfulException ex) {
                getErrorWin("Couldn't get weather information.\n" +
                            " Check your network and try again");
            }
        });
        return addFavorite;
    }
    
    /**
     * finds a right image for wind direction, weather and other icons.
     * preconditions: id should be known.
     * postconditions: right image should be found and returned
     * @param id id of the weather or number assigned on wind directions and
     * other icons
     * @return imageview which can be used to show image
     */
    private ImageView getImage(int id){
        // Create Image and ImageView
        // first get weather icon, then drop icon for water,
        // wind icon for windspeed and direction
        // direction is shown as compass
        String pathFile;
        if (id == 600 || id == 601) {
            pathFile= "file:icons/snow.png";
        }else if(id == 800) {
            pathFile = "file:icons/clearsky.png";
        } else if (id == 801) {
            pathFile = "file:icons/fewclouds.png";
        } else if (id >= 803 && id <= 804) {
            pathFile = "file:icons/brokenclouds.png";
        } else if ((id >= 300 && id <= 321) || (id >= 520 && id <= 531)) {
            pathFile = "file:icons/showerrain.png";
        } else if ((id >= 500 && id <= 504) || (id >= 600 && id <= 622 &&
                id != 616)) {
            pathFile = "file:icons/rain.png";
        } else if (id >= 200 && id <= 232) {
            pathFile = "file:icons/thunderstorm.png";
        } else if (id == 511 || (id >= 603 && id <= 615) || (id >= 617 &&
                id <= 622)) {
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
            // if none of the top then something went wrong
            pathFile = "file:icons/error.png";
        }  
        Image img = new Image(pathFile);
        ImageView imageView = new ImageView(img);

        return imageView;
    }

    /**
     * gets image for wind direction as a compass.
     * converts direction as degrees to String format and calls getImage(int id)
     * to get the final imageview.
     * preconditions: when called this function the program should know the
     * wind direction as degrees
     * postconditions: right image should be found and returned
     * @param direction as degrees.
     * @return imageview that can be used to show image
     */
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
    
    /**
     * returns unit button. when clicked it changes the unit system to metric or
     * imperial
     * preconditions:GUI should be initialized and the private String units
     * should be "metric" or "imperial"
     * postconditions: unitbutton should be shown in the GUI
     * @return button to change units
     */
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
                    lastWeather=events.getWeather(latLong, units);
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
                    lastWeather=events.getWeather(latLong, units);
                    currentW=lastWeather.getCurrentWeather();
                } catch (InvalidUnitsException ex) {
                    getErrorWin("Invalid unit, must be metric or imperial");
                } catch (APICallUnsuccessfulException ex) {
                    getErrorWin("Couldn't get weather information.\n" +
                                " Check your network and try again");
                }
            }
            try {
                root.setTop(menuBar());
                root.setCenter(showStart());
            } catch (InvalidUnitsException ex) {
                getErrorWin("Invalid unit, must be metric or imperial");
            } catch (APICallUnsuccessfulException ex) {
                getErrorWin("Couldn't get weather information.\n" +
                            " Check your network and try again");
            }
        }); 
        return unitBtn;
    }
    
    /**
     * quit buttons action is declared here
     * calls Events to shut down and close the application
     * preconditions: GUI should be initialized
     * postconditions: quitbutton is shown in the GUI
     * @return quit button
     */
    private Button getQuitButton() {
        Button button = new Button("Quit");
        
        //Adding an event to the button to terminate the application.
        button.setOnAction((ActionEvent event) -> {
            try {
                events.shutDown();
            } catch (IOException ex) {
                //TODO: handle IOException
            }
            Platform.exit();
        });
        return button;
    }
    
    /**
     * changes all the day buttons colors and places them in original style and
     * place. If one button is pressed it's color is different and it returns it
     * back to the same row as others
     * preconditions: one day button has been pressed
     * postcondition: all buttons are in same row and same style
     * @param buttons arraylist of all the 5 day buttons
     */
    private void changeColor(ArrayList<Button> buttons) {
        // part of the day buttons' color and placing 
        for( Button button :buttons){
            if (button.getStyle().contains("-fx-background-color: #a9c6f4;")){
                //button back to its place
                button.setTranslateY(0);
            }
            button.setStyle("-fx-min-width: 115px; -fx-max-width: 115px;"
                    + " -fx-min-height: 50px; -fx-max-height: 50px;"
                    + "-fx-background-color: #a0e6ff;"
                    + " -fx-background-radius: 10;"
            );
        }
    }
    
    /**
     * gets imperial or metric unit Strings for temperature or wind speed
     * precondition: function should know witch kind of unit it wants. If unit
     * is metric then it gets metric units for speed and temperature. If unit is
     * imperial it gets imperial units.
     * postcondition: right units are shown on the screen
     * @param info String tells if temperature's unit is needed or speed's unit 
     * is needed
     * @return unit as String for example getUnitFor("speed") and units="metric"
     * returns m/s
     */
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
    
    /**
     * window to show what went wrong if exception is caught
     * precondition: exception should have been caught
     * postcondition: error window should be seen on the screen
     * @param message message which will be shown in the error window.
     * it tells user what is the problem
     */
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

