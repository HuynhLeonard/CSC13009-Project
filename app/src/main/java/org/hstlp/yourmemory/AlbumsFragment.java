package org.hstlp.yourmemory;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {
    private FloatingActionButton btnAdd;
    private RecyclerView albumRecView;
    private AlbumsAdapter albumsAdapter;

    private ArrayList<String> albums;
    private final int CAMERA_CAPTURED = 1;
    private Context context;

    public AlbumsFragment(Context context) {
        this.context = context;
    }

    public static AlbumsFragment getInstance(Context context) {
        return new AlbumsFragment(context);
    }

    public ArrayList<String> getAlbums() {
        return AlbumUtility.getInstance(context).getAllAlbums();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albums = AlbumUtility.getInstance(context).getAllAlbums();

        ((MainActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)context).getSupportActionBar().setTitle("Gallery");
        ((MainActivity)context).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((MainActivity)context).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View albumsFragment = inflater.inflate(R.layout.albums_fragment, container, false);

        albumRecView = albumsFragment.findViewById(R.id.albumsRecView);
        albumsAdapter = new AlbumsAdapter(context, albums);
        albumRecView.setAdapter(albumsAdapter);
        albumRecView.setLayoutManager(new GridLayoutManager(albumsFragment.getContext(),3));
        btnAdd = (FloatingActionButton) albumsFragment.findViewById(R.id.btnAdd_AlbumsFragment);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAlbum();
            }
        });

        return albumsFragment;
    }

    private void addNewAlbum() {
        View addNewAlbumForm = LayoutInflater.from(context).inflate(R.layout.add_album_form, null);
        EditText edtAlbumName = addNewAlbumForm.findViewById(R.id.edtAlbumName);

        AlertDialog.Builder addDialog = new AlertDialog.Builder(context, androidx.appcompat.R.style.AlertDialog_AppCompat);
        addDialog.setView(addNewAlbumForm);
        addDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAlbumName = edtAlbumName.getText().toString();
                if (newAlbumName.length() != 0) {
                    if (AlbumUtility.getInstance(context).addNewAlbum(newAlbumName)) {
                        albumsAdapter.addAlbum(newAlbumName);
                        Toast.makeText(context, "New album created successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Error: Failed to create new album!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Empty Album Name!", Toast.LENGTH_LONG).show();
                }
            }
        });
        addDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        addDialog.create();
        addDialog.show();
    }
}