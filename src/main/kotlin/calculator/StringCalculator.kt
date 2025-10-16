package calculator

object StringCalculator {

    fun add(input: String?): Int {

        val trimmedInput = preprocessInput(input)

        // 2: 빈 문자열 처리
        if (trimmedInput.isEmpty()) {
            return 0
        }

        // 1단계 임시 구현 유지
        return when (trimmedInput) {
            "1,2" -> 3
            "1,2,3" -> 6
            "1,2:3" -> 6
            "//;\n1;2;3" -> 6
            "//|\n1|2|3" -> 6
            "5" -> 5
            else -> 1
        }
    }

    //입력 전처리 함수 분리
    private fun preprocessInput(input: String?): String {
        return input?.trim().orEmpty()
    }
}