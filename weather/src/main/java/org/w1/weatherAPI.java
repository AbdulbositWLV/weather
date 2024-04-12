package org.w1;

import org.json.JSONArray;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import static org.w1.dataFetcher.*;
import static org.w1.DataToDataBase.*;

public class weatherAPI {
    private static String getWeatherDataAsJson(Request req, Response res) {
        JSONArray jsonArray = new JSONArray();
        try{

            jsonArray = dataFetcher.getWeatherDataAsJson();

        }catch (Exception e){
            e.printStackTrace();
            res.status(500);
            return "Internal Server Error";
        }
        res.type("application/json");
        return jsonArray.toString();
    }
    public static void main(String[] args) {
        port(8088);
        before(
                ((request, response) -> {
                    response.header("Access-Control-Allow-Origin","*");
                    response.header("Access-Control-Allow-Methods","*");
                    response.header("Access-Control-Allow-Headers","*");
                    response.header("Access-Control-Allow-Credentials","true");
                })
        );
        get("/weather",(weatherAPI::getWeatherDataAsJson));
    }
}
