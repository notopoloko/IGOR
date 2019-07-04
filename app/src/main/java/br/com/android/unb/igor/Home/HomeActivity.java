package br.com.android.unb.igor.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.android.unb.igor.NovaAventura.NovoActivity;
import br.com.android.unb.igor.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = NovoActivity.newIntent(getApplicationContext());
                startActivity(i);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frame_layout);

        if (fragment == null) {
            fragment  = new AventuraFragment();
            fm.beginTransaction().add(R.id.frame_layout, fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, HomeActivity.class);
    }


}