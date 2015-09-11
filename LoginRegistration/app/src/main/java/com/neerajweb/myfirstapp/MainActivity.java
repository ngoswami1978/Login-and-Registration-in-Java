package com.neerajweb.myfirstapp;


import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
//import android.support.v7.app.AppCompatActivity;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.neerajweb.myfirstapp.adapter.SpinnerFlatsAdapter;
import com.neerajweb.myfirstapp.R;
import com.neerajweb.myfirstapp.dao.FlatDAO;
import com.neerajweb.myfirstapp.dao.OwnerDAO;
import com.neerajweb.myfirstapp.dao.my_SqliteDatabaseHelper;
import com.neerajweb.myfirstapp.model.owner_model;
import com.neerajweb.myfirstapp.model.flat_model;

public class MainActivity extends Activity implements OnItemSelectedListener  {

    public static final String TAG = "AddOwnerActivityInMainActivity";

    private Button bSignUp;
    private  EditText etname,etAge,etUsername,etpwd,etEmail;
    private TextView tvLoginLink;
    private ImageView imageview_profile;
    public static final int PickImageId = 1000;
    private BitmapDrawable bitmap;

    private Spinner mSpinnerFlats;
    private long lngFlatId;

    private OwnerDAO mOwnerDao;
    private FlatDAO mFlatDao;

    private flat_model mSelectedFlats;
    private SpinnerFlatsAdapter mAdapter;
    private int images[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        try
        {
            registerViews();

            // Create our sqlite database object
            this.mOwnerDao = new OwnerDAO(this);
            this.mFlatDao= new FlatDAO(this);

            //fill the spinner with All Flats
            List<flat_model> listflats = mFlatDao.getAllFlats();
            if (listflats.size()!=0)
            {
                if(listflats != null)
                {
                    mAdapter = new SpinnerFlatsAdapter(this, listflats);
                    mSpinnerFlats.setAdapter(mAdapter);
                    mSpinnerFlats.setOnItemSelectedListener(this);
                }
            }
            else {
                // Fill all flats from resource file Flats_array
                fillflats();

                //fill the spinner with All Flats
                List<flat_model> listflats1 = mFlatDao.getAllFlats();
                mAdapter = new SpinnerFlatsAdapter(this, listflats1);
                mSpinnerFlats.setAdapter(mAdapter);
                mSpinnerFlats.setOnItemSelectedListener(this);
            }
        }catch (Exception e)
        {
            Log.d(TAG, "Error in Ocreate method : " + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void fillflats() {
        Resources res = getResources();
        String[] flats = res.getStringArray(R.array.Flats_array);

        for(int i=0;i<flats.length;i++) {
            String arr[]=flats[i].split(",");
            flat_model createdFlat = mFlatDao.createFlats(arr[0].trim(),arr[1].trim());
            }
    }

    private void registerViews() {
        images = new int[]  {R.drawable.apartment1, R.drawable.apartment2, R.drawable.apartment3, R.drawable.apartment4};

        //Resources res = getApplicationContext().getResources();
        //TransitionDrawable transition = (TransitionDrawable) res.getDrawable(R.drawable.expand_collapse);
        //this.imageview_profile= (ImageView) findViewById(R.id.imageview_profile);
        //imageview_profile.setImageDrawable(transition);
        //transition.startTransition(5000);

        this.imageview_profile= (ImageView) findViewById(R.id.imageview_profile);
        animate(imageview_profile, images, 0,true);

        this.etname =  (EditText)findViewById (R.id.etname);
        // TextWatcher would let us check validation error on the fly
        this.etname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etname);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        this.mSpinnerFlats = (Spinner) findViewById(R.id.flatspinner);
        this.etAge = (EditText)findViewById(R.id.etAge);
        this.etUsername  = (EditText)findViewById(R.id.etUsername);
        this.etUsername.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etUsername);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        this.etpwd  = (EditText)findViewById(R.id.etpwd);
        this.etpwd.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etpwd);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        this.etEmail  = (EditText)findViewById(R.id.etEmail);
        this.etEmail.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(etEmail, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        this.bSignUp= (Button)findViewById(R.id.bSignUp);
        this.bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Validation class will check the error and display the error on respective fields
                but it won't resist the form submission, so we need to check again before submit
                 */
                if (checkValidation())
                    submitForm();
                else
                    Toast.makeText(MainActivity.this, "Form contains error", Toast.LENGTH_LONG).show();
            }
        });
        this.tvLoginLink = (TextView)findViewById(R.id.tvLoginLink);
        tvLoginLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {
        //imageView <-- The View which displays the images
        //images[] <-- Holds R references to the images to display
        //imageIndex <-- index of the first image to show in images[]
        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

        int fadeInDuration = 5000; // Configure time values here
        int timeBetween = 3000;
        int fadeOutDuration = 5000;

        imageView.setVisibility(View.VISIBLE);    //Visible or invisible by default - this will apply when the animation ends
        imageView.setImageResource(images[imageIndex]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (images.length - 1 > imageIndex) {
                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
                }
                else {
                    if (forever == true){
                        animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
                    }
                }
            }
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void submitForm() {
        // Submit your form here. your form is valid
        Toast.makeText(this, "Submitting form...", Toast.LENGTH_LONG).show();

        try
        {
            Editable e_name = etname.getText();
            Editable e_age = etAge.getText();
            Editable e_username = etUsername.getText();
            Editable e_password = etpwd.getText();
            Editable e_email = etEmail.getText();

            //mSelectedCompany = (Company) mSpinnerCompany.getSelectedItem();
            int int_age = Integer.valueOf(e_age.toString());
            int int_Approvalstatus=0; // when created a new User Approval status should set to '0'.

            owner_model createdOwner = mOwnerDao.createOwner(e_name.toString() , lngFlatId , e_username.toString(),e_password.toString(),e_email.toString() ,int_age,int_Approvalstatus);
            Toast.makeText(this, R.string.owner_registered_successfully, Toast.LENGTH_LONG).show();

            Log.d(TAG, "added new owner : " + createdOwner.getName() + " " + createdOwner.getFlatno() + ", OwnerId : " + createdOwner.getId());

            //setResult(RESULT_OK);
            //finish();

            UpdateApprovalStatus(createdOwner);

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }catch (Exception e)
        {
            Log.d(TAG, "Error while inserting new owner record : " + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateApprovalStatus(owner_model SaveNotifyOwner)  {
        try
        {
            int intRecordUpdate;

            // Update Mail Table with 0 as Approval Status
            SaveNotifyOwner.setStatus(0);
            intRecordUpdate = mOwnerDao.updateApprovalStatus(SaveNotifyOwner);
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while sending notification : " + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(etname)) ret = false;
        if (!Validation.hasText(etEmail)) ret = false;
        if (!Validation.hasText(etpwd )) ret = false;
        if (!Validation.hasText(etUsername)) ret = false;
        if (!Validation.isEmailAddress(etEmail,true)) ret=false;
        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOwnerDao.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedFlats = mAdapter.getItem(position);
        lngFlatId = mSelectedFlats.getId();
        Log.d(TAG, "selectedFlat : " + mSelectedFlats.getName());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //An inner class which is an implementation of the TImerTask interface to be used by the Timer.
    class MyTimer extends TimerTask {

        public void run() {

            //This runs in a background thread.
            //We cannot call the UI from this thread, so we must call the main UI thread and pass a runnable
            runOnUiThread(new Runnable() {

                public void run() {
                    //Random rand = new Random();

                    //The random generator creates values between [0,256) for use as RGB values used below to create a random color
                    //We call the RelativeLayout object and we change the color.  The first parameter in argb() is the alpha.
                    imageview_profile.setImageResource (images[getRandomNumber()]);
                }
            });
        }

        private int getRandomNumber() {
            //Note that general syntax is Random().nextInt(n)
            //It results in range 0-4
            //So it should be equal to number of images in images[] array
            return new Random().nextInt(4);
        }
    }
}
