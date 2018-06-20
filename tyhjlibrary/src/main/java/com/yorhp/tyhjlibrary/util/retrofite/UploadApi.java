package com.yorhp.tyhjlibrary.util.retrofite;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Tyhj on 2017/10/26.
 */

public interface UploadApi {
    @Multipart
    @POST("uploadAgreement")
    Observable<String> update(@Header("token") String token,@Query("serialNumber") String serialNumber,
                              @Query("coachId") String coachId,
                              @Query("orderNo") String orderNo,
                              @Part MultipartBody.Part pictureFile);

}