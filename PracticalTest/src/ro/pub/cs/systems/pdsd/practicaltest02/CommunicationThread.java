package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


public class CommunicationThread extends Thread {

    private Server serverThread;
    private Socket socket;

    public CommunicationThread(Server serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket       = socket;
    }


    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client!");

                    String data = bufferedReader.readLine();

                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Client says: " + data);

                    String resp = null;
                    
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpGet httpGet = new HttpGet(Constants.SERVICE_INTERNET_ADDREESS
							+ "?" + Constants.QUERY_ATTRIBUTE + "=" + data);
					ResponseHandler<String> responseHandlerGet = new BasicResponseHandler();
					try {
						resp = httpClient.execute(httpGet, responseHandlerGet);
						if (resp != null) {
							
							Log.d(Constants.TAG, resp);
							
							int index1 = resp.indexOf(Constants.WORD_TAG1);
							int index2 = resp.indexOf(Constants.WORD_TAG2);
							Log.d(Constants.TAG, "Fisrt index " + index1 + "Second index " + index2);
							
							String result = resp.substring(index1 + Constants.WORD_TAG1.length(), index2);
							//Log.d(Constants.TAG, "RESULT IS " + result) ;
							printWriter.println(result);
			                printWriter.flush();
							
							
							
						}
						
					} catch (ClientProtocolException clientProtocolException) {
						Log.e(Constants.TAG, clientProtocolException.getMessage());
						if (Constants.DEBUG) {
							clientProtocolException.printStackTrace();
						}
					} catch (IOException ioException) {
						Log.e(Constants.TAG, ioException.getMessage());
						if (Constants.DEBUG) {
							ioException.printStackTrace();
						}
					}

                    printWriter.println(resp);
                    printWriter.flush();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        } else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }

    }
}
