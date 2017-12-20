package ru.nsu.instaliker;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import ru.nsu.instaliker.view.ConsoleView;

import java.util.function.Function;

public class Wizard {
    private static String CLIENT_ID = "222f02e5be0f49698e5e3e736677ebfc";
    private static String CLIENT_SECRET = "cef279e0d7dd4f39bf252a7edbeedcb9";

    private static final Token EMPTY_TOKEN = null;

    private ConsoleView view;
    private Instagram instagram;

    Instagram initAuthorization(ConsoleView view) {
        InstagramService service = new InstagramAuthService().apiKey(CLIENT_ID)
                                                            .apiSecret(CLIENT_SECRET)
                                                            .callback("http://localhost")
                                                            .scope("likes")
                                                            .build();

        view.printString("[Authorization]");
        String authorizationUrl = service.getAuthorizationUrl();
        view.printString("Insert this URL in browser address line: " + authorizationUrl);
        view.printString("Enter response code from response: ");

        String code = view.readString();
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(verifier);

        instagram = new Instagram(accessToken);
        view.printString("[Success]");

        return instagram;
    }

    private Object getUserInput(String text, Function<String, Object> validator) {
        String userInput = null;

        while (true) {
            view.printString(text);
            userInput = view.readString();

            Object retVal = validator.apply(userInput);
            if (retVal != null)
                return retVal;
        }
    }

    Liker getLiker() {
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
    }

    void setView(ConsoleView view) {
        this.view = view;
    }


    /*  Создание лайкера с параметрами
        Очередь лайкеров
        Возможность паузы/возобновления
        Остановка задачи (переключение на следующую), в случае если при работе наткнулись на лайкнутую фотографию
        Удаление текущего лайкера (переключение на следующую задачу)
    * */
}
