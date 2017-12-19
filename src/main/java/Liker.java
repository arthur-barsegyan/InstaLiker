import org.jinstagram.Instagram;

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

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }
}
