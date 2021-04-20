package com.example.accesscontrol.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.accesscontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class NotificationsFragment extends Fragment {

    final String randomKey = UUID.randomUUID().toString();
    private NotificationsViewModel notificationsViewModel;
    private Button scannerButton;
    private Context context;
    private ImageView picImageView;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        scannerButton = root.findViewById(R.id.scannerButton);
        picImageView = root.findViewById(R.id.picImageView);
        scannerButton.setOnClickListener(this::scanQR);
        context = getContext();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return root;
    }

    public void scanQR(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this.getActivity());

        intentIntegrator.setPrompt("Acerque un codigo QR");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        IntentIntegrator.forSupportFragment(this).initiateScan();
        //intentIntegrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //Log.d("ImageURL", intentResult.getContents());
        if (intentResult.getContents() != null) {

            Glide.with(context).load(intentResult.getContents()).into(picImageView);
            HashMap<Object, String> hashMap = new HashMap<>();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm a");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(cal.getTime());
            String time = hourFormat.format(cal.getTime());
            hashMap.put("date", date);
            hashMap.put("hour", time);
            databaseReference.child("History").child(randomKey).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Visita Registrada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al Registrar visita", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Result");
//
//            builder.setMessage(intentResult.getContents());
//
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    dialog.dismiss();
//                }
//            });
//            builder.show();
        } else {
            Toast.makeText(context, "OOPS... You did not scan anything", Toast.LENGTH_SHORT).show();
        }

    }
}