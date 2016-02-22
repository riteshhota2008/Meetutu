/*
 * Copyright 2016 Abhishek S. Dabholkar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.abhisheksd.meetutu.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import org.abhisheksd.meetutu.R;
import org.abhisheksd.meetutu.adapter.CardAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements MapFragment.Callbacks {

    @Bind(R.id.viewpager) ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mapFragment = MapFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.container_map, mapFragment).commit();
        }
    }

    @Override
    public void onMarkerSelect(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        CardAdapter adapter = new CardAdapter(getSupportFragmentManager());
        adapter.addFragment(MapInfoFragment.newInstance("Abhishek Dabholkar", "Android Developer", "+91 98306 92256"));
        adapter.addFragment(MapInfoFragment.newInstance("Ritesh Hota", "Web Developer", "+91 95516 31252"));
        adapter.addFragment(MapInfoFragment.newInstance("Sruthik P", "Backend Developer", "+91 98846 68114"));
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mapFragment != null) {
                    mapFragment.selectMarker(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
