package com.easemob.livedemo.common.reponsitories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.easemob.livedemo.data.model.LiveRoom;
import com.easemob.livedemo.data.restapi.ApiService;
import com.easemob.livedemo.data.restapi.LiveManager;
import com.easemob.livedemo.data.restapi.model.ResponseModule;
import com.easemob.qiniu_sdk.OnCallBack;
import com.easemob.qiniu_sdk.PushStreamHelper;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

/**
 * 用于从app server获取数据
 */
public class AppServerRepository {
    private ApiService apiService;

    public AppServerRepository() {
        apiService = LiveManager.getInstance().getApiService();
    }

    public LiveData<Resource<LiveRoom>> createLiveRoom(LiveRoom module) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {

            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.createLiveRoom(module));
            }

        }.asLiveData();
    }

    public LiveData<Resource<List<LiveRoom>>> getLiveRoomList(int limit, String cursor) {
        return new NetworkOnlyResource<List<LiveRoom>, ResponseModule<List<LiveRoom>>>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<ResponseModule<List<LiveRoom>>>> callBack) {
                callBack.onSuccess(apiService.getLiveRoomList(limit, cursor));
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<LiveRoom>>> getLivingRoomLists(int limit, String cursor) {
        return new NetworkOnlyResource<List<LiveRoom>, ResponseModule<List<LiveRoom>>>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<ResponseModule<List<LiveRoom>>>> callBack) {
                callBack.onSuccess(apiService.getLivingRoomList(limit, cursor));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoom>> changeLiveStatus(String roomId, String username, String status) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.changeLiveStatus(roomId, username, status));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoom>> getLiveRoomDetails(String roomId) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.getLiveRoomDetail(roomId));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoom>> updateLiveRoom(String roomId, RequestBody body) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.updateLiveRoom(roomId, body));
            }
        }.asLiveData();
    }

    public LiveData<Resource<String>> getPublishUrl(String roomId) {
        return new NetworkOnlyResource<String, String>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<String>> callBack) {
                PushStreamHelper.getInstance().getPublishUrl(roomId, new OnCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Log.e("TAG", "get publish url = "+data);
                        callBack.onSuccess(new MutableLiveData<String>(data));
                    }

                    @Override
                    public void onFail(String message) {
                        callBack.onError(ErrorCode.UNKNOWN_ERROR, message);
                    }
                });
            }
        }.asLiveData();
    }

    public LiveData<Resource<String>> getPlayUrl(String roomId) {
        return new NetworkOnlyResource<String, String>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<String>> callBack) {
                PushStreamHelper.getInstance().getPlayUrl(roomId, new OnCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Log.e("TAG", "get play url = "+data);
                        callBack.onSuccess(new MutableLiveData<String>(data));
                    }

                    @Override
                    public void onFail(String message) {
                        callBack.onError(ErrorCode.UNKNOWN_ERROR, message);
                    }
                });
            }
        }.asLiveData();
    }
}
