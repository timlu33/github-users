package tim.githubusers.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tim.githubusers.api.GithubServices
import tim.githubusers.api.MockWebDispatcher
import tim.githubusers.common.Configs
import tim.githubusers.models.GithubRepository
import tim.githubusers.ui.home.HomeViewModel
import tim.githubusers.ui.profile.ProfileViewModel
import java.util.concurrent.TimeUnit

val viewModelModules = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}

val networkModules = module {
    single { createNetworkService<GithubServices>(get(), Configs.BASE_URL) }
    single { createOkHttpClient() }
}

val repositoryModules = module {
    single { GithubRepository(get()) }
}

val mockWebModule = module {
    single { MockWebDispatcher() }
    single { createMockWebServer(get()) }
}

val diModules = listOf(
    viewModelModules,
    networkModules,
    repositoryModules,
    mockWebModule
)


inline fun <reified T> createNetworkService(okHttpClient: OkHttpClient, baseUrl: String): T {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(T::class.java)
}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .build()
}

fun createMockWebServer(dispatcher: MockWebDispatcher): MockWebServer {
    val mockWebServer = MockWebServer()
    mockWebServer.setDispatcher(dispatcher)
    return mockWebServer
}