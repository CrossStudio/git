package cross.xam.newsplusplus;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class AddResourceClickListener implements OnClickListener {

	static SearchActivity activity;
	
	static {
		activity = SearchActivity.getInstance();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(activity, NewResourceForm.class);
		activity.startActivity(intent);
	}

}
