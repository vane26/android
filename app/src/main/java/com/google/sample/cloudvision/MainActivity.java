/*
 * Created by Vane on 29/08/2016.
 */

package com.google.sample.cloudvision;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Landmark;
import com.google.api.services.vision.v1.model.Position;
import com.google.gson.Gson;
import com.google.sample.cloudvision.BD.Registro;
import com.google.sample.cloudvision.BD.RegistroDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyAeBwFSno5fvo7D3tMgeHUEaD1Ur1-pMUI";
    public static final String FILE_NAME = "Clave de API 1";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    private BatchAnnotateImagesResponse respuesta;
    RegistroDbHelper db;
    private TextView mImageDetails;
    private ImageView mMainImage;
    public Context myContext;
    private Button Guardar;
    EditText editIndice, editCalidad;
    TextView textView, textView1, textView4;
    CopiarArchivo copia;
    //private Canvas canvas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = new RegistroDbHelper(this, RegistroDbHelper.data_base, null, RegistroDbHelper.version);
        //db.getWritableDatabase(); //accion a realizar, lectura o escritura.
        db.comenzarProceso();
        //Grabar = (Button) findViewById(R.id.button);
        editIndice = (EditText) findViewById(R.id.editText);
        editIndice.setVisibility(View.INVISIBLE);


        editCalidad = (EditText) findViewById(R.id.editText2);
        editCalidad.setVisibility(View.INVISIBLE);


        textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);


        textView1 = (TextView) findViewById(R.id.textView2);
        textView1.setVisibility(View.INVISIBLE);


        textView4 = (TextView) findViewById(R.id.textView);
        textView4.setVisibility(View.INVISIBLE);


        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setVisibility(View.INVISIBLE);


        final FloatingActionButton grabar = (FloatingActionButton) findViewById(R.id.grabar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);


        grabar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1

                        .setMessage(R.string.dialog_select_registro)
                        .setPositiveButton(R.string.dialog_select_grabar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editIndice.setVisibility(View.VISIBLE);
                                editCalidad.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                textView1.setVisibility(View.VISIBLE);
                                //Insertar una fila o registro en la tabla "registro"
                                //si la inserción es correcta devolverá true
                                if ((editIndice.length() != 0) && (editCalidad.length() != 0)) { // si los campos estan vacios puede grabar
                                    boolean resultado = true;
                                    //db.agregar2(editIndice.getText().toString(), editCalidad.getText().toString());
                                    if (resultado) {
                                        Toast.makeText(getApplicationContext(),
                                                "datos guardados correctamente", Toast.LENGTH_LONG).show();
                                        fab.setVisibility(View.VISIBLE);
                                        grabar.setVisibility(View.INVISIBLE);
                                        textView4.setVisibility(View.VISIBLE);
                                    } else
                                        Toast.makeText(getApplicationContext(),
                                                "No se ha podido guardar", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(getApplicationContext(),
                                            "Debe completar los campos antes de grabar", Toast.LENGTH_LONG).show();

                            }

                        })
                        .setNegativeButton(R.string.dialog_select_salir, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(RESULT_OK);
                                finish();

                            }
                        });


                builder1.create().show();
            }


        });


        grabar.callOnClick();
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder

                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGalleryChooser();
                                try {
                                    backupDatabase(fab);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                exportDB();


                            }
                        })
                        .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                                try {
                                    backupDatabase(fab);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                exportDB();


                            }


                        });

                builder.create().show();
            }


        });


        mImageDetails = (TextView) findViewById(R.id.image_details);
        mMainImage = (ImageView) findViewById(R.id.main_image);


    }


    public void backupDatabase(View view) throws IOException {
        if (Environment.getExternalStorageState() != null) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyApp");
            if (dir.exists()) {
                //dir.delete();
            } else {
                dir.mkdir();
            }

            String fromPath = "";
            if (android.os.Build.VERSION.SDK_INT >= 4.2) {
                fromPath = getApplicationInfo().dataDir + "/databases/" + "registro_db.db";
            } else {
                fromPath = "/data/data/" + getPackageName() + "/databases/" + "registro_db.db";
            }

            File currentDB = getApplicationContext().getDatabasePath(fromPath);


            String toPath = "/registro_db.db";
            File backupDB = new File(dir, toPath);

            if (currentDB.exists()) {
                FileInputStream fis = new FileInputStream(currentDB);
                FileOutputStream fos = new FileOutputStream(backupDB);
                fos.getChannel().transferFrom(fis.getChannel(), 0, fis.getChannel().size());
                // or fis.getChannel().transferTo(0, fis.getChannel().size(), fos.getChannel());
                fis.close();
                fos.close();


                //This is to refresh the folders in Windows USB conn.
                MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyApp"}, null, null);
                MediaScannerConnection.scanFile(this, new String[]{toPath}, null, null);
            }
        }
    }


    private void exportDB() {

        File dbFile = getDatabasePath("registro_db.db");
        RegistroDbHelper dbhelper = new RegistroDbHelper(this, RegistroDbHelper.data_base, null, RegistroDbHelper.version);
        File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyApp");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "registro_db.csv");
        try {
            file.createNewFile();
            CsvWriter csvWrite = new CsvWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM registro", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


    public void startGalleryChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"),
                GALLERY_IMAGE_REQUEST);
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }


    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            uploadImage(Uri.fromFile(getCameraFile()));
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.permissionGranted(
                requestCode,
                CAMERA_PERMISSIONS_REQUEST,
                grantResults)) {
            startCamera();
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                callCloudVision(bitmap);
                //mMainImage.setImageBitmap(bitmap);
                reconocimiento(bitmap);
            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {

        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);
        // Do the real work in an async task, because we need to use the network anyway
        CloudVision cloudVision= new CloudVision(bitmap);
        try {
            cloudVision.execute((Void)null).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {


        String message = "I found these things:\n\n";


        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format("%.3f: %s", label.getScore(), label.getDescription());
                message += "\n";


            }
        } else {
            message += "nothing";
        }


        List<EntityAnnotation> texts = response.getResponses().get(0).getTextAnnotations();


        if (texts != null) {
            for (EntityAnnotation text : texts) {
                message += String.format("%s: %s", text.getScore(), text.getDescription());
                message += "\n";
                //db.agregar(message);

            }






            try {



                AnnotateImageResponse annotateImageResponse=response.getResponses().get(0);
                List<FaceAnnotation> faceAnnotations=annotateImageResponse.getFaceAnnotations();

                Gson gson =  new Gson();
                String json = gson.toJson(faceAnnotations);




                db.insert(new Registro(editIndice.getText().toString(), message, editCalidad.getText().toString(), json));



                
                List<Registro> list = db.ListadoGeneral();
                String[] array = new String[list.size()];
                int index = 0;
                for (Object value : list) {
                    array[index] = (String) value;
                    index++;

                    for (int i = 0; i < array[index].length(); i++) {
                        Log.d(TAG, "FOR");
                        if ((array[index].equals(message)) && (array[index].equals(json))) {
                            Toast.makeText(this, "No registra información en nuestra base de datos", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Registra información en nuestra base de datos", Toast.LENGTH_LONG).show();
                        }
                    }
                }





                db.finalizaProceso();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            message += "nothing";

        }


        return message;


    }










    public void reconocimiento(Bitmap bitmap) {

        Bitmap tempBitMap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitMap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        paint.setColor(Color.RED);


        canvas.drawBitmap(bitmap, 0, 0, null);
        AnnotateImageResponse response=respuesta.getResponses().get(0);
        List<FaceAnnotation> faces = response.getFaceAnnotations();

        if (faces != null) {
            for (FaceAnnotation face : faces) {
               // drawRectangle(canvas,face.getPosition(),face.getWidth(),face.getHeight());
                for(Landmark landmark:face.getLandmarks()){
                    drawPoint(canvas,landmark.getPosition());
                }
            }
        }
        mMainImage.setImageBitmap(tempBitMap);

    }


    private void drawPoint(Canvas canvas, Position point){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);

        float x = scaleX(point.getX());
        float y = scaleY(point.getY());

        canvas.drawCircle(x, y, 1, paint);
    }

    private void drawRectangle(Canvas canvas, PointF point, float width,
                               float height){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        float x1 = scaleX(point.x);
        float y1 = scaleY(point.y);
        float x2 = scaleX(point.x + width);
        float y2 = scaleY(point.y + height);

        RectF rect = new RectF(x1, y1, x2, y2);
        canvas.drawRect(rect, paint);
    }

    public float scaleX(float horizontal) {
        return horizontal * 1.0f;
    }

    public float scaleY(float vertical) {
        return vertical * 1.0f;
    }





    public class CloudVision extends AsyncTask<Void,Void,String>{
        private Bitmap bitmap;

        public CloudVision(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
        @Override
        protected String doInBackground(Void... params) {
            try {

                HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = GsonFactory.getDefaultInstance();


                Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                builder.setVisionRequestInitializer(new
                        VisionRequestInitializer(CLOUD_VISION_API_KEY));
                Vision vision = builder.build();

                //Gson resultado = new Gson();
              //  Registro registro = (Registro) resultado.fromJson(String.valueOf(jsonFactory), Registro.class);
               // Log.d(TAG, registro.getImagen());



                BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                        new BatchAnnotateImagesRequest();
                batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {


                    {
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();


                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);

                            Feature textDetection = new Feature();
                            textDetection.setType("TEXT_DETECTION");
                            textDetection.setMaxResults(10);
                            add(textDetection);

                            Feature landmarkDetection = new Feature();
                            landmarkDetection.setType("LANDMARK_DETECTION");
                            landmarkDetection.setMaxResults(10);
                            add(landmarkDetection);

                            Feature faceDetection = new Feature();
                            faceDetection.setType("FACE_DETECTION");
                            faceDetection.setMaxResults(10);
                            add(faceDetection);



                        }});


                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }
                });


                Vision.Images.Annotate annotateRequest =
                        vision.images().annotate(batchAnnotateImagesRequest);
                // Due to a bug: requests to Vision API containing large images fail when GZipped.
                annotateRequest.setDisableGZipContent(true);
                Log.d(TAG, "created Cloud Vision request object, sending request");

                BatchAnnotateImagesResponse response = annotateRequest.execute();

                respuesta = response;



                //System.out.print(json);


                return convertResponseToString(response);


            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mImageDetails.setText(s);
        }
    }



}
