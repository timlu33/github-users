package tim.githubusers.api

sealed class ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>()
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ): ResultOf<Nothing>()
}

data class BaseResp<T>(
    val data: T,
    val message: String?
)