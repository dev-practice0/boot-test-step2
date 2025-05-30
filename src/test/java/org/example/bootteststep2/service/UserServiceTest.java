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
        testUser = new User("피카츄", "electric_mouse@poke.com", 25);
        // 실제로는 DB에서 생성되는 ID를 임의로 설정
        testUser.setId(1L); // 테스트에서만 가능 (실제로는 DB가 생성)
    }

    @Test
    @DisplayName("Mockito 기본 동작 확인")
    void mockitoBasicTest() {
        // Given - Mock 동작 정의 (BDD - Behavior-Driven Development)
        // TDD -> BDD
        // 테스트 주도 개발, 행동 주도 개발 (given.when.then)
        given(userRepository.existsByEmail("test@example.com")).willReturn(false);
        // "test@example.com" -> false
        given(userRepository.save(any(User.class))).willReturn(testUser);
        // save -> testUser

        // When - 실제 메서드 호출
        User newUser = new User("테스트", "test@example.com", 30);
        User savedUser = userService.createUser(newUser);
        // savedUser ? -> newUser??? -> testUser.
        // 겉으로 보기엔 createUser만 호출이 되었지만...
        // 속에는...? 여러 유효성 검증들이 있어요... (repository)

        // Then - 결과 검증
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("피카츄"); // Mock이 반환한 데이터

        // Mock 호출 검증
        verify(userRepository).existsByEmail("test@example.com"); // 이거 호출 된 적 있어요?
        verify(userRepository).save(any(User.class));
    }

    // 사용자 생성 테스트
    @Test
    @DisplayName("사용자 생성 - 정상 케이스")
    void createUserTest() {
        // Given
        User newUser = new User("아구몬", "nine@digital.com", 9);
        given(userRepository.existsByEmail("nine@digital.com")).willReturn(false); // 너 mocking으로 조회해보면 없어~
        given(userRepository.save(any(User.class)))
                .willReturn(testUser);

        // When
        User result = userService.createUser(newUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("피카츄");
        assertThat(result.getEmail()).isEqualTo("electric_mouse@poke.com");

        // Mock 호출 검증
        verify(userRepository).existsByEmail("nine@digital.com");
        verify(userRepository).save(newUser);
    }
}
