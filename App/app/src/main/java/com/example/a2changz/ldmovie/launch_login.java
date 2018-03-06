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
import java.util.HashMap;
import java.util.Map;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class launch_login extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super .onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);


    }

    public void go_next(View view)
    {
        Intent goToIntent = new Intent(this, search_page.class);

        startActivity(goToIntent);

    }

    public void check_mysql(View view) {
        final Map<String, String> params = new HashMap<String, String>();


        // no user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);

        final Context context = this;
        String url = "http://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:8080/TomcatTest/servlet/TomcatTest";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.d("response", response);
                        ((TextView) findViewById(R.id.http_response)).setText(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                }

        )
        {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }


        };
        queue.add(postRequest);


        return ;

    }



}
