package calculator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("StringCalculator 기능별 단위 테스트")
class StringCalculatorTest {

    @Nested
    @DisplayName("1. 입출력 인터페이스")
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

    @Nested
    @DisplayName("2. 입력 전처리 및 빈 문자열 처리")
    inner class Step2_InputPreprocessing {

        @Test
        @DisplayName("null 입력을 빈 문자열로 변환하여 0을 반환한다")
        fun `null_입력_빈_문자열_변환하여_0_반환`() {
            assertEquals(0, StringCalculator.add(null))
        }

        @Test
        @DisplayName("다양한 공백 패턴을 빈 문자열로 변환하여 0을 반환한다")
        fun `다양한_공백_패턴_빈_문자열_변환`() {
            assertEquals(0, StringCalculator.add(""))
            assertEquals(0, StringCalculator.add("   "))
            assertEquals(0, StringCalculator.add(" \t\n "))
            assertEquals(0, StringCalculator.add("\r \n"))
        }

        @Test
        @DisplayName("앞뒤 공백을 제거한 후 처리한다")
        fun `앞뒤_공백_제거_처리`() {
            assertEquals(3, StringCalculator.add(" 1,2 "))
            assertEquals(6, StringCalculator.add("  1,2:3  "))
            assertEquals(5, StringCalculator.add(" 5 "))
        }

        @Test
        @DisplayName("전처리 과정이 예외 없이 동작한다")
        fun `전처리_과정_예외_없이_동작`() {
            assertDoesNotThrow { StringCalculator.add(null) }
            assertDoesNotThrow { StringCalculator.add("") }
            assertDoesNotThrow { StringCalculator.add("   ") }
            assertDoesNotThrow { StringCalculator.add(" 1,2 ") }
        }
    }

    @Nested
    @DisplayName("3단계: 구분자 감지 및 분류")
    inner class Step3_DelimiterDetection {

        @Test
        @DisplayName("기본 구분자 모드 감지")
        fun 기본_구분자_모드_감지() {
            assertFalse(StringCalculator.isCustomDelimiterFormat("1,2"))
            assertFalse(StringCalculator.isCustomDelimiterFormat("1:2"))
            assertFalse(StringCalculator.isCustomDelimiterFormat("123"))
            assertFalse(StringCalculator.isCustomDelimiterFormat("//;1;2;3")) // \n 없음
        }

        @Test
        @DisplayName("커스텀 구분자 모드 감지")
        fun 커스텀_구분자_모드_감지() {
            assertTrue(StringCalculator.isCustomDelimiterFormat("//;\n1;2;3"))
            assertTrue(StringCalculator.isCustomDelimiterFormat("//|\n1|2|3"))
            assertTrue(StringCalculator.isCustomDelimiterFormat("//*\n1*2*3"))
            assertTrue(StringCalculator.isCustomDelimiterFormat("//.\n1.2.3"))
        }

        @Test
        @DisplayName("경계 케이스 처리")
        fun 경계_케이스_처리() {
            assertFalse(StringCalculator.isCustomDelimiterFormat("//"))
            assertFalse(StringCalculator.isCustomDelimiterFormat("//\n"))
            assertFalse(StringCalculator.isCustomDelimiterFormat("/\n1;2"))
        }

        @Test
        @DisplayName("모드 분기에 따른 처리 확인")
        fun 모드별_처리_확인() {
            // 기본 모드
            assertEquals(3, StringCalculator.add("1,2"))
            assertEquals(6, StringCalculator.add("1,2:3"))

            // 커스텀 모드 (실제 계산값으로 수정)
            assertEquals(6, StringCalculator.add("//;\n1;2;3"))
            assertEquals(6, StringCalculator.add("//|\n1|2|3"))
            assertEquals(6, StringCalculator.add("//#\n1#2#3"))  // 10 → 6으로 수정
        }
    }

    @Nested
    @DisplayName("4단계: 기본 구분자 처리")
    inner class Step4_DefaultDelimiters {

        @Test
        @DisplayName("쉼표 구분자 처리")
        fun 쉼표_구분자_처리() {
            assertEquals(3, StringCalculator.add("1,2"))
            assertEquals(6, StringCalculator.add("1,2,3"))
            assertEquals(10, StringCalculator.add("1,2,3,4"))
            assertEquals(15, StringCalculator.add("1,2,3,4,5"))
        }

        @Test
        @DisplayName("콜론 구분자 처리")
        fun 콜론_구분자_처리() {
            assertEquals(3, StringCalculator.add("1:2"))
            assertEquals(6, StringCalculator.add("1:2:3"))
            assertEquals(10, StringCalculator.add("1:2:3:4"))
        }

        @Test
        @DisplayName("혼재된 구분자 처리")
        fun 혼재된_구분자_처리() {
            assertEquals(6, StringCalculator.add("1,2:3"))
            assertEquals(10, StringCalculator.add("1:2,3:4"))
            assertEquals(15, StringCalculator.add("1,2:3,4:5"))
        }

        @Test
        @DisplayName("단일 숫자 처리")
        fun 단일_숫자_처리() {
            assertEquals(1, StringCalculator.add("1"))
            assertEquals(5, StringCalculator.add("5"))
            assertEquals(123, StringCalculator.add("123"))
        }

        @Test
        @DisplayName("커스텀 구분자는 기존 방식 유지")
        fun 커스텀_구분자_기존_방식_유지() {
            assertEquals(6, StringCalculator.add("//;\n1;2;3"))
            assertEquals(6, StringCalculator.add("//|\n1|2|3"))
            assertEquals(6, StringCalculator.add("//#\n1#2#3"))  // 10 → 6으로 수정
        }
    }

    @Nested
    @DisplayName("5단계: 커스텀 구분자 파싱")
    inner class Step5_CustomDelimiterParsing {

        @Test
        @DisplayName("세미콜론 커스텀 구분자")
        fun 세미콜론_커스텀_구분자() {
            assertEquals(6, StringCalculator.add("//;\n1;2;3"))
            assertEquals(10, StringCalculator.add("//;\n1;2;3;4"))
            assertEquals(1, StringCalculator.add("//;\n1"))
        }

        @Test
        @DisplayName("다양한 일반 문자 구분자")
        fun 다양한_일반_문자_구분자() {
            assertEquals(6, StringCalculator.add("//#\n1#2#3"))
            assertEquals(6, StringCalculator.add("//&\n1&2&3"))
            assertEquals(6, StringCalculator.add("//a\n1a2a3"))
        }

        @Test
        @DisplayName("정규식 특수문자 구분자 처리")
        fun 정규식_특수문자_구분자_처리() {
            assertEquals(6, StringCalculator.add("//.\n1.2.3"))
            assertEquals(6, StringCalculator.add("//|\n1|2|3"))
            assertEquals(6, StringCalculator.add("//*\n1*2*3"))
            assertEquals(6, StringCalculator.add("//+\n1+2+3"))
            assertEquals(6, StringCalculator.add("//?\n1?2?3"))
        }

        @Test
        @DisplayName("커스텀 구분자로 큰 숫자 처리")
        fun 커스텀_구분자_큰_숫자_처리() {
            assertEquals(60, StringCalculator.add("//;\n10;20;30"))
            assertEquals(600, StringCalculator.add("//|\n100|200|300"))
        }

        @Test
        @DisplayName("기본 구분자는 여전히 정상 동작")
        fun 기본_구분자_정상_동작_유지() {
            assertEquals(6, StringCalculator.add("1,2,3"))
            assertEquals(6, StringCalculator.add("1:2:3"))
            assertEquals(6, StringCalculator.add("1,2:3"))
        }
    }

    @Nested
    @DisplayName("6단계: 토큰 분할 및 검증")
    inner class Step6_TokenValidation {

        @Test
        @DisplayName("연속 구분자로 인한 빈 토큰 예외 처리")
        fun 연속_구분자_빈_토큰_예외() {
            // 기본 구분자
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("1,,2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("1::2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("1,2,") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add(",1,2") }
        }

        @Test
        @DisplayName("커스텀 구분자 빈 토큰 예외 처리")
        fun 커스텀_구분자_빈_토큰_예외() {
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("//;\n1;;2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("//;\n;1;2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("//;\n1;2;") }
        }

        @Test
        @DisplayName("공백만 있는 토큰 예외 처리")
        fun 공백_토큰_예외() {
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("1, ,2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("1:\t:2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("//;\n1; ;2") }
        }

        @Test
        @DisplayName("숫자가 아닌 토큰 예외 처리")
        fun 숫자_아닌_토큰_예외() {
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("1,a,2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("1:xyz:2") }
            assertThrows(IllegalArgumentException::class.java) { StringCalculator.add("//;\n1;hello;2") }
        }

        @Test
        @DisplayName("정상적인 토큰은 올바르게 합산")
        fun 정상_토큰_올바른_합산() {
            assertEquals(6, StringCalculator.add("1,2,3"))
            assertEquals(6, StringCalculator.add("1:2:3"))
            assertEquals(6, StringCalculator.add("//;\n1;2;3"))
            assertEquals(15, StringCalculator.add("1,2:3,4,5"))
        }

        @Test
        @DisplayName("기존 기능 정상 동작 유지")
        fun 기존_기능_정상_동작() {
            assertEquals(0, StringCalculator.add(""))
            assertEquals(5, StringCalculator.add("5"))
            assertEquals(6, StringCalculator.add("//|\n1|2|3"))
        }
    }

}
