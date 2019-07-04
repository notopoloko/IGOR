package br.com.android.unb.igor.AventuraAndamento;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.android.unb.igor.R;

public class NovaSessaoFragment extends Fragment {

    private TextView nomeSessao;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nova_sessao, container, false);
        nomeSessao = v.findViewById(R.id.titulo);

        Button dateButtom = v.findViewById(R.id.data);
        dateButtom.setText(new SimpleDateFormat( "dd/MM", Locale.ENGLISH ).format(new Date()));
        dateButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }
}
