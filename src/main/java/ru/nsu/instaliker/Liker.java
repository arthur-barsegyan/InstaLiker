package ru.nsu.instaliker;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.tags.TagInfoData;
import org.jinstagram.entity.tags.TagInfoFeed;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Хештег + Кол-во лайков (если на фото больше лайков чем задано,
  то не ставим лайк) + таймер (от 50 - 60 секунд) + одного и того же пользователя больше одного раза
  лайкать нельзя + кол-во лайков (с ограничением) */
public class Liker implements Runnable {
    private static int SLEEP_TIMEOUT = 50000;
    private Logger logger = null;

    private Instagram instagram;
    private String targetHashTag;
    private List<MediaFeedData> mediaFeeds;
    private ArrayList<String> likedMedia = new ArrayList<>();

    // For tests only
    public ArrayList<String> getLikedMedia() {
        return likedMedia;
    }

    enum LikerState {
        PAUSED,
        ACTIVE,
        CANCELED
    }

    private LikerState state = LikerState.ACTIVE;
    private boolean firstInvoke = true;

    private Integer likeThreshold;
    private Integer likesCount = 0;

    private Long feedLength;
    private Long likedSuccessfully = 0L;

    private Long currentTask = 0L;
    private Integer currentSubTask = 0;

    public Liker(Instagram instagram) {
        this.instagram = instagram;
    }

    public void setTargetHashTag(String targetHashTag) {
        this.targetHashTag = targetHashTag;
        this.logger = LogManager.getLogger("Liker:#" + targetHashTag);
    }

    public void setLikeThreshold(Integer likeThreshold) {
        this.likeThreshold = likeThreshold;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    boolean isPaused() {
        return state == LikerState.PAUSED;
    }

    void resume() {
        state = LikerState.ACTIVE;
    }

    void pause() {
        state = LikerState.PAUSED;
    }

    void cancel() {
        state = LikerState.CANCELED;
    }

    @Override
    public void run() {
        if (targetHashTag == null)
            throw new IllegalStateException("Missing target hash tag");

        try {
            if (firstInvoke) {
//            TagInfoFeed feed = instagram.getTagInfo(targetHashTag);
//            TagInfoData tagData = feed.getTagInfo();
//
//            feedLength = tagData.getMediaCount();
//
//            TagMediaFeed mediaFeed = instagram.getRecentMediaTags(targetHashTag, 1000);
//            List<MediaFeedData> mediaFeeds = mediaFeed.getData();

                MediaFeed mediaFeed = instagram.getUserRecentMedia();
                mediaFeeds = mediaFeed.getData();
                System.out.format("Found %d media\n", mediaFeeds.size());

                firstInvoke = false;
            }

            Random random = new Random();
            for (; currentTask < likesCount; ) {
                for (; currentSubTask < mediaFeeds.size() && currentTask < likesCount; currentSubTask++, currentTask++) {
                    MediaFeedData feedData = mediaFeeds.get(currentSubTask);
                    if (state != LikerState.PAUSED && state != LikerState.CANCELED) {
                        instagram.setUserLike(feedData.getId());
                        likedMedia.add(feedData.getId());
                        System.out.println(feedData.getLink());

                        try {
                            int randomDigit = random.nextInt(10);
                            Thread.sleep(SLEEP_TIMEOUT + randomDigit * 1000);
                            logger.debug("Sleep time: " + (SLEEP_TIMEOUT + randomDigit * 1000));
                        } catch (InterruptedException ignored) {}
                    } else {
                        logger.debug("Paused");
                        return;
                    }
                }

                // TODO: Set minTagID
//                mediaFeed = instagram.getRecentMediaTags(targetHashTag);
//                mediaFeeds = mediaFeed.getData();

                currentSubTask = 0;
            }
        } catch (InstagramException e) {
            logger.debug("Error: " + e.getErrorType());
            // TODO: Handle this
        }
    }
}
