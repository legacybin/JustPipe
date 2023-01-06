package org.schabi.newpipe.player;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import org.schabi.newpipe.R;
import android.util.Log;
import org.schabi.newpipe.util.PermissionHelper;

import static org.schabi.newpipe.player.BackgroundPlayer.ACTION_CLOSE;

public final class BackgroundPlayerActivity extends ServicePlayerActivity {

    private static final String TAG = "BackgroundPlayerActivity";
    private static final boolean DEBUG = BasePlayer.DEBUG;

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getSupportActionTitle() {
        return getResources().getString(R.string.title_activity_background_player);
    }

    @Override
    public Intent getBindIntent() {
        return new Intent(this, BackgroundPlayer.class);
    }

    @Override
    public void startPlayerListener() {
        if (player != null && player instanceof BackgroundPlayer.BasePlayerImpl) {
            ((BackgroundPlayer.BasePlayerImpl) player).setActivityListener(this);
        }
    }

    @Override
    public void stopPlayerListener() {
        if (player != null && player instanceof BackgroundPlayer.BasePlayerImpl) {
            ((BackgroundPlayer.BasePlayerImpl) player).removeActivityListener(this);
        }
    }

    @Override
    public int getPlayerOptionMenuResource() {
        return R.menu.menu_play_queue_bg;
    }

    @Override
    public boolean onPlayerOptionSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_switch_popup) {

            if (!PermissionHelper.isPopupEnabled(this)) {
                PermissionHelper.showPopupEnablementToast(this);
                return true;
            }

            this.player.setRecovery();
            getApplicationContext().sendBroadcast(getPlayerShutdownIntent());
            getApplicationContext().startService(
                    getSwitchIntent(PopupVideoPlayer.class)
                            .putExtra(BasePlayer.START_PAUSED, !this.player.isPlaying())
            );
            return true;
        }
        return false;
    }

    @Override
    public Intent getPlayerShutdownIntent() {
        return new Intent(ACTION_CLOSE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            Log.d(TAG, "destroy() called");
        }
        /*
         * Not until we are sure the application main window is actually closed
         * For further ref:
         *   https://developer.android.com/reference/android/
         *   app/ActivityManager.RunningAppProcessInfo
         *
         * getApplicationContext().sendBroadcast(getPlayerShutdownIntent());
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
	}
}
