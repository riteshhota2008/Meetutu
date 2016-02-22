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

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.abhisheksd.meetutu.R;

import java.util.ArrayList;

public class MapFragment extends com.google.android.gms.maps.MapFragment implements
        GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private Marker mActiveMarker = null;
    private BitmapDescriptor ICON_ACTIVE;
    private BitmapDescriptor ICON_NORMAL;

    private GoogleMap mMap;
    private Rect mMapInsets = new Rect();

    ArrayList<Marker> markerList = new ArrayList<>();

    private int COLOR;

    private static final int REQUEST_CODE_LOCATION = 0;

    public interface Callbacks {
        void onMarkerSelect(int position);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onMarkerSelect(int position) {

        }
    };

    private Callbacks mCallbacks = sDummyCallbacks;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ICON_ACTIVE = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        ICON_NORMAL = BitmapDescriptorFactory.defaultMarker();

        COLOR = ContextCompat.getColor(getActivity(), R.color.accent_translucent);

        getMapAsync(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new ClassCastException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);

        setMapInsets(mMapInsets);

        return mapView;
    }

    public void setMapInsets(int left, int top, int right, int bottom) {
        mMapInsets.set(left, top, right, bottom);
        if (mMap != null) {
            mMap.setPadding(mMapInsets.left, mMapInsets.top, mMapInsets.right, mMapInsets.bottom);
        }
    }

    public void setMapInsets(Rect insets) {
        mMapInsets.set(insets.left, insets.top, insets.right, insets.bottom);
        if (mMap != null) {
            mMap.setPadding(mMapInsets.left, mMapInsets.top, mMapInsets.right, mMapInsets.bottom);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(this);
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(false);
        mapUiSettings.setMapToolbarEnabled(false);

        setupMap(true);
    }

    private void setupMap(boolean resetCamera) {
        LatLng ASD = new LatLng(13.051732, 80.211669);
        LatLng RH = new LatLng(13.049446, 80.209396);
        LatLng SP = new LatLng(13.052895, 80.213902);
        markerList.add(mMap.addMarker(new MarkerOptions().position(ASD).title("Abhishek Dabholkar")));
        markerList.add(mMap.addMarker(new MarkerOptions().position(RH).title("Ritesh Hota")));
        markerList.add(mMap.addMarker(new MarkerOptions().position(SP).title("Sruthik P")));
        selectActiveMarker(markerList.get(0));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ASD));

        Circle circle = mMap.addCircle(new CircleOptions().center(ASD).radius(1000).strokeWidth(5f).strokeColor(COLOR));
        circle.setVisible(true);
        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(ASD)
                        .zoom(getZoomLevel(circle)).tilt(0f).build());
        mMap.animateCamera(camera);
    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 0;
        if (circle != null){
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(getActivity().findViewById(R.id.container_map), "Give permission",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_LOCATION);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void deselectActiveMarker() {
        if (mActiveMarker != null) {
            mActiveMarker.setIcon(ICON_NORMAL);
            mActiveMarker = null;
        }
    }

    private void selectActiveMarker(Marker marker) {
        if (mActiveMarker == marker) {
            return;
        }
        if (marker != null) {
            mActiveMarker = marker;
            mActiveMarker.setIcon(ICON_ACTIVE);
        }
    }

    public void selectMarker(int position) {
       onMarkerClick(markerList.get(position));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final String title = marker.getTitle();

        if (title.equals("Abhishek Dabholkar")) {
            mCallbacks.onMarkerSelect(0);
        } else if (title.equals("Ritesh Hota")) {
            mCallbacks.onMarkerSelect(1);
        } else if (title.equals("Sruthik P")) {
            mCallbacks.onMarkerSelect(2);
        }

        deselectActiveMarker();

        selectActiveMarker(marker);

        marker.showInfoWindow();

        return true;
    }
}
