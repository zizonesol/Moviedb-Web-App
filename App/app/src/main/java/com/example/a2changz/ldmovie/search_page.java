package com.example.a2changz.ldmovie;

/**
 * Created by 2Changz on 3/5/2018.
 */
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class search_page extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState)
    {
        super .onCreate(savedInstanceState);
        setContentView(R.layout.seach_page);
    }

    public void search_movie(View view)
    {
        final Map<String, String> params = new HashMap<String, String>();

        String msg = ((EditText)findViewById(R.id.movie_title)).getText().toString();

        if(msg.equals("") != true) {
            msg.replace(" ", "+");
            // no user is logged in, so we must connect to the server
            RequestQueue queue = Volley.newRequestQueue(this);

            final Context context = this;
            String url = "http://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:8080/project3/servlet/searchpageapp?movie_title=" + msg;
            final Intent goToIntent = new Intent(this, result_page.class);
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            Log.d("response", response);

                            goToIntent.putExtra("result", response);
                            startActivity(goToIntent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("security.error", error.toString());
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }


            };
            queue.add(postRequest);

        }
        else
        {
            Toast.makeText(this, "Search cannot be empty.", Toast.LENGTH_LONG).show();
        }

        return ;

    }

}
