package com.example.memomia;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadImageFragment extends Fragment {

    private ImageView mPickImage;
    private ActivityResultLauncher<String> mGetContent;
    private NewActivity parent;
    private StorageReference storageRef;
    private EditText editText;
    private ImageButton back;

    public UploadImageFragment(NewActivity act) {
        this.parent = act;
    }

    //Handle delete image in the next scrum
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_images, container, false);
        editText = view.findViewById(R.id.album_title);
        if (!parent.entry.getTitle().equals("")) {
            editText.setText(parent.entry.getTitle());
        }

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText.getText().toString();
                if (!title.trim().equals("")) {
                    parent.entry.setTitle(title);
                }
                getFragmentManager().popBackStackImmediate();
            }
        });

        mPickImage = view.findViewById(R.id.image_1);
        if (parent.entry.getImages() != null && parent.entry.hasImage()) {
           setmPickImage(parent.entry.getImages().get("0_key"));
        }

        storageRef = FirebaseStorage.getInstance().getReference();
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                mPickImage.setImageURI(result);
                mPickImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mPickImage.setAlpha(0.6f);
                uploadImage(result);
            }
        });
        mPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        return view;
    }

    private void uploadImage(Uri uri) {

        if (uri != null) {
            StorageReference riversRef = storageRef.child("images/"+uri.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(uri);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getActivity().getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(parent.getApplicationContext(), "Upload success", Toast.LENGTH_SHORT).show();
                    downloadImage("images/"+ uri.getLastPathSegment());
                }
            });
        }

    }

    private void downloadImage(String id) {
        storageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                parent.entry.addImage(uri);
                Toast.makeText(parent.getApplicationContext(), "Added to entry", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getActivity().getApplicationContext(), "Add image failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setmPickImage(String url) {
        if (url != null) {
            Log.d("url", url);
            Picasso.get().load(url).centerCrop().fit().into(mPickImage);
            mPickImage.setAlpha(0.6f);
        }
    }
}