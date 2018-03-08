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

        String msg = ((EditText)findViewById(R.id.movie_title)).getText().toString();

        if(msg.equals("") != true) {
            msg.replace(" ", "+");

            Intent goToIntent = new Intent(this, result_page.class);
            goToIntent.putExtra("page","0");
            goToIntent.putExtra("search", msg);
            startActivity(goToIntent);

        }
        else
        {
            Toast.makeText(this, "Search cannot be empty.", Toast.LENGTH_LONG).show();
        }

        return ;

    }

}
