package service;

import domain.error.DataBaseDownException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import domain.model.Account;
import domain.services.impl.ServicesLoginImpl;
import dao.DaoLogin;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceLoginTest {

    //system under test (SUT)
    @InjectMocks
    private ServicesLoginImpl servicesLogin;
    // dependencies
    @Mock
    private DaoLogin daoLogin;


    @Nested
    @DisplayName("Login")
    class loginTest{
        @Test
        @DisplayName("Login with correct credentials")
        void loginTest1(){
            Account account = new Account();
            account.setUsername("test");
            account.setPassword("test");
            when(daoLogin.doLogin("test", "test")).thenReturn(true);
            when(daoLogin.login("test", "test")).thenReturn(account);
            assertEquals(account, servicesLogin.login("test", "test"));
        }
        @Test
        @DisplayName("Login with incorrect credentials")
        void loginTest2(){
            when(daoLogin.doLogin("test", "test")).thenReturn(false);
            assertNull(servicesLogin.login("test", "test"));
        }


    }

    @Nested
    @DisplayName("Register")
    class registerTest {
        @Test
        @DisplayName("Register with correct credentials")
        void registerTest1(){
            Account account = new Account();
            account.setUsername("test");
            account.setPassword("test");
            LocalDateTime now = LocalDateTime.now();
            when(daoLogin.register(eq(account), eq("test"), eq(now))).thenReturn(true);
            assertTrue(servicesLogin.register(account, "test", now));
        }

        @Test
        @DisplayName("Register with incorrect credentials")
        void registerTest2(){
            Account account = new Account();
            account.setUsername("test");
            account.setPassword("test");
            LocalDateTime now = LocalDateTime.now();
            when(daoLogin.register(eq(account), eq("test"), eq(now))).thenReturn(false);
            assertFalse(servicesLogin.register(account, "test", now));
        }


    }

    @Nested
    @DisplayName("Activate")
    class activateTest {
        @Test
        @DisplayName("Activate with correct credentials")
        void activateTest1() {
            doReturn(true).when(daoLogin).checkActive(eq("test"), any(LocalDateTime.class));
            doReturn(true).when(daoLogin).activate(eq("test"));

            assertTrue(servicesLogin.activate("test", LocalDateTime.now()));
        }

        @Test
        @DisplayName("Activate with incorrect credentials")
        void activateTest2(){
            when(daoLogin.checkActive(eq("test"), any(LocalDateTime.class))).thenReturn(false);
            assertFalse(servicesLogin.activate("test", LocalDateTime.now()));
        }


    }

    @Nested
    @DisplayName("Get")
    class getTest {
        @Test
        @DisplayName("Get with correct credentials")
        void getTest1(){
            Account account = new Account();
            account.setUsername("test");
            account.setPassword("test");
            when(daoLogin.get()).thenReturn(Collections.singletonList(account));
            assertEquals(Collections.singletonList(account), servicesLogin.get());
        }
        @Test
        @DisplayName("Get with incorrect credentials")
        void getTest2(){
            when(daoLogin.get()).thenReturn(null);
            assertNull(servicesLogin.get());
        }

    }


}
