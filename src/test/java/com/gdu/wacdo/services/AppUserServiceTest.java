package com.gdu.wacdo.services;

import com.gdu.wacdo.models.AppUser;
import com.gdu.wacdo.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    private AppUserService appUserService;
    private AppUserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        userRepository = mock(AppUserRepository.class);
        appUserService = new AppUserService();

        // injection manuelle
        var field = AppUserService.class.getDeclaredField("userRepository");
        field.setAccessible(true);
        field.set(appUserService, userRepository);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        AppUser user = new AppUser();
        user.setEmail("test@test.com");
        user.setPassword("password123");
        user.setRole("ADMIN");

        when(userRepository.findByEmail("test@test.com")).thenReturn(user);

        UserDetails userDetails = appUserService.loadUserByUsername("test@test.com");

        assertNotNull(userDetails);
        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_UserNotExists() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(null);

        UserDetails userDetails = appUserService.loadUserByUsername("unknown@test.com");

        assertNull(userDetails);
    }
}
