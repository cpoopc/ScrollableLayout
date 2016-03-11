package com.test.cp.myscrolllayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.cp.myscrolllayout.fragment.PagerHeaderFragment;
import com.test.cp.myscrolllayout.fragment.ParallaxImageHeaderFragment;
import com.test.cp.myscrolllayout.fragment.SimpleDemoFragment;


public class MainActivity extends AppCompatActivity {

    private View scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
    }

    public void showParallaxFragment(View view) {
        showFragment(ParallaxImageHeaderFragment.class);
    }

    public void showPagerHeaderFragment(View view) {
        showFragment(PagerHeaderFragment.class);
    }

    public void showSimpleDemoFragment(View view) {
        showFragment(SimpleDemoFragment.class);
    }

    public <T extends Fragment> void showFragment(Class<T> clzz) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        try {
            if (fragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, clzz.newInstance(), clzz.getSimpleName()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, clzz.newInstance(), clzz.getSimpleName()).commit();
            }
            getSupportActionBar().setTitle(clzz.getSimpleName());
            scrollView.setVisibility(View.GONE);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!removeFragment()) {
            super.onBackPressed();
        } else {
            scrollView.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    public boolean removeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            return true;
        } else {
            return false;
        }
    }
}
