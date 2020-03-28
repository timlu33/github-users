package tim.githubusers.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tim.githubusers.models.User

interface GithubServices {

    @GET("users")
    fun getUsers(@Query("since") since: Int): Observable<List<User>>

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Observable<User>

}