package ru.nsu.instaliker;

import org.jinstagram.Instagram;

/* Хештег + Кол-во лайков (если на фото больше лайков чем задано,
  то не ставим лайк) + таймер (от 50 - 60 секунд) + одного и того же пользователя больше одного раза
  лайкать нельзя + кол-во лайков (с ограничением) */
public class Liker {
    private Instagram instagram;
    private String targetHashTag;

    private Integer likeThreshold;
    private Integer likesCount;

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


}
