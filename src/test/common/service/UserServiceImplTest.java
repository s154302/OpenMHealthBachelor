package common.service;

import common.User;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    User user;
    UserServiceImpl userService;

    @Before
    public void setUp() throws Exception {
        user = new User("name", "accessToken");
        userService = mock(UserServiceImpl.class);

    }

    @Test
    public void executeAddUserSuccess() throws IOException {
        // Given
        HttpClient httpClient = mock(HttpClient.class);
        HttpPost httpPost = mock(HttpPost.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(201);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(httpPost)).thenReturn(httpResponse);
        when(userService.getAccessToken(user.getUsername(), "password")).thenReturn("accessToken");

        // Test
        try {
            Class[] argTypes = {String.class, String.class, HttpClient.class, HttpPost.class};
            Object[] args = {user.getUsername(), "password", httpClient, httpPost};
            Method executeAddUser = UserServiceImpl.class.getDeclaredMethod("executeAddUser", argTypes);
            executeAddUser.setAccessible(true);
            assertEquals(executeAddUser.invoke(userService, args), HttpStatus.OK);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}