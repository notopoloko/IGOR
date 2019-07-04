package br.com.android.unb.igor.AventuraAndamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import br.com.android.unb.igor.DatePickerFragment;
import br.com.android.unb.igor.Model.Aventura;
import br.com.android.unb.igor.R;
import br.com.android.unb.igor.Service;

public class AventuraAndamentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String aventura_id = "br.unb.android.igor.aventuraid";
    private static final String DIALOG_DATE = "DialogDate";

    public static Intent newIntent(Context packageContext, String idAventura) {
        Intent intent = new Intent(packageContext, AventuraAndamentoActivity.class);
        intent.putExtra(aventura_id, idAventura);
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
//                Toast.makeText(getApplicationContext(), "Não implementando", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
//                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
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

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new AventuraAndamentoPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        Service.getAventuraByID(getIntent().getStringExtra(aventura_id)).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if (ds != null){
                    updateUI(new Aventura(ds.get("nome").toString(),
                            new Date(),
                            ds.getLong("nImage").intValue(),
                            ds.get("mestre").toString(),
                            ds.get("sinopse").toString(),
                            UUID.fromString(ds.getId()))
                );
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        TextView sinopseTexto = findViewById(R.id.sinopseAventura);
        if (aventura.getSinopse().length() == 0){
            sinopseTexto.setText(R.string.no_description);
        } else {
            sinopseTexto.setText(aventura.getSinopse());
        }

        final TextView nomeMestre = findViewById(R.id.nomeJogador);
        Service.getJogador(aventura.getMestre()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if (ds != null){
                    nomeMestre.setText(ds.get("nome").toString());
                } else {
                    Toast.makeText(AventuraAndamentoActivity.this, "Erro ao resgatar o mestre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView tituloAventura = findViewById(R.id.aventuraTitulo);
        tituloAventura.setText(aventura.getNome());

        // Pegar foto de usuario
        final ImageView iconeJogador = findViewById(R.id.iconeJogador);
        File localFile = null;
        try {
            localFile = File.createTempFile("caozinho1", "jpg");
        } catch (IOException io) {
            Toast.makeText(this, "Erro no arquivo", Toast.LENGTH_SHORT).show();
        }
        final File localFileTemp = localFile;
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("caozinho1.jpg");
        sr.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AventuraAndamentoActivity.this, "Baixou arquivo", Toast.LENGTH_SHORT).show();
                // Alterar para qualquer jogador
                Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).resize(60,60).into(iconeJogador);
            }
        });
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
