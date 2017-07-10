package me.tripsit.tripmobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import me.tripsit.tripmobile.R;

public class DrugResultFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_drug_result, container, false);

        String drugString = getArguments().getString("data");
        JSONObject drug = null;
        try {
            drug = new JSONObject(drugString);
            // Display the first 500 characters of the response string.
            JSONObject drugData = drug.getJSONArray("data").getJSONObject(0);
            //resBox.setText(chemName);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        return v;
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < 100; i++){

            TableLayout layout = (TableLayout) v.findViewById(R.id.list);

            TableRow tableRow = new TableRow(v.getContext());
            TextView label = new TextView(v.getContext());
            TextView content = new TextView(v.getContext());

            label.setText("Test " + i);
            content.setText(Math.random() + "");

            label.setLayoutParams(params1);
            content.setLayoutParams(params1);

            tableRow.addView(label);
            tableRow.addView(content);

            tableRow.setLayoutParams(params2);

            layout.addView(tableRow);
        }
    }
}