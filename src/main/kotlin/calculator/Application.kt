package calculator

import camp.nextstep.edu.missionutils.Console

fun main() {
    // 1단계: Console API를 사용한 입력 안내 메시지 출력
    println("덧셈할 문자열을 입력해 주세요.")

    // 1단계: Console.readLine()으로 사용자 입력 수신
    val input = Console.readLine()

    // 1단계: StringCalculator 호출 및 결과 계산
    val result = StringCalculator.add(input)

    // 1단계: "결과 : {합계}" 형식으로 결과 출력
    println("결과 : $result")

    // 1단계에서는 예외 처리를 하지 않음
    // 예외 처리는 9단계에서 구현 예정
}
