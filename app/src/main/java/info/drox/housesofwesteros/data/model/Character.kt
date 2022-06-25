package info.drox.housesofwesteros.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.net.URL

@Entity
data class Character(
    @PrimaryKey val url: URL,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val mother: String,
    val father: String,
    val spouse: String,
    val titles: Array<String>,
    val aliases: Array<String>,
    val allegiances: Array<String>
) {
    companion object {
        fun getIDFromUrl(url: URL): Int = Uri.parse(url.toString()).lastPathSegment!!.toInt()
        fun getUrlFromId(id: Int): URL = URL("https://www.anapioficeandfire.com/api/characters/$id")
    }

    @Ignore
    val id: Int = getIDFromUrl(url)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Character

        if (url != other.url) return false
        if (name != other.name) return false
        if (gender != other.gender) return false
        if (culture != other.culture) return false
        if (born != other.born) return false
        if (died != other.died) return false
        if (mother != other.mother) return false
        if (father != other.father) return false
        if (spouse != other.spouse) return false
        if (!titles.contentEquals(other.titles)) return false
        if (!aliases.contentEquals(other.aliases)) return false
        if (!allegiances.contentEquals(other.allegiances)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + culture.hashCode()
        result = 31 * result + born.hashCode()
        result = 31 * result + died.hashCode()
        result = 31 * result + mother.hashCode()
        result = 31 * result + father.hashCode()
        result = 31 * result + spouse.hashCode()
        result = 31 * result + titles.contentHashCode()
        result = 31 * result + aliases.contentHashCode()
        result = 31 * result + allegiances.contentHashCode()
        return result
    }
}