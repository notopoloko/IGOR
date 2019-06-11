package br.com.android.unb.igor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class AventuraAndamentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String aventura_nome = "br.unb.android.igor.aventuraNome";

    public static Intent newIntent(Context packageContext, String nomeAventura) {
        Intent intent = new Intent(packageContext, AventuraAndamentoActivity.class);
        intent.putExtra(aventura_nome, nomeAventura);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aventura_andamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.nova_sessao);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Não implementando", Toast.LENGTH_SHORT).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("aventuras").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        // Pensar numa maneira deixar isso generalizado
                        Map<String, Object> data = document.getData();
                        if (data.get("nome").toString().equals(getIntent().getStringExtra(aventura_nome))) {
                            updateUI(new Aventura(data.get("nome").toString(), new Date(), document.getLong("nImage").intValue(), document.getString("mestre"), document.getString("sinopse")));
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new AventuraAndamentoPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void updateUI(Aventura aventura) {
        ImageView miniaturaImageView = findViewById(R.id.aventura_miniatura);
        switch (aventura.getnImage()) {
            case 0:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.miniatura_coast));
                break;
            case 1:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.miniatura_corvali));
                break;
            case 2:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.miniatura_heartlands));
                break;
            case 3:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.miniatura_imagem_automatica));
                break;
            case 4:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.miniatura_krevast));
                break;
        }
//        TextView sinopseTexto = findViewById(R.id.sinopseTexto);
//        sinopseTexto.setText(aventura.getSinopse());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aventura_andamento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
