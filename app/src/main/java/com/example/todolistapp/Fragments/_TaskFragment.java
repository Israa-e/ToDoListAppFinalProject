package com.example.todolistapp.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.Adapter.ToDoAdapter;
import com.example.todolistapp.Adds.AddNewTask;
import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.Adds.OnDialogCloseListener;
import com.example.todolistapp.R;
import com.example.todolistapp.Adds.TouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class _TaskFragment extends Fragment implements OnDialogCloseListener {
    View view;
    public static final String TAGS = "AddNewTask";
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private FirebaseFirestore firebaseFirestore;
    private ToDoAdapter toDoAdapter;
    FirebaseUser user;
    Button mBtnSave;
    EditText task;
    TextView no_task;
    ImageView empty_imageView_task;
    private ImageButton star_btn;
    private ToDoModel toDoModel;
    private List<ToDoModel> toDoModels;
    private ListenerRegistration listenerRegistration;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    String uid;



    public _TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment___task, container, false);

        _IdFind();
        recycler_Fun();
        ShowData();
        return view;
    }


    public void _IdFind() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView_cat);
        task = view.findViewById(R.id.task_ed);
        mBtnSave = view.findViewById(R.id.save);
        no_task = view.findViewById(R.id.no_task);
        empty_imageView_task = view.findViewById(R.id.empty_imageView_task);
        floatingActionButton = view.findViewById(R.id.floatingActionButton_cat);
        star_btn = view.findViewById(R.id.starBtn);
    }

    public void recycler_Fun() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton.setOnClickListener(view -> {
            AddNewTask.newInstance().show(getParentFragmentManager(), AddNewTask.TAG);
        });
        toDoModels = new ArrayList<>();
        toDoAdapter = new ToDoAdapter((HomePageActivity) getContext(), getActivity(), toDoModels);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(toDoAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstElementPosition = lManager.findFirstVisibleItemPosition();
                Log.d("TAG", "onScrolled: " + firstElementPosition);

                if (firstElementPosition < 0) {
                    empty_imageView_task.setVisibility(View.VISIBLE);
                    no_task.setVisibility(View.VISIBLE);
                } else {
                    empty_imageView_task.setVisibility(View.GONE);
                    no_task.setVisibility(View.GONE);
                }
            }

        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ShowData() {
        firebaseFirestore.collection("task").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document :querySnapshot.getDocuments()){
                        if(document.getId()!=null){
                            firebaseFirestore.collection("task").whereEqualTo("uid",getActivity().getSharedPreferences("logged_user",MODE_PRIVATE).getString("user_id","")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        int count = task.getResult().size();
                                        if(count==0){
                                            empty_imageView_task.setVisibility(View.VISIBLE);
                                            no_task.setVisibility(View.VISIBLE);
                                        }else {
                                            empty_imageView_task.setVisibility(View.GONE);
                                            no_task.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
        Query query = firebaseFirestore.collection("task").whereEqualTo("uid",getActivity().getSharedPreferences("logged_user",MODE_PRIVATE).getString("user_id","")).orderBy("time", Query.Direction.DESCENDING);
        listenerRegistration = query.addSnapshotListener(this::onEvent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDialogCloseListener(DialogInterface dialogInterface) {
        toDoModels.clear();
        ShowData();

    }
    int x = 0;
    @SuppressLint("NotifyDataSetChanged")
    private void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {

        for (DocumentChange documentChange : Objects.requireNonNull(value).getDocumentChanges()) {
            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                String id = documentChange.getDocument().getId();
                ToDoModel Model = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                toDoModels.add(Model);
                 toDoAdapter.notifyDataSetChanged();
                x++;
            }

        }
        listenerRegistration.remove();
        //  toDoAdapter.notifyItemRangeChanged(toDoModels.size()-1,toDoAdapter.getItemCount());

        System.out.println("this is x " + x);
    }
}