package com.wengjianfeng.wretrofitrxjava.http;

public class ObservableManager {

    private static class SingletonHolder {
        static final ObservableManager INSTANCE = new ObservableManager();
    }

    public static ObservableManager getInstance() {
        return ObservableManager.SingletonHolder.INSTANCE;
    }

    /**
     * login
     */
    public BaseRequestEntity<LoginRequestEntity> getLoginRequestEntity() {
        BaseRequestEntity<LoginRequestEntity> requestModel = new BaseRequestEntity<>();
        requestModel.setHeader(HeaderUtils.setHeaderModel());
        LoginRequestEntity loginRequestModel = new LoginRequestEntity();
        requestModel.setData(loginRequestModel);
        return requestModel;
    }

}