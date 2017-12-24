package com.logistics.sj.appv1.ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.logistics.sj.appv1.R;
import com.logistics.sj.appv1.database.Contract;
import com.logistics.sj.appv1.database.DBActivities;
import com.logistics.sj.appv1.model.TruckBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Secondary extends AppCompatActivity {

    private File storageDir=new File(Environment.getExternalStorageDirectory(),"/Appv1");
    private static  final  int IMAGE_CAPTURE=0;
    private static final int SHARE_IMAGE=1;
    private static final int GET_IMAGE=2;

    final String BUNDLE_EXTRA="BUNDLE_EXTRA";
    final String TRUCK_NUMBER="TRUCK_NUMBER";
    final String PHONE_NUMBER="PHONE_NUMBER";

    ArrayList<String> ownerNumberList=new ArrayList<String>();
    ArrayList<String> accountNumberList=new ArrayList<String>();

    String copiedImage="";

    EditText truckNumber;
    EditText ownerName;
    EditText ownerNumberText;
    AppCompatSpinner ownerNumbers;
    EditText accountNumberText;
    AppCompatSpinner accountNumbers;
    TruckBean truckBean=new TruckBean();
    ArrayAdapter<String> ownerNumberAdapter;
    ArrayAdapter<String> accountNumberAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        truckNumber=(EditText)findViewById(R.id.TruckNumber);
        ownerName=(EditText)findViewById(R.id.OwnerName);
        ownerNumberText=(EditText)findViewById(R.id.OwnerNumberText);
        ownerNumbers=(AppCompatSpinner)findViewById(R.id.OwnerNumbers);
        accountNumberText=(EditText)findViewById(R.id.AccountNumberText);
        accountNumbers=(AppCompatSpinner)findViewById(R.id.accounts);

        ownerNumberList=new ArrayList<String>();
        accountNumberList=new ArrayList<String>();
       /*
        truckNumbertext= ((EditText) findViewById(R.id.TruckNumberText));
        driverNameText=((EditText) findViewById(R.id.DriverNameText));
        phoneNumbertext=((EditText) findViewById(R.id.PhoneNumberText));
        panIndicator=(ImageButton)findViewById(R.id.PANIndicator);
        rcIndicator=(ImageButton)findViewById(R.id.RCIndicator);
        */

        try {
            TruckBean truckBean=(TruckBean)getIntent().getSerializableExtra("truckDetails");

            if (truckBean!=null)
            {
                truckNumber.setText(truckBean.getTruckNumber());
                ownerName.setText(truckBean.getTruckOwnerName());

                for (String ownerNumber:truckBean.getTruckOwnerPhoneNumberS()) {
                    ownerNumberList.add(ownerNumber);
                }

                for (String accountNumber:truckBean.getAccountNumbers()) {
                    accountNumberList.add(accountNumber);
                }
            }

            populateSpinners();
            //code for color change of button
//            File temp = new File(storageDir,truckNumbertext.getText().toString()+"_PAN.jpg");
//            if (temp.exists())
//                panIndicator.setBackgroundColor(getResources().getColor(R.color.Green,null));
//            else
//               panIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));
//
//            temp = new File(storageDir, truckNumbertext.getText().toString()+"_RC.jpg");
//            if (temp.exists())
//                rcIndicator.setBackgroundColor(getResources().getColor(R.color.Green,null));
//            else
//               rcIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));

        }catch (Exception e)
        {
            //Toast.makeText(this,"some error has occured while loading detail screen"+e.getMessage(),Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"error oncreate "+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    public void populateSpinners() {

        try {

            //driverNumbers
            if (ownerNumberList!=null) {
                ownerNumberAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ownerNumberList);
                ownerNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ownerNumbers.setAdapter(ownerNumberAdapter);
                ownerNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ownerNumberText.setText(parent.getItemAtPosition(position).toString());//setiing to latest
                      //  Toast.makeText(getBaseContext(), "" + parent.getItemAtPosition(position) + "selected", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            //accountNumber
            if (accountNumberList!=null) {
                accountNumberAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountNumberList);
                accountNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                accountNumbers.setAdapter(accountNumberAdapter);
                accountNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        accountNumberText.setText(parent.getItemAtPosition(position).toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }catch (Exception e) {
            Toast.makeText(this,"error populating spinners"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    public void addOwnerNumber(View view) {

        ownerNumberList.add(ownerNumberText.getText().toString());

        populateSpinners();
    }

    public void removeOwnerNumber(View view){

//        for (String ownerNumber:ownerNumberList)
//        {
//            if (ownerNumber.equalsIgnoreCase(ownerNumberText.getText().toString()))
//                ownerNumberList.remove(ownerNumber);
//        }
        ownerNumberList.remove(ownerNumberText.getText().toString());
        populateSpinners();
    }

    public void addAccountNumber(View view) {
        accountNumberList.add(accountNumberText.getText().toString());
        populateSpinners();
    }

    public void removeAccoutNumber(View view){

        accountNumberList.remove(accountNumberText.getText().toString());
        populateSpinners();
    }

    public void takePicture(View view) {

        String suffixString="";
        if (view.getId()==R.id.takePAN) {
            suffixString = "_PAN.jpg";
        }
        else if (view.getId()==R.id.takeRC)
            suffixString="_RC.jpg";
        else if (view.getId()==R.id.takeTDS)
            suffixString="_TDS.jpg";
        else
            suffixString="_AC.jpg";


        Toast.makeText(this, "from view"+view.getId()+"from rid"+R.id.takePAN,Toast.LENGTH_SHORT);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imgFile = new File(storageDir, truckNumber.getText().toString()+suffixString);

        try{
            Uri imgSaveUri = FileProvider.getUriForFile(view.getContext(),
                    "com.logistics.sj.appv1.provider",
                    imgFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgSaveUri);
            startActivityForResult(intent,IMAGE_CAPTURE);
        }catch (Exception e) {
            Toast.makeText(Secondary.this, "Issue occured while capturing image", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewImage(View view) {

        String suffixString="";
        if (view.getId()==R.id.viewPAN)
        suffixString="_PAN.jpg";
        else if (view.getId()==R.id.viewRC)
            suffixString="_RC.jpg";
        else if (view.getId()==R.id.viewTDS)
            suffixString="_TDS.jpg";
        else
            suffixString="_AC.jpg";

        File imgFile= new File(storageDir, truckNumber.getText().toString()+suffixString);
        Toast.makeText(this,imgFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        Uri galleryImageUri = FileProvider.getUriForFile(view.getContext(),
                "com.logistics.sj.appv1.provider",
                imgFile);
        Intent galleryIntent=new Intent(Intent.ACTION_VIEW,galleryImageUri);
        galleryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(galleryIntent);
    }

    public void shareImage(View view) {

        String suffixString="";
        if (view.getId()==R.id.sharePAN)
            suffixString="_PAN.jpg";
        else if (view.getId()==R.id.shareRC)
            suffixString="_RC.jpg";
        else if (view.getId()==R.id.shareTDS)
            suffixString="_TDS.jpg";
        else
            suffixString="_AC.jpg";

        File  imgFile = new File(storageDir,truckNumber.getText()+suffixString);

        if (!imgFile.exists()) {
            Toast.makeText(this,"File doesn't exits",Toast.LENGTH_SHORT).show();
            return;
        }
            Uri imgSaveUri1 = FileProvider.getUriForFile(view.getContext(),
                "com.logistics.sj.appv1.provider",
                imgFile);

        //choose to share
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,imgSaveUri1);
        intent.setType("image/jpeg");
        startActivityForResult(Intent.createChooser(intent,"send it"),SHARE_IMAGE);


    }

    public void getImageFromGallery(View view){

        if (view.getId()==R.id.getPAN)
            copiedImage=truckNumber.getText()+"_PAN.jpg";
        else if (view.getId()==R.id.getRC)
            copiedImage=truckNumber.getText()+"_RC.jpg";
        else if (view.getId()==R.id.getTDS)
            copiedImage=truckNumber.getText()+"_TDS.jpg";
        else
            copiedImage=truckNumber.getText()+"_AC.jpg";


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GET_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Toast.makeText(Secondary.this, " image saved", Toast.LENGTH_SHORT).show();
        }else if(requestCode == SHARE_IMAGE && resultCode == RESULT_OK)
        {
            Toast.makeText(Secondary.this, " image shared", Toast.LENGTH_SHORT).show();
        }else if(requestCode == GET_IMAGE && resultCode == RESULT_OK)
        {
            try {

                OutputStream out;

                File file = new File(storageDir + File.separator +copiedImage);
                Toast.makeText(Secondary.this, "new  file"+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                file.createNewFile();
                out = new FileOutputStream(file);


                Uri imageUri=data.getData();
                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                out.write(readBytes(inputStream));
                out.close();
            }catch (Exception e)
            {
                Toast.makeText(this,"Error while getting copying image",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(Secondary.this, " image saved", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this,"Image not saved/shared",Toast.LENGTH_SHORT).show();
        }

        //code for color change of button
//        File temp = new File(storageDir,truckNumbertext.getText().toString()+"_PAN.jpg");
//
//        if (temp.exists())
//            panIndicator.setBackgroundColor(getResources().getColor(R.color.Green,null));
//        else
//            panIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));
//
//        temp = new File(storageDir,truckNumbertext.getText().toString()+"_RC.jpg");
//
//        if (temp.exists())
//            rcIndicator.setBackgroundColor(getResources().getColor(R.color.Green,null));
//        else
//            rcIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));
//

    }

    //while copying image
    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public void addRecord(View view) {

        if (truckNumber.getText()==null || truckNumber.getText().toString().equals(""))
        {
            Toast.makeText(this,"Trucknumber cannot be left blank",Toast.LENGTH_SHORT).show();
            return;
        }
        TruckBean truckBean=new TruckBean();
        truckBean.setTruckNumber((truckNumber.getText().toString()));
        truckBean.setTruckOwnerName(ownerName.getText().toString());

          String[] ownerNumbers=new String[ownerNumberList.size()];
        for (int i=0;i<ownerNumberList.size();i++)
            ownerNumbers[i]=ownerNumberList.get(i);
        truckBean.setTruckOwnerPhoneNumberS(ownerNumbers);

        String[] accountNumbers=new String[accountNumberList.size()];
        for (int i=0;i<accountNumberList.size();i++)
            accountNumbers[i]=accountNumberList.get(i);
        truckBean.setAccountNumbers(accountNumbers);

       //TODO
        // truckBean.setPicLocation("Location");
        //truckBean.setIcon(1);
      try {
          new DBActivities().addTruck(truckBean,Secondary.this);
              Toast.makeText(Secondary.this,"Added "+truckBean.getTruckNumber()+" to database",Toast.LENGTH_SHORT).show();

      //cosmetic
//              truckNumbertext.setEnabled(false);
//              driverNameText.setEnabled(false);
//              phoneNumbertext.setEnabled(false);
//
      }catch (Exception e) {
          Toast.makeText(Secondary.this,"Failed to add to database"+e.getMessage(),Toast.LENGTH_SHORT).show();
      }
    }

    public void setEditable(View view) {
       // ((EditText)findViewById(R.id.TruckNumberText)).setEnabled(true);
//        phoneNumbertext.setEnabled(true);
//        driverNameText.setEnabled(true);
//        //code for color change of button
//        panIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));
//        rcIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));
//
    }

    public void updateRecord(View view) {

        TruckBean updatedTruck=new TruckBean();
        updatedTruck.setTruckNumber((truckNumber.getText().toString()));
        updatedTruck.setTruckOwnerName(ownerName.getText().toString());

        String[] ownerNumbers=new String[ownerNumberList.size()];
        Integer arrayLength=ownerNumbers.length;
        for (int i=0;i<arrayLength;i++) {
            ownerNumbers[i] = ownerNumberList.get(i);
        }
        updatedTruck.setTruckOwnerPhoneNumberS(ownerNumbers);


       String[] accountNumbers=new String[accountNumberList.size()];
        for (int i=0;i<accountNumberList.size();i++)
            accountNumbers[i]=accountNumberList.get(i);
        updatedTruck.setAccountNumbers(accountNumbers);

        int recordsUpdated=new DBActivities().updateTruck(updatedTruck,this);

//        phoneNumbertext.setEnabled(false);
//        driverNameText.setEnabled(false);

        //code for color change of button
//        File temp = new File(storageDir,truckNumbertext.getText().toString()+"_PAN.jpg");
//
//        if (temp.exists())
//            panIndicator.setBackgroundColor(getResources().getColor(R.color.Green,null));
//        else
//            panIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));
//
//        temp = new File(storageDir,truckNumbertext.getText().toString()+"_RC.jpg");
//
//        if (temp.exists())
//            rcIndicator.setBackgroundColor(getResources().getColor(R.color.Green,null));
//        else
//            rcIndicator.setBackgroundColor(getResources().getColor(R.color.Red,null));
//        ///////
        Toast.makeText(Secondary.this,recordsUpdated+" records updated",Toast.LENGTH_SHORT).show();



    }

    public void call(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +ownerNumberList.get(0).toString()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE},
                    1);
            return;
        }

        startActivity(callIntent);
    }


}
