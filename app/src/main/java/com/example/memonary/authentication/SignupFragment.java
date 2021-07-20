package com.example.memonary.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.memonary.MainActivity;
import com.example.memonary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class SignupFragment extends Fragment implements View.OnClickListener{
    
    private Button signupButton;
    private EditText emailTextField;
    private EditText passwordTextField;
    private EditText confirmPasswordTextField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        signupButton = root.findViewById(R.id.signupButton2);
        signupButton.setOnClickListener(this);
        emailTextField = root.findViewById(R.id.editTextTextEmailAddress2);
        passwordTextField = root.findViewById(R.id.editTextTextPassword1);
        confirmPasswordTextField = root.findViewById(R.id.editTextTextPassword2);
        return root;
    }

    @Override
    public void onClick(View view) {
        if (validateInputs()) {
            MainActivity.mAuth.createUserWithEmailAndPassword(emailTextField.getText().toString(), passwordTextField.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getContext(), MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public boolean validateInputs() {
        return true;
    }
}
