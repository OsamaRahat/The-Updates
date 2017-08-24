package com.android.theupdates.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.theupdates.AppLoader;
import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.entites.SideBarItem;
import com.android.theupdates.entites.UpdatesGroup;
import com.android.theupdates.fragments.AdvertisementFragment;
import com.android.theupdates.fragments.BaseFragment;
import com.android.theupdates.fragments.GroupInfoFragment;
import com.android.theupdates.fragments.HomeFragment;
import com.android.theupdates.fragments.SettingFragment;
import com.android.theupdates.fragments.ShareUpdateFragment;
import com.android.theupdates.fragments.SignInFragment;
import com.android.theupdates.fragments.UserProfileFragment;
import com.android.theupdates.fragments.WeatherFragment;
import com.android.theupdates.helper.TheUpdatesPreferenceHelper;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.viewbinder.SideBarBinder;
import com.android.theupdates.webapi.LoadingListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.theupdates.constants.Constants.REQUEST_CODE_GALLERY;
import static com.android.theupdates.constants.Constants.REQUEST_CODE_VIDEOS;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoadingListener {


    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.lst_menu_items)
    ListView lstMenuItems;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.loaders)
    RelativeLayout loaders;
    @BindView(R.id.main_toolbar_title)
    public TextView mainToolbarTitle;
    @BindView(R.id.adzLine)
    public View adzLine;
    @BindView(R.id.adzBgLine)
    public View adzBgLine;

    ArrayListAdapter<SideBarItem> listAdapter;
    ActionBarDrawerToggle toggle;

    public CallbackManager callbackManager;

    public TheUpdatesPreferenceHelper preferenceHelper;
    @BindView(R.id.imgTopLogo)
    public ImageView imgTopLogo;
    View headerView;
    View footerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //makeKeyHash();
        preferenceHelper = new TheUpdatesPreferenceHelper(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        loaders.setOnClickListener(this);
        adzLine.setOnClickListener(this);

        if (preferenceHelper.getUser() != null)
            setNavigationList();

        setInitialFragment();

        if (AppLoader.isAdzAvailable()) {
            adzBgLine.setVisibility(View.VISIBLE);
        } else
            adzBgLine.setVisibility(View.GONE);



    }


    private void setInitialFragment() {
        if (preferenceHelper.getUser() == null) {
            addFragmentOnce(SignInFragment.getInstance().getClass().getName(), SignInFragment.getInstance());
        } else {
            addFragmentOnce(HomeFragment.getInstance().getClass().getName(), HomeFragment.getInstance());
        }

    }

    private void makeKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void setNavigationList() {
        ArrayList<SideBarItem> lstSideBar = new ArrayList<>();
        String[] arrMenu = this.getResources().getStringArray(R.array.arrSideMenu);
        String[] arrStatusUpdate = this.getResources().getStringArray(R.array.arrStatusUpdate);
        int[] arrDrawableColors = {0, R.drawable.red_dots_siderbar, R.drawable.skyblue_dots_siderbar, R.drawable.dots_siderbar, R.drawable.brown_dots_siderbar, R.drawable.yellow_dots_siderbar, R.drawable.grey_dots_siderbar, R.drawable.purple_dots_siderbar, R.drawable.pink_dots_siderbar, R.drawable.orange_dots_siderbar, R.drawable.darkblue_dots_siderbar};
        int[] arrBgColors = {0, R.color.red, R.color.sky, R.color.green, R.color.brown, R.color.yellow, R.color.colorGrey, R.color.purple, R.color.pink, R.color.orange, R.color.blue};

        for (int i = 0; i < arrMenu.length; i++) {
            SideBarItem sideBarItem;
            if (i == 0) {
                sideBarItem = new SideBarItem(i, arrMenu[i], true, arrDrawableColors[i], arrBgColors[i], "");
            } else {
                sideBarItem = new SideBarItem(i, arrMenu[i], false, arrDrawableColors[i], arrBgColors[i], arrStatusUpdate[i]);
            }
            lstSideBar.add(sideBarItem);
        }

        listAdapter = new ArrayListAdapter<SideBarItem>(this, lstSideBar, new SideBarBinder());
        lstMenuItems.setAdapter(listAdapter);
        lstMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                navigationToFragment(position, false);


                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }


            }
        });

        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        ImageView imgUserPic = (ImageView) headerView.findViewById(R.id.imgUserPic);
        TextView txtUserLabel = (TextView) headerView.findViewById(R.id.txtUserLabel);

        if (preferenceHelper.getUser() != null) {
            ImageLoader.getInstance().displayImage(preferenceHelper.getUser().getUserPicture(), imgUserPic, getDefaultOptions());
            txtUserLabel.setText(preferenceHelper.getUser().getUsername());
        }


        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, null);
        ImageView imgSetting = (ImageView) footerView.findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(this);

        lstMenuItems.addHeaderView(headerView);
        lstMenuItems.addFooterView(footerView);
    }

    public void navigationToFragment(int position, boolean isCallFragment) {
        if (isCallFragment) {
            if (position == 6) {
                emptyBackStack();
                addFragment(WeatherFragment.getInstance(listAdapter.getItemFromList(position)).getClass().getName(), WeatherFragment.getInstance(listAdapter.getItemFromList(position)));

            } else if (position >= 1 && position <= 11) {
                emptyBackStack();
                addFragment(GroupInfoFragment.getInstance(listAdapter.getItemFromList(position)).getClass().getName(), GroupInfoFragment.getInstance(listAdapter.getItemFromList(position)));

            }
        } else if (!isCallFragment) {
            if (position == 0) {
                emptyBackStack();
                addFragment(UserProfileFragment.getInstance(preferenceHelper.getUser().getUserId()).getClass().getName(), UserProfileFragment.getInstance(preferenceHelper.getUser().getUserId()));

            } else if (position == 1) {
                emptyBackStack();
                addFragmentOnce(HomeFragment.getInstance().getClass().getName(), HomeFragment.getInstance());

            } else if (position == 7) {
                emptyBackStack();
                addFragment(WeatherFragment.getInstance(listAdapter.getItemFromList(position - 1)).getClass().getName(), WeatherFragment.getInstance(listAdapter.getItemFromList(position - 1)));

            } else if (position > 1 && position <= 11) {
                emptyBackStack();
                addFragment(GroupInfoFragment.getInstance(listAdapter.getItemFromList(position - 1)).getClass().getName(), GroupInfoFragment.getInstance(listAdapter.getItemFromList(position - 1)));

            }
        }
    }

    public void setSideMenu(boolean isSetNavigation) {
        drawer.closeDrawer(GravityCompat.START);
        if (isSetNavigation) {
            toggle.setDrawerIndicatorEnabled(isSetNavigation);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            toggle.setDrawerIndicatorEnabled(isSetNavigation);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void addFragment(String tag, BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment, tag).addToBackStack(null).commit();
    }

    public void addFragmentOnce(String tag, BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment, tag).commit();
    }

    public void addFragmentWithAnimation(String tag, BaseFragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.push_right_in,
                    R.anim.push_right_out,
                    R.anim.push_right_in,
                    R.anim.push_right_out);

            fragmentTransaction.replace(R.id.content_main, fragment,
                    tag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } catch (IllegalStateException e) {

        }
    }

    public void addFragmentWithFlipAnimation(String tag, BaseFragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.slideup_animation,
                    R.anim.slidedown_animation,
                    R.anim.slideup_animation,
                    R.anim.slidedown_animation);

            fragmentTransaction.replace(R.id.content_main, fragment,
                    tag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } catch (IllegalStateException e) {

        }
    }

    public void setEnableAdz() {
        adzBgLine.setVisibility(View.VISIBLE);
    }

    public void setDisableAdz() {
        adzBgLine.setVisibility(View.GONE);
    }

    private static MainActivity mInst;

    public static MainActivity instance() {
        return mInst;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLoader.activityResumed();
        mInst = this;

//        if (adzBgLine.getVisibility() == View.VISIBLE)
//            UIHelper.animateFadeInOut(adzBgLine);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLoader.activityPaused();
        mInst = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLoader.activityPaused();
        mInst = null;
    }

    @Override
    public void onBackPressed() {
        if (loaders.getVisibility() == View.VISIBLE) {
            UIHelper.showLongToastInCenter(this, "Please wait..");
            return;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void emptyBackStack() {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0)
            return;
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                0);

        if (entry != null) {

            getSupportFragmentManager().popBackStack(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSetting:
                emptyBackStack();
                addFragment(SettingFragment.getInstance().getClass().getName(), SettingFragment.getInstance());
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.loaders:
                UIHelper.showLongToastInCenter(getApplicationContext(), "Please wait..");
                break;

            case R.id.adzLine:
                if (adzBgLine.getVisibility() == View.VISIBLE) {
                    addFragmentWithAnimation(AdvertisementFragment.getInstance().getClass().getName(), AdvertisementFragment.getInstance());
                }
                break;

        }


    }

    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(100))
                .cacheOnDisk(true)
                .build();

        return defaultOptions;
    }


    @Override
    public void onLoadingStarted() {
        loaders.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFinished() {
        loaders.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY || requestCode == REQUEST_CODE_VIDEOS) {
            getSupportFragmentManager().findFragmentByTag(ShareUpdateFragment.getInstance(null).getClass().getName()).onActivityResult(requestCode, resultCode, data);
        }

    }

    public void editNavigationList(ArrayList<UpdatesGroup> arrayList) {
        if (arrayList.size() == 0)
            return;

        for (int i = 1, j = 0; i < listAdapter.getList().size(); i++, j++) {
            listAdapter.getList().get(i).setStrCaption(arrayList.get(j).getGroupName());
            listAdapter.getList().get(i).setPPR(arrayList.get(j).getPPR());
            listAdapter.getList().get(i).setGroupStatus(arrayList.get(j).getGroupStatus());

        }
        //TODO MUST CHECK IF CHANGES APPLY..
        listAdapter.notifyDataSetChanged();
    }

    public void emtpyNavigationList() {
        lstMenuItems.removeHeaderView(headerView);
        lstMenuItems.removeFooterView(footerView);

    }


}
