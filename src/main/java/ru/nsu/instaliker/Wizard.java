package ru.nsu.instaliker;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import ru.nsu.instaliker.view.ConsoleView;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;

public class Wizard {
    private static String CLIENT_ID = "222f02e5be0f49698e5e3e736677ebfc";
    private static String CLIENT_SECRET = "cef279e0d7dd4f39bf252a7edbeedcb9";
    private static String authURL = "https://api.instagram.com/oauth/authorize/?";

    private static final Token EMPTY_TOKEN = null;

    private ConsoleView view;
    private Instagram instagram;

    private void print_content(HttpsURLConnection con){
        if(con != null) {

            try {
                System.out.println("****** Content of the URL ********");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);
                }

                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public Instagram initAuthorization() {
//        InstagramService service = new InstagramAuthService().apiKey(CLIENT_ID)
//                                                            .apiSecret(CLIENT_SECRET)
//                                                            .callback("http://localhost")
//                                                            .scope("likes")
//                                                            .build();

        view.printlnString("[Authorization]");
        String token = "2116020742.222f02e.6ca64e4119bb4489be2a017418d76790";
//https://www.instagram.com/oauth/authorize/?client_id=222f02e5be0f49698e5e3e736677ebfc&redirect_uri=http%3A%2F%2Flocalhost&response_type=token
//        view.printlnString("Insert this URL in browser address line: " + authorizationUrl);
//        view.printString("Enter response code from response: ");
//
//        String code = view.readString();
//        Verifier verifier = new Verifier(code);
//        service.getAccessToken(verifier);
        Token accessToken = new Token(token, CLIENT_SECRET);

        instagram = new Instagram(accessToken);
        view.printlnString("[Success]");

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

    public Liker getLiker() {
        System.out.println("[Configure your liker]");
        Liker liker = new Liker(instagram);

        String targetHashTag = (String) getUserInput("Enter target hash tag: #", input -> {
            if (input.isEmpty())
                return null;

            return input;
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

    public void setView(ConsoleView view) {
        this.view = view;
    }


    /*  Создание лайкера с параметрами
        Очередь лайкеров
        Возможность паузы/возобновления
        Остановка задачи (переключение на следующую), в случае если при работе наткнулись на лайкнутую фотографию
        Удаление текущего лайкера (переключение на следующую задачу)
    * */
}
