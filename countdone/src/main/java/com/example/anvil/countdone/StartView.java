package com.example.anvil.countdone;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static trikita.anvil.DSL.*;

import trikita.anvil.RenderableAdapter;
import trikita.anvil.RenderableView;

//
// A screen with a big black button to start new task (or to resume the current
// one). Also contains a list of finished tasks (failed or completed).
//
// When scrolled, the list applies the parallax effect to the header.
//
public class StartView extends RenderableView {

	private int mTopViewHeight = Style.MAX_TOP_HEIGHT;

	private RenderableAdapter mTasksAdapter = new TasksAdapter();
	
	private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
		public void onScroll(AbsListView v, int first, int count, int total) {
			int top = 0;
			if (v.getChildCount() > 0) {
				top = -v.getChildAt(0).getTop() + first * v.getChildAt(0).getHeight();
				if (top >= 0) {
					mTopViewHeight = Math.max((int) (Style.MAX_TOP_HEIGHT - top/(getResources().getDisplayMetrics().density)/1.5f), Style.MIN_TOP_HEIGHT);
				}
			}
		}
		public void onScrollStateChanged(AbsListView v, int state) {}
	};

	public StartView(Context c) {
		super(c);
	}

	@Override
	public void view() {
		linearLayout(() -> {
			size(FILL, FILL);
			Style.startBackground();
			orientation(LinearLayout.VERTICAL);

			frameLayout(() -> {
				size(FILL, dip(mTopViewHeight));
				backgroundColor(mTopViewHeight < Style.MAX_TOP_HEIGHT ?
					Style.listHeaderColor(mTopViewHeight) : 0);

				Style.blackButton(App.state().currentTask() == null || App.state().isTaskNew() ? R.string.create : R.string.resume);
			});

			v(View.class, () -> {
				size(FILL, dip(2));
				visibility(mTopViewHeight < Style.MAX_TOP_HEIGHT ? View.VISIBLE : View.GONE);
				backgroundColor(0x0f000000);
			});
			
			list();
		});
	}

	private void list() {
		listView(() -> {
			size(FILL, FILL);
			margin(dip(12));
			adapter(mTasksAdapter);
			divider(null);
			overScrollMode(ScrollView.OVER_SCROLL_NEVER);
			verticalScrollBarEnabled(false);
			selector(R.drawable.list_selector);
			onScroll(mScrollListener);
			onItemClick((adapterView, v, pos, id) -> {
				// make it a current task
				if ((pos - 1) < App.state().completed().size() ||
					App.state().completed().size() == 0) {
					pos--;
				} else {
					pos -= 2;
				}
				App.dispatch(new CountdoneAction(CountdoneAction.Type.POP_TASK, pos));
				((MainActivity) getContext()).toCountdown();
			});
		});
	}

	//
	// A list adapter that shows merged list of completed and failed tasks
	//
	private class TasksAdapter extends RenderableAdapter {
		private int completedSize;
		private int failedSize;

		@Override
		public int getCount() {
			completedSize = App.state().completed().size();
			failedSize = App.state().failed().size();
			int i = completedSize + failedSize + (completedSize > 0 ? 1 : 0) + (failedSize > 0 ? 1 : 0);
			return i;
		}

		@Override
		public Task getItem(int pos) {
			int completedOffset = (completedSize > 0 ? 1 : 0);
			int failedOffset = completedOffset + (failedSize > 0 ? 1 : 0);
			if (pos == 0 && completedSize > 0) {
				return null;
			} else if (pos - completedOffset < completedSize) {
				return App.state().completed().get(pos - 1);
			} else if (pos - completedOffset == completedSize) {
				return null;
			} else {
				return App.state().failed().get(pos - completedOffset - completedSize - 1);
			}
		}

		@Override
		public boolean isEnabled(int pos) {
			return getItem(pos) != null;
		}

		@Override
		public void view(int pos) {
			Task task = getItem(pos);
			boolean isHeader = (task == null);
			String itemText;
			final String itemDate;
			if (task == null) {
				itemDate = null;
				if (pos == 0 && completedSize != 0) {
					itemText = getContext().getString(R.string.list_group_completed);
				} else {
					itemText = getContext().getString(R.string.list_group_failed);
				}
			} else {
				itemText = task.name();
				Calendar now = Calendar.getInstance();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(task.stopped());
				if (calendar.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
					// If something happened in the previous years - show year, month and date
					itemDate = new SimpleDateFormat("MMM d, ''yy").format(calendar.getTime());
				} else if (calendar.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
					// For current year show only month and date
					itemDate = new SimpleDateFormat("MMM d").format(calendar.getTime());
				} else {
					// For today's tasks show only hours and minutes
					itemDate = new SimpleDateFormat("HH:mm").format(calendar.getTime());
				}
			}

			Style.taskItem(itemText, itemDate);
		}
	};
}
