import junit.framework.Assert;
import org.jinstagram.Instagram;
import org.jinstagram.exceptions.InstagramException;
import org.junit.Test;
import ru.nsu.instaliker.Liker;
import ru.nsu.instaliker.Wizard;
import ru.nsu.instaliker.view.ConsoleView;

public class MainTest extends Assert {
    @Test
    public void authTest() {
        Wizard wizard = new Wizard();
        wizard.setView(new ConsoleView());
        wizard.initAuthorization();
    }

    @Test
    public void likerTest() throws InstagramException {
        Wizard wizard = new Wizard();
        ConsoleView view = new ConsoleView();
        wizard.setView(view);

        Instagram instagram = wizard.initAuthorization();
        Liker liker = new Liker(wizard.initAuthorization(), Liker.WaitingMode.NONWAITING);
        liker.setTargetHashTag("cats");
        liker.setLikesCount(1);
        liker.run();

        assertEquals(instagram.getMediaInfo(liker.getLikedMedia().get(0)).getData().isUserHasLiked(), true);
    }
}
