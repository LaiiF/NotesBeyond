package com.example.group4.notesbeyond;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditWindow extends AppCompatActivity implements
        View.OnClickListener, View.OnTouchListener, MediaPlayer.OnPreparedListener {

    EditText text;
    String savedData;
    Button photo;
    Button drawing;
    Button buttonText;
    Uri bmap;
    DateFormat dFormat = new SimpleDateFormat("MMM.dd.yyy 'at' HH.mm.ss");
    Note tempNote;
    ImageView imageView;
    MyCanvas myCanvas;
    float x=0,y=0,dx=0,dy=0;

    Button buttonStart, buttonStop, buttonRecord;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    public static void moveToFront(View child) {
        ViewGroup parent = (ViewGroup)child.getParent();
        if (parent != null) {
            parent.bringChildToFront(child);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_window);
        photo = findViewById(R.id.button2);
        drawing = findViewById(R.id.button3);
        photo.setOnClickListener(this);
        drawing.setOnClickListener(this);
        myCanvas = new MyCanvas(this,null);
        buttonText = findViewById(R.id.button8);
        text = findViewById(R.id.editText7);
        text.setBackground(null);
        text.setZ(4f);
        ViewOutlineProvider k = text.getOutlineProvider();
        k = null;

        savedData = text.getText().toString();


            text.setText(getIntent().getStringExtra("text"));
            if ((Uri)getIntent().getParcelableExtra("uri") != null) {
                System.out.println((Uri)getIntent().getParcelableExtra("uri"));
                imageView = (ImageView) findViewById(R.id.imageView2);
                imageView.setOnTouchListener(this);
                System.out.println("check1");
                //try{
                   /* String path = getRealPathFromURI(this,(Uri)getIntent().getParcelableExtra("uri"));
                    File file = new File(path);
                    Bitmap tempMap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    System.out.println("check2");
                imageView.setImageBitmap(tempMap);
                    System.out.println("check3");
                bmap = (Uri)getIntent().getParcelableExtra("uri");
                    System.out.println("check4");
                }catch(Exception e){System.out.println(e);}
                */
            }


        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setOutputFile(dFormat.format(Calendar.getInstance().getTime()));
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaPlayer = new MediaPlayer();
        buttonStart = (Button) findViewById(R.id.button5);
        buttonStop = (Button) findViewById(R.id.button6);
        buttonRecord = (Button) findViewById(R.id.button7);
        buttonRecord.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonStart.setOnClickListener(this);

    }

    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("uri", bmap);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        intent.putExtra("text", text.getText().toString());
        setResult(1, intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if(view.getId() == photo.getId()){
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
        else if(view.getId() == drawing.getId()){
            try {
                myCanvas.setZ(Math.max(text.getZ() + 1, imageView.getZ() + 1));
            }catch (Exception e){myCanvas.setZ(text.getZ() + 1);}
        }
        else if(view.getId() == buttonText.getId()){
            try {
                text.setZ(Math.max(myCanvas.getZ() + 1, imageView.getZ() + 1));
            }catch (Exception e){text.setZ(myCanvas.getZ() + 1);}
        }
        else if(view.getId() == buttonStart.getId()){
            System.out.println("banana");
            Intent audioIntent = new Intent(Intent.ACTION_GET_CONTENT);
            audioIntent.setType("audio/*");
            startActivityForResult(audioIntent, 2);
            mediaPlayer.start();
        }
        else if(view.getId() == buttonStop.getId()){
            //mediaRecorder.stop();
            mediaPlayer.stop();
        }
        else if(view.getId() == buttonRecord.getId()){
            try {
                mediaRecorder.prepare();
            }catch (IOException e){}
            mediaRecorder.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View view, MotionEvent event){
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN :
            {
                dx = event.getX() - view.getX();
                dy = event.getY() - view.getY();
            }
            break;
            case MotionEvent.ACTION_MOVE :
            {
                view.setX(event.getX()-dx);
                view.setY(event.getY()-dy);
            }
            break;
        }
        imageView.setZ(1f);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onActivityResult(int reqCode, int resCode, Intent data){
        Uri uri = data.getData();
        if(reqCode == 1) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView = (ImageView) findViewById(R.id.imageView2);
                imageView.setZ(Math.max(text.getZ() + 1, myCanvas.getZ() + 1));
                imageView.setOnTouchListener(this);
                imageView.setImageBitmap(bitmap);
                bmap = uri;
            } catch (IOException e) {}
        }
        else if(reqCode == 2){
            try {
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
            }catch (IOException e){}
        }
    }

    public void onPrepared(MediaPlayer mp){
        mp.start();
    }
}

