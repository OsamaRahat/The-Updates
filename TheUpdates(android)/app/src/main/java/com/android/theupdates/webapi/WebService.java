package com.android.theupdates.webapi;

import android.content.res.TypedArray;

import com.android.theupdates.entites.ADzData;
import com.android.theupdates.entites.CommentPost;
import com.android.theupdates.entites.ContentText;
import com.android.theupdates.entites.FollowUser;
import com.android.theupdates.entites.LikeCommentCounter;
import com.android.theupdates.entites.LikeUser;
import com.android.theupdates.entites.PostItem;
import com.android.theupdates.entites.UpdatesGroup;
import com.android.theupdates.entites.UserInfo;
import com.android.theupdates.entites.UserProfile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface WebService {

    @FormUrlEncoded
    @POST("fblogin")
    public Call<WebResponse<UserInfo>> userSignUp(
            @Field("fbid") String fbid,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("getcontent")
    public Call<WebResponseList<ContentText>> getUpdatesContent(
            @Header("USERID") String userId,
            @Field("slug") String slug,
            @Field("isgroup") int isgroup
    );


    @GET("getgroups")
    public Call<WebResponseList<UpdatesGroup>> getUpdatesGroup(
            @Header("USERID") String userId
    );

    @GET("getadd")
    public Call<WebResponse<ADzData>> getAdz(
            @Header("USERID") String userId
    );



    @FormUrlEncoded
    @POST("feedback")
    public Call<WebResponse<String>> sendFeedBack(
            @Header("USERID") String userId,
            @Field("name") String name,
            @Field("email") String email,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("getposts")
    public Call<WebResponseList<PostItem>> getPosts(
            @Header("USERID") String userId,
            @Field("group") String groupId,
            @Field("offset") int offset

    );

    @FormUrlEncoded
    @POST("getposts")
    public Call<WebResponseList<PostItem>> getPosts(
            @Header("USERID") String userId,
            @Field("group") String groupId,
            @Field("offset") int offset,
            @Field("postedby") String postedby

    );

    @FormUrlEncoded
    @POST("searchpost")
    public Call<WebResponseList<PostItem>> searchPost(
            @Header("USERID") String userId,
            @Field("group") String groupId,
            @Field("offset") int offset,
            @Field("key") String key

    );

    @FormUrlEncoded
    @POST("likepost")
    public Call<WebResponse<LikeCommentCounter>> setLikePost(
            @Header("USERID") String userId,
            @Field("postid") String postid,
            @Field("liked") int liked

    );

    @FormUrlEncoded
    @POST("reportpost")
    public Call<WebResponse<String>> reportPost(
            @Header("USERID") String userId,
            @Field("postid") String postid,
            @Field("touserid") String touserid

    );

    @FormUrlEncoded
    @POST("reportpost")
    public Call<WebResponse<String>> reportUser(
            @Header("USERID") String userId,
            @Field("postid") String postid,
            @Field("touserid") String touserid,
            @Field("reason") String reason

    );

    @FormUrlEncoded
    @POST("commentpost")
    public Call<WebResponse<LikeCommentCounter>> setCommentPost(
            @Header("USERID") String userId,
            @Field("postid") String postid,
            @Field("comment") String userComment

    );


    @FormUrlEncoded
    @POST("likeoncomment")
    public Call<WebResponse<LikeCommentCounter>> setLikeOnComments(
            @Header("USERID") String userId,
            @Field("postid") String postid,
            @Field("commentid") String commentid,
            @Field("liked") int liked

    );

    @Multipart
    @POST("createpost")
    public Call<WebResponse<LikeCommentCounter>> createpost(
            @Header("USERID") String userId,
            @Part("group") String groupId,
            @Part("posttext") String strPostTxt,
            @Part("postlink") String postLink,
            @Part("postvideolink") String postvideolink,
            @Part("files") TypedArray files

    );

    @FormUrlEncoded
    @POST("getlikes")
    public Call<WebResponseList<LikeUser>> getLikes(
            @Header("USERID") String userId,
            @Field("postid") String postid,
            @Field("offset") int offset

    );

    @FormUrlEncoded
    @POST("getcomments")
    public Call<WebResponseList<CommentPost>> getComments(
            @Header("USERID") String userId,
            @Field("postid") String postid,
            @Field("offset") int offset

    );

    //followuser
    @FormUrlEncoded
    @POST("followuser")
    public Call<WebResponse<FollowUser>> followUser(
            @Header("USERID") String userId,
            @Field("touserid") String touserid,
            @Field("isfollow") int isfollow

    );

    @FormUrlEncoded
    @POST("profile")
    public Call<WebResponse<UserProfile>> getProfile(
            @Header("USERID") String userId,
            @Field("touserid") String touserid,
            @Field("offset") int offset

    );

    @FormUrlEncoded
    @POST("getfollowings")
    public Call<WebResponseList<LikeUser>> getFollowingUser(
            @Header("USERID") String userId,
            @Field("offset") int offset

    );

    @FormUrlEncoded
    @POST("getfollowers")
    public Call<WebResponseList<LikeUser>> getFollowerUser(
            @Header("USERID") String userId,
            @Field("offset") int offset

    );

    @Multipart
    @POST("createpost")
    public Call<WebResponseList<PostItem>> createPost(
            @Header("USERID") String userId,
            @Part("group") RequestBody group,
            @Part("posttext") RequestBody posttext,
            @Part("postlink") RequestBody postlink,
            @Part MultipartBody.Part video,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3,
            @Part MultipartBody.Part image4,
            @Part MultipartBody.Part image5
            );

    @FormUrlEncoded
    @POST("pushreg")
    public Call<WebResponse<String>> pushRegistration(
            @Header("USERID") String userId,
            @Field("deviceid") String deviceid,
            @Field("devicetoken") String devicetoken,
            @Field("platform") String platform

    );

    //


//    @GET("/sign_in")
//    public void loginUser(
//
//            @Query("email") String email,
//            @Query("password") String password,
//            CustomWebResponse<UserInfo> response);


//    @GET("/taxonomies")
//    public void getTaxonomies(
//            @Header("X-Spree-Locale") String header,
//            CustomWebResponse<TaxonomiesObj> response);
//
//
//    @GET("/taxonomies/{catId}/taxons.json")
//    public void getTaxonsOfTaxonomies(
//            @Header("X-Spree-Locale") String header,
//            @Path("catId") int catId,
//            CustomWebResponse<ArrayList<Taxons>> response);
//
//    //X-Spree-token: 29ad537a22e6a0b1a1c2d6684bba62a12544c463bcfda68a
//
//    @GET("/products.json")
//    public void getProductsOfTaxons(
//            @Header("X-Spree-Locale") String header,
//            @Header("X-Spree-token") String token,
//            @Query("page") int page,
//            @Query("per_page") int per_page,
//            CustomWebResponse<ProductObj> response);
//
//
//    @Multipart
//    @POST("/orders.json/")
//    public void getOrderToken(
//            @Part("token") TypedString token,
//            CustomWebResponse<OrderCreation> response);
//
//
//    @POST("/orders.json/")
//    public void getGuestOrderToken(
//            CustomWebResponse<OrderCreation> response);
//
//
//    @Multipart
//    @POST("/line_items.json/")
//    public void addToCartItems(
//            @Part("line_item[variant_id]") int variant_id,
//            @Part("line_item[quantity]") int quantity,
//            @Part("order_token") TypedString token,
//            CustomWebResponse<AddToCartItems> response);
//
//    @Multipart
//    @PUT("/")
//    public void updateItem(
//
//            @Part("line_item[variant_id]") int variant_id,
//            @Part("line_item[quantity]") int quantity,
//            @Part("order_token") TypedString token,
//            CustomWebResponse<AddToCartItems> response);
//
//
//
//
//    @DELETE("/")
//    public void deleteItem(
//            @Query("line_item[variant_id]") int variant_id,
//            @Query("line_item[quantity]") int quantity,
//            @Query("order_token") String token,
//            CustomWebResponse<Object> response);
//
//    @Multipart
//    @PUT("/orders/{order_number}/empty")
//    public void deleteAllItems(
//            @Path("order_number") String order_number,
//            @Part("order_token") TypedString token,
//            CustomWebResponse<Object> response);
//
//
//    @GET("/orders/{number}.json")
//    public void getOrders(
//            @Header("X-Spree-Locale") String header,
//            @Path("number") String order,
//            @Query("order_token") String order_token,
//
//            CustomWebResponse<ShoppingCartObj> response);
//
//    //checkouts/<orderNumber>/next.json
//    @Multipart
//    @PUT("/checkouts/{ordernumber}/next.json")
//    public void checkoutOrder(
//            @Header("X-Spree-Locale") String header,
//            @Path("ordernumber") String ordernumber,
//            @Part("order_token") TypedString token,
//            CustomWebResponse<ShoppingCartObj> response);
//
//    /**
//     * use as a guest user and also user enter their credentials at runtime.
//     * @param number
//     * @param order_token
//     * @param response
//     */
//
//    @Multipart
//    @PUT("/orders/{number}.json")
//    public void updateOrdersAsGuestUser(
//            @Path("number") String number,
//            @Part("order_token") TypedString order_token,
//            @Part("order[email]") TypedString email,
//            CustomWebResponse<ShoppingCartObj> response);
//
//    /**
//     * use for shipping details
//     * Send the parameters as a body
//     * @param ordernumber
//     * @param token
//     * @param response
//     */
//
//    @Headers("Content-Type:application/json")
//    @PUT("/checkouts/{ordernumber}.json")
//    public void shippingDetails(
//            @Path("ordernumber") String ordernumber,
//            @Body OrderObj order,
//            @Query("order_token") String token,
//            CustomWebResponse<ShoppingCartObj> response);
//
//    @Headers("Content-Type:application/json")
//    @PUT("/checkouts/{ordernumber}.json")
//    public void paymentSelection(
//            @Path("ordernumber") String ordernumber,
//            @Body PaymentTypeObj order,
//            @Query("order_token") String token,
//            CustomWebResponse<ShoppingCartObj> response);
//
//
//    @Headers("Content-Type:application/json")
//    @PUT("/checkouts/{ordernumber}.json")
//    public void orderPlacement(
//            @Path("ordernumber") String ordernumber,
//            @Query("order_token") String token,
//            CustomWebResponse<ShoppingCartObj> response);
//
//
//    @Headers("Content-Type:application/json")
//    @PUT("/checkouts/{ordernumber}.json")
//    public void orderPlacementKNET(
//            @Path("ordernumber") String ordernumber,
//            @Query("order_token") String token,
//            Callback<String> response);
//
//
//    @PUT("/addresses/{address_number}")
//    public void updateShipAddress(
//            @Path("address_number") String address_number,
//            @Query("address[firstname]") String firstname,
//            @Query("address[lastname]") String lastname,
//            @Query("address[address1]") String address1,
//            @Query("address[city]") String city,
//            @Query("address[zipcode]") String zipcode,
//            @Query("address[phone]") String phone,
//            @Query("address[state_name]") String state_name,
//            @Query("address[alternative_phone]") String alternative_phone,
//            @Query("address[block]") String block,
//            @Query("address[street]") String street,
//            @Query("address[building]") String building,
//            @Query("address[floor]") String floor,
//            @Query("address[suite]") String suite,
//            @Query("address[address_type]") String address_type,
//            @Query("address[paci]") String paci,
//            @Query("address[country_id]") String country_id,
//            @Query("order_id") String order_id,
//            @Query("order_token") String token,
//            CustomWebResponse<BillAddressItem> response);
//
//    @GET("/wishlist")
//    public void getWishLists(
//            @Header("X-Spree-Locale") String header,
//            @Query("token") String userToken,
//            CustomWebResponse<WishList> response);
//
//    @Multipart
//    @POST("/wishlist/product/{variant_id}")
//    public void addToWishLists(
//            @Path("variant_id") int variant_id,
//            @Part("token") TypedString userToken,
//            CustomWebResponse<WishList> response);
//
//    @DELETE("/")
//    public void removeWishListItem(
//            @Query("token") String token,
//            CustomWebResponse<Object> response);
//
//
//    @GET("/orders/")
//    public void getOrderHistory(
//            @Query("token") String userToken,
//            CustomWebResponse<OrderListObj> response);
//
//
//    @Multipart
//    @POST("/")
//    public void contactUs(
//            @Part("name") TypedString name,
//            @Part("email") TypedString email,
//            @Part("comments") TypedString comments,
//            CustomWebResponse<Object> response);
//
//    //user[email], user[password], user[first_name], user[last_name], user[mobile_no]
//    @PUT("/users/{userid}")
//    public void updateProfile(
//            @Header("X-Spree-Token") String header,
//            @Path("userid") int userid,
//            @Query("user[email]") String email,
//            @Query("user[password]") String password,
//            @Query("user[first_name]") String first_name,
//            @Query("user[last_name]") String last_name,
//            @Query("user[mobile_no]") String mobile_no,
//            CustomWebResponse<UserInfo> response);
//
//    @GET("/reset_password")
//    public void forgotPassword(
//            @Query("email") String email,
//            CustomWebResponse<Object> response);
//
//    @GET("/notifications")
//    public void getNotifications(
//            @Header("Authorization") String header,
//            @Query("app_id") String app_id,
//            @Query("limit") int limit,
//            @Query("offset") int offset,
//            CustomWebResponse<NotificationItem> response);
//
//    @GET("/taxons/products.json")
//    public void getCategoryHiddenProducts(
//            @Header("X-Spree-Locale") String header,
//            @Query("id") int id,
//            @Query("page") int page,
//            @Query("per_page") int per_page,
//            CustomWebResponse<ProductObj> response);
//
//    @GET("/taxons")
//    public void getCategoryHiddenProducts(
//            @Query("q[id_eq]") int id,
//            CustomWebResponse<ArrayList<Products>> response);
}
