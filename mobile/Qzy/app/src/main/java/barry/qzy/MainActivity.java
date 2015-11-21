package barry.qzy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    String      username = "minh";
    boolean[][] answerSheet;

    void    getTest(String  username , final String   testURL) {
        new AsyncTask<String, String, String>() {
            ProgressDialog  progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Searching for test");
                progressDialog.show();
            }

            @Override
            protected String  doInBackground(String... params) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.10.213.203:3000/contest/get");

                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("id" , testURL));
                String      response = "";

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs , "UTF-8"));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpClient.execute(httpPost , responseHandler);

                    return response;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }
            @Override
            protected void onPostExecute(String     response) {
                progressDialog.dismiss();
                container.removeView(getTestView);
                container.addView(testView);

                try {
                    JSONObject  jsonResponse = new JSONObject(response);
                    Test        mTest = new Test();
                    mTest.title = jsonResponse.getString("header");
                    mTest.id    = jsonResponse.getString("_id");
                    mTest.quizs = new Quiz[jsonResponse.getJSONArray("questions").length()];
                    for (int i = 0; i < mTest.quizs.length; i++) {
                        mTest.quizs[i] = new Quiz(jsonResponse.getJSONArray("questions").getJSONObject(i).getString("text") ,
                                jsonResponse.getJSONArray("questions").getJSONObject(i).getJSONArray("choices"));
                    }

                    setUpTestView(mTest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    void    getQuiz(String  username) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.10.213.203:3000/contest/quizz");

                String      response = "";

                try {
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpClient.execute(httpPost , responseHandler);

                    return response;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected void  onPostExecute(String response) {
                try {
                    JSONObject  jsonRes = new JSONObject(response);
                    Quiz    mQuiz = new Quiz(jsonRes.getString("text"),
                                            jsonRes.getJSONArray("choices"),
                                            jsonRes.getJSONArray("choices"));

                    setUpQuizView(mQuiz);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    void    setUpGetTest() {
        final EditText    testUrl = (EditText) getTestView.findViewById(R.id.test_url);
        Button      getTestButton = (Button) getTestView.findViewById(R.id.get_test);
        getTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTest(username, testUrl.getText().toString());
            }
        });
    }

    void    submitAnswer(final JSONArray      answer , final String   contestID) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.10.213.203:3000/contest/submit");

                JSONObject  jdata = new JSONObject();
                try {
                    jdata.put("id" , contestID);
                    jdata.put("answers" , answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String      response = "";

                try {
                    StringEntity    se = new StringEntity(jdata.toString());
                    httpPost.setEntity(se);
                    httpPost.addHeader("Content-type", "application/json");
                    //httpPost.addHeader("Accept-Charset" , "UTF-8");
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpClient.execute(httpPost , responseHandler);

                    return response;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected   void    onPostExecute(String    response) {
                System.out.println(response);
                try {
                    JSONObject  jsonRes = new JSONObject(response);
                    TextView    resultText = (TextView) testView.findViewById(R.id.resultText);
                    resultText.setText("Bạn làm được " + Integer.toString(jsonRes.getInt("correct")) + "/" + Integer.toString(jsonRes.getInt("total")) + " câu.");
                    resultText.setTextColor(Color.RED);
                    resultText.setTextSize(18);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    void    setUpTestView(final Test  tTest) {
        TextView    testTitle = (TextView) testView.findViewById(R.id.test_title);
        testTitle.setText(tTest.title);
        TableLayout quizTable = (TableLayout) testView.findViewById(R.id.quizs);
        Button      submitButton  = (Button)    testView.findViewById(R.id.submit_button);

        quizTable.removeAllViews();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send the answer sheet to be judge and display the score
                System.out.println(answerSheet);
                JSONArray   jsonAnswer = new JSONArray();
                for (int i = 0; i < answerSheet.length; i++) {
                    JSONArray   jsonRow = new JSONArray();
                    for (int j = 0; j < tTest.quizs[i].answers.length; j++) {
                        try {
                            jsonRow.put(j , answerSheet[i][j]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        jsonAnswer.put(i , jsonRow);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(jsonAnswer.toString());
                submitAnswer(jsonAnswer , tTest.id);
            }
        });

        answerSheet = new boolean[tTest.quizs.length][10];

        for (int i = 0; i < tTest.quizs.length; i++) {
            TableRow    tableRow  = new TableRow(this);
            View        quizView  = getLayoutInflater().inflate(R.layout.quiz_layout , null);
            TextView    questionTextView = (TextView) quizView.findViewById(R.id.quiz_question);
            TableLayout answerTable       = (TableLayout) quizView.findViewById(R.id.quiz_answers);

            questionTextView.setText(tTest.quizs[i].question.toString());
            for (int j = 0; j < tTest.quizs[i].answers.length; j++) {
                TableRow    answerRow   = new TableRow(this);
                View        answerView  = getLayoutInflater().inflate(R.layout.answer_layout , null);
                final Button      answerButton = (Button) answerView.findViewById(R.id.button);
                answerButton.setText((char) ('A' + j) + ". " + tTest.quizs[i].answers[j].toString());
                final int finalI = i;
                final int finalJ = j;
                answerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // update answer sheet
                        answerSheet[finalI][finalJ] ^= true;
                        if (answerSheet[finalI][finalJ]) answerButton.setBackgroundColor(Color.parseColor("#2979ff"));
                        else answerButton.setBackgroundColor(Color.parseColor("#e0e0e0"));
                    }
                });

                answerRow.addView(answerView);
                answerTable.addView(answerRow);
            }
            tableRow.addView(quizView);
            quizTable.addView(tableRow);
        }
    }

    void    setUpQuizView(final Quiz  mQuiz) {
        TextView    questionTextView = (TextView)       quizView.findViewById(R.id.quiz_question);
        TableLayout answerTable      = (TableLayout)    quizView.findViewById(R.id.quiz_answers);
        Button      submitButton     = (Button)         quizView.findViewById(R.id.quiz_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                for (int i = 0; i < mQuiz.answers.length; i++) {
                    if (answerSheet[0][i] != mQuiz.result[i]) {
                        check = false;
                    }
                }

                TextView quizResult = (TextView) quizView.findViewById(R.id.quizResult);

                if (check) {
                    quizResult.setText("Đúng");
                } else {
                    quizResult.setText("Sai");
                }
            }
        });

        questionTextView.setText(mQuiz.question.toString());
        answerTable.removeAllViews();

        answerSheet = new boolean[1][10];
        for (int i = 0; i < mQuiz.answers.length; i++) {
            TableRow    answerRow = new TableRow(this);
            View        answerView = getLayoutInflater().inflate(R.layout.answer_layout , null);
            final Button      answerButton = (Button) answerView.findViewById(R.id.button);

            answerButton.setText((char) ('A' + i) + ". " + mQuiz.answers[i].toString());
            final int finalI = i;
            answerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update the answer sheet
                    answerSheet[0][finalI] ^= true;
                    if (answerSheet[0][finalI]) answerButton.setBackgroundColor(Color.parseColor("#2979ff"));
                    else answerButton.setBackgroundColor(Color.parseColor("#e0e0e0"));
                }
            });

            answerRow.addView(answerView);
            answerTable.addView(answerRow);
        }
    }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    View    testView , quizView , getTestView;
    FrameLayout     container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        container = (FrameLayout) findViewById(R.id.container);
        testView = getLayoutInflater().inflate(R.layout.test_layout , null);
        getTestView = getLayoutInflater().inflate(R.layout.get_test_layout , null);
        quizView = getLayoutInflater().inflate(R.layout.take_quiz_layout , null);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                container.removeAllViews();
                container.addView(getTestView);
                setUpGetTest();
                break;
            case 2:
                container.removeAllViews();
                container.addView(quizView);
                getQuiz(username);
                break;
            case 3:
                container.removeAllViews();
                break;
            case 4:
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
