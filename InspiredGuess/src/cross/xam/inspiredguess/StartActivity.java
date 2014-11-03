package cross.xam.inspiredguess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.content.*;
import android.view.View.*;
import android.view.*;

public class StartActivity extends Activity {

	private static final String USER_LOGIN = "vano123";
	private static final String USER_PASSWORD = "29.02.1993";
	
	/**
	 * Хранит ссылку на View-элемент EditText для ввода логина пользователя
	 */
	EditText etLogin;
	/**
	 * Хранит ссылку на View-элемент EditText для ввода пароля пользователя
	 */
	EditText etPassword;
	/**
	 * Хранит ссылку на View-элемент Button для перехода в личный кабинет пользователя
	 */
	Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_page);
		
		etLogin = (EditText) findViewById(R.id.etLogin);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		
		btnLogin.setOnClickListener(new LoginListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
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
	
	/**
	 * Отвечает за обработку одиночного нажатия кнопки Login
	 * @author XAM
	 *
	 */
	private class LoginListener implements OnClickListener{
		
		@Override
		public void onClick(View v){
			checkLoginData();
		}
		
		/**
		 * Проверяет соответствие введенных данных логина и пароля
		 * и данных введенных при регистрации и переводит на Main страницу
		 */
		private void checkLoginData() {
			String loginInput = etLogin.getText().toString();
			String passInput = etPassword.getText().toString();
			if (loginInput.hashCode() == USER_LOGIN.hashCode()
					&& passInput.hashCode() == USER_PASSWORD.hashCode()){
				Toast.makeText(StartActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(StartActivity.this, MainActivity.class));
			}
			else {
				Toast.makeText(StartActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
