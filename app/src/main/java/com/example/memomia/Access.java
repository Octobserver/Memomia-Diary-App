package com.example.memomia;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class Access extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference dbr;
    private TabLayout tl;
    protected static String userEmail;

    @IgnoreExtraProperties
    protected class User {

        protected String username;
        protected String email;
        protected String password;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public void setEmail(String email){
            this.email = email;
        }

        public void setUsername(String name){
            this.username = name;
        }

        public void setPassword(String pw){
            this.password = pw;
        }

    }

    protected User user = new User();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.access);






        tl = findViewById(R.id.tab_layout);

        TabLayout.Tab signup = tl.newTab();
        signup.setText("Sign Up");
        tl.addTab(signup);

        TabLayout.Tab signin = tl.newTab();
        signin.setText("Sign In");
        tl.addTab(signin,true);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.FrameLayout, new Signin());

        ft.commit();

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment fragment;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new Signup();

                        break;
                    case 1:
                        fragment = new Signin();
                        break;
                    default:
                        return;

                }


                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.FrameLayout, fragment);

                ft.commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


        auth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    protected void onStart(){
        super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            userEmail = currentUser.getEmail();
            intent.putExtra("userId", userEmail);
            startActivity(intent);
        }

    }



    private void createAccount(String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null){
                                user.getIdToken(true)
                                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                if (task.isSuccessful()) {
                                                    String id = task.getResult().getToken();
                                                    if (id!=null)
                                                        dbr.child("Users").child(id).setValue(user);
                                                }
                                            }
                                        });
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                userEmail = user.getEmail();
                                intent.putExtra("userId", userEmail);
                                startActivity(intent);
                            }

                        }
                        else {

                            Toast.makeText(Access.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void signIn(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser user = auth.getCurrentUser();
                            //go to new activity for now
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            userEmail = email;
                            intent.putExtra("userId", userEmail);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Access.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }






    public static class Signup extends Fragment {

        public Signup() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.signup, container, false);

            Button btn = view.findViewById(R.id.signup);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Access act = (Access) getActivity();
                    if(act != null) {

                        EditText txt = getView().findViewById(R.id.EmailAddress);
                        act.user.setEmail(txt.getText().toString());
                        txt = getView().findViewById(R.id.Password);
                        String p1 = txt.getText().toString();
                        txt = getView().findViewById(R.id.ConfirmPassword);
                        String p2 = txt.getText().toString();
                        if (p1.equals(p2)){
                            act.user.setPassword(p1);
                            txt = getView().findViewById(R.id.username);
                            act.user.setUsername(txt.getText().toString());
                            act.createAccount(act.user.email, act.user.password);


                        }

                        else
                            Toast.makeText(act.getApplicationContext(), "Passwords do not match!", LENGTH_SHORT).show();



                    }





                }
            });


            return view;
        }








    }

    public static class Signin extends Fragment {

        public Signin() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.signin, container, false);

            Button btn = view.findViewById(R.id.signin);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Access act = (Access) getActivity();
                    if(act != null) {
                        EditText txt = getView().findViewById(R.id.EmailAddress);
                        act.user.setEmail(txt.getText().toString());
                        txt = getView().findViewById(R.id.Password);
                        act.user.setPassword(txt.getText().toString());
                        act.signIn(act.user.email, act.user.password);


                    }

                }
            });



            return view;
        }

    }



}
