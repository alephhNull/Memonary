package com.example.memonary.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memonary.MainActivity;
import com.example.memonary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class ResetFragment extends Fragment {

    private Button sendBtn;
    private TextView loginTxt;
    private EditText emailTxtField;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reset, container, false);
        emailTxtField = root.findViewById(R.id.editTextTextEmailAddressReset);
        loginTxt = root.findViewById(R.id.ResetTxtToLogin);
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_resetFragment_to_loginFragment2);
            }
        });
        sendBtn = root.findViewById(R.id.ResetButton);
        sendBtn.setOnClickListener(v -> {
            if (isValid()) {
                MainActivity.mAuth.sendPasswordResetEmail(emailTxtField.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Email Sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "There is a problem with internet connection or email address", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Invalid Email Address.", Toast.LENGTH_SHORT).show();
            }

        });
        return root;
    }

    private boolean isValid() {
        return !emailTxtField.getText().toString().equals("");
    }
}