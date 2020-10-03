package com.sab.recipesapp.util;

import androidx.lifecycle.LiveData;

import com.sab.recipesapp.requests.repsonses.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        //Check #1: Does CallAdapter return LiveData type?
        if(CallAdapter.Factory.getRawType(returnType) != LiveData.class){
            return null;
        }

        //Check #2: Is LiveData wrapping ApiResponse type?
        Type observableType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);

        Type rawObservableType = CallAdapter.Factory.getRawType(observableType);
        if(rawObservableType != ApiResponse.class){
           throw new IllegalArgumentException("Type must be a defined resource");
        }

        //Check #3: Is ApiResponse parameterized?
        if(!(observableType instanceof ParameterizedType)){
            throw new IllegalArgumentException("Resource must me parameterized");
        }

        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataCallAdapter<Type>(bodyType);
    }
}
