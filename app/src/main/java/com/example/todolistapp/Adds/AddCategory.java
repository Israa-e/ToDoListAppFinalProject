package com.example.todolistapp.Adds;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.todolistapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCategory extends AppCompatDialogFragment {

    private FirebaseFirestore firebaseFirestore;
    EditText categoryName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.add_category, null);
        categoryName = view.findViewById(R.id.ed_category);

        builder.setView(view).setTitle("Test Dialog ")

                .setNegativeButton(" Cancel", (dialog, which) -> {

                }).setPositiveButton("add", (dialog, which) -> {

                    Map<String, Object> taskMap = new HashMap<>();
                    long timeStamp = System.currentTimeMillis();
                    String name = categoryName.getText().toString();
                    taskMap.put("category_name", name);
                    taskMap.put("time", timeStamp);
                    taskMap.put("uid", getActivity().getSharedPreferences("logged_user",MODE_PRIVATE).getString("user_id",""));
                    firebaseFirestore.collection("categories").add(taskMap).addOnCompleteListener(add_category -> {
                        if (add_category.isSuccessful()) {
                            Log.d("Successful Category Created", "onClick: ");
                        } else {
                            Log.d("Failed Category Created", "onClick: ");                            }
                    }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

                });
        return builder.create();
    }
}