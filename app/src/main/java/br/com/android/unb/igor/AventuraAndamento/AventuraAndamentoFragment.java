package br.com.android.unb.igor.AventuraAndamento;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import br.com.android.unb.igor.Model.Aventura;
import br.com.android.unb.igor.R;
import br.com.android.unb.igor.Service;

public class AventuraAndamentoFragment extends Fragment {

    public static final String aventura_id = "br.unb.android.igor.aventuraid";
    private static final String DIALOG_DATE = "DialogDate";

    private ImageView miniaturaImageView;
    TextView sinopseTexto;
    TextView tituloAventura;
    ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String id;
        if (bundle != null) {
            id = bundle.getString(aventura_id);
            Service.getAventuraByID(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds != null) {
                        updateUI(new Aventura(ds.get("nome").toString(),
                                new Date(),
                                ds.getLong("nImage").intValue(),
                                ds.get("mestre").toString(),
                                ds.get("sinopse").toString(),
                                UUID.fromString(ds.getId()))
                        );
                    } else {
                        Toast.makeText(getActivity(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Não foi possível obter o id da aventura", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_aventura_andamento, container, false);

        viewPager = v.findViewById(R.id.viewpager);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        miniaturaImageView = v.findViewById(R.id.aventura_miniatura);
        tituloAventura = v.findViewById(R.id.aventuraTitulo);
        return v;
    }

    private void updateUI(Aventura aventura) {

        viewPager.setAdapter(new AventuraAndamentoPagerAdapter(getChildFragmentManager(), aventura ));

        switch (aventura.getnImage()) {
            case 0:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_coast));
                break;
            case 1:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_corvali));
                break;
            case 2:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_heartlands));
                break;
            case 3:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_imagem_automatica));
                break;
            case 4:
                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_krevast));
                break;
        }
        tituloAventura.setText(aventura.getNome());
//        if (aventura.getSinopse().length() == 0){
//            sinopseTexto.setText(R.string.no_description);
//        } else {
//            sinopseTexto.setText(aventura.getSinopse());
//        }
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Service.getAventuraByID(getIntent().getStringExtra(aventura_id)).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot ds = task.getResult();
//                if (ds != null){
//                    updateUI(new Aventura(ds.get("nome").toString(),
//                            new Date(),
//                            ds.getLong("nImage").intValue(),
//                            ds.get("mestre").toString(),
//                            ds.get("sinopse").toString(),
//                            UUID.fromString(ds.getId()))
//                );
//                } else {
//                    Toast.makeText(getApplicationContext(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void updateUI(Aventura aventura) {
//        ImageView miniaturaImageView = findViewById(R.id.aventura_miniatura);
//        switch (aventura.getnImage()) {
//            case 0:
//                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_coast));
//                break;
//            case 1:
//                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_corvali));
//                break;
//            case 2:
//                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_heartlands));
//                break;
//            case 3:
//                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_imagem_automatica));
//                break;
//            case 4:
//                miniaturaImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.miniatura_krevast));
//                break;
//        }
//        TextView sinopseTexto = findViewById(R.id.sinopseAventura);
//        if (aventura.getSinopse().length() == 0){
//            sinopseTexto.setText(R.string.no_description);
//        } else {
//            sinopseTexto.setText(aventura.getSinopse());
//        }
//
//        final TextView nomeMestre = findViewById(R.id.nomeJogador);
//        Service.getJogador(aventura.getMestre()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot ds = task.getResult();
//                if (ds != null){
//                    nomeMestre.setText(ds.get("nome").toString());
//                } else {
//                    Toast.makeText(getActivity(), "Erro ao resgatar o mestre", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//

//
//        // Pegar foto de usuario
//        final ImageView iconeJogador = findViewById(R.id.iconeJogador);
//        File localFile = null;
//        try {
//            localFile = File.createTempFile("caozinho1", "jpg");
//        } catch (IOException io) {
//            Toast.makeText(getActivity(), "Erro no arquivo", Toast.LENGTH_SHORT).show();
//        }
//        final File localFileTemp = localFile;
//        StorageReference sr = FirebaseStorage.getInstance().getReference().child("caozinho1.jpg");
//        sr.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
////                Toast.makeText(AventuraAndamentoFragment.this, "Baixou arquivo", Toast.LENGTH_SHORT).show();
//                // Alterar para qualquer jogador
//                Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).resize(60,60).into(iconeJogador);
//            }
//        });
//    }
}
