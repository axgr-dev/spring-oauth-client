package dev.axgr

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class YouTube(private val client: RestClient) {

  fun details(id: String): Video? {
    val response = client.get()
      .uri {
        it
          .path("/videos")
          .queryParam("id", id)
          .queryParam("part", "snippet,contentDetails,statistics")
          .build()
      }
      .retrieve()
      .body(VideoListResponse::class.java)

    return response?.items?.firstOrNull()
  }

  fun update(video: Video, title: String) {
    val request = VideoUpdateRequest(video.id, title, video.category)

    client.put()
      .uri {
        it
          .path("/videos")
          .queryParam("part", "snippet,status,localizations")
          .build()
      }
      .body(request)
      .retrieve()
      .toBodilessEntity()
  }
}


