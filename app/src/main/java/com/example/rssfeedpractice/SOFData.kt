package com.example.rssfeedpractice

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class SOFData(val id: String, val title: String, val published: String)



class XMLParser {
    private val ns: String? = null
    var author: String? = null

    fun parse(inputStream: InputStream): List<SOFData> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }
    }

    private fun readSongsRssFeed(parser: XmlPullParser): List<SOFData> {

        val questions = mutableListOf<SOFData>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")


        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "entry") {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var id: String? = null
                var title: String? = null

                var published: String? = null
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "id" -> id = readId(parser)
                        "title" -> title = readTitle(parser)
                        "published" -> published = readPublished(parser)
                        else -> skip(parser)
                    }
                }
                questions.add(SOFData(id.toString(), title.toString(), published.toString().split("T")[0]))
            } else {
                skip(parser)
            }
        }
        return questions
    }

    private fun readId(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "id")
        val id = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "id")
        return id
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readPublished(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "published")
        val publish = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "published")
        return publish
    }

    private fun readAuthor(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "name")
        val author = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "name")
        return author
    }


    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {

            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
