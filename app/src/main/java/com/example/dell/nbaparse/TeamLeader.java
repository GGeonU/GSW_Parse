package com.example.dell.nbaparse;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.dell.nbaparse.Adapter.FragmentViewAdapter;
import com.example.dell.nbaparse.Fragment.LeaderContent;

public class TeamLeader extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        setToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentViewAdapter adapter = new FragmentViewAdapter(getSupportFragmentManager());
        adapter.addFragment(new LeaderContent().newInstance("PTS"), "Points");
        adapter.addFragment(new LeaderContent().newInstance("AST"), "Assist");
        adapter.addFragment(new LeaderContent().newInstance("REB"), "Rebound");
        adapter.addFragment(new LeaderContent().newInstance("FT_PCT"), "FreeThrow%");
        adapter.addFragment(new LeaderContent().newInstance("FG_PCT"), "FieldGoal%");
        adapter.addFragment(new LeaderContent().newInstance("FG3_PCT"), "3Point%");
        adapter.addFragment(new LeaderContent().newInstance("STL"), "Steal");
        adapter.addFragment(new LeaderContent().newInstance("BLK"), "Block");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(7);   // load page

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team Leader");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
