package br.com.android.unb.igor.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import br.com.android.unb.igor.Model.Jogador;
import br.com.android.unb.igor.R;
import br.com.android.unb.igor.Service;


public class NovoJogadorFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private TextView mEmail;
    private TextView mSenha;
    private TextView mNomeUsuario;
    private TextView mDataNascimento;
    private Spinner mSexo;
    private boolean verificacaoStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nova_conta, container, false);
        mEmail = v.findViewById(R.id.email_text_view);
        mSenha = v.findViewById(R.id.senha_text_view);
        mNomeUsuario = v.findViewById(R.id.nome_usuario_textview);
        mDataNascimento = v.findViewById(R.id.data_nascimento_textview);
        mSexo = v.findViewById(R.id.sexo_spinner_view);

        (v.findViewById(R.id.criar_conta_botao)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarConta();
            }
        });

        return v;
    }

    private void criarConta() {
        final String email = mEmail.getText().toString();
        final String senha = mSenha.getText().toString();
        final String nome = mNomeUsuario.getText().toString();
        final String sexo = mSexo.getSelectedItem().toString();
        final Date dataNascimento;
        try {
            String a = mDataNascimento.getText().toString();
            dataNascimento = (new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)).parse(a);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Preencha a data com formato: dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.equals("") || senha.equals("") || sexo.equals("") || nome.equals("")){
            Toast.makeText(getContext(), "Preencha os dados corretamente", Toast.LENGTH_SHORT).show();
            return;
        }

        mFirebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            sendVerificationMail();
                            Jogador novoJogador = new Jogador(FirebaseAuth.getInstance().getCurrentUser().getUid(), email, senha, nome, sexo, dataNascimento);
                            Toast.makeText(getContext(), "Usuário criado. Dê uma olhada no seu email enquanto criamos seu jogador", Toast.LENGTH_LONG).show();
                            Service.postJogador(novoJogador).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getFragmentManager().popBackStack();
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "Usuário não criado", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void sendVerificationMail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( user == null )
            return;
        user.sendEmailVerification();
    }


}
