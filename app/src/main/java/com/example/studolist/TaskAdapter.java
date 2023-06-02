package com.example.studolist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studolist.Models.Task;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;


public class TaskAdapter  extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskViewHolder> {


    Context context;
    Dialog myDialog;
    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options, Context context) {
        super(options);

        this.context = context;
        myDialog = new Dialog(context);

    }


    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Task task) {
        String formatted = Utility.formatDate(task.getDueDate());

        holder.todoTxt.setText(task.getTask());
        holder.todayChip.setText(formatted);

        int color = Utility.priorityColor(task);
        holder.todayChip.setTextColor(color);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,TaskDetailsActivity.class);
            intent.putExtra("task",task.getTask());
            intent.putExtra("dueDate",task.getDueDate().getTime());
            intent.putExtra("priority", task.getPriority().ordinal());
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

        holder.radioButton.setOnClickListener((v) -> {
                DocumentReference documentReference;
                String docId = this.getSnapshots().getSnapshot(position).getId();
                documentReference = Utility.loadTaskFromDb_tasks().document(docId);
                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if(task.isSuccessful()){
                            //note is competed
                            Utility.showToast(context,"Task completed");
                        }else{
                            Utility.showToast(context,"Failed while deleting task");
                        }
                    }
                });
            });
        if (task.getImgUri() == null) {
            holder.camChip.setVisibility(View.INVISIBLE);
        } else {
            holder.camChip.setVisibility(View.VISIBLE);
        }
        holder.camChip.setOnClickListener(v -> {
                    showPopup(v,task.getImgUri());
        });

        holder.shareChip.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, task.toShareStr() );
            intent.setType("text/plain");

            if(intent.resolveActivity(context.getPackageManager()) !=null)
            {
                context.startActivity(intent);
            }
        });


    }
    public void showPopup(View v,String imgUri) {
        TextView txtclose;
        ImageView imageViewPopup;
        myDialog.setContentView(R.layout.popup_img);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        imageViewPopup = (ImageView) myDialog .findViewById(R.id.img_popup);
        if(imgUri !=null)
        {
            Picasso.get()
                    .load(imgUri)
                    .resize(900,880)
                    .centerInside()
                    .into(imageViewPopup);
        }


        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


        @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new TaskViewHolder(view);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView todoTxt;
        RadioButton radioButton;
        Chip todayChip;
        Chip camChip;
        Chip shareChip;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            todoTxt = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            camChip = itemView.findViewById(R.id.cam_chip);
            shareChip = itemView.findViewById(R.id.share_chip);
        }

    }

}
