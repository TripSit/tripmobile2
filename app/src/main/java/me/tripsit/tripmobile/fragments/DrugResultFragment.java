package me.tripsit.tripmobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONObject;

import me.tripsit.tripmobile.R;

public class DrugResultFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_drug_result, container, false);

        final TextView resBox = (TextView) v.findViewById(R.id.drugResult);
        String drugString = getArguments().getString("data");
        JSONObject drug = null;
        try {
            drug = new JSONObject(drugString);
            // Display the first 500 characters of the response string.
            String chemName = drug.getJSONArray("data").getJSONObject(0).getString("name");
            resBox.setText(chemName);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        return v;
    }
}