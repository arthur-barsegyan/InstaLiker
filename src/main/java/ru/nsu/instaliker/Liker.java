package ru.nsu.instaliker;

import org.jinstagram.Instagram;
import org.jinstagram.entity.tags.TagInfoData;
import org.jinstagram.entity.tags.TagInfoFeed;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.List;
import java.util.Random;

/* Хештег + Кол-во лайков (если на фото больше лайков чем задано,
  то не ставим лайк) + таймер (от 50 - 60 секунд) + одного и того же пользователя больше одного раза
  лайкать нельзя + кол-во лайков (с ограничением) */
public class Liker implements Runnable {
    private static int SLEEP_TIMEOUT = 50000;

    private Instagram instagram;
    private String targetHashTag;

    private Integer likeThreshold;
    private Integer likesCount;

    private Long feedLength;
    private Long likedSuccessfully = 0L;

    Liker(Instagram instagram) {
        this.instagram = instagram;
    }

    public void setTargetHashTag(String targetHashTag) {
        this.targetHashTag = targetHashTag;
    }

    public void setLikeThreshold(Integer likeThreshold) {
        this.likeThreshold = likeThreshold;
    }

    void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public void run() {
        try {
//            TagInfoFeed feed = instagram.getTagInfo(targetHashTag);
//            TagInfoData tagData = feed.getTagInfo();
//
//            feedLength = tagData.getMediaCount();
            MediaFeed mediaFeed = instagram.getUserRecentMedia();
            List<MediaFeedData> mediaFeeds = mediaFeed.getData();
            System.out.format("Found %d media\n", mediaFeeds.size());

            Random random = new Random();
//            TagMediaFeed mediaFeed = instagram.getRecentMediaTags(targetHashTag, 1000);
//            List<MediaFeedData> mediaFeeds = mediaFeed.getData();
            for (long counter = 0; counter < likesCount; counter++) {
                for (MediaFeedData feedData : mediaFeeds){
                    instagram.setUserLike(feedData.getId());
                    System.out.println(feedData.getLink());
                    int randomDigit = random.nextInt(10);
                    try {
                        Thread.sleep(SLEEP_TIMEOUT + randomDigit * 1000);
                        System.out.println("Sleep time: " + (SLEEP_TIMEOUT + randomDigit * 1000));
                    } catch (InterruptedException ignored) {}
                }

                // TODO: Set minTagID
//                mediaFeed = instagram.getRecentMediaTags(targetHashTag);
//                mediaFeeds = mediaFeed.getData();
            }
        } catch (InstagramException e) {
            System.out.println("ERROR: " + e.getErrorType());
        }
    }
}
