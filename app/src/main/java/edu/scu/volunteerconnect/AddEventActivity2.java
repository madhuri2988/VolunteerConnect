package edu.scu.volunteerconnect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class AddEventActivity2 extends AppCompatActivity {
    File imageFile;
    String albumName = "photo";
    Button saveEvent,cancelEvent;
    EditText eventdetails;
    android.widget.Spinner spinner;
    String category;
    String imageFileString;
    final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/";
    Firebase firebaseRoot;
    static int REQUEST_CAMERA = 11;
    static int SELECT_FILE = 12;
    ImageView mImageView;
    static  int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        eventdetails = (EditText) findViewById(R.id.eventdetail);
        mImageView = (ImageView)findViewById(R.id.imageView3);

        Firebase.setAndroidContext(this);
        firebaseRoot = new Firebase(FIREBASE_URL);


        spinner = (android.widget.Spinner) findViewById(R.id.category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eventcategory, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageButton camera =(ImageButton) findViewById(R.id.imageButton);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission();
                        return;

                    } else {
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                            //  openImageIntent();

                            selectImage();
                        } else {
                            Toast.makeText(getApplicationContext(), "cannot take picture on this device", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (Exception ex) {
                    System.out.println("err" + ex.toString());
                    Log.e("err", ex.toString());
                }
            }
        });
        cancelEvent = (Button) findViewById(R.id.Cancel);
        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity2.this);
                builder.setTitle("Cancel Event")
                        .setMessage("Do you want to proceed?")
                        .setPositiveButton("Yes, I do!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent main = new Intent(getApplicationContext(), OrganizerHomepage.class);
                                startActivity(main);
                            }
                        })
                        .setNegativeButton("No, I don't!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Okay", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                ;
                builder.create().show();
            }
        });
        saveEvent = (Button) findViewById(R.id.button2);
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Event event = (Event) getIntent().getSerializableExtra("event");
                if (event != null) {
                    event.setImaglebase64(imageFileString);
                    category = spinner.getSelectedItem().toString();
                    event.setCategory(category);
                    event.setEvenDetails(eventdetails.getText().toString());

                    //  STORE IN FIREBASE

                    Firebase event1 = firebaseRoot.child("events");
                    event1.push().setValue(event, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                Log.e("Firebase Insert Error:", "Data could not be saved");
                            } else {
                                Toast.makeText(getApplicationContext(), "Event successfully added!", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent main = new Intent(getApplicationContext(), OrganizerHomepage.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(main);
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Missing values!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    private  void requestCameraPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
        {
            // you can show dialog here for grant permission (camera) and handle dialog event according to your need
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        }
        else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                try{
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestWriteExternalStoragePermissions();

                    } else {
                        File destination = new File(Environment.getExternalStorageDirectory(),
                                System.currentTimeMillis() + ".jpg");
                        FileOutputStream fo;
                        imageFileString = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                        Log.d("Base", imageFileString);
                        try {
                            destination.createNewFile();
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }}
                catch (Exception e){
                    Log.e("write permission",e.toString());
                }
                mImageView.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestWriteExternalStoragePermissions();

                    } else {
                        Uri selectedImageUri = data.getData();
                        String[] projection = {MediaStore.MediaColumns.DATA};
                        CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                                null);
                        Cursor cursor = cursorLoader.loadInBackground();
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        cursor.moveToFirst();
                        String selectedImagePath = cursor.getString(column_index);
                        Bitmap bm;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(selectedImagePath, options);
                        final int REQUIRED_SIZE = 200;
                        int scale = 1;
                        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                            scale *= 2;
                        options.inSampleSize = scale;
                        options.inJustDecodeBounds = false;
                        bm = BitmapFactory.decodeFile(selectedImagePath, options);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        imageFileString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                        Log.d("gallery", imageFileString);

                        mImageView.setImageBitmap(bm);
                    }
                }catch (Exception e){
                        Log.e("write permission",e.toString());
                    }
            }
        }

    }


    private  void requestWriteExternalStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))        {
            // you can show dialog here for grant permission (camera) and handle dialog event according to your need

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0 );
            //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0 );
        }
        else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0 );
            //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0 );
        }

    }
    String mCurrentPhotoPath;
    private File createImageFile() {
        File image = null;

        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestWriteExternalStoragePermissions();
                return image;

            } else {


                try {
                    // Create an image file name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = getAlbumStorageDir();
                    image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );
                    mCurrentPhotoPath = image.getAbsolutePath();
                } catch (Exception e) {
                    Log.e("photonotes", "failed to create image file.  We will crash soon!");
                    // we should do some meaningful error handling here !!!
                }

                Log.d(" mCurrentPhotoPath", image.getAbsolutePath());
            }
        }catch (Exception ex) {
            Toast.makeText(getApplicationContext(), " wwwwwwPermission to WRITE_EXTERNAL_STORAGE is not provided" + ex.toString(), Toast.LENGTH_LONG).show();
            System.out.println("err" + ex.toString());
        }
        //storeDB();
        return image;
    }
    public File getAlbumStorageDir() {        // Same as Environment.getExternalStorageDirectory() + "/Pictures/" + albumName

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (file.exists()) {
            Log.d("VC", "Album directory exists");
        } else if (file.mkdirs()) {
            Log.i("VC", "Album directory is created");
        } else {
            Log.e("VC", "Failed to create album directory.  Check permissions and storage.");
        }
        return file;
    }

}
