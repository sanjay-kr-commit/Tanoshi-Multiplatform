package tanoshi.extensions.anime

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.jsoup.Jsoup
import tanoshi.multiplatform.common.extension.*
import tanoshi.multiplatform.common.extension.helper.defaultArg
import tanoshi.multiplatform.common.extension.helper.request
import tanoshi.multiplatform.common.util.SelectableMenu

class Gogoanime : PlayableExtension , ApplicationAccess() {
    
    override val name: String = "Gogoanime"
    override val domainsList: SelectableMenu<String>  = SelectableMenu( "https://ww2.gogoanimes.fi/" )
    override val language: String = "English"

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
                        PlayableEntry(
                            name = it.select( "div>a" ).attr( "title" ) ,
                            url = domainsList.activeElementValue + it.select( "p.name>a" ).attr("href").substring(1) ,
                            coverArt = it.select( "div.img > a > img" ).attr( "src" )
                        )
                    )
                } catch ( _ : Exception ) {}
            }
        
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

}

fun main() {
    Gogoanime().search( "One Piece" , 1 ).forEach {
        println( it.name )
        println( it.url )
        println( it.coverArt )
    }
}