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

import java.security.Policy;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.insuranceis.MESSAGE";

    public void addStudentActivity (View view) {
        Intent intent = new Intent(this,AddInsuredActivity.class);
        String message = "Add client.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private RequestQueue requestQueue;
    private TextView Insured;
    private TextView Policy;
    private String urlInsured = "https://insurance-is-dev.azurewebsites.net/api/v1/Insured";
    private String urlPolicy = "https://insurance-is-dev.azurewebsites.net/api/v1/InsurancePolicy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Insured = (TextView) findViewById(R.id.Insured);
        Policy = (TextView) findViewById(R.id.Policy);
    }

    public void prikaziInsured(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(urlInsured, insuredArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    public void prikaziPolicy(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(urlPolicy, policyArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> insuredArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            Policy.setVisibility(View.INVISIBLE);
            Insured.setVisibility(View.VISIBLE);
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

    private Response.Listener<JSONArray> policyArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            Insured.setVisibility(View.INVISIBLE);
            Policy.setVisibility(View.VISIBLE);
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                try{
                    JSONObject object = response.getJSONObject(i);
                    Integer policyID = object.getInt("insurancePolicyID");
                    Integer cost = object.getInt("finalSum");
                    String dateFrom = object.getString("dateFrom").split("T")[0];
                    String dateTo = object.getString("dateTo").split("T")[0];
                    Integer insuredID = object.getInt("insuredID");
                    Integer insuranceSubjectID = object.getInt("insuranceSubjectID");
                    Integer insuranceSubtypeID = object.getInt("insuranceSubtypeID");

                    data.add(policyID + ", " + cost + "€ " + dateFrom + " to " + dateTo + "; " + insuredID + ", " + insuranceSubjectID + ", " + insuranceSubtypeID);
                }catch (JSONException e){
                    e.printStackTrace();
                    return;
                }
            }

            Policy.setText("");
            for (String row: data){
                String currentText = Policy.getText().toString();
                Policy.setText(currentText + "\n\n" + row);

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