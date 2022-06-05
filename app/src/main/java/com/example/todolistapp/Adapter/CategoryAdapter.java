package com.example.todolistapp.Adapter;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.Model.CategoryModel;
import com.example.todolistapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolderCategory> {
    private Context context;
    private ArrayList<CategoryModel> arrayList;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_category, parent, false);
        return new CategoryAdapter.ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolderCategory holder, int position) {
        CategoryModel categoryModel = arrayList.get(position);
        String id = categoryModel.getId();
        String category_name = categoryModel.getCategory_name();
        long time = categoryModel.getTime();
        holder.textView.setText(category_name);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolderCategory extends RecyclerView.ViewHolder {
    TextView textView;
        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_Category);
        }
    }
}
