package org.example.bootteststep2.service;

import org.example.bootteststep2.entity.User;
import org.example.bootteststep2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// 일반적으로...
// import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("UserService 단위 테스트")
@ExtendWith(MockitoExtension.class) // 가짜 객체 -> 의존성 주입
public class UserServiceTest {
    @Mock
    private UserRepository userRepository; // 의존성 주입 당할 대상
    @InjectMocks
    private UserService userService; // 의존성 주입을 할 대상
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("아리아나 그란데", "tall@starbucks.com", 30);
        testUser.setId(1L); // 편의상... 테스트에서 직접해줘야 함
    }

    @Test
    @DisplayName("Mockito 기본 동작 확인")
    void mockitoBasicTest() {
        // Given (BDD style)
        // Mock 동작을 정의
        given(userRepository.existsByEmail("short@startbucks.com"))
                .willReturn(false);
        given(userRepository.save(any(User.class)))
                .willReturn(testUser);
        // When - 실제 메서드 호출
        User newUser = new User("아이유", "jeju@samdasu.com", 30);
        User savedUser = userService.createUser(newUser);

        // Then - 결과 검증
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("아리아나 그란데"); // Mock이 반환한 데이터

        // Mock 호출 검증
        verify(userRepository).existsByEmail("jeju@samdasu.com");
        verify(userRepository).save(any(User.class));
    }
}
