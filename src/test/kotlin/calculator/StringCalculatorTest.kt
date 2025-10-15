package calculator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("StringCalculator 기능별 단위 테스트")
class StringCalculatorTest {

    @Nested
    @DisplayName("1단계: 입출력 인터페이스")
    inner class Step1InputOutputInterface {

        @Test
        @DisplayName("빈 문자열 또는 null 입력 시 0을 반환한다")
        fun `빈_문자열_입력시_0_반환`() {
            assertEquals(0, StringCalculator.add(""))
            assertEquals(0, StringCalculator.add(null))
            assertEquals(0, StringCalculator.add("   ")) // 공백만 있는 경우
        }

        @Test
        @DisplayName("기본 구분자 쉼표(,) 처리")
        fun `기본_구분자_쉼표_처리`() {
            assertEquals(3, StringCalculator.add("1,2"))
            assertEquals(6, StringCalculator.add("1,2,3"))
        }

        @Test
        @DisplayName("기본 구분자 혼합(쉼표, 콜론) 처리")
        fun `기본_구분자_혼합_처리`() {
            assertEquals(6, StringCalculator.add("1,2:3"))
        }

        @Test
        @DisplayName("커스텀 구분자 처리")
        fun `커스텀_구분자_처리`() {
            assertEquals(6, StringCalculator.add("//;\n1;2;3"))
            assertEquals(6, StringCalculator.add("//|\n1|2|3"))
        }

        @Test
        @DisplayName("앞뒤 공백이 있는 입력 처리")
        fun `앞뒤_공백_처리`() {
            assertEquals(3, StringCalculator.add(" 1,2 "))
        }

        @Test
        @DisplayName("단일 숫자 입력 처리")
        fun `단일_숫자_입력_처리`() {
            assertEquals(5, StringCalculator.add("5"))
        }

        @Test
        @DisplayName("입출력 인터페이스가 정상적으로 동작한다")
        fun `입출력_인터페이스_기본_동작`() {
            // StringCalculator.add() 메서드가 존재하고 호출 가능한지 확인
            assertDoesNotThrow { StringCalculator.add("1,2") }
            assertDoesNotThrow { StringCalculator.add("") }
            assertDoesNotThrow { StringCalculator.add(null) }
        }
    }
}
