package calculator

object StringCalculator {
    fun add(input: String?): Int {
        // null이나 공백만 있으면 0 반환
        if (input.isNullOrBlank()) return 0

        // 입력 정규화: \\n 리터럴을 실제 개행으로 변환
        val normalizedInput = input.trim().replace("\\n", "\n")

        // 커스텀 구분자 형식인지 확인하여 분기 처리
        return if (isCustomDelimiterFormat(normalizedInput)) {
            processWithCustomDelimiter(normalizedInput)
        } else {
            processWithDefaultDelimiter(normalizedInput)
        }
    }

    // "//<delimiter>\n..." 형식 여부
    internal fun isCustomDelimiterFormat(input: String): Boolean {
        if (input.length < 4) return false  // 최소 길이 체크
        return input.startsWith("//") && input.indexOf('\n', 2) > 2
    }

    // 커스텀 구분자 추출 후 분할하여 합산
    private fun processWithCustomDelimiter(input: String): Int {
        val newlineIndex = input.indexOf('\n')
        val customDelimiter = input.substring(2, newlineIndex)
        val numbersText = input.substring(newlineIndex + 1)

        // trailing 빈 토큰도 보존하기 위해 limit 명시
        val tokens = numbersText.split(Regex(Regex.escape(customDelimiter)), Int.MAX_VALUE)
        return validateAndSumTokens(tokens)
    }

    // 쉼표, 콜론으로 분할하여 합산
    private fun processWithDefaultDelimiter(input: String): Int {
        // trailing 빈 토큰도 보존하기 위해 limit 명시
        val tokens = input.split(Regex("[,:]"), Int.MAX_VALUE)
        return validateAndSumTokens(tokens)
    }

    // 토큰 검증 및 합산
    private fun validateAndSumTokens(tokens: List<String>): Int {
        var sum: Long = 0  // Long으로 중간 계산하여 오버플로우 방지
        for (token in tokens) {
            // 빈 값이나 공백만 있는 토큰 검증
            if (token.isBlank()) {
                throw IllegalArgumentException("빈 값은 허용되지 않습니다.")
            }

            // 숫자 변환 및 검증
            val number = token.toIntOrNull()
                ?: throw IllegalArgumentException("'$token'은(는) 올바른 숫자가 아닙니다.")

            // 양수 검증
            if (number <= 0) { // 0도 양수가 아니므로 예외 발생
                throw IllegalArgumentException("양수만 입력할 수 있습니다: $number")
            }
            sum += number
        }
        // Int 범위 초과 검증
        if (sum > Int.MAX_VALUE) {
            throw IllegalArgumentException("계산 결과가 Int 범위를 초과합니다: $sum")
        }

        return sum.toInt()
    }
}
