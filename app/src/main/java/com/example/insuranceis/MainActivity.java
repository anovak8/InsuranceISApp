package com.example.insuranceis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView Insured;
    private String url = "https://insurance-is-dev.azurewebsites.net/api/v1/Insured";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Insured = (TextView) findViewById(R.id.Insured);
    }

    public void prikaziInsured(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                try{
                    JSONObject object = response.getJSONObject(i);
                    String name = object.getString("firstMidName");
                    String surname = object.getString("lastName");
                    String address = object.getString("address");
                    Integer zipcode = object.getInt("zipCode");
                    String city = object.getString("city");

                    data.add(name + " " + surname + "; " + address + ", " + zipcode + " " + city);
                }catch (JSONException e){
                    e.printStackTrace();
                    return;
                }
            }

            Insured.setText("");
            for (String row: data){
                String currentText = Insured.getText().toString();
                Insured.setText(currentText + "\n\n" + row);
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };


}