package com.example.group4.notesbeyond;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener
{

    ListView listView;
    ArrayList<String> myNotes;
    ArrayList<Note> NoteData = new ArrayList<>();
    ArrayAdapter<String> myNotesAdapter;
    TextView myTextView;
    EditText myEditText;
    Button myAddButton, myDeleteButton, myEditButton;
    int selectedListPosition;
    Map<String, Note> storedData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.myNotesId);
        myTextView = (TextView) findViewById(R.id.textView2);
        myTextView.setText("");
        myEditText = (EditText) findViewById(R.id.editText);
        myAddButton = (Button) findViewById(R.id.addButton);
        myDeleteButton = (Button) findViewById(R.id.deleteButton);
        myEditButton = (Button) findViewById(R.id.editbutton);
        selectedListPosition = -1;

        myNotes = new ArrayList<String>();

        myNotesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myNotes);

        listView.setAdapter(myNotesAdapter);
        listView.setOnItemClickListener(this);
        myAddButton.setOnClickListener(this);
        myDeleteButton.setOnClickListener(this);
        myEditButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String myItemSelected;

        selectedListPosition = position;

        myItemSelected = (String) parent.getItemAtPosition(position);

        myTextView.setText(myItemSelected);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == myAddButton.getId()){
            String whatUserEntered = myEditText.getText().toString();
            myNotes.add(whatUserEntered);
            myNotesAdapter.notifyDataSetChanged();
        }
        else if(view.getId() == myDeleteButton.getId() && selectedListPosition != -1){
            NoteData.remove(getNote(myNotes.get(selectedListPosition)));
            myNotes.remove(selectedListPosition);
            myNotesAdapter.notifyDataSetChanged();
            selectedListPosition = -1;
        }
        else if(view.getId() == myEditButton.getId() && selectedListPosition != -1){
            if(getNote(myNotes.get(selectedListPosition)) != null){
                Intent startNewActivity = new Intent(this, EditWindow.class);
                startNewActivity.putExtra("name", myNotes.get(selectedListPosition));
                startNewActivity.putExtra("uri", getNote(myNotes.get(selectedListPosition)).getBitmap());
                startNewActivity.putExtra("text", getNote(myNotes.get(selectedListPosition)).getData());
                startActivityForResult(startNewActivity, 6);
            }
            else {
                Intent startNewActivity = new Intent(this, EditWindow.class);
                startNewActivity.putExtra("name", myNotes.get(selectedListPosition));
                startNewActivity.putExtra("text", "                                                            \n                                                            \n                                                            \n                                                           \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                           \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \\n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \\n                                                            \\n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                            \n                                                           \n                                                            \n                                                            \n                                                                                                    \n");
                System.out.println("banana");
                NoteData.add(new Note(myEditText.getText().toString()));
                startActivityForResult(startNewActivity, 6);
            }
        }

    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(data != null){
            NoteData.get(findIndex(data.getStringExtra("name"))).editData((Uri)data.getParcelableExtra("uri"));
            System.out.println((Uri)data.getParcelableExtra("uri"));
            NoteData.get(findIndex(data.getStringExtra("name"))).editData(data.getStringExtra("text"));
        }
        else
            System.out.println("babababababa");
    }

    public Note getNote(String data){
        for(Note i: NoteData){
            if(i.getName().equals(data))
                return i;
        }
        return null;
    }

    public int findIndex(String name){
        for(int i = 0; i < NoteData.size(); i++){
            if(NoteData.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

}
