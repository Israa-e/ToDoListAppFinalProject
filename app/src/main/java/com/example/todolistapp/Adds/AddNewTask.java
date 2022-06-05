package com.example.todolistapp.Adds;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolistapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";
    private TextView setDueDate;
    private EditText mTaskEdit;
    private Context context;
    private String dueDate = "";
    private String id = "";
    private Button mBtnSave;
    private ImageButton speechBtn,star_btn;

    private FirebaseFirestore firebaseFirestore;
    private boolean fav;
    private static final int RECOGNIZER_CODE = 10;
    private ProgressDialog progressDialog;
    String task;
    SharedPreferences sharedPreferences;

    String dueDateUpdate;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }


    //inflate design add_new_task
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences= getActivity().getSharedPreferences("logged_user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "");
        //id Inflate
        setDueDate = view.findViewById(R.id.set_due_tv);
        mTaskEdit = view.findViewById(R.id.task_ed);
        mBtnSave = view.findViewById(R.id.save);
        speechBtn = view.findViewById(R.id.speech_btn);
        star_btn = view.findViewById(R.id.starBtn);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        //Task Name Is Empty , Make Btn Gray
        if (mTaskEdit.getText().toString().isEmpty()) {
            mBtnSave.setEnabled(false);
            mBtnSave.setBackgroundColor(Color.GRAY);
        }

        // Speech Btn Listener
        speechBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            try {
                startActivityForResult(intent, RECOGNIZER_CODE);
            } catch (Exception e) {
                Toast.makeText(context, "Error Of Code", Toast.LENGTH_SHORT).show();
            }
        });

        // firebaseFirestore call
        firebaseFirestore = FirebaseFirestore.getInstance();

        //check Status
        boolean isUpdate = false;

        //move date in Bundle
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            task = bundle.getString("task");
            id = bundle.getString("uid");
            dueDateUpdate = bundle.getString("due");
            fav=bundle.getBoolean("fav");
            //get date inside Edit Text
            mTaskEdit.setText(task);
            setDueDate.setText(dueDateUpdate);
        }

        //check Edit Text Status
        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Is Empty
                if (charSequence.toString().equals("")) {
                    mBtnSave.setEnabled(false);
                    mBtnSave.setBackgroundColor(Color.GRAY);
                } else {

                    mBtnSave.setEnabled(true);
                    mBtnSave.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Set Date in side Text View " Set Date in side Text View "
        setDueDate.setOnClickListener(this::onClick);

        boolean finalIsUpdate = isUpdate;

        // firebaseFirestore Added Or Updated Data
        mBtnSave.setOnClickListener(view12 -> {

            String task = mTaskEdit.getText().toString();
            String due = setDueDate.getText().toString();

            if (finalIsUpdate) {
                progressDialog.setMessage("Updating Task ...!");
                progressDialog.show();
                progressDialog.dismiss();
                mBtnSave.setEnabled(true);
                mBtnSave.setText("UPDATE");
                mBtnSave.setBackgroundColor(getResources().getColor(R.color.yellow));
                firebaseFirestore.collection("task").document(id).update("task", task, "due", due);
                Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show();


            } else if (task.isEmpty()) {
                Toast.makeText(context, "Empty Task Not Allowed ?", Toast.LENGTH_SHORT).show();

            } else {
                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put("task", task);
                taskMap.put("due", dueDate);
                taskMap.put("status", 0);
                taskMap.put("uid", user_id);
                taskMap.put("fav", false);
                taskMap.put("time", FieldValue.serverTimestamp());
                firebaseFirestore.collection("task").add(taskMap).addOnCompleteListener(task1 -> {
                    progressDialog.setMessage("Saving Task ...!");
                    progressDialog.show();
                    if (task1.isSuccessful()) {

                        progressDialog.dismiss();
                        Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();

                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(context, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());

            }
            dismiss();
        });

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTaskEdit.setText(Objects.requireNonNull(data).getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogCloseListener(dialog);
        }
    }

    private void onClick(View view13) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
            i1 = i1 + 1;
            setDueDate.setText(i2 + "/" + i1 + "/" + i);
            dueDate = i2 + "/" + i1 + "/" + i;
        }, year, month, day);
        datePickerDialog.show();
    }
}
