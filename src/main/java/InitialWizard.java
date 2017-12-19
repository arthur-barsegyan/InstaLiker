import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.exceptions.InstagramException;

import java.util.Scanner;

public class InitialWizard {
    private static String CLIENT_ID = "222f02e5be0f49698e5e3e736677ebfc";
    private static String CLIENT_SECRET = "cef279e0d7dd4f39bf252a7edbeedcb9";

    private static final Token EMPTY_TOKEN = null;

    private Scanner reader;

    private Instagram instagram;

    public InitialWizard() {
        reader = new Scanner(System.in);
    }

    public void start() throws InstagramException {
        InstagramService service = new InstagramAuthService().apiKey(CLIENT_ID)
                                                            .apiSecret(CLIENT_SECRET)
                                                            .callback("http://localhost")
                                                            .scope("likes")
                                                            .build();

        System.out.println("[Authorization]");
        String authorizationUrl = service.getAuthorizationUrl();
        System.out.println("Insert this URL in browser address line: " + authorizationUrl);
        System.out.print("Enter response code from response: ");

        String code = reader.nextLine();
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(verifier);
        instagram = new Instagram(accessToken);
        System.out.println("[Success]");
    }

    public Liker getLiker() {
        System.out.println("[Configure your liker]");
        Liker liker = new Liker(instagram);

        System.out.println("Enter target hash tag: #");
        /* Хештег + Кол-во лайков (если на фото больше лайков чем задано
         , то не ставим лайк + таймер (от 50 - 60 секунд) + кол-во лайков (с ограничением) */
    }


    /*  Создание лайкера с параметрами
        Очередь лайкеров
        Возможность паузы/возобновления
        Остановка задачи (переключение на следующую), в случае если при работе наткнулись на лайкнутую фотографию
        Удаление текущего лайкера (переключение на следующую задачу)
    * */
}
