//package com.example.finalprojesicamerax3;
//
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.Firebase;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class Gallery extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.data_base); // your second layout XML
//
//        FirebaseApp.initializeApp(Gallery.this);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        FloatingActionButton add = findViewById(R.id.addNote);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View view1 = LayoutInflater.from(Gallery.this).inflate(R.layout.add_note_dialog, null);
//                TextInputLayout titleLayout, contentLayout;
//                titleLayout = view1.findViewById(R.id.titleLayout);
//                contentLayout = view1.findViewById(R.id.contentLayout);
//                TextInputEditText titleET, contentET;
//                titleET = view1.findViewById(R.id.titleET);
//                contentET = view1.findViewById(R.id.contentET);
//                AlertDialog alertDialog = new AlertDialog.Builder(Gallery.this)
//                        .setTitle("Add")
//                        .setView(view1)
//                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
//                                    titleLayout.setError("This field is required!");
//                                } else if (Objects.requireNonNull(contentET.getText().toString().isEmpty())) {
//                                    contentLayout.setError("This feild is required!");
//                                } else {
//                                    ProgressDialog dialog = new ProgressDialog(Gallery.this);
//                                    dialog.setMessage("Sorting in Database...");
//                                    dialog.show();
//                                    Note note = new Note();
//                                    note.setTitle(titleET.getText().toString());
//                                    note.setContent(contentET.getText().toString());
//                                    database.getReference().child("notes").push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            dialog.dismiss();
//                                            dialogInterface.dismiss();
//                                            Toast.makeText(Gallery.this, "Başarıyla Kaydedildi!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            dialog.dismiss();
//                                            Toast.makeText(Gallery.this, "Veri kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .create();
//                alertDialog.show();
//            }
//        });
//
//        TextView empty = findViewById(R.id.empty);
//
//        RecyclerView recyclerView = findViewById(R.id.recycler);
//
//        database.getReference().child("notes").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<Note> arrayList = new ArrayList<>();
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    Note note = dataSnapshot.getValue(Note.class);
//                    Objects.requireNonNull(note).setKey(dataSnapshot.getKey());
//                    arrayList.add(note);
//                }
//
//                if (arrayList.isEmpty()) {
//                    empty.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                } else {
//                    empty.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                }
//
//                NoteAdapter adapter = new NoteAdapter(Gallery.this, arrayList);
//                recyclerView.setAdapter(adapter);
//
//                adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
//                    @Override
//                    public void onClick(Note note) {
//                        View view = LayoutInflater.from(Gallery.this).inflate(R.layout.add_note_dialog, null);
//                        TextInputLayout titleLayout, contentLayout;
//                        TextInputEditText titleET, contentET;
//
//                        titleET = view.findViewById(R.id.titleET);
//                        contentET = view.findViewById(R.id.contentET);
//                        titleLayout = view.findViewById(R.id.titleLayout);
//                        contentLayout = view.findViewById(R.id.contentLayout);
//
//                        titleET.setText(note.getTitle());
//                        contentET.setText(note.getContent());
//
//
//                        ProgressDialog progressDialog = new ProgressDialog(Gallery.this);
//                        AlertDialog alertDialog = new AlertDialog.Builder(Gallery.this)
//                                .setTitle("Edit")
//                                .setView(view)
//                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
//                                            titleLayout.setError("This field is required!");
//                                        } else if (Objects.requireNonNull(contentET.getText().toString().isEmpty())) {
//                                            contentLayout.setError("This feild is required!");
//                                        } else {
//                                            progressDialog.setMessage("Saving...");
//                                            progressDialog.show();
//                                            Note note1 = new Note();
//                                            note1.setTitle(titleET.getText().toString());
//                                            note1.setContent(contentET.getText().toString());
//                                            database.getReference().child("notes").child(note.getKey()).setValue(note1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    progressDialog.dismiss();
//                                                    dialogInterface.dismiss();
//                                                    Toast.makeText(Gallery.this, "Başarıyla Kaydedildi!", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    progressDialog.dismiss();
//                                                    Toast.makeText(Gallery.this, "Veri kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                        }
//                                    }
//                                })
//                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        progressDialog.setTitle("Deleting...");
//                                        progressDialog.show();
//                                        database.getReference().child("notes").child(note.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                progressDialog.dismiss();
//                                                Toast.makeText(Gallery.this, "Başarıyla Silindi", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                progressDialog.dismiss();
//                                            }
//                                        });
//                                    }
//                                }).create();
//                        alertDialog.show();
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//}
//
