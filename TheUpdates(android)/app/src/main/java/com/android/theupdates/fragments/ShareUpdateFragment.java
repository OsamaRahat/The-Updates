package com.android.theupdates.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.callbacks.WebLinkCallBack;
import com.android.theupdates.callbacks.iGenericCallBack;
import com.android.theupdates.dialogfragments.AddWebLinkDialogFragemnt;
import com.android.theupdates.entites.PostItem;
import com.android.theupdates.entites.SideBarItem;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponseList;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.guiguegon.gallerymodule.GalleryActivity;
import es.guiguegon.gallerymodule.GalleryHelper;
import es.guiguegon.gallerymodule.model.GalleryMedia;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
import static com.android.theupdates.constants.Constants.REQUEST_CODE_GALLERY;
import static com.android.theupdates.constants.Constants.REQUEST_CODE_VIDEOS;
import static com.android.theupdates.constants.Constants.UPDATE_OBJ;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class ShareUpdateFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.imgGallery)
    ImageButton imgGallery;
    @BindView(R.id.imgWebLink)
    ImageButton imgWebLink;
    @BindView(R.id.txtPost)
    TextView txtPost;
    @BindView(R.id.txtShareHint)
    EditText txtShareHint;
    @BindView(R.id.linAttachment)
    LinearLayout linAttachment;
    @BindView(R.id.imgVideo)
    ImageButton imgVideo;
    private SideBarItem postItem;

    private int videoIndex = 0;


    private String webUrl = "";
    MultipartBody.Part body1 = null, body2 = null, body3 = null, body4 = null, body5 = null, body6 = null;
    HashMap<Integer, MultipartBody.Part> hmpVideos = new HashMap<>();

    AddWebLinkDialogFragemnt dialogFragemnt;

    iGenericCallBack iGenericCallBack;


    public static ShareUpdateFragment getInstance(SideBarItem postItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(UPDATE_OBJ, postItem);
        ShareUpdateFragment shareUpdateFragment = new ShareUpdateFragment();
        shareUpdateFragment.setArguments(bundle);
        return shareUpdateFragment;
    }

    public void setOnUpdateFragment(iGenericCallBack iGenericCallBack)
    {
        this.iGenericCallBack = iGenericCallBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postItem = (SideBarItem) getArguments().get(UPDATE_OBJ);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_content, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtPost.setBackgroundResource(postItem.getColorBackground());

        txtShareHint.setHint(postItem.getLabelStatusUpdates());

        imgGallery.setOnClickListener(this);
        imgWebLink.setOnClickListener(this);
        txtPost.setOnClickListener(this);
        imgVideo.setOnClickListener(this);


    }


    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText(postItem.getStrCaption());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().onBackPressed();
            }
        });

        toolbar.setBackgroundResource(postItem.getColorBackground());
        setStatusBarColor(postItem.getColorBackground());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgGallery:
                openGalleryMultiplSelection(6);
                break;
            case R.id.imgVideo:
                openVideoSelection();
                break;
            case R.id.imgWebLink:
                dialogFragemnt = AddWebLinkDialogFragemnt.newInstance();


                dialogFragemnt.setOnAddWebLinkDialogListener(new WebLinkCallBack() {
                    @Override
                    public void getWebLink(final String url) {
                        dialogFragemnt.dismiss();
                        UIHelper.hideSoftKeyboard(getMainActivityContext(), imgWebLink);
                        addViewIndividual(url);

                    }
                });
                dialogFragemnt.show(getMainActivityContext().getFragmentManager(), dialogFragemnt.getClass().getName());
                break;
            case R.id.txtPost:
                UIHelper.hideSoftKeyboard(getMainActivityContext(), txtPost);
                makePost();
                break;
        }
    }

    private void addViewIndividual(String url) {
        View v = LayoutInflater.from(getMainActivityContext()).inflate(R.layout.list_item_sharelinks, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 500);
        params.setMargins(0, 15, 0, 0);
        v.setLayoutParams(params);

        final ImageView imgUpdate = (ImageView) v.findViewById(R.id.imgUpdate);

        TextView txtUrl = (TextView) v.findViewById(R.id.txtUrl);

        ImageView imgClose = (ImageView) v.findViewById(R.id.imgClose);
        imgClose.setTag(v);

        String content = "";
        String property = "";
        String itemprop = "";

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Document doc = Jsoup.connect(url.trim()).get();
            Elements metaElems = doc.select("meta");
            for (Element metaElem : metaElems) {
                content = metaElem.attr("content");
                property = metaElem.attr("property");
                itemprop = metaElem.attr("itemprop");

                if (property.contains("image") || itemprop.contains("image")) {
                    //ONLY GOOGLE.
                    if (itemprop.contains("image")) {
                        content = metaElem.baseUri().substring(0, metaElem.baseUri().indexOf("?") - 1) + content;
                    }
                    String link = webUrl.equalsIgnoreCase("") ? "" + content : " " + content;
                    imgClose.setContentDescription(link);
                    webUrl = webUrl.equalsIgnoreCase("") ? "" + content : webUrl + " " + content;
                    ImageLoader.getInstance().displayImage(content, imgUpdate);

                    break;
                }


            }

            if (TextUtils.isEmpty(content) == false) {

                imgUpdate.setVisibility(View.VISIBLE);
            } else {
                txtUrl.setText(url);
                txtUrl.setVisibility(View.VISIBLE);
            }

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(view.getContentDescription()) == false && webUrl.contains(view.getContentDescription())) {
                        webUrl = webUrl.replace(view.getContentDescription(), "").trim();
                    }
                    linAttachment.removeView((View) view.getTag());
                    linAttachment.invalidate();

                }
            });

             linAttachment.addView(v);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;

        } catch (IOException e) {
            e.printStackTrace();
            UIHelper.showLongToastInCenter(getMainActivityContext(), "Url is not valid.");
            return;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            UIHelper.showLongToastInCenter(getMainActivityContext(), "Add http:// or https:// before your URL.");
            return;

        }

    }

    private void makePost() {

        filterImages();

        if (txtShareHint.getText().toString().length() <= 2) {
            UIHelper.showLongToastInCenter(getMainActivityContext(), "Enter atleast two characters for post..");
            return;
        }

        if (galleryMedias != null && galleryMedias.size() > 6) {
            UIHelper.showLongToastInCenter(getMainActivityContext(), "Only six images allow to proceed..");
            return;
        } else if (galleryMedias != null && hmpVideos.size() > 1) {
            UIHelper.showLongToastInCenter(getMainActivityContext(), "Only one video allow to proceed..");
            return;
        }

        //TEMPORARY SOLUTIONS...
        if (tmpLists != null)
            makeImagesAttachment();

    }

    private void createPostService() {
        getMainActivityContext().onLoadingStarted();

        MultipartBody.Part videoPart = hmpVideos.entrySet().iterator().hasNext() ? hmpVideos.entrySet().iterator().next().getValue() : null;

        Call<WebResponseList<PostItem>> response = getWebServiceInstance().createPost(getUserId(), createPartFromString(postItem.getId() + ""), createPartFromString(txtShareHint.getText().toString().trim()), createPartFromString(webUrl.trim()), videoPart, body1, body2, body3, body4, body5, body6);
        response.enqueue(new CustomWebResponse<WebResponseList<PostItem>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<PostItem>> call, Response<WebResponseList<PostItem>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponseList<PostItem> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    UIHelper.showLongToastInCenter(getMainActivityContext(), webResponse.getMessage());
                    iGenericCallBack.iBackResult(true);
                    getMainActivityContext().onBackPressed();


                }
            }


        });
    }

    private ArrayList<GalleryMedia> tmpLists = new ArrayList<>();;

    private void filterImages() {

        //tmpLists = new ArrayList<>();

        if (galleryMedias == null)
            return;

        //tmpLists.addAll(galleryMedias);

        if (filterGalleryMedias.size() == 0)
            return;

        for (int i = 0; i < galleryMedias.size(); i++) {
            for (int inner = 0; inner < filterGalleryMedias.size(); inner++) {

                if (filterGalleryMedias.get(inner) == galleryMedias.get(i)) {
                    tmpLists.remove(filterGalleryMedias.get(i));
                }
            }
        }
    }

    private void makeImagesAttachment() {
        for (int i = 0, loop = 0; i < tmpLists.size(); i++) {

            if (tmpLists.get(i).mimeType().contains("video/mp4")) {
                continue;
            }

            File file = null;
            try {
                file = new File(tmpLists.get(i).mediaUri());
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            if (loop == 0) {
                body1 = MultipartBody.Part.createFormData("files[]", file.getName(), reqFile);
            }
            if (loop == 1) {
                body2 = MultipartBody.Part.createFormData("files[]", file.getName(), reqFile);
            }
            if (loop == 2) {
                body3 = MultipartBody.Part.createFormData("files[]", file.getName(), reqFile);
            }
            if (loop == 3) {
                body4 = MultipartBody.Part.createFormData("files[]", file.getName(), reqFile);
            }
            if (loop == 4) {
                body5 = MultipartBody.Part.createFormData("files[]", file.getName(), reqFile);
            }
            if (loop == 5) {
                body6 = MultipartBody.Part.createFormData("files[]", file.getName(), reqFile);
            }
            loop++;

        }
        try {
            createPostService();
        } catch (OutOfMemoryError memoryError) {
            //UIHelper.showLongToastInCenter(getMainActivityContext(),memoryError.getMessage());
        }

    }

    private void openGalleryMultiplSelection(int maxSelectedItems) {
        getMainActivityContext().startActivityForResult(new GalleryHelper().setMultiselection(true)
                .setMaxSelectedItems(maxSelectedItems)
                .setShowVideos(false)
                .getCallingIntent(getMainActivityContext()), REQUEST_CODE_GALLERY);
    }

    private void openVideoSelection() {
        getMainActivityContext().startActivityForResult(new GalleryHelper().setMultiselection(false)
                .setShowVideos(true)
                .getCallingIntent(getMainActivityContext()), REQUEST_CODE_VIDEOS);
    }


    List<GalleryMedia> galleryMedias ;
    List<GalleryMedia> filterGalleryMedias = new ArrayList<GalleryMedia>();

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_GALLERY || requestCode == REQUEST_CODE_VIDEOS) && resultCode == RESULT_OK) {
            galleryMedias =
                    data.getParcelableArrayListExtra(GalleryActivity.RESULT_GALLERY_MEDIA_LIST);
            if ((requestCode == REQUEST_CODE_GALLERY || requestCode == REQUEST_CODE_VIDEOS) && !(galleryMedias.get(0).isVideo() )) {
                tmpLists.addAll(galleryMedias);
                attachImagesToContainer();
            } else if (requestCode == REQUEST_CODE_VIDEOS) {
                attachVideoToContainer(galleryMedias.get(0).mediaUri());
            }

        }


    }


    private void attachImagesToContainer() {
        for (int i = 0; i < galleryMedias.size(); i++) {
            View v = LayoutInflater.from(getMainActivityContext()).inflate(R.layout.list_item_shareitems, null, false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 500);
            params.setMargins(0, 15, 0, 0);
            v.setLayoutParams(params);

            ImageView imgUpdate = (ImageView) v.findViewById(R.id.imgUpdate);
            ImageView imgClose = (ImageView) v.findViewById(R.id.imgClose);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Bitmap bmp = BitmapFactory.decodeFile(galleryMedias.get(i).mediaUri(), options);

            imgUpdate.setImageBitmap(bmp);
            bmp = null;
            System.gc();

            v.setTag(i);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterGalleryMedias.add(galleryMedias.get((int) view.getTag()));
                    linAttachment.removeView(view);
                    linAttachment.invalidate();
                }
            });
            linAttachment.addView(v);
        }
    }

    private void attachVideoToContainer(String filePath) {
        for (int i = 0; i < 1; i++) {
            View v = LayoutInflater.from(getMainActivityContext()).inflate(R.layout.list_item_shareitems, null, false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 500);
            params.setMargins(0, 15, 0, 0);
            v.setLayoutParams(params);

            ImageView imgUpdate = (ImageView) v.findViewById(R.id.imgUpdate);
            ImageView imgClose = (ImageView) v.findViewById(R.id.imgClose);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;


            Bitmap bmp = ThumbnailUtils.createVideoThumbnail(filePath, MINI_KIND);

            imgUpdate.setImageBitmap(bmp);
            bmp = null;
            System.gc();

            v.setTag(i);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterGalleryMedias.add(galleryMedias.get((int) view.getTag()));
                    linAttachment.removeView(view);
                    linAttachment.invalidate();

                    hmpVideos.remove(view.getTag());
                    videoIndex--;
                }
            });


            linAttachment.addView(v);

            //PUT VIDEO PART INTO HASHMAP...
            File file = new File(filePath);
            RequestBody reqFile = RequestBody.create(MediaType.parse("video/*"), file);
            MultipartBody.Part partVideo = MultipartBody.Part.createFormData("video[]", file.getName(), reqFile);
            hmpVideos.put(videoIndex, partVideo);
            videoIndex++;
        }
    }

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }


}
