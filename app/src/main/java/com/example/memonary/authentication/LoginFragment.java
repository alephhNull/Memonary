package com.example.memonary.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.memonary.AppActivity;
import com.example.memonary.MainActivity;
import com.example.memonary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private Button loginButton;
    private TextView signupButton;
    private EditText emailTextField;
    private EditText passwordTextField;
    private TextView forgotPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = root.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        signupButton = root.findViewById(R.id.signupButton);
        forgotPassword = root.findViewById(R.id.forgotPassword);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_signupFragment2);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment2_to_resetFragment);
            }
        });
        emailTextField = root.findViewById(R.id.editTextTextEmailAddress);
        passwordTextField = root.findViewById(R.id.editTextTextPassword);
        return root;
    }

    @Override
    public void onClick(View view) {
        if (validateInput()) {
            MainActivity.mAuth.signInWithEmailAndPassword(emailTextField.getText().toString(), passwordTextField.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getContext(), AppActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(),"Fill the required fields please.",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validateInput() {
        return !emailTextField.getText().toString().equals("") && !passwordTextField.getText().toString().equals("");
    }
}
