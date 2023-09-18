package dev.axgr

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class YouTubeUpdater(private val youtube: YouTube, private val props: YouTubeProperties) {

  companion object {
    private val log = LoggerFactory.getLogger(YouTubeUpdater::class.java)
  }

  private var views = 0

  @Scheduled(cron = "\${youtube.cron}")
  fun update() {
    log.info("Updating..")
    try {
      val video = youtube.details(props.video)
      log.info(video.toString())

      if (video != null && video.views > views) {
        val title = props.title.format(video.views)
        youtube.update(video, title)
        views = video.views
      }
    } catch (cause: Exception) {
      log.error("Failed to connect to YouTube: ${cause.message}")
    }
  }
}
