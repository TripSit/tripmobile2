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

    public interface OnDrugSearchListener {
        public void onDrugSearchComplete(JSONObject json);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            drugCallback = (OnDrugSearchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        Button b = (Button) v.findViewById(R.id.drugSearchButton);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drugSearchButton:
                final EditText textBox = (EditText) getView().findViewById(R.id.drugTextBox);
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                String url = "http://tripbot.tripsit.me/api/tripsit/getDrug?name=" + textBox.getText();

                // Request a string response from the provided URL.
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                drugCallback.onDrugSearchComplete(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                break;
        }
    }
}