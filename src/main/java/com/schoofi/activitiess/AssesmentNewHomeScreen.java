package com.schoofi.activitiess;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.schoofi.fragments.FirstFragment;
import com.schoofi.fragments.FourthFragment;
import com.schoofi.fragments.SecondFragment;
import com.schoofi.fragments.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.library.BottomBarHolderActivity;
import me.riddhimanadib.library.BottomNavigationBar;
import me.riddhimanadib.library.NavigationPage;


public class AssesmentNewHomeScreen extends BottomBarHolderActivity implements FirstFragment.OnFragmentInteractionListener, SecondFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationPage page1 = new NavigationPage("Home", ContextCompat.getDrawable(this, R.drawable.ic_home_black_24dp), FirstFragment.newInstance());
        NavigationPage page2 = new NavigationPage("Support", ContextCompat.getDrawable(this, R.drawable.ic_mail_black_24dp), SecondFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Billing", ContextCompat.getDrawable(this, R.drawable.ic_assessment_black_24dp), ThirdFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp), FourthFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);

        super.setupBottomBarHolderActivity(navigationPages);


    }

    @Override
    public void onClickedOnBottomNavigationMenu(int menuType) {
        super.onClickedOnBottomNavigationMenu(menuType);

        switch (menuType) {
            case BottomNavigationBar.MENU_BAR_1:

                break;
        }

    }

    @Override
    public void onClicked() {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();


    }
}
