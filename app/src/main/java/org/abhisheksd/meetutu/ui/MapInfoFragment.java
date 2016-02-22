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

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.abhisheksd.meetutu.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapInfoFragment extends Fragment {

    public static MapInfoFragment newInstance(String name, String job, String phone) {
        MapInfoFragment fragment = new MapInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putString("name", name);
        arguments.putString("job", job);
        arguments.putString("phone", phone);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.job) TextView job;
    @Bind(R.id.phone) TextView phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.card_info, container, false);

        ButterKnife.bind(this, root);

        Bundle data = getArguments();

        name.setText(data.getString("name"));
        job.setText(data.getString("job"));
        phone.setText(data.getString("phone"));

        return root;
    }

    @OnClick(R.id.chat)
    public void onChat() {
        PackageManager packageManager = getActivity().getPackageManager();

        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            Uri uri = Uri.parse("smsto:" + "");
            waIntent.setType("text/plain");
            String text = "I'm interested om your services";

            PackageInfo info= packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
