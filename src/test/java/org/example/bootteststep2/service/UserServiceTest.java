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
        // 각 테스트에서 사용할 공통 테스트 데이터
        testUser = new User("홍길동", "hong@example.com", 25);
        // 실제로는 DB에서 생성되는 ID를 임의로 설정
        testUser.setId(1L); // 테스트에서만 가능 (실제로는 DB가 생성)
    }

    @Test
    @DisplayName("Mockito 기본 동작 확인")
    void mockitoBasicTest() {
        // Given - Mock 동작 정의 (BDD - Behavior-Driven Development)
        given(userRepository.existsByEmail("test@example.com")).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(testUser);

        // When - 실제 메서드 호출
        User newUser = new User("테스트", "test@example.com", 30);
        User savedUser = userService.createUser(newUser);

        // Then - 결과 검증
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("홍길동"); // Mock이 반환한 데이터

        // Mock 호출 검증
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }
}
