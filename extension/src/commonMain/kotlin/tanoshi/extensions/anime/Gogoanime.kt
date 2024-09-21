package tanoshi.extensions.anime

import androidx.compose.runtime.Composable
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.jsoup.Jsoup
import tanoshi.multiplatform.common.exception.EndOfListException
import tanoshi.multiplatform.common.extension.PlayableContent
import tanoshi.multiplatform.common.extension.PlayableEntry
import tanoshi.multiplatform.common.extension.PlayableExtension
import tanoshi.multiplatform.common.extension.PlayableMedia
import tanoshi.multiplatform.common.extension.annotations.*
import tanoshi.multiplatform.common.extension.core.SharedDependencies
import tanoshi.multiplatform.common.util.SelectableMenu
import tanoshi.multiplatform.common.util.toast.ToastTimeout

@IconName( "icon.png" )
class Gogoanime : PlayableExtension , SharedDependencies() {

    @Variable( "Sub" , "enableSub" )
    var isSubEnabled = false
    
    val config = Config()

    override val name: String = "Gogoanime"
    override val domainsList: SelectableMenu<String>  = SelectableMenu( "https://ww2.gogoanimes.fi/" )
    override val language: String = "English"

    @VariableReciever( "searchEnabled" )
    override fun search(name: String, index: Int): List<PlayableEntry> {
        val list = ArrayList<PlayableEntry>()
        val searchQuery = "https://ww2.gogoanimes.fi/search.html?keyword=${
            name.trimStart().trimEnd().replace( "  " , " " ).replace( " " , "%20" )
        }&page=$index"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url( searchQuery )
            .get()
            .build()
        val response = client.newCall( request ).execute()
        Jsoup.parse( response.body().string() )
            .select( "ul.items>li" )
            .forEach {
                try {
                    list.add(
                        PlayableEntry().apply {
                            this.name = it.select( "div>a" ).attr( "title" )
                            url = domainsList.activeElementValue + it.select( "p.name>a" ).attr("href").substring(1)
                            coverArt = it.select( "div.img > a > img" ).attr( "src" )
                        }
                    )
                } catch ( _ : Exception ) {}
            }
        if ( list.isEmpty() ) throw EndOfListException()
        return list
    }

    override fun fetchDetail(entry: PlayableEntry) {
        TODO("Not yet implemented")
    }

    override fun fetchPlayableContentList(entry: PlayableEntry) {
        TODO("Not yet implemented")
    }

    override fun fetchPlayableMedia(entry: PlayableContent): List<PlayableMedia> {
        TODO("Not yet implemented")
    }

    @VariableReciever( "enableDub" , "enableSub" )
    @ExportTab( "Popular" )
    fun popular( pageIndex : Int ) : List<PlayableEntry> {
        return search( "popular" , pageIndex ) ;
    }

    @ExportComposable( "Hello World Composable" )
    fun helloWorld(){
        exportComposable?.let { startComposableView : (@Composable () -> Unit ).() -> Unit ->
            (@Composable{
                HelloWorld()
            }).startComposableView()

        } ?: run {
            showToast?.let {
                "Allow Composable Access".it( ToastTimeout.SHORT )
            } ?: logger?.let {
                it log {
                    ERROR
                    title = "Starting Composable Failed"
                    """
                        Failed to start composable
                        Failed to invoke toast function
                        Give permission to Extension to allow
                        Invoking these functions
                    """.trimIndent()
                }
            }
        }
    }

}

fun main() {
    Gogoanime().search( "One Piece" , 1 ).forEach {
        println( it.name )
        println( it.url )
        println( it.coverArt )
    }
}