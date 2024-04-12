package org.w1;

import com.sun.tools.javac.Main;
import org.eclipse.jetty.util.IO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.w1.dataFetcher.*;

public class DataToDataBase {
    private static final String API_KEY = "9a12cb1dda0099e10c1db19e80e6d37b";
    private static final String dbURL = "jdbc:postgresql://localhost:5432/weather";
    private static final String USER = "postgres";
    private static final String PASSWORD = "abdulbosit2003";

    private static JSONObject getWeatherDataObject(String city) throws IOException {
        String urlAPI = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+ API_KEY;
        URL url = new URL(urlAPI);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line = reader.readLine();

        while (line != null) {
            response.append(line);
            line = reader.readLine();
        }
        reader.close();

        return new JSONObject(response.toString());
    }
    public static void inputDataToDB(JSONObject object) throws SQLException {
        Connection connectionDB = DriverManager.getConnection(dbURL,USER,PASSWORD);
        String sqlQuery = "insert into weather_data(city,temp,humidity,feels_like) values(?,?,?,?)";
        PreparedStatement preparedStatement = connectionDB.prepareStatement(sqlQuery);
        preparedStatement.setString(1, object.getString("name"));
        preparedStatement.setDouble(2, object.getJSONObject("main").getDouble("temp"));
        preparedStatement.setDouble(3, object.getJSONObject("main").getDouble("humidity"));
        preparedStatement.setDouble(4, object.getJSONObject("main").getDouble("feels_like"));
        preparedStatement.execute();
        connectionDB.close();
    }
    public static void main(String[] args) throws IOException, SQLException {

        JSONObject res = getWeatherDataObject("Manchester");
        inputDataToDB(res);

        try {
            JSONArray weatherData = getWeatherDataAsJson();
            System.out.println(weatherData.toString());
        } catch (SQLException e) {
            System.out.println("Error retrieving weather data: " + e.getMessage());
        }

    }
}
