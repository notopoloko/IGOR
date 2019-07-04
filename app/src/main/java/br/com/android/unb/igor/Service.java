package br.com.android.unb.igor;


import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.android.unb.igor.Model.Aventura;
import br.com.android.unb.igor.Model.Jogador;

public class Service {

    public static Task<QuerySnapshot> getAventuras(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("aventuras").get();
    }

    public static Task<DocumentSnapshot> getAventuraByID(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("aventuras").document(id).get();
    }

    public static Task<QuerySnapshot> getJogadores(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("jogadores").get();
    }

    public static Task<DocumentSnapshot> getJogador(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("jogadores").document(id).get();
    }

    public static Task<Void> postAventura(Aventura aventura) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("aventuras").document(aventura.getId().toString()).set(aventura);
    }

    public static Task<Void> postJogador(Jogador jogador){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("jogadores").document(jogador.getId()).set(jogador);
    }

    public static UploadTask putProfilePicture(String nome, Uri profilePicture){
        StorageReference sr = FirebaseStorage.getInstance().getReference();
        StorageReference imageReference = sr.child(nome+".jpg");

        return imageReference.putFile(profilePicture);
    }

//    public static Task<DocumentReference> postSessao() {
//        FirebaseFirestore fb = FirebaseFirestore.getInstance();
//
//    }
}
