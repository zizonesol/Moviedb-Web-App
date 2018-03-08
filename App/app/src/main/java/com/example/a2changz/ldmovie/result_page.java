package com.example.a2changz.ldmovie;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 2Changz on 3/6/2018.
 */

public class result_page extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState)
    {
        super .onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        Bundle bundle = getIntent().getExtras();

        String sr = bundle.getString("search");
        String pg = bundle.getString("page");

        get_result(sr,pg);

    }

    private void make_table(String msg)
    {
        Gson gson = new GsonBuilder().create();
        Movie_List list_movie = gson.fromJson(msg,Movie_List.class);
        TableLayout tl = (TableLayout) findViewById(R.id.result_table);
        if(list_movie.mlist.size() == 0)
        {
            no_result();
        }
        else
        {
            for(int i = 0; i < list_movie.mlist.size(); i++)
            {
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setId(100+i);
                row.setLayoutParams(lp);

                TextView title = new TextView(this);
                title.setText(list_movie.mlist.get(i).getTitle());
                title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1));

                TextView year = new TextView(this);
                year.setText(list_movie.mlist.get(i).getYear());
                year.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1));
    
                TextView dir = new TextView(this);
                dir.setText(list_movie.mlist.get(i).getDirector());
                dir.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1));

                TextView star = new TextView(this);
                star.setText(list_movie.mlist.get(i).getAllstar());
                star.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1));

                TextView genre = new TextView(this);
                genre.setText(list_movie.mlist.get(i).getGenre());
                genre.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1));

                row.addView(title);
                row.addView(year);
                row.addView(dir);
                row.addView(star);
                row.addView(genre);
                tl.addView(row);
            }
        }
    }

    private void no_result()
    {
        Toast.makeText(this, "Not like this.", Toast.LENGTH_LONG).show();
    }

    private void get_result(String sr,String pg)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Map<String, String> params = new HashMap<String, String>();

        String url = "http://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:8080/project3/servlet/searchpageapp?movie_title="+ sr+"&pgnum="+pg;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        make_table(response);
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

        return ;
    }

}
