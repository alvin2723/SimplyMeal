package id.ac.umn.simplymeal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.ac.umn.simplymeal.LoginRegister.LoginActivity;
import id.ac.umn.simplymeal.LoginRegister.Preferences;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


public class AccountFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private Button btn_login;

    private ImageView imgProfile;
    private ProgressBar progressBar;
    private Button btnChangePass;
    private Button btnLogOut;

    ProgressDialog pd;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference databaseReference;

    TextView address;
    TextView name;
    TextView birthOfDate;;
    TextView gender;
    TextView phoneNumber;
    TextView email;

    //storage
    StorageReference storageReference;
    //Path buat save image
    String storagePath = "Users_Profile_Image/";
    //permission
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    String[] cameraPermissions;
    String storagePermissions[];

    Uri image_uri;
    View view;


    Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        final Context context = getContext();
        final String usrEmail = Preferences.getLoggedInEmail(context);

        databaseReference =FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = getInstance().getReference();

        //Get info according to currently signed user
        if(firebaseAuth.getCurrentUser() != null && (Preferences.getLoggedInStatus(context) == true)) {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.sidebar_menu);
            toolbar.setOnMenuItemClickListener(this);

            handler= new Handler();

            imgProfile = view.findViewById(R.id.myPict);
            name = view.findViewById(R.id.name);
            birthOfDate = view.findViewById(R.id.birth);
            address = view.findViewById(R.id.address);
            gender = view.findViewById(R.id.gender);
            phoneNumber = view.findViewById(R.id.noHp);
            email = view.findViewById(R.id.email);
            pd = new ProgressDialog(getActivity());
            //setText profile
            name.setText(Preferences.getLoggedInNamaLengkapUser(context));
            email.setText(Preferences.getLoggedInEmail(context));
            gender.setText(Preferences.getLoggedInGenderUser(context));
            birthOfDate.setText(Preferences.getLoggedInDateBirthUser(context));
            phoneNumber.setText(Preferences.getLoggedInMobileUser(context));
            address.setText(Preferences.getLoggedInAddress(context));

            progressBar = view.findViewById(R.id.progressBar);


            cameraPermissions = new String[] {
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE };
            storagePermissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE };

            databaseReference.child(usrEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data
                    if(dataSnapshot.exists()){

                        //Get data
                        final String nama = String.valueOf(dataSnapshot.child("firstName").getValue() + " " + dataSnapshot.child(
                                "lastName").getValue());
                        final String emails = "" + dataSnapshot.child("emailUser").getValue();
                        final String bod = "" + dataSnapshot.child("birthOfDate").getValue();
                        final String gen = "" + dataSnapshot.child("gender").getValue();
                        final String hp = "" + dataSnapshot.child("phoneUser").getValue();
                        final String image = "" + dataSnapshot.child("profilePhoto").getValue();

                        //Set data
                        Preferences.setLoggedInNamaLengkapUser(context, nama);
                        Preferences.setLoggedInGenderUser(context,gen);
                        Preferences.setLoggedInDateBirthUser(context,bod);
                        Preferences.setLoggedInMobileUser(context, hp);

                        //masukin address ke database
                        databaseReference.child(usrEmail).child("address").setValue(Preferences.getLoggedInAddress(context));

                        try {
                            //jika ada image
                            progressBar.setVisibility(View.INVISIBLE);
                            Picasso.get().load(image).into(imgProfile);
                        } catch (Exception e) {
                            //jika gaada image -> set default
                            progressBar.setVisibility(View.INVISIBLE);
                            Picasso.get().load(R.drawable.ic_person).into(imgProfile);
                        }
                    }
                    else{
                        Toast.makeText(view.getContext(), "No Profile Data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePicDialog();
                }
            });

            Button buttonEditName = view.findViewById(R.id.buttonEditName);
            buttonEditName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editName();
                }
            });

            view.findViewById(R.id.buttonEditGender).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editGender();
                }
            });

            view.findViewById(R.id.buttonEditBoD).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editBoD();
                }
            });
        }
        else{
            view = inflater.inflate(R.layout.fragment_noaccount, container, false);

            btn_login = view.findViewById(R.id.btnLogin);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, LoginActivity.class));
                    getActivity().finish();
                }
            });
        }
        return view;
    }

    private void editBoD() {
        Calendar calendar;
        int year, month, dayOfMonth;
        Date date;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String mon = "";
                        switch (month){
                            case 1:
                                mon = "January";
                                break;
                            case 2:
                                mon = "February";
                                break;
                            case 3:
                                mon = "Maret";
                                break;
                            case 4:
                                mon = "April";
                                break;
                            case 5:
                                mon = "May";
                                break;
                            case 6:
                                mon = "June";
                                break;
                            case 7:
                                mon = "July";
                                break;
                            case 8:
                                mon = "August";
                                break;
                            case 9:
                                mon = "September";
                                break;
                            case 10:
                                mon = "October";
                                break;
                            case 11:
                                mon = "November";
                                break;
                            case 12:
                                mon = "December";
                                break;
                        }
                        String date = day + " " + mon + " " + year;

                        final Context context = getContext();
                        final String usrEmail = Preferences.getLoggedInEmail(context);
                        birthOfDate.setText(date);
                        databaseReference.child(usrEmail).child("birthOfDate").setValue(date);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.setTitle("Edit Birth Of Date: ");
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void editGender() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.fragment_edit_gender, null);
        final RadioGroup rgGender = alertLayout.findViewById(R.id.rg_gender);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Edit Gender");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gen = "";
                switch (rgGender.getCheckedRadioButtonId()){
                    case R.id.rb_male :
                        gen = "Male";
                        break;
                    case R.id.rb_female :
                        gen = "Female";
                        break;
                }
                final Context context = getContext();
                gender.setText(gen);
                final String usrEmail = Preferences.getLoggedInEmail(context);

                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child(usrEmail).child("gender").setValue(gen);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void editName() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.fragment_edit_name, null);
        final EditText fname = alertLayout.findViewById(R.id.fname);
        final EditText lname = alertLayout.findViewById(R.id.lname);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Edit Name");
        alert.setView(alertLayout);
//         disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();

                name.setText(firstName+" "+lastName);
                final Context context = getContext();
                final String usrEmail = Preferences.getLoggedInEmail(context);

                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child(usrEmail).child("firstName").setValue(firstName);
                databaseReference.child(usrEmail).child("lastName").setValue(lastName);

                fname.onEditorAction(EditorInfo.IME_ACTION_DONE);
                lname.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED);
        return result;
    }

    public void requestStoragePermission(){
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    public boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_DENIED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED);
        return (result && result1);
    }

    public void requestCameraPermission(){
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //camera
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        pickFromCamera();
                    }
                }else if(which == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted =
                            grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        pickFromCamera();
                    }else{
                        //permission denied
                        Toast.makeText(getActivity(), "Please enable camera and storage " +
                                "permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                boolean writeStorageAccepted =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(writeStorageAccepted){
                    pickFromGallery();
                }else{
                    //permission denied
                    Toast.makeText(getActivity(), "Please enable storage " +
                            "permission", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                uploadProfile(image_uri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                uploadProfile(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfile(final Uri uri) {
        String filePathAndName = storagePath + "_" + user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //upload image
                        final Context context = getContext();
                        final String usrEmail = Preferences.getLoggedInEmail(context);
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String link = uriTask.getResult().toString();
                        databaseReference.child(usrEmail).child("profilePhoto").setValue(link);
                        Uri downloadUri = uriTask.getResult();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get().load(link).into(imgProfile);
                                pd.dismiss();
                            }
                        },1500);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            pd.setMessage("Upload Foto...");
                            pd.show();
                            pd.setCancelable(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void logout() {
        if (Preferences.getLoggedInStatus(view.getContext()) == true) {
            FirebaseAuth.getInstance().signOut();
            Preferences.clearLoggedInUser(view.getContext());
            startActivity(new Intent(view.getContext(), LoginActivity.class));
            getActivity().finish();
        } else {
            Toast.makeText(view.getContext(), "Please Login First", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePass() {
        final Context context = getContext();

        // get alert_dialog.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.promp_reset, null);

        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(context);
        passwordResetDialog.setView(promptsView);

        final EditText resetPasswords = (EditText) promptsView.findViewById(R.id.editPass);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // extract the email and send reset link
                String newPassword = resetPasswords.getText().toString();
                if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(getActivity(), "This field is required.", Toast.LENGTH_SHORT).show();
                } else {
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Password Reset Successfully, Please Login Again!", Toast.LENGTH_LONG).show();

                            FirebaseAuth.getInstance().signOut();
                            Preferences.clearLoggedInUser(view.getContext());
                            startActivity(new Intent(view.getContext(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Password Reset Failed. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close
            }
        });

        passwordResetDialog.create().show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.change_pass:
                changePass();
                return true;
            case R.id.aboutUs:
                Intent intent = new Intent(getActivity(), AboutUs.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}