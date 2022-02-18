package com.example.filemanager.adapter_classes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.FileListActivity;
import com.example.filemanager.R;

import java.io.File;
import java.util.Date;

public class FileList_Adapter extends RecyclerView.Adapter<FileList_Adapter.File_ListViewHolder> {
    Context context;
    File[] filesAndFolders;

    public FileList_Adapter(Context context, File[] filesAndFolders) {
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }

    @NonNull
    @Override
    public File_ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_list_item, parent, false);
        return new File_ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull File_ListViewHolder holder, int position) {
        File selectedFile = filesAndFolders[position];
        holder.textView.setText(selectedFile.getName());
        File time = new File(selectedFile.getPath());
        Date lastModified = new Date(time.lastModified());

        holder.fileTimeStamp.setText(lastModified.toString());
        if (selectedFile.isDirectory()) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        }

        holder.itemView.setOnClickListener(view -> {
            if (selectedFile.isDirectory()) {
                Intent intent = new Intent(context, FileListActivity.class);
                String path = selectedFile.getAbsolutePath();
                intent.putExtra("path", path);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                // Open the file
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    String type = "image/*";
                    intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.d("Error", "onClick: " + e);
                    Toast.makeText(context.getApplicationContext(), "Cannot open the file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public class File_ListViewHolder extends RecyclerView.ViewHolder {
        TextView textView, fileTimeStamp;
        ImageView imageView;

        public File_ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            imageView = itemView.findViewById(R.id.icon_view);
            fileTimeStamp = itemView.findViewById(R.id.file_time_text_view);
        }
    }
}
