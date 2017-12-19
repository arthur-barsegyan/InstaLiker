import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.exceptions.InstagramException;

import java.util.Scanner;
import java.util.function.Function;

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

    private Object getUserInput(String text, Function<String, Object> validator) {
        String userInput = null;

        while (true) {
            System.out.print(text);
            userInput = reader.nextLine();

            Object retVal = validator.apply(userInput);
            if (retVal != null)
                return retVal;
        }
    }

    public Liker getLiker() {
        System.out.println("[Configure your liker]");
        Liker liker = new Liker(instagram);

        String targetHashTag = (String) getUserInput("Enter target hash tag: #", input -> {
            if (input.isEmpty())
                return null;

            return true;
        });
        liker.setTargetHashTag(targetHashTag);

        Function<String, Object> digitValidator = input -> {
            try {
                Integer threshold = Integer.parseInt(input);
                if (threshold > 0)
                    return threshold;
            } catch (NumberFormatException e) {
                return null;
            }

            return null;
        };

        Integer likeThreshold = (Integer) getUserInput("Enter likes threshold for media: ", digitValidator);
        liker.setLikeThreshold(likeThreshold);

        Integer likesCount = (Integer) getUserInput("Enter total count of likes: ", digitValidator);
        liker.setLikesCount(likesCount);

        return liker;
        /* Хештег + Кол-во лайков (если на фото больше лайков чем задано,
          то не ставим лайк) + таймер (от 50 - 60 секунд) + одного и того же пользователя больше одного раза
          лайкать нельзя + кол-во лайков (с ограничением) */
    }


    /*  Создание лайкера с параметрами
        Очередь лайкеров
        Возможность паузы/возобновления
        Остановка задачи (переключение на следующую), в случае если при работе наткнулись на лайкнутую фотографию
        Удаление текущего лайкера (переключение на следующую задачу)
    * */
}
