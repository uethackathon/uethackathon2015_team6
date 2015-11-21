package barry.qzy;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by barry on 20/11/2015.
 */
public class Quiz {
    String      question;
    String[]    answers;
    boolean[]   result;

    Quiz() {
        question = "?";
        answers = new String[4];
        answers[0] = "a";
        answers[1] = "b";
        answers[2] = "c";
        answers[3] = "d";
    }

    Quiz(String     tQuestion , JSONArray tAnswers) {
        question = tQuestion;
        answers = new String[tAnswers.length()];
        for (int i = 0; i < tAnswers.length(); i++) {
            try {
                answers[i] = tAnswers.getJSONObject(i).getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    Quiz(String     tQuestion , JSONArray tAnswers , JSONArray  tAnswer2) {
        question = tQuestion;
        answers = new String[tAnswers.length()];
        result = new boolean[tAnswers.length()];
        for (int i = 0; i < tAnswers.length(); i++) {
            try {
                answers[i] = tAnswers.getJSONObject(i).getString("text");
                result[i] = tAnswer2.getJSONObject(i).getBoolean("correct");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
