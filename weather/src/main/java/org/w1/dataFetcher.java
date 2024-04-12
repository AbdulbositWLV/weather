package org.w1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class dataFetcher {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/weather";
    private static final String USER = "postgres";
    private static final String PASSWORD = "abdulbosit2003";

    public static JSONArray getWeatherDataAsJson() throws SQLException {
        JSONArray jsonArray = new JSONArray();
        try{
            Class.forName("org.postgresql.Driver");
            try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)){
                String sql = "SELECT * FROM weather_data";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("city", resultSet.getString("city"));
                    jsonObject.put("temp", resultSet.getDouble("temp"));
                    jsonObject.put("humidity", resultSet.getDouble("humidity"));
                    jsonObject.put("feels_like", resultSet.getDouble("feels_like"));
                    jsonArray.put(jsonObject);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return jsonArray;
    }

}
