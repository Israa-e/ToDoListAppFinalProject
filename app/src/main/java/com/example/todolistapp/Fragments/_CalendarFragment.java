package com.example.todolistapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.Adapter.Calendar;
import com.example.todolistapp.Adapter.ToDoAdapter;
import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class _CalendarFragment extends Fragment {
    View view;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private Calendar toDoAdapter;
    private List<ToDoModel> toDoModels;
    private CalendarView calender_id;
    private ListenerRegistration listenerRegistration;
    Query query;
    String date;

    public _CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment___calendar, container, false);
        _IdFind();
        recycler_Fun();
        ShowData();

        return view;
    }

    public void _IdFind() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.RV_calendar);
        calender_id = view.findViewById(R.id.calender_id);
    }

    public void recycler_Fun() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //add BottomSheetDialogFragment {AddNewTask} inside Fragment < click Btn >

        toDoModels = new ArrayList<>();
        toDoAdapter = new Calendar((HomePageActivity) getContext(), getActivity(), toDoModels);
        recyclerView.setAdapter(toDoAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ShowData() {
        calender_id.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                date = day + "/" + month + "/" + year;
                set_show_date(date);
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void set_show_date(String date) {
        query = firebaseFirestore.collection("task").whereEqualTo("due", date);
        listenerRegistration = query.addSnapshotListener(this::onEvent);
        toDoAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
        for (DocumentChange documentChange : Objects.requireNonNull(value).getDocumentChanges()) {
            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                String id = documentChange.getDocument().getId();
                ToDoModel Model = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                toDoModels.add(Model);
                toDoAdapter.notifyDataSetChanged();
            }
        }
        listenerRegistration.remove();
    }
}