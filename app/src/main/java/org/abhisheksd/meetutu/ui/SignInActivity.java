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
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Button;

import org.abhisheksd.meetutu.R;

import butterknife.Bind;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity {

    @Bind(R.id.sign_in)
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        setTitleDisabled();

        Snackbar.make(signIn, "I'm sorry you have to go through this repeatedly. Also yeah, bugs. College guys here. Time!!!!!", Snackbar.LENGTH_INDEFINITE).show();
    }

    @OnClick(R.id.sign_in)
    public void onSignIn() {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
