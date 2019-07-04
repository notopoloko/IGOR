package br.com.android.unb.igor.NovaAventura;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import br.com.android.unb.igor.Model.Aventura;
import br.com.android.unb.igor.R;
import br.com.android.unb.igor.Service;

public class NovoFragment extends Fragment {
    private Random mRandom = new Random();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.content_novo, container, false);

        ImageButton fechar = v.findViewById(R.id.fechar);
        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        ImageButton imageButton = v.findViewById(R.id.pronto);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aventura aventura;
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userName = auth.getCurrentUser() == null ? "Usuário sem nome" : auth.getCurrentUser().getUid();

                EditText titulo = v.findViewById(R.id.titulo);
                String aventuraTitulo = titulo.getText().toString();

                EditText sinopse = v.findViewById(R.id.aventura_sinopse);
                String aventuraSinopse = sinopse.getText().toString();
                if (aventuraTitulo.equals("")){
                    aventura = new Aventura(
                            "Aventura sem título",
                            new Date(),
                            mRandom.nextInt(5),
                            userName,
                            aventuraSinopse,
                            UUID.randomUUID()
                    );
                } else {
                    aventura = new Aventura(
                            aventuraTitulo,
                            new Date(),
                            mRandom.nextInt(5),
                            userName,
                            aventuraSinopse,
                            UUID.randomUUID()
                    );
                }

                Service.postAventura(aventura).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Aventura criada", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Falha no registro da aventura", Toast.LENGTH_SHORT).show();
                    }
                });
                getFragmentManager().popBackStack();
            }
        });

        return v;
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.novo, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {
//
//        }// else if (id == R.id.nav_share) {
////
////        } else if (id == R.id.nav_send) {
////
////        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

//    public static Intent newIntent(Context packageContext){
//        return new Intent(packageContext, NovoFragment.class);
//    }
}
