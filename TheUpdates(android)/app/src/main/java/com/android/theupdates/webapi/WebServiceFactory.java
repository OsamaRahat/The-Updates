package com.android.theupdates.webapi;


import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.theupdates.constants.URL.BASE_URL_PRODUCTION;

public class WebServiceFactory {

	public static WebService service;
    public static WebService serviceOneSignal;

	public static WebService getInstance() {
		if (service == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient name = new OkHttpClient.Builder().connectionPool(new ConnectionPool(5, 15 * 1000, TimeUnit.MILLISECONDS)).addInterceptor(logging).build();

            Retrofit retrofitAdapter = new Retrofit.Builder()
                    .baseUrl(BASE_URL_PRODUCTION)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(name)
                    .build();

			service = retrofitAdapter.create(WebService.class);
		}

		return  service;

	}


	

}
