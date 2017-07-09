package me.tripsit.tripmobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import me.tripsit.tripmobile.fragments.AboutFragment;
import me.tripsit.tripmobile.fragments.ChatFragment;
import me.tripsit.tripmobile.fragments.ContactFragment;
import me.tripsit.tripmobile.fragments.DrugResultFragment;
import me.tripsit.tripmobile.fragments.InfoFragment;

public class MainActivity extends AppCompatActivity implements InfoFragment.OnDrugSearchListener {

    private TextView mTextMessage;

    public void onDrugSearchComplete(JSONObject drug){
        FragmentManager manager = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString("data", drug.toString());
        DrugResultFragment drf = new DrugResultFragment();
        drf.setArguments(args);
        manager.beginTransaction().replace(R.id.content, drf).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    manager.beginTransaction().replace(R.id.content, new ChatFragment()).commit();
                    return true;
                case R.id.navigation_info:
                    manager.beginTransaction().replace(R.id.content, new InfoFragment()).commit();
                    return true;
                case R.id.navigation_about:
                    manager.beginTransaction().replace(R.id.content, new AboutFragment()).commit();
                    return true;
                case R.id.navigation_contact:
                    manager.beginTransaction().replace(R.id.content, new ContactFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);

        if (savedInstanceState != null) {
            return;
        }

        // Create a new Fragment to be placed in the activity layout
        ChatFragment firstFragment = new ChatFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, firstFragment).commit();
    }

}
