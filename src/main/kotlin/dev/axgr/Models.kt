package dev.axgr

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import org.springframework.boot.jackson.JsonComponent

data class Video(val id: String, val title: String, val views: Int, val category: Int)

data class VideoListResponse(val items: List<Video>)
data class VideoUpdateRequest(val id: String, val title: String, val category: Int)

@JsonComponent
class VideoDeserializer : JsonDeserializer<VideoListResponse>() {

  override fun deserialize(parser: JsonParser, context: DeserializationContext): VideoListResponse {
    val root = parser.codec.readTree<JsonNode>(parser)
    val items = mutableListOf<Video>()

    root.withArray<JsonNode>("items").forEach { item ->
      val id = item["id"].textValue()
      val title = item["snippet"]["title"].textValue()
      val category = item["snippet"]["categoryId"].asInt()
      val views = item["statistics"]["viewCount"].asInt()
      items.add(Video(id, title, views, category))
    }

    return VideoListResponse(items)
  }
}

@JsonComponent
class VideoUpdateRequestSerializer : JsonSerializer<VideoUpdateRequest>() {

  override fun serialize(value: VideoUpdateRequest, generator: JsonGenerator, serializers: SerializerProvider) {
    generator.writeStartObject()
    generator.writeStringField("id", value.id)
    generator.writeObjectFieldStart("snippet")
    generator.writeNumberField("categoryId", value.category)
    generator.writeStringField("title", value.title)
    generator.writeEndObject()
    generator.writeEndObject()
  }
}
