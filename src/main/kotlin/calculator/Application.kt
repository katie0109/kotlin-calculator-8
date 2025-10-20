package calculator

import camp.nextstep.edu.missionutils.Console

fun main() {
    // 1단계: Console API를 사용한 입력 안내 메시지 출력
    println("덧셈할 문자열을 입력해 주세요.")
    val input = Console.readLine()
    val result = StringCalculator.add(input)
    println("결과 : $result")
}
