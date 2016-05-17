package ro.pub.cs.systems.pdsd.practicaltest02;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class PracticalTest02MainActivity extends Activity {

	private EditText server_port, client_port, client_address, word;
	private Button connectBtn, get_definationBtn;
	private TextView definationView;
	
	Server server;
	Client client;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        
        server_port = (EditText)findViewById(R.id.editText1);
        client_port = (EditText)findViewById(R.id.editText3);
        client_address = (EditText)findViewById(R.id.editText2);
        word = (EditText)findViewById(R.id.editText4);
        
        connectBtn = (Button)findViewById(R.id.button1);
        connectBtn.setOnClickListener(new ConnectServerClickListener());
        get_definationBtn = (Button)findViewById(R.id.button2);
        get_definationBtn.setOnClickListener(new GetWordDefinationOnClickListener());
        
        definationView = (TextView)findViewById(R.id.textView3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.practical_test02_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    class ConnectServerClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String serverPort = server_port.getText().toString();
		    if (serverPort == null || serverPort.isEmpty()) {
		    	Toast.makeText(getApplicationContext(), "Server port should be filled!",
		    		  		 Toast.LENGTH_SHORT).show();
		    	return;
		    }
		    server = new Server(Integer.parseInt(serverPort));
		    if (server.getServerSocket() != null) {
		    	server.start();
		    } else {
		      Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
		    }
			
			
		}
	}
    

	class GetWordDefinationOnClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String clientAddress = client_address.getText().toString();
		    String clientPort    = client_port.getText().toString();
		    if (clientAddress == null || clientAddress.isEmpty() || clientPort == null || clientPort.isEmpty()) {
		    	Toast.makeText(getApplicationContext(), "Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
		    	return;
		    }
		    if (server == null || !server.isAlive()) {
		    	Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
		    	return;
		    }
		    String word_request = word.getText().toString();
		    if (word_request == null || word_request.isEmpty()) {
		    	Toast.makeText(getApplicationContext(), "Parameters from client (word) should be filled!", Toast.LENGTH_SHORT
		    			).show();
		    	return;
		    }
		    
		    client = new Client(clientAddress, Integer.parseInt(clientPort), word_request, definationView);
		    client.start();
		  }
	}
}
