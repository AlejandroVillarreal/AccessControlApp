package com.example.accesscontrol.ui.dashboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.accesscontrol.BuildConfig;
import com.example.accesscontrol.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class DashboardFragment extends Fragment {

    final String randomKey = UUID.randomUUID().toString();
    private DashboardViewModel dashboardViewModel;
    private Button uploadImageButton;
    private Button generateQRButton;
    private ImageButton shareQR;
    private ImageView qrImageView;
    private Uri imageUri;
    private String qrURL;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        uploadImageButton = root.findViewById(R.id.uploadPhotoButton);
        generateQRButton = root.findViewById(R.id.generateQRButton);
        shareQR = root.findViewById(R.id.shareQR);
        qrImageView = root.findViewById(R.id.qrImageView);
        uploadImageButton.setOnClickListener(this::uploadImage);
        generateQRButton.setOnClickListener(this::generateQRCode);
        shareQR.setOnClickListener(this::shareQRCode);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        context = getContext();
        return root;
    }

    public void uploadImage(View v) {
        choosePicture();
    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            Log.d("UploadImage", "test");
            uploadPicture();
        }
    }

    public void uploadPicture() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando imagen...");
        progressDialog.show();
        //final String randomKey = UUID.randomUUID().toString();
        UploadTask uploadTask;
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageReference.child("id_pictures/" + randomKey);
        uploadTask = riversRef.putFile(imageUri);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "No se pudo cargar la imagen", Snackbar.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                progressDialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Imagen Cargada", Snackbar.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Cargando: " + (int) progressPercent + "%");
            }
        });
    }

    public void generateQRCode(View v) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String user_id = user.getUid();
        DatabaseReference databaseReference = database.getReference("Users").child(user_id).child("name");
        final String[] user_name = new String[1];
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name[0] = snapshot.getValue().toString();
                Log.d("UserName ", user_name[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MultiFormatWriter writer = new MultiFormatWriter();


        if (isAdded()) {
            storageReference.child("id_pictures/" + randomKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (isAdded()) {
                        qrURL = uri.toString() + " " + user_name[0];
                        Log.d("ImageURL", qrURL);
                        //Glide.with(context).load(qrURL).into(qrImageView);
                        try {
                            BitMatrix matrix = writer.encode(qrURL, BarcodeFormat.QR_CODE, 700, 700);
                            BarcodeEncoder encoder = new BarcodeEncoder();
                            Bitmap bitmap = encoder.createBitmap(matrix);
                            qrImageView.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "No se pudo cargar la imagen", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    // Usar cuando se lea el codigo QR para mostrar imagen del id
//    public void generateQRCode(View v) {
//        if(isAdded()) {
//            storageReference.child("id_pictures/" + randomKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    if (isAdded()) {
//                        qrURL = uri.toString();
//                        Log.d("ImageURL", qrURL);
//                        Glide.with(context).load(qrURL).into(qrImageView);
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Snackbar.make(getActivity().findViewById(android.R.id.content), "No se pudo cargar la imagen", Snackbar.LENGTH_LONG).show();
//                }
//            });
//        }
//    }
    public void shareQRCode(View v) {
        Drawable drawable = qrImageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        try {
            File file = new File(context.getExternalCacheDir(), File.separator + "code.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpg");

            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}