package me.tripsit.tripmobile.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import me.tripsit.tripmobile.R;

public class InfoFragment extends Fragment implements OnClickListener{

    OnDrugSearchListener drugCallback;
    OnComboSearchListener comboCallback;

    public interface OnDrugSearchListener {
        public void onDrugSearchComplete(JSONObject json);
    }

    public interface OnComboSearchListener {
        public void onComboSearchComplete(JSONObject json);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            drugCallback = (OnDrugSearchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDrugSearchListener");
        }

        try {
            comboCallback = (OnComboSearchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnComboSearchListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        Button drugSearchButton = (Button) v.findViewById(R.id.drugSearchButton);
        drugSearchButton.setOnClickListener(this);

        Button comboSearchButton = (Button) v.findViewById(R.id.comboSearchButton);
        comboSearchButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.searchProgress);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url;
        JsonObjectRequest stringRequest;
        switch (v.getId()) {
            case R.id.drugSearchButton:
                progressBar.setVisibility(View.VISIBLE);

                final EditText textBox = (EditText) getView().findViewById(R.id.drugTextBox);
                // Instantiate the RequestQueue.

                url = "http://tripbot.tripsit.me/api/tripsit/getDrug?name=" + textBox.getText();

                // Request a string response from the provided URL.
                stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.INVISIBLE);
                                drugCallback.onDrugSearchComplete(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        System.err.println("That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                break;
            case R.id.comboSearchButton:
                progressBar.setVisibility(View.VISIBLE);

                final EditText textBox1 = (EditText) getView().findViewById(R.id.drugComboBox1);
                final EditText textBox2 = (EditText) getView().findViewById(R.id.drugComboBox2);
                // Instantiate the RequestQueue.
                url = "http://tripbot.tripsit.me/api/tripsit/getInteraction?drugA=" + textBox1.getText() + "&drugB=" + textBox2.getText();

                // Request a string response from the provided URL.
                stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.INVISIBLE);
                                comboCallback.onComboSearchComplete(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        System.err.println("That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                break;
        }
    }
}