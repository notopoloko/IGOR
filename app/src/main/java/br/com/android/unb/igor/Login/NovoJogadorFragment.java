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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.UUID;

import br.com.android.unb.igor.Model.Jogador;
import br.com.android.unb.igor.R;


public class NovoJogadorFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private TextView mEmail;
    private TextView mSenha;
    private TextView mNomeUsuario;
    private TextView mDataNascimento;
    private Spinner mSexo;

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
        String email = mEmail.getText().toString();
        String senha = mSenha.getText().toString();
        String nome = mNomeUsuario.getText().toString();
        String sexo = mSexo.getSelectedItem().toString();
        Date dataNascimento = new Date(mDataNascimento.getText().toString());

        Jogador novoJogador = new Jogador(UUID.randomUUID().toString(), email, senha, nome, sexo, dataNascimento);

        if (!email.equals("") && !senha.equals("")){
            mFirebaseAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                            } else {

                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Preencha as informações de email e senha corretamente", Toast.LENGTH_SHORT).show();
        }
    }


}
