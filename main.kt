class Action(val read: Char? = null,
             val topStack: Char? = null,
             val pushStack: Char? = null,
             val target: Int)

class State(val id: Int, private val availableMove: List<Action> = mutableListOf()) {
    companion object {
        const val STATE_ORIGIN = 0

        const val STATE_S = 101
        const val STATE_P = 102
        const val STATE_O = 103
        const val STATE_K = 104

        const val STATE_ERROR = -1
    }

    fun move(read: Char? = null, topStack: Char? = null) = availableMove
        .find { it.read == read && it.topStack == topStack }
        ?: Action(target = STATE_ERROR)

}

class Main {
    companion object {

        private val statesToken = listOf (
            State(State.STATE_ORIGIN, listOf (
                Action(State.STATE_S.toTokenChar(), '#', target = State.STATE_ORIGIN, pushStack = State.STATE_S.toTokenChar()),
                Action(State.STATE_P.toTokenChar(), State.STATE_S.toTokenChar(), target = State.STATE_ORIGIN, pushStack = State.STATE_P.toTokenChar()),
                Action(State.STATE_O.toTokenChar(), State.STATE_P.toTokenChar(), target = State.STATE_ORIGIN, pushStack = State.STATE_O.toTokenChar()),
                Action(State.STATE_K.toTokenChar(), State.STATE_O.toTokenChar(), target = State.STATE_ORIGIN, pushStack = '*'),
                Action(State.STATE_K.toTokenChar(), State.STATE_P.toTokenChar(), target = 4, pushStack = '*'),
                Action(null, State.STATE_P.toTokenChar(), target = 2, pushStack = '*'),
                Action(null, State.STATE_O.toTokenChar(), target = 3, pushStack = '*')
            )),
            State(2, listOf (
                Action(null, State.STATE_S.toTokenChar(), target = State.STATE_ORIGIN, pushStack = '*')
            )),
            State(3, listOf (
                Action(null, State.STATE_P.toTokenChar(), target = 2, pushStack = '*')
            )),
            State(4, listOf (
                Action(null, State.STATE_S.toTokenChar(), target = State.STATE_ORIGIN, pushStack = '*')
            ))
        )

        private fun Int.toTokenChar() = when (this) {
            State.STATE_S -> 's'
            State.STATE_P -> 'p'
            State.STATE_O -> 'o'
            State.STATE_K -> 'k'
            else -> 'e'
        }

        private fun isTokenValidSentence(token: List<Char>) : Boolean {
            var i = 0
            var curState = State.STATE_ORIGIN
            val stack = mutableListOf('#')

            while ((i < token.size || stack.last() != '#') && curState != State.STATE_ERROR) {
                val evaluateChar = when (curState) {
                    1, 2, 3, 4 -> null
                    else -> if (i < token.size) token[i] else null
                }

                val evaluateTopStack = when (curState) {
                    1 -> null
                    else -> stack.lastOrNull()
                }

                val evaluation = statesToken.first { it.id == curState }.move(evaluateChar, evaluateTopStack)
                curState = evaluation.target
                val pushChar = evaluation.pushStack

                when (pushChar) {
                    null -> {}
                    '*' -> stack.removeAt(stack.size - 1)
                    else -> stack.add(pushChar)
                }

                when (curState) {
                    State.STATE_ORIGIN -> i += 1
                }
            }

            return stack.last() == '#' && curState != State.STATE_ERROR
        }

//        private val testCaseToken = listOf('s', 'p', 'p', 'o', 'k')
        private val testCaseToken = listOf('s', 'p', 'o', 'k')
//        private val testCaseToken = listOf('s', 'p', 'o')
//        private val testCaseToken = listOf('s', 'p', 'k')
//        private val testCaseToken = listOf('s', 'p')
//        private val testCaseToken = listOf('s', 'p', 'p')
//        private val testCaseToken = listOf('s')

        @JvmStatic
        fun main(args: Array<String>) {
            println(testCaseToken)
            val isValid = isTokenValidSentence(testCaseToken)
            println(isValid)
        }
    }
}
