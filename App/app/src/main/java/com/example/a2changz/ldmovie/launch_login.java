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



    protected void onCreate(Bundle savedInstanceState)
    {
        super .onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

    }

    private void acc(String re)
    {
        if(re.equals("1"))
        {
            Intent goToIntent = new Intent(this, search_page.class);
            startActivity(goToIntent);
        }
        else
        {
            Toast.makeText(this, "Username/Password is incorrect.", Toast.LENGTH_LONG).show();
        }
    }

    public void check_mysql(View view) {


        String us = ((EditText)findViewById(R.id.login_id)).getText().toString();
        String pw = ((EditText)findViewById(R.id.login_pw)).getText().toString();

        if(!us.equals("") && !pw.equals("")) {
            final Map<String, String> params = new HashMap<String, String>();
            RequestQueue queue = Volley.newRequestQueue(this);

            final Context context = this;
            String url = "http://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:8080/project3/servlet/loginapp?email=" + us + "&password=" + pw;

            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            acc(response);

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
            Toast.makeText(this, "Username/Password cannot be empty.", Toast.LENGTH_LONG).show();
        }

        return ;

    }



}
