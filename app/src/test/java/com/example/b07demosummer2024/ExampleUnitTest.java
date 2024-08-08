package com.example.b07demosummer2024;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.b07demosummer2024.interfaces.LoginMVP;
import com.example.b07demosummer2024.utilities.PrefConstants;
import com.example.b07demosummer2024.utilities.Preferences;
import com.example.b07demosummer2024.utilities.Presenter;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    private LoginMVP.View view;

    @Mock
    private LoginMVP.Model model;

    @Mock
    private Context context;

    private Presenter presenter;

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mock SharedPreferences behavior
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);

        presenter = new Presenter(model, context);
        presenter.attachView(view);
    }

    @Test
    public void testAttachView() {
        presenter.attachView(view);
        assertEquals(presenter.getView(), view);
    }

    @Test
    public void testDetachView(){
        presenter.attachView(view);
        presenter.detachView();
        assertNull(presenter.getView());
    }


    @Test
    public void testHandleLoginSuccess() {
        // Arrange
        doAnswer(invocation -> {
            LoginMVP.Model.LoginCallback callback = invocation.getArgument(2);
            callback.onSuccess("Login successful!", "admin1");
            return null;
        }).when(model).login(anyString(), anyString(), any(LoginMVP.Model.LoginCallback.class));

        // Act
        presenter.handleLogin("admin1", "pass");

        // Assert
        verify(view).onLoginSuccess("Login successful!");

        InOrder inOrder = inOrder(editor);
        inOrder.verify(editor).putBoolean(PrefConstants.LOGIN_STATE, true);
        inOrder.verify(editor).putString(PrefConstants.USER, "admin1");
        inOrder.verify(editor).apply();
    }

    @Test
    public void testHandleLoginError() {
        // Arrange
        String errorMessage = "Login failed!";
        doAnswer(invocation -> {
            LoginMVP.Model.LoginCallback callback = invocation.getArgument(2);
            callback.onError(errorMessage);
            return null;
        }).when(model).login(anyString(), anyString(), any(LoginMVP.Model.LoginCallback.class));

        presenter.handleLogin("admin1", "wrongpass");

        verify(view).onLoginError(errorMessage);

        // Verify the login state is set to false
        InOrder inOrder = inOrder(editor);
        inOrder.verify(editor).putBoolean(PrefConstants.LOGIN_STATE, false);
        inOrder.verify(editor).apply();
    }

}