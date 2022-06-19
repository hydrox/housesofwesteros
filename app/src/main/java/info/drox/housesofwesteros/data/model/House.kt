package info.drox.housesofwesteros.data.model

import java.net.URL

data class House(
    val url: URL,
    val name: String,
    val words: String,
    val region: String,
    val coatOfArms: String,
    val currentLord: String,
    val titles: Array<String>,
    val seats: Array<String>,
    val swornMembers: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as House

        if (url != other.url) return false
        if (name != other.name) return false
        if (words != other.words) return false
        if (region != other.region) return false
        if (coatOfArms != other.coatOfArms) return false
        if (currentLord != other.currentLord) return false
        if (!titles.contentEquals(other.titles)) return false
        if (!seats.contentEquals(other.seats)) return false
        if (!swornMembers.contentEquals(other.swornMembers)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + words.hashCode()
        result = 31 * result + region.hashCode()
        result = 31 * result + coatOfArms.hashCode()
        result = 31 * result + currentLord.hashCode()
        result = 31 * result + titles.contentHashCode()
        result = 31 * result + seats.contentHashCode()
        result = 31 * result + swornMembers.contentHashCode()
        return result
    }
}