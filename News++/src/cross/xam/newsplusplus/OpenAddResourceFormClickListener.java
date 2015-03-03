package cross.xam.newsplusplus;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class OpenAddResourceFormClickListener implements OnClickListener {

	static SearchActivity activity;
	
	@Override
	public void onClick(View v) {
		activity = (SearchActivity) v.getContext();
		Intent intent = new Intent(activity, NewResourceForm.class);
		activity.startActivity(intent);
	}

}
