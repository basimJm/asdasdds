package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.schoofi.activities.LoginScreen;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;


public class NotificationSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_splash_screen);

        Thread splash = new Thread(){
            @Override
            public void run() {

                super.run();


                try {
                    //final ProgressDialog loading = ProgressDialog.show(SplashScreen.this, "Loading your data please wait!!", "",false,false);
                    Preferences.getInstance().loadPreference(NotificationSplashScreen.this);


                   Thread.sleep(3000);

                    if(Preferences.getInstance().isLoggedIn)
                    {
                        if(Preferences.getInstance().userRoleId.matches("7")) {
                            Intent intent = new Intent(NotificationSplashScreen.this, ChairmanHomeScreen.class);
                            startActivity(intent);
                            finish();
                        }

                        else if(Preferences.getInstance().userRoleId.matches("5"))
                        {
                            Intent intent = new Intent(NotificationSplashScreen.this, StudentHomeScreen.class);
                            startActivity(intent);
                            finish();
                        }


                        else if(Preferences.getInstance().userRoleId.matches("6"))
                        {
                            Intent intent = new Intent(NotificationSplashScreen.this, ParentHomeScreen.class);
                            startActivity(intent);
                            finish();
                        }

                        else if(Preferences.getInstance().userRoleId.matches("4"))
                        {
                            Intent intent = new Intent(NotificationSplashScreen.this, TeacherInnerHomeScreen.class);
                            startActivity(intent);
                            finish();
                        }

                        else if(Preferences.getInstance().userRoleId.matches("8"))
                        {
                            Intent intent = new Intent(NotificationSplashScreen.this, ChairmanDashboard.class);
                            startActivity(intent);
                            finish();
                        }

                        else
                            if(Preferences.getInstance().userRoleId.matches("0"))
                            {
                                Intent intent = new Intent(NotificationSplashScreen.this, AuditUserHomeScreen.class);
                                startActivity(intent);
                                finish();
                            }
                    }

                    else
                    {
                        Intent intent = new Intent(NotificationSplashScreen.this,LoginScreen.class);
                        startActivity(intent);
                        finish();
                    }








                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }
        };
        splash.start();
    }
}
