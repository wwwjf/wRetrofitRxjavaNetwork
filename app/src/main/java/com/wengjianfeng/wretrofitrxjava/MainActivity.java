package com.wengjianfeng.wretrofitrxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wengjianfeng.wretrofitrxjava.http.BaseObserver;
import com.wengjianfeng.wretrofitrxjava.http.BaseRequestEntity;
import com.wengjianfeng.wretrofitrxjava.http.BaseResponseEntity;
import com.wengjianfeng.wretrofitrxjava.http.LoginRequestEntity;
import com.wengjianfeng.wretrofitrxjava.http.LoginResponseEntity;
import com.wengjianfeng.wretrofitrxjava.http.ObservableManager;
import com.wengjianfeng.wretrofitrxjava.http.RetrofitHandler;
import com.wengjianfeng.wretrofitrxjava.http.RxTransformerHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitHandler.getInstance()
                .getAPIService()
                .login(ObservableManager.getInstance().getLoginRequestEntity())
                .compose(RxTransformerHelper.<BaseResponseEntity<LoginResponseEntity>>observableIO2Main(this))
                .subscribe(new BaseObserver<LoginResponseEntity>() {
                    @Override
                    protected void onSuccess(BaseResponseEntity<LoginResponseEntity> tBaseEntity) {

                    }

                    @Override
                    protected void onFailure(String errorMessage) {

                    }
                });
    }
}
