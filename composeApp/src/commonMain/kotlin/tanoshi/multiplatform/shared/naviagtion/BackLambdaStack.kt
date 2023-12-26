package tanoshi.multiplatform.shared.naviagtion

// why create this class because we are gonna perform stack operation
// and linked constant time for retrieval and addition
// lambda was extension of this class instead of simple lambda so it
// user can return status without any issue
class BackLambdaStack {
    
    val keepAfterInvocation : WorkStatus = WorkStatus.KeepAfterInvocation
    val removeAfterInvocation : WorkStatus = WorkStatus.RemoveAfterInvocation
    
    enum class WorkStatus {
        KeepAfterInvocation ,
        RemoveAfterInvocation
    }
    
    private data class LambdaFunctionNode (
        val lamda : BackLambdaStack.() -> Any ,
        var prev : LambdaFunctionNode? = null
    )
    
    private var tail : LambdaFunctionNode? = null
    
    fun push( backLambda : BackLambdaStack.() -> Any ) = backLambda.let { lockedValue ->
        tail = LambdaFunctionNode( lockedValue , tail )
    }
    
    fun pop() = tail?.let {
        tail = it.prev
    }
    
    fun peek() = tail?.let {
        it.lamda
    }
    
    companion object {
        fun backLambdaStack() : BackLambdaStack = BackLambdaStack()
    }
    
}