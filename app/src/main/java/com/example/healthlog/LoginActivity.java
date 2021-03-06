package com.example.healthlog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText hospitalId;
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hospitalId = (EditText) findViewById(R.id.loginActivity_login_editText);
        login = findViewById(R.id.loginActivity_login_btn);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signIn();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // COMPLETED(Danish) check if user is already login
        if (HealthLog.ID != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signIn() {
        final String hId = hospitalId.getText().toString().trim();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Hospital").document(hId);
        docRef
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        HealthLog.addHospitalIdToSharedPreference(hId);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(
                                                        getApplicationContext(), "Wrong Login credentials", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                } else {
                                    Log.d("Loginok", "get failed with ", task.getException());
                                }
                            }
                        });
    }
}
