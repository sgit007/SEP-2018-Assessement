package uts.sep.westfieldparkmate.Activity.UserActivities;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class LoginActivityTest {

    @Test
    public void isValidEmail() {
        LoginActivity loginActivity = mock(LoginActivity.class);
        String email = "jacob@skms.com";
        boolean expected = false;
        boolean output;
        output = mock(LoginActivity.class).isValidEmail(email);
        assertEquals(expected, output);
    }
}