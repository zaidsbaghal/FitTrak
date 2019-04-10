package com.example.fitnesstracker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassActivity extends AppCompatActivity {

    private static final String TAG = "forgotPassActivity";
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
    private EditText emailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        // Button
        emailEt = findViewById(R.id.passEditTextEmail);
        findViewById(R.id.resetPassBTN).setOnClickListener(onClickListener);
    }

    public void sendPasswordReset(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(forgotPassActivity.this, "Reset email sent.",
                                    Toast.LENGTH_SHORT).show();
                            finish(); // Goes back to sign in activity
                        } else {
                            Toast.makeText(forgotPassActivity.this, "Invalid email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Required.");
            valid = false;
        } else {
            emailEt.setError(null);
        }

        return valid;
    }

    // OnClickListener
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.resetPassBTN) {
                v.startAnimation(buttonClick);
                if (!validateForm()) {
                    return;
                } else {
                    sendPasswordReset(emailEt.getText().toString());
                }
            }
        }
    };
}
