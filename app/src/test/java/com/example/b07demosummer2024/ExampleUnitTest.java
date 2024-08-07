package com.example.b07demosummer2024;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doAnswer;

import android.content.Context;

import com.example.b07demosummer2024.interfaces.LoginMVP;
import com.example.b07demosummer2024.utilities.Preferences;
import com.example.b07demosummer2024.utilities.Presenter;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

//    @Mock
//    private LoginMVP.View view;
//
//    @Mock
//    private LoginMVP.Model model;

//    @Mock
//    private Context context;

    @Mock
    private Presenter presenter;

//    @Captor
//    private ArgumentCaptor<LoginMVP.Model.LoginCallback> ModelArgumentCaptor;
//
//    @Mock
//    private Preferences preferences;

    @Test
    public void test() {
        assertEquals(1, 1);
    }

//    @Test
//    public void testHandleLoginSuccess() {
//
//        Presenter presenter = new Presenter(model, context);
//
//        presenter.handleLogin("Username", "Password");
//
//        verify(model).login("Username", "Password", ModelArgumentCaptor.capture());
//        LoginMVP.Model.LoginCallback callback = ModelArgumentCaptor.getValue();
//
//        callback.onSuccess("Message", "Password");
//        verify(preferences).saveLogin(context, true);
//        verify(preferences).saveUser(context, "Username");
//        verify(view).onLoginSuccess("Message");
//
//        callback.onError("Message");
//        verify(preferences).saveLogin(context, false);
//        verify(view).onLoginError("Message");
//
//    }



//
//    @Test
//    public void test() {
//
//        Presenter presenter = new Presenter(model, context);
//        presenter.attachView(view);
//        presenter.handleLogin("Username", "Password");
//
//    }

//    @Test
//    public void test() {
//
//        Presenter presenter = new Presenter(model, context);
//        presenter.attachView(view);
//        presenter.detachView();
//
//    }

}